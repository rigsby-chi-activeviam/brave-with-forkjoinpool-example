package com.rigsby.customized;

import java.lang.reflect.Method;
import java.util.concurrent.ForkJoinTask;

import org.springframework.util.ReflectionUtils;

@SuppressWarnings("serial")
public abstract class ATraceableForkJoinTask<T> extends ForkJoinTask<T> {

	protected abstract ForkJoinTask<T> delegate();

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return delegate().cancel(mayInterruptIfRunning);
	}

	@Override
	public void completeExceptionally(Throwable ex) {
		delegate().completeExceptionally(ex);
	}

	@Override
	public void complete(T value) {
		delegate().complete(value);
	}

	@Override
	public void reinitialize() {
		delegate().reinitialize();
	}

	@Override
	public boolean tryUnfork() {
		return delegate().tryUnfork();
	}

	@Override
	public T getRawResult() {
		return delegate().getRawResult();
	}

	@Override
	protected void setRawResult(T value) {
		Method method = ReflectionUtils.findMethod(delegate().getClass(), "setRawResult", value.getClass());
		ReflectionUtils.makeAccessible(method);
		ReflectionUtils.invokeMethod(method, delegate(), value);
	}
	
}
