package com.xn.alex.data.worker;

import java.util.Date;

import com.xn.alex.data.database.SqlTask;


public abstract class AbstractWorker implements IWorker {


    private long timeInQueue = -1;
    
    SqlTask task;

    /* (non-Javadoc)
     * @see com.ericsson.nbi.mr.concurrency.worker.IWorker#getTask()
     */
    public SqlTask getTask() {
        return task;
    }

    /* (non-Javadoc)
     * @see com.ericsson.nbi.mr.concurrency.worker.IWorker#setTask(com.ericsson.nbi.mr.task.ITask)
     */
    public void setTask(final SqlTask task) {
        this.task = (SqlTask) task;

    }

    public long getTimeInQueue() {
        return timeInQueue;
    }

    public void setTimeInQueue(final long timeInQueue) {
        this.timeInQueue = timeInQueue;
    }

    public boolean checkTimeOverdue(final long expiredTime) {
        boolean rt = false;
        long leftTime;

        //validate time in queue;
        final long costTimeInQueue = getTimeCost();
        leftTime = expiredTime - costTimeInQueue;
        if (leftTime < 0) {
            rt = true;
            //final String err = String.format("Task is overdue due to wait in task pool long time (%d seconds). "
            //        + "It will be cancelled", costTimeInQueue / 1000);
        }
        return rt;
    }

    protected long getTimeCost() {
        final long currentTime = new Date().getTime();
        final long ret = currentTime - getTimeInQueue();
        return ret;
    }

}
