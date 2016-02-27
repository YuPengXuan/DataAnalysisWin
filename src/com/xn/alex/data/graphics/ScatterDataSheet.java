package com.xn.alex.data.graphics;

import java.awt.Font;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import com.xn.alex.data.resultobj.algorithmResultObj2;

public class ScatterDataSheet extends JFrame{
	public ScatterDataSheet() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	
	private static ScatterDataSheet scatterDataSheetHandler = null;
	
	private JFreeChart chart = null;
	
	private double[][] data = null; 
	
	public static ScatterDataSheet Instance(){
		
		if(null == scatterDataSheetHandler){
			
			scatterDataSheetHandler = new ScatterDataSheet();
			
		}
		
		return scatterDataSheetHandler;
		
	}
	
	private void clearPreviousVar(){
		chart = null;
		data = null;
	}
	
	
    public void show(Vector<algorithmResultObj2> resultObjVec){
    	
    	clearPreviousVar();
		
		this.setContentPane(createPanel(resultObjVec));
		
		Instance().pack();
		
		Instance().setVisible(true);
		
		Instance().setLocationRelativeTo(null);
		
	}
	
	private JPanel createPanel(Vector<algorithmResultObj2> resultObjVec)
    {
        chart =createChart(createScatterSet(resultObjVec));
        return new ChartPanel(chart); //��chart�������Panel�����ȥ��ChartPanel���Ѽ̳�Jpanel
    }
	
	private JFreeChart createChart(XYDataset xydataset){
		
		JFreeChart chart=ChartFactory.createScatterPlot("ROC����ͼ", "�׿�����", "��߿�����", xydataset, PlotOrientation.VERTICAL, true, false, false);
	    
        chart.getLegend().setItemFont(new Font("����", Font.BOLD+Font.PLAIN, 12)); 
    	
        chart.setTitle(new TextTitle("ĳ�㷨��״ͼ",new Font("����",Font.BOLD+Font.ITALIC,20)));
        
        XYPlot plot=(XYPlot)chart.getPlot();//���ͼ���м䲿�֣���plot
        
        plot.setNoDataMessage("û�����ݣ�");
        
        //X axis      
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis(); 
   
        domainAxis.setAutoRange(true); 
        
        domainAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD+Font.PLAIN, 12));  
        
        domainAxis.setLabelFont(new Font("����", Font.BOLD+Font.PLAIN, 12));
        
        // Y axis  
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();  
        
        rangeAxis.setAutoRange(true); 
                
        rangeAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD+Font.PLAIN, 12));  
        
        rangeAxis.setLabelFont(new Font("����", Font.BOLD+Font.PLAIN, 12));
        
		
		return chart;
		
	}
	
	private XYDataset createScatterSet(Vector<algorithmResultObj2> resultObjVec){
		
		DefaultXYDataset xydataset = new DefaultXYDataset();
		
		 data = new double[2][12];
		 
		 
         for(int i=0;i<resultObjVec.size();i++){
        	 
        	 algorithmResultObj2 obj = resultObjVec.get(i);
        	 
        	 if(null == obj){
        		 
        		 continue;
        		 
        	 }
        	 
        	// x axis data
        	 data[0][i] = (double) obj.x;
        	 
        	// y axis data 
     	    data[1][i] = (double) obj.y;
       	  
         }
	          
	    xydataset.addSeries("ROC���", data);  
		
		return xydataset;
		
	}
	
	public void testScatterSet(){
		
		data = new double[2][12];  
        
	     // x axis data
	     data[0] = new double[]{400,397,360,402,413,427,389,388,405,422,411,433};  
	          
	    // y axis data 
	    data[1] = new double[]{21.7,20.7,17.7,21.9,23.7,25.7,20.4,20.1,22.9,24.8,22.5,25.9};
	    
	    Vector<algorithmResultObj2> testVec = new Vector<algorithmResultObj2>();
	    
	    for(int i=0;i<12;i++){
	    	
	    	algorithmResultObj2 obj = new algorithmResultObj2();
	    	
	    	obj.x = data[0][i];
	    	
	    	obj.y = data[1][i];
	    	
	    	testVec.add(obj);	    	
	    }
		
		Instance().show(testVec);
	}

}
