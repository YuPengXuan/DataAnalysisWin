package com.xn.alex.data.c45;

import java.util.Vector;

import com.xn.alex.data.graphics.TreeDataSheet;
import com.xn.alex.data.resultobj.treeNodeResultObj;

public class MainC45 {
    private static final Vector<Vector<String>> dataList = new Vector<Vector<String>>();
//	private static final Vector<Vector<String>> dataList[][] = {{"youth","high","no","fair","no"},{"youth","high","no","fair","no"}};
    private static final Vector<String> attributeList = new Vector<String>();
    
    public static void main(String args[]){
    	MainC45.attributeList.add("age");
    	MainC45.attributeList.add("income");
    	MainC45.attributeList.add("student");
    	MainC45.attributeList.add("credit_rating");
    	MainC45.attributeList.add("interest");
    	MainC45.attributeList.add("TARGET_VALUE");
    
        Object[] array = new Object[] {
new String[] { "old",          "high",     "no",    "fair",	"1",          "no"  },
new String[] { "old",          "high",     "no",    "good",	"2",          "no"  },
new String[] { "old",          "high",     "no",    "fair",	"3",          "no"  },
new String[] { "old",          "high",     "no",    "good",	"4",          "yes"  },
new String[] { "old",          "high",     "no",    "fair",	"5",          "yes"  },
new String[] { "old",          "high",     "no",    "good",	"6",          "yes"  },
/*new String[] { "middle_aged",  "low",      "yes",     "excellent",     "yes" },
new String[] { "youth",        "medium",   "no",      "fair",          "no"  },
new String[] { "youth",        "low",      "yes",     "fair",          "yes" },
new String[] { "senior",       "medium",   "yes",     "fair",          "yes" },
new String[] { "youth",        "medium",   "yes",     "excellent",     "yes" },
new String[] { "middle_aged",  "medium",   "no",      "excellent",     "yes" },
new String[] { "middle_aged",  "high",     "yes",     "fair",          "yes" },
new String[] { "senior",       "medium",   "no",      "excellent",     "no"  }
*/
};
        for(int i=0,len = array.length;i<len;i++){

        	Vector<String> b = new Vector<String>();

			String[] h = (String[]) array[i];

			int j = 0;

			int jlen = h.length;

			for(;j<jlen;j++){

				b.add(h[j]);

			}

			dataList.add(b);			

		}    	
    	

        DecisionTree dt = new DecisionTree(5);
 //       TreeNode node = dt.createDT(configData(),configAttribute());
        treeNodeResultObj node = dt.createDT(dataList,attributeList,5);
        TreeDataSheet.Instance().show(node);
        System.out.println();
//        for(treeNodeResultObj t:DecisionTree.entireTree)
//        	System.out.println(t);
    }
}
