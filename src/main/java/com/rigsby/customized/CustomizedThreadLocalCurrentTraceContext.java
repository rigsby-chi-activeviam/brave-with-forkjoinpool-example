package com.rigsby.customized;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import brave.internal.Nullable;
import brave.propagation.CurrentTraceContext;
import brave.propagation.TraceContext;

/**
 * Modified from ThreadLocalCurrentTraceContext to support ForkJoinPool
 * 
 * @author Rigsby
 *
 */
public class CustomizedThreadLocalCurrentTraceContext extends CurrentTraceContext {

	Logger LOGGER = LoggerFactory.getLogger(CustomizedThreadLocalCurrentTraceContext.class);

	public static CurrentTraceContext create() {
		return new Builder().build();
	}

	public static CurrentTraceContext.Builder newBuilder() {
		return new Builder();
	}

	static final class Builder extends CurrentTraceContext.Builder {

		@Override
		public CurrentTraceContext build() {
			return new CustomizedThreadLocalCurrentTraceContext(this, DEFAULT);
		}

		Builder() {
		}
	}

	public static final ThreadLocal<TraceContext> DEFAULT = new ThreadLocal<>();

	final ThreadLocal<TraceContext> local;

	CustomizedThreadLocalCurrentTraceContext(CurrentTraceContext.Builder builder, ThreadLocal<TraceContext> local) {
		super(builder);
		if (local == null)
			throw new NullPointerException("local == null");
		this.local = local;
	}

	@Override
	public TraceContext get() {
		return local.get();
	}

	@Override
	public Scope newScope(@Nullable TraceContext currentSpan) {
		final TraceContext previous = local.get();
		local.set(currentSpan);
		class ThreadLocalScope implements Scope {
			@Override
			public void close() {
				local.set(previous);
			}
		}
		Scope result = new ThreadLocalScope();
		return decorateScope(currentSpan, result);
	}

	public <T> ForkJoinTask<T> wrap(final ForkJoinTask<T> delegate) {
		final TraceContext invocationContext = get();

		@SuppressWarnings("serial")
		class TraceableForkJoinTask<V> extends ATraceableForkJoinTask<T> {
			protected ForkJoinTask<T> delegate() {
				return delegate;
			}

			@Override
			protected boolean exec() {

				try (Scope scope = maybeScope(invocationContext)) {
					Method method = ReflectionUtils.findMethod(delegate().getClass(), "exec");
					ReflectionUtils.makeAccessible(method);
					return (boolean) ReflectionUtils.invokeMethod(method, delegate());
				} catch (Exception e) {
					LOGGER.error("Failed to wrap ForkJoinTask", e);
					return false;
				}
			}
		}
		return new TraceableForkJoinTask<>();
	}

	@Override
	public Runnable wrap(Runnable task) {
		final TraceContext invocationContext = get();

		class CurrentTraceContextRunnable implements Runnable {
			@Override
			public void run() {
				try (Scope scope = maybeScope(invocationContext)) {
					task.run();
				}
			}
		}
		return new CurrentTraceContextRunnable();
	}

	public ForkJoinPool getTraceableForkJoinPool(int parallelism, ForkJoinWorkerThreadFactory factory,
			UncaughtExceptionHandler handler, boolean asyncMode) {
		class TraceableForkJoinPool extends ATraceableForkJoinPool {

			public TraceableForkJoinPool(int parallelism, ForkJoinWorkerThreadFactory factory,
					UncaughtExceptionHandler handler, boolean asyncMode) {
				super(parallelism, factory, handler, asyncMode);
			}

			@Override
			protected <T> ForkJoinTask<T> wrap(ForkJoinTask<T> task) {
				return CustomizedThreadLocalCurrentTraceContext.this.wrap(task);
			}

			@Override
			protected <C> Callable<C> wrap(Callable<C> task) {
				return CustomizedThreadLocalCurrentTraceContext.this.wrap(task);
			}

			@Override
			protected Runnable wrap(Runnable task) {
				return CustomizedThreadLocalCurrentTraceContext.this.wrap(task);
			}
		}
		return new TraceableForkJoinPool(parallelism, factory, handler, asyncMode);
	}
}
