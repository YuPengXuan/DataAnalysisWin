package com.xn.alex.data.threadpool;

import java.util.Date;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.xn.alex.data.worker.AbstractWorker;
import com.xn.alex.data.worker.IWorker;


public class ThreadPoolImpl implements IThreadPool {

    private static final int KEEP_ALIVE_TIME_DEF = 10; // 10min

    private static final int SIZE_DEF = Runtime.getRuntime().availableProcessors();

    private final PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>();

    private final PriorityThreadPoolExecutor executor;

    public ThreadPoolImpl(final int aSize, final int aTime) {
        final int size = (aSize < 0 ? SIZE_DEF : aSize);
        int keepAliveTime = KEEP_ALIVE_TIME_DEF;
        if (aTime > 0) {
            keepAliveTime = aTime;
        }
        executor = new PriorityThreadPoolExecutor(size, size, keepAliveTime, TimeUnit.MINUTES, queue);
    }

    public int getActiveCount() {
        return executor.getActiveCount();
    }

    public void setCorePoolSize(final int size) {
        if (size < 0) {
            executor.setCorePoolSize(SIZE_DEF);
        } else {
            executor.setCorePoolSize(size);
        }
    }

    public void submitTask(final IWorker worker) {
        if (worker == null) {
            System.out.println("Task is null");
        } else {
        	System.out.println("Submit worker is " + worker);
            if (worker instanceof AbstractWorker) {
                ((AbstractWorker) worker).setTimeInQueue((new Date()).getTime());
            }
            executor.execute(worker);
        }
    }

    public Future<Runnable> submitTaskWithFuture(final IWorker worker) {
        if (worker == null) {
        	System.out.println("Task is null");
            return null;
        }
        System.out.println("Submit worker is " + worker);
        if (worker instanceof AbstractWorker) {
            ((AbstractWorker) worker).setTimeInQueue((new Date()).getTime());
        }
        @SuppressWarnings("unchecked")
        final Future<Runnable> future = (Future<Runnable>) executor.submit(worker);

        return future;
    }

    public void shutdown() {
        executor.shutdown();

    }

    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        final boolean ret = executor.awaitTermination(timeout, unit);
        return ret;
    }

}
