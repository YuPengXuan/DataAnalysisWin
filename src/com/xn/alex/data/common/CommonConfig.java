package com.xn.alex.data.common;

public class CommonConfig {

    public enum FILE_TYPE{DBF_FILE, XLS_FILE, XLSX_FILE, INVALID_FILE};
    
    public enum ROC_TYPE{ROC_EXT, ROC_SMS, ROC_CUST, ROC_APP, ROC_WARN};
	//please add path for it
    public final String dataFileFormatFile = "config.ini";
    
    public enum CURRENT_ACTION{NONE,DB_OPERATION,JTABLE_EDIT};
    
    public final static String REG_EKY_VALUE = "2592000000";  //30*24*3600*1000
    
    public final static String IF_OP = "IF";
    
    public final static String AND_OP = "AND";
    
    public final static String OR_OP = "OR";
    
    public final static String THEN_OP = "THEN";
    
    public final static String ISMISSING_OP = "IS MISSING";
    
    public final static String NOTMISSING_OP = "NOT MISSING";
    
    public final static String LEFT_PARENTHESE = "(";
    
    public final static String RIGHT_PARENTHESE = ")";
    
    public final static String SPACE = " ";
    
    public final static int IVALID_VALUE = -99999;
    
    public final static String MAX_PROFIT = "最大利润";
    
    public final static String MAX_F0 = "F0";
    
    public final static String MAX_F1 = "F1";
    
    public final static String MAX_F2 = "F2";
    
    public final static String MAX_SALE_PEOPLE = "营销人数";
    
    public final static String MAX_PRED_PEOPLE = "预计成功人数";
    
    public final static String MAX_PRED_DIST_PEOPLE = "预计打扰人数";
    
    public final static String MAX_LEAF_NODE_NUM = "涉及叶子节点个数";
    
}
