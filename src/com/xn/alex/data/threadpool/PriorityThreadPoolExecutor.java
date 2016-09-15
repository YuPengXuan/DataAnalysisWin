/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.xn.alex.data.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

    /**
     * ComparableFutureTask<V> is used for FutureTask to cast to Comparable
     * 
     *   It is a bug of JavaSE 6.  Please refer to below link:
     *   http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6539720
     * @author ezhibhe
     * @since 2011
     *
     * @param <V>
     */
    private static class ComparableFutureTask<V> extends FutureTask<V> {

        final Object object;

        ComparableFutureTask(final Callable<V> callable) {
            super(callable);
            object = callable;
        }

        ComparableFutureTask(final Runnable runnable, final V result) {
            super(runnable, result);
            object = runnable;
        }
    }

    /**
     * PriorityThreadPoolExecutor constructor
     * @param corePoolSize     
     * @param maximumPoolSize
     * @param keepAliveTime    
     * @param unit
     * @param workQueue
     */
    public PriorityThreadPoolExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime,
            final TimeUnit unit, final BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    public String toString() {
        return String.format("PriorityThreadPoolExecutor Active:%d Queue:%d", getActiveCount(), getQueue().size());
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value) {
        return new ComparableFutureTask<T>(runnable, value);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
        return new ComparableFutureTask<T>(callable);
    }

}