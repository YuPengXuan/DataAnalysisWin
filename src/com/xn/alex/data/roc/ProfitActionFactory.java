package com.xn.alex.data.roc;

import com.xn.alex.data.common.CommonConfig.ROC_TYPE;

public class ProfitActionFactory {
	
	public static ProfitModelInterface getNewProfitAction(ROC_TYPE rocType){
		
		ProfitModelInterface newAction = null;;
		
		switch(rocType){
		    case ROC_EXT: 
		    	newAction = new ProfitModelAction2();
		    	break;
		    case ROC_SMS:
		    	newAction = new ProfitModelAction3();
		    	break;
		    case ROC_CUST:
		    	newAction = new ProfitModelAction4();
		    	break;
		    case ROC_APP:
		    	newAction = new ProfitModelAction5();
		    	break;
		    case ROC_WARN:
		    	newAction = new ProfitModelAction6();
		    default:
		    	break;
		}
		
		return newAction;
	}

}
