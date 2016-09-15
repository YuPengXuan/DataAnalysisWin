package com.xn.alex.data.worker;

import com.xn.alex.data.database.GetDistinctNumberOfColumnTask;
import com.xn.alex.data.database.GetDistinctValueTask;
import com.xn.alex.data.database.GetNumberByConditionTask;
import com.xn.alex.data.database.GetNumericColRangeTask;
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
            else if(task instanceof GetDistinctValueTask){
            	worker = new GetDistinctWoker();
            }
            else if(task instanceof GetNumberByConditionTask){
            	worker = new GetDistinctWoker();
            }
            else if(task instanceof GetNumericColRangeTask){
            	worker = new GetDistinctWoker();
            }
            
            
            if (worker != null) {
                worker.setTask(task);
            } else {
                System.out.println("Failed create correct worker for task " + task);
            }
        }
              
        return worker;
    }
}
