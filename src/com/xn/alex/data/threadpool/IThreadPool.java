/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 Ericsson.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.xn.alex.data.threadpool;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.xn.alex.data.worker.IWorker;


public interface IThreadPool {

    void submitTask(IWorker worker);

    Future<Runnable> submitTaskWithFuture(final IWorker worker);

    void shutdown();

    int getActiveCount();

    void setCorePoolSize(final int size);

    boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException;

}
