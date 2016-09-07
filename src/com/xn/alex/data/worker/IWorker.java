
package com.xn.alex.data.worker;

import com.xn.alex.data.database.SqlTask;

public interface IWorker extends Runnable {
    SqlTask getTask();

    void setTask(SqlTask task);

    long getTimeInQueue();
}
