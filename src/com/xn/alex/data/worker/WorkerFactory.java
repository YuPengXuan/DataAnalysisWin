package com.xn.alex.data.worker;

import com.xn.alex.data.database.GetDistinctNumberOfColumnTask;
import com.xn.alex.data.database.SqlTask;

public final class WorkerFactory {

    private WorkerFactory() {
    }

    public static IWorker getOneTaskWorker(final SqlTask task) {
        IWorker worker = null;
        if (task == null) {
            System.out.println("Task should not be empty, null worker will be returned.");
        } else {
            if (task instanceof GetDistinctNumberOfColumnTask) {
                worker = new GetDistinctWoker();
            }
        }
        return worker;
    }
}
