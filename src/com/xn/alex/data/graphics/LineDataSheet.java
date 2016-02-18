package com.xn.alex.data.graphics;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.xn.alex.data.common.CommonConfig.ROC_TYPE;
import com.xn.alex.data.resultobj.ProfitResultNode;
import com.xn.alex.data.resultobj.algorithmResultObj2;
import com.xn.alex.data.roc.ProfitActionFactory;
import com.xn.alex.data.roc.ProfitModelInterface;
import com.xn.alex.data.rule.RocParameterGenerate;

public class LineDataSheet extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5L;
	
	private JFreeChart chart = null;
    	
	private String preTitle = "";
	
	private JPanel contentPane;
	
	private JPanel northPanel;
	
    private JPanel southPanel;
    
    private JButton profitButton;
    
    private String xLabel = "";
    
	private String yLabel = "";
	
	private ROC_TYPE rocType;
	
	private Object resultObj;
	
	private Vector<algorithmResultObj2> m_resultObjVec;
	
	public Object getResultObj() {
		return resultObj;
	}

	public void setResultObj(Object resultObj) {
		this.resultObj = resultObj;
	}
	
	public ROC_TYPE getRocType() {
		return rocType;
	}

	public void setRocType(ROC_TYPE rocType) {
		this.rocType = rocType;
	}
	
	public String getxLabel() {
		return xLabel;
	}

	public void setxLabel(String xLabel) {
		this.xLabel = xLabel;
	}
	
	public String getyLabel() {
		return yLabel;
	}

	public void setyLabel(String yLabel) {
		this.yLabel = yLabel;
	}
			
	public String getPreTitle() {
		return preTitle;
	}

	public void setPreTitle(String title) {
		preTitle = title;
	}
		

	public LineDataSheet(String title){
		
        super(title + "ROC曲线图");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
	
	
	private void clearPreviousVar(){
		this.dispose();
		chart = null;
	}
	
	
	public void show(Vector<algorithmResultObj2> resultObjVec){
		m_resultObjVec = resultObjVec;
		
		clearPreviousVar();
		
		contentPane = new JPanel();
		
		this.setContentPane(contentPane);		
		contentPane.setLayout(new BorderLayout(0, 0));
		
		northPanel = createPanel(resultObjVec);
		
		contentPane.add(northPanel,BorderLayout.CENTER);
		
		southPanel = new JPanel();
		
		profitButton = new JButton("利润分析");
		profitButton.addActionListener(this);
		
		southPanel.add(profitButton);
		
				
		contentPane.add(southPanel,BorderLayout.SOUTH);
		
        //this.setContentPane(createPanel(resultObjVec));
		
		this.pack();
		
		this.setVisible(true);
		
		this.setLocationRelativeTo(null);
		
	}
	
	private JPanel createPanel(Vector<algorithmResultObj2> resultObjVec){
		
		chart =createChart(createLineDataset(resultObjVec));
		
        return new ChartPanel(chart); //将chart对象放入Panel面板中去，ChartPanel类已继承Jpanel
		
	}
	
	private JFreeChart createChart(XYDataset dataset){
		
		JFreeChart chart = ChartFactory.createXYLineChart(
				"ROC曲线图",
				xLabel, 
				yLabel, 
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false);
		
		chart.getLegend().setItemFont(new Font("宋体", Font.BOLD+Font.PLAIN, 12));
		
		chart.setTitle(new TextTitle("ROC曲线图",new Font("宋体",Font.BOLD+Font.ITALIC,20)));
		
		XYPlot plot = (XYPlot)chart.getPlot();
		
		//plot.setRangeGridlinePaint(Color.white);
		
		plot.setBackgroundAlpha(0.5f);
		
		plot.setForegroundAlpha(0.5f);
		
		ValueAxis categoryAxis=plot.getDomainAxis();//获得横坐标
		   
        categoryAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD+Font.PLAIN, 11));
        
        categoryAxis.setLabelFont(new Font("宋体",Font.BOLD,12));//设置横坐标字体       
		
		NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
				
		rangeAxis.setAutoRangeIncludesZero(true);
		
		rangeAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD+Font.PLAIN, 12)); 
		
		rangeAxis.setLabelFont(new Font("宋体", Font.BOLD+Font.PLAIN, 12));
		
		 XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)plot.getRenderer();
		 
		 xylineandshaperenderer.setBaseShapesVisible(true);
		 
		 xylineandshaperenderer.setBaseShapesFilled(true);
		 
		 xylineandshaperenderer.setBaseItemLabelsVisible(true);
		 
		 xylineandshaperenderer.setDrawOutlines(true);
		
		return chart;
		
	}
	
	private XYDataset createLineDataset(Vector<algorithmResultObj2> resultObjVec){
		
		 XYSeriesCollection dataset = new XYSeriesCollection();
				
		try{
			
		
		    Map<String, XYSeries> xySeriesMap = new HashMap<String, XYSeries>();
		
		    for(int i=0;i<resultObjVec.size();i++){
			
			    algorithmResultObj2 obj = resultObjVec.get(i);
			
			    if(null == obj){
				    continue;
			    }
			
			    String type = obj.type;
			
			    if(null == xySeriesMap.get(type)){
				    XYSeries xySeries = new XYSeries(type);
				    
				    xySeriesMap.put(type, xySeries);
			    }			
			
			    XYSeries currentSeries = xySeriesMap.get(type);
			    //currentSeries.add(obj.y, obj.x);
			    currentSeries.add(obj.x, obj.y);
		    }
		
		    for(Map.Entry<String, XYSeries> entry : xySeriesMap.entrySet()){
		    	
		    	XYSeries addXySeries = entry.getValue();
		    	
		    	dataset.addSeries(addXySeries);
			
		    }
		
		}
		catch(Exception e){
			
			e.printStackTrace();
			
			return null;
			
		}
		
		return dataset;
		
	}
	
     public void testCategoryLineDataSheet(){
    	
    	algorithmResultObj2 obj = new algorithmResultObj2();
    	
    	obj.x = 1;
    	
    	obj.y = 20;
    	
    	obj.type = "a";
    	
    	Vector<algorithmResultObj2> resultObjVec = new Vector<algorithmResultObj2>();
    	
    	resultObjVec.add(obj);
    	
    	algorithmResultObj2 obj2 = new algorithmResultObj2();
    	
    	obj2.x = 2;
    	
    	obj2.y = 35;
    	
    	obj2.type = "a";
    	
    	resultObjVec.add(obj2);
    	
    	show(resultObjVec);
    	
    }

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if(event.getSource() == profitButton){
			
			 ProfitModelInterface profitAction = ProfitActionFactory.getNewProfitAction(rocType);
			 
			 profitAction.setResultObjVec(m_resultObjVec);
			 
			 if(null == resultObj){
				 System.out.println("计算利润值失败");
				 return;
			 }
			 
			 Vector<ProfitResultNode> profitVec = ((RocParameterGenerate)resultObj).getProfitVec();
			 
			 profitAction.setpResultVec(profitVec);
			 
			 profitAction.setRocType(rocType);
			  
			 profitAction.takeAction();
			 
		}
		
	}
}
