package com.xn.alex.data.worker;

import com.xn.alex.data.database.DataAnalysisException;
import com.xn.alex.data.database.MySqlExecuter;

public class GetDistinctWoker extends AbstractWorker {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		 MySqlExecuter mySqlExecuter = MySqlExecuter.getMySqlExecuter();
		 
		 try {
			mySqlExecuter.executer(getTask());
		} catch (DataAnalysisException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
