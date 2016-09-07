/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.xn.alex.data.threadpool;

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
    private static class ComparableFutureTask<V> extends FutureTask<V> implements Comparable<ComparableFutureTask<V>> {

        final Object object;

        ComparableFutureTask(final Callable<V> callable) {
            super(callable);
            object = callable;
        }

        ComparableFutureTask(final Runnable runnable, final V result) {
            super(runnable, result);
            object = runnable;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        public int compareTo(final ComparableFutureTask<V> o) {
            if (o == null) {
                return -1;
            }
            if (o == this) {
                return 0;
            }

            // DON't check object == null or o.object == null due to 
            // it will get NullException in constructor
            if (object.getClass().equals(o.object.getClass())) {
                if (object instanceof Comparable) {
                    return ((Comparable) object).compareTo(o.object);
                }
            }
            return object.getClass().toString().compareTo(o.object.getClass().toString());
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
            final TimeUnit unit, final PriorityBlockingQueue<Runnable> workQueue) {
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