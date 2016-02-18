package com.xn.alex.data.graphics;

import java.awt.Font;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.xn.alex.data.resultobj.algorithmResultObj1;

public class CategoryDataSheet extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	private static CategoryDataSheet categoryDataSheetHandler = null;
	
	private JFreeChart chart = null;
	
	private CategoryDataSheet(String title) {
		super(title);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// TODO Auto-generated constructor stub
	}
	
	private void clearPreviousVar(){
		this.dispose();
		chart = null;
	}
	
    public static  CategoryDataSheet Instance(){
		
		if(null == categoryDataSheetHandler){
			
			categoryDataSheetHandler = new CategoryDataSheet("统计直方图");
			
		}		
		return categoryDataSheetHandler;		
	}
	
	public CategoryDataset createDataset(Vector<algorithmResultObj1> resultObjVec){
		
		DefaultCategoryDataset dataset=new DefaultCategoryDataset();
		
		try{
		
		for(int i=0;i<resultObjVec.size();i++){
			
			algorithmResultObj1 obj = resultObjVec.get(i);
			
			if(null == obj){
				continue;
			}
			
			dataset.addValue(obj.y, obj.type, obj.x);
			
		}
		}
		catch(Exception e){
			
			e.printStackTrace();
			
		}
		
		return dataset;
		
	}
	
	public void show(Vector<algorithmResultObj1> resultObjVec, CategoryDataDisplayObj obj){
		clearPreviousVar();
		
		this.setContentPane(createPanel(resultObjVec));
		
		Instance().pack();
		
		Instance().setVisible(true);
		
		Instance().setLocationRelativeTo(null);
		
	}
	
	public JPanel createPanel(Vector<algorithmResultObj1> resultObjVec)
    {
        chart =createChart(createDataset(resultObjVec));
        return new ChartPanel(chart); //将chart对象放入Panel面板中去，ChartPanel类已继承Jpanel
    }
	
    public JFreeChart createChart(CategoryDataset dataset) {
    	
    	JFreeChart chart=ChartFactory.createBarChart("hi", "", "", dataset, PlotOrientation.VERTICAL, true, true, false);
    	
    	chart.getLegend().setItemFont(new Font("宋体", Font.BOLD+Font.PLAIN, 12)); 
    	
        chart.setTitle(new TextTitle("统计直方图",new Font("宋体",Font.BOLD+Font.ITALIC,20)));
        
        CategoryPlot plot=(CategoryPlot)chart.getPlot();//获得图标中间部分，即plot
        
        CategoryAxis categoryAxis=plot.getDomainAxis();//获得横坐标
   
        categoryAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD+Font.PLAIN, 11));
        
        categoryAxis.setLabelFont(new Font("宋体",Font.BOLD,12));//设置横坐标字体
        
        ValueAxis rAxis = plot.getRangeAxis(); 
        
        rAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD+Font.PLAIN, 12));  
        
        rAxis.setLabelFont(new Font("宋体", Font.BOLD+Font.PLAIN, 12));
        
        return chart;		
		
	}
    
    public void saveToJpg(){
    	
    	try {
			OutputStream os = new FileOutputStream("algorithmResultObj1.jpeg");
			
			ChartUtilities.writeChartAsJPEG(os, chart, 1000, 800);
			
			os.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("保存图片失败");
			
		}
    	
    }
    
    public void testCategoryDataSheet(){
    	
    	algorithmResultObj1 obj = new algorithmResultObj1();
    	
    	obj.x = "类型1";
    	
    	obj.y = 20;
    	
    	obj.type = "a";
    	
    	Vector<algorithmResultObj1> resultObjVec = new Vector<algorithmResultObj1>();
    	
    	resultObjVec.add(obj);
    	
    	algorithmResultObj1 obj2 = new algorithmResultObj1();
    	
    	obj2.x = "类型2";
    	
    	obj2.y = 35;
    	
    	obj2.type = "b";
    	
    	resultObjVec.add(obj2);
    	
    	CategoryDataSheet.Instance().show(resultObjVec, null);
    	
    }


}
