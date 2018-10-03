package com.rigsby.customized;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ATraceableForkJoinPool extends ForkJoinPool {

	public ATraceableForkJoinPool(int parallelism, ForkJoinWorkerThreadFactory factory, UncaughtExceptionHandler handler, boolean asyncMode) {
		super(parallelism, factory, handler, asyncMode);
	}

	protected abstract <C> Callable<C> wrap(Callable<C> task);

	protected abstract Runnable wrap(Runnable task);

	protected abstract <T> ForkJoinTask<T> wrap(ForkJoinTask<T> task);

	<T> Collection<? extends Callable<T>> wrap(Collection<? extends Callable<T>> tasks) {
		ArrayList<Callable<T>> result = new ArrayList<>(tasks.size());
		for (Callable<T> task : tasks) {
			result.add(wrap(task));
		}
		return result;
	}

	@Override
	public <T> T invoke(ForkJoinTask<T> task) {
		return super.invoke(wrap(task));
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
		return super.invokeAll(wrap(tasks));
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		return super.invokeAll(wrap(tasks), timeout, unit);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return super.invokeAny(wrap(tasks));
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return super.invokeAny(wrap(tasks), timeout, unit);
	}

	@Override
	public void execute(ForkJoinTask<?> task) {
		super.execute(wrap(task));
	}

	@Override
	public void execute(Runnable task) {
		super.execute(wrap(task));
	}

	@Override
	public <T> ForkJoinTask<T> submit(ForkJoinTask<T> task) {
		return super.submit(wrap(task));
	}

	@Override
	public <T> ForkJoinTask<T> submit(Callable<T> task) {
		return super.submit(wrap(task));
	}

	@Override
	public <T> ForkJoinTask<T> submit(Runnable task, T result) {
		return super.submit(wrap(task), result);
	}

	@Override
	public ForkJoinTask<?> submit(Runnable task) {
		return super.submit(wrap(task));
	}

	@Override
	public boolean hasQueuedSubmissions() {
		return super.hasQueuedSubmissions();
	}

	@Override
	protected ForkJoinTask<?> pollSubmission() {
		return super.pollSubmission();
	}

	@Override
	protected int drainTasksTo(Collection<? super ForkJoinTask<?>> c) {
		return super.drainTasksTo(c);
	}

	@Override
	public void shutdown() {
		super.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return super.shutdownNow();
	}

	@Override
	protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
		return super.newTaskFor(runnable, value);
	}

	@Override
	protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
		return super.newTaskFor(callable);
	}

	@Override
	public boolean isQuiescent() {
		return super.isQuiescent();
	}

	@Override
	public long getQueuedTaskCount() {
		return super.getQueuedTaskCount();
	}

	@Override
	public int getQueuedSubmissionCount() {
		return super.getQueuedSubmissionCount();
	}

	@Override
	public boolean isShutdown() {
		return super.isShutdown();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return super.awaitTermination(timeout, unit);
	}

	@Override
	public boolean awaitQuiescence(long timeout, TimeUnit unit) {
		return super.awaitQuiescence(timeout, unit);
	}

	@Override
	public ForkJoinWorkerThreadFactory getFactory() {
		return super.getFactory();
	}

	@Override
	public UncaughtExceptionHandler getUncaughtExceptionHandler() {
		return super.getUncaughtExceptionHandler();
	}

	@Override
	public int getParallelism() {
		return super.getParallelism();
	}

	@Override
	public int getPoolSize() {
		return super.getPoolSize();
	}

	@Override
	public boolean getAsyncMode() {
		return super.getAsyncMode();
	}

	@Override
	public int getRunningThreadCount() {
		return super.getRunningThreadCount();
	}

	@Override
	public int getActiveThreadCount() {
		return super.getActiveThreadCount();
	}

	@Override
	public long getStealCount() {
		return super.getStealCount();
	}

	@Override
	public boolean isTerminated() {
		return super.isTerminated();
	}

	@Override
	public boolean isTerminating() {
		return super.isTerminating();
	}
	
	

}
