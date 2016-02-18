package com.xn.alex.data.roc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

import com.xn.alex.data.common.CommonConfig;
import com.xn.alex.data.common.CommonConfig.ROC_TYPE;
import com.xn.alex.data.file.SaveToTxtFile;
import com.xn.alex.data.resultobj.ProfitResultNode;
import com.xn.alex.data.resultobj.algorithmResultObj2;
import com.xn.alex.data.resultobj.treeNodeResultObj;
import com.xn.alex.data.rule.GenerateTreeByLeaf;

public class ProfitGraph2 implements ActionListener{

	private JFrame frame;
	
	private JPanel contentPane1;
	
	private JPanel contentPane2;
	
	private JButton bt;
	
    private Vector<algorithmResultObj2> yToProfitVec = null;
	
	private Vector<algorithmResultObj2> yToSuccRateVec = null;
	
	private Vector<algorithmResultObj2> salePToProfitVec = null;
	
	private Vector<algorithmResultObj2> salePToPredicPvec = null;
	
	private Vector<ProfitResultNode> m_pResultVec = null;
	
	private Vector<algorithmResultObj2> resultVec = null;
	
	private ROC_TYPE rocType;
	
	private Map<String, Double> outPutMap = null;
	
	public ProfitGraph2(Vector<algorithmResultObj2> yToProfitVec, Vector<algorithmResultObj2> yToSuccRateVec, Vector<algorithmResultObj2> salePToProfitVec, Vector<algorithmResultObj2> salePToPredicPvec, Vector<ProfitResultNode> pResultVec, Map<String, Double> outPutMap, ROC_TYPE rocType, Vector<algorithmResultObj2> resultVec){
		this.yToProfitVec = yToProfitVec;
		this.yToSuccRateVec = yToSuccRateVec;
		this.salePToProfitVec = salePToProfitVec;
		this.salePToPredicPvec = salePToPredicPvec;
		this.m_pResultVec = pResultVec;
		this.outPutMap = outPutMap;
		this.rocType = rocType;
		this.resultVec = resultVec;
	}

	//命中率 - 利润值
	 public void takeAction(){
		 
		 try{
			 
		     createWindow();
		     
		 }
		 catch(Exception e){
			 
			 System.out.print("生成利润分析失败");
			 //JOptionPane.showMessageDialog(null,"该范围内不存在点！","提示",JOptionPane.INFORMATION_MESSAGE);
		 }
		 
	 }
	 
	 //命中率 - 利润值
	 public void createWindow(){
		 
		frame = new JFrame("利润曲线");
		
		//frame.setLayout(new GridLayout(2, 2));
		 
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.setLayout(new BorderLayout());
		
		contentPane1 = new JPanel();
		
		contentPane1.setLayout(new GridLayout(0, 2));
		 
		contentPane2 = new JPanel();
		
		//1
		JFreeChart chart1 = createChart1(createXYDataset1());
		 
		ChartPanel localChartPanel1 = new ChartPanel(chart1, false);
		 
		contentPane1.add(localChartPanel1);
		
		frame.add(contentPane1,BorderLayout.CENTER);
		
		//2
		JFreeChart chart2 = createChart2(createXYDataset2());
		
		ChartPanel localChartPanel2 = new ChartPanel(chart2, false);
		 
		contentPane1.add(localChartPanel2);
		
		frame.add(contentPane1,BorderLayout.CENTER);
		
		//3
		JFreeChart chart3 = createChart3(createXYDataset3());
		
		ChartPanel localChartPanel3 = new ChartPanel(chart3, false);
		 
		contentPane1.add(localChartPanel3);
		
		frame.add(contentPane1,BorderLayout.CENTER);
		
		//4
		JFreeChart chart4 = createChart4(createXYDataset4());
		
		ChartPanel localChartPanel4 = new ChartPanel(chart4, false);
		 
		contentPane1.add(localChartPanel4);
		
		frame.add(contentPane1,BorderLayout.CENTER);
		
		//
		DecimalFormat df = new DecimalFormat("#0.0000");
		
		DecimalFormat df2 = new DecimalFormat("#.#");
		
		contentPane2 = new JPanel();
		
		contentPane2.setLayout(new BoxLayout(contentPane2, 1));
		
		JLabel a = new JLabel("利润分析");
		
		a.setFont(new Font("Dialog",1,20));
		
		contentPane2.add(a);
		
		JLabel b = new JLabel("最大利润:");
		
		contentPane2.add(b);
		
		JLabel k = new JLabel(df.format(outPutMap.get(CommonConfig.MAX_PROFIT))+"万");
		
		contentPane2.add(k);
		
		JLabel c = new JLabel("F0: "+df.format(outPutMap.get(CommonConfig.MAX_F0)));
		
		contentPane2.add(c);
		
		JLabel d = new JLabel("F1: "+df.format(outPutMap.get(CommonConfig.MAX_F1)));
		
		contentPane2.add(d);
		
		JLabel e = new JLabel("F2: "+df.format(outPutMap.get(CommonConfig.MAX_F2)));
		
		contentPane2.add(e);
		
		JLabel f = new JLabel("营销人数: ");
		
		contentPane2.add(f);
		
		JLabel l = new JLabel(df.format(outPutMap.get(CommonConfig.MAX_SALE_PEOPLE))+"万");
		
		contentPane2.add(l);
		
		JLabel g = new JLabel("预计成功人数: ");
		
		contentPane2.add(g);
		
		JLabel h = new JLabel(df.format(outPutMap.get(CommonConfig.MAX_PRED_PEOPLE))+"万");
		
		contentPane2.add(h);
		
		JLabel i = new JLabel("预计打扰人数: ");
		
		contentPane2.add(i);
		
		JLabel j = new JLabel(df.format(outPutMap.get(CommonConfig.MAX_PRED_DIST_PEOPLE))+"万");
		
		contentPane2.add(j);
		
		
		JLabel m = new JLabel("涉及叶子节点个数：");

		contentPane2.add(m);
		
		JLabel n = new JLabel(""+df2.format(outPutMap.get(CommonConfig.MAX_LEAF_NODE_NUM)));
		
		contentPane2.add(n);

		bt = new JButton("输出营销方案");
		
		bt.addActionListener(this);
		
		contentPane2.add(bt);
		
		frame.add(contentPane2,BorderLayout.EAST);
		
		//
	    frame.pack();

	    frame.setLocationRelativeTo(null);
	    
	    frame.setLocation(100, 100);
	     
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    frame.setSize(screenSize.width-100, screenSize.height-100);
	    //frame.setSize(1500,800);
	     
	    frame.setResizable(true);

        frame.setVisible(true);
        
        frame.setLocationRelativeTo(null);
	 }
	 	    
		public static JFreeChart createChart1(XYDataset dataset) {      
	        // 创建JFreeChart对象：ChartFactory.createXYLineChart      
	        JFreeChart jfreechart = ChartFactory.createXYLineChart("命中率 - 利润值关系图", // 标题      
	                "命中率 - F0", // categoryAxisLabel （category轴，横轴，X轴标签）      
	                "利润值 - P (万)", // valueAxisLabel（value轴，纵轴，Y轴的标签）      
	                dataset, // dataset      
	                PlotOrientation.VERTICAL,   
	                true, // legend      
	                true, // tooltips      
	                false); // URLs      
	 
	        // 使用CategoryPlot设置各种参数。以下设置可以省略。      
	        XYPlot plot = (XYPlot) jfreechart.getPlot();      
	        // 背景色 透明度      
	        plot.setBackgroundAlpha(0.5f);      
	        // 前景色 透明度      
	        plot.setForegroundAlpha(0.5f);      
	        // 其它设置可以参考XYPlot类      
	        
	        
	        jfreechart.getLegend().setItemFont(new Font("宋体", Font.BOLD+Font.PLAIN, 12));
			
	        jfreechart.setTitle(new TextTitle("命中率 - 利润值关系图",new Font("宋体",Font.BOLD+Font.ITALIC,20)));
					
			plot.setRangeGridlinePaint(Color.white);
			
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
	        //xylineandshaperenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
	        //xylineandshaperenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
	        xylineandshaperenderer.setBaseItemLabelsVisible(true);// 显示每个点上的数据值的提示工具,数据标签是否可见
	        xylineandshaperenderer.setDrawOutlines(true);
	        //((ToolTipTagFragmentGenerator) xylineandshaperenderer).generateToolTipFragment("");
	 
	        return jfreechart;      
	    }
		
	    private XYDataset createXYDataset1() { 
	    	
	    	 XYSeries xyseries1 = new XYSeries("Line-1");    
	    	 
	         for (int i = 0; i < yToProfitVec.size(); i++) {
				
	        	 xyseries1.add(yToProfitVec.get(i).x.floatValue(), yToProfitVec.get(i).y.floatValue());
			}   

	    
	 
	        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();      
	 
	        xySeriesCollection.addSeries(xyseries1);   
	 
	        return xySeriesCollection;     
	    }
	    
	    public static JFreeChart createChart2(XYDataset dataset) {      
	        // 创建JFreeChart对象：ChartFactory.createXYLineChart      
	        JFreeChart jfreechart = ChartFactory.createXYLineChart("命中率 - 成功率关系图", // 标题      
	                "命中率 - F0", // categoryAxisLabel （category轴，横轴，X轴标签）      
	                "成功率 - F2", // valueAxisLabel（value轴，纵轴，Y轴的标签）      
	                dataset, // dataset      
	                PlotOrientation.VERTICAL,   
	                true, // legend      
	                true, // tooltips      
	                false); // URLs      
	 
	        // 使用CategoryPlot设置各种参数。以下设置可以省略。      
	        XYPlot plot = (XYPlot) jfreechart.getPlot();      
	        // 背景色 透明度      
	        plot.setBackgroundAlpha(0.5f);      
	        // 前景色 透明度      
	        plot.setForegroundAlpha(0.5f);      
	        // 其它设置可以参考XYPlot类      
	        
            jfreechart.getLegend().setItemFont(new Font("宋体", Font.BOLD+Font.PLAIN, 12));
			
	        jfreechart.setTitle(new TextTitle("命中率 - 成功率关系图",new Font("宋体",Font.BOLD+Font.ITALIC,20)));
					
			plot.setRangeGridlinePaint(Color.white);
			
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
	        //xylineandshaperenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
	        //xylineandshaperenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
	        xylineandshaperenderer.setBaseItemLabelsVisible(true);// 显示每个点上的数据值的提示工具,数据标签是否可见
	        xylineandshaperenderer.setDrawOutlines(true);
	        //((ToolTipTagFragmentGenerator) xylineandshaperenderer).generateToolTipFragment("");
	 
	        return jfreechart;      
	    }
		
	    private XYDataset createXYDataset2() { 
	    	
	    	 XYSeries xyseries1 = new XYSeries("Line-2");    
	    	 
	         for (int i = 0; i < yToSuccRateVec.size(); i++) {
				
	        	 xyseries1.add(yToSuccRateVec.get(i).x.floatValue(), yToSuccRateVec.get(i).y.floatValue());
			}   

	    
	 
	        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();      
	 
	        xySeriesCollection.addSeries(xyseries1);   
	 
	        return xySeriesCollection;     
	    }
	    
	    public static JFreeChart createChart3(XYDataset dataset) {      
	        // 创建JFreeChart对象：ChartFactory.createXYLineChart      
	        JFreeChart jfreechart = ChartFactory.createXYLineChart("营销人数 - 利润值关系图", // 标题      
	                "营销人数 (万)", // categoryAxisLabel （category轴，横轴，X轴标签）      
	                "利润值 - P (万)", // valueAxisLabel（value轴，纵轴，Y轴的标签）      
	                dataset, // dataset      
	                PlotOrientation.VERTICAL,   
	                true, // legend      
	                true, // tooltips      
	                false); // URLs      
	 
	        // 使用CategoryPlot设置各种参数。以下设置可以省略。      
	        XYPlot plot = (XYPlot) jfreechart.getPlot();      
	        // 背景色 透明度      
	        plot.setBackgroundAlpha(0.5f);      
	        // 前景色 透明度      
	        plot.setForegroundAlpha(0.5f);      
	        // 其它设置可以参考XYPlot类      
	        
            jfreechart.getLegend().setItemFont(new Font("宋体", Font.BOLD+Font.PLAIN, 12));
			
	        jfreechart.setTitle(new TextTitle("ROC曲线图",new Font("营销人数 - 利润值关系图",Font.BOLD+Font.ITALIC,20)));
					
			plot.setRangeGridlinePaint(Color.white);
			
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
	        //xylineandshaperenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
	        //xylineandshaperenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
	        xylineandshaperenderer.setBaseItemLabelsVisible(true);// 显示每个点上的数据值的提示工具,数据标签是否可见
	        xylineandshaperenderer.setDrawOutlines(true);
	        //((ToolTipTagFragmentGenerator) xylineandshaperenderer).generateToolTipFragment("");
	 
	        return jfreechart;      
	    }
		
	    private XYDataset createXYDataset3() { 
	    	
	    	 XYSeries xyseries1 = new XYSeries("Line-3");    
	    	 
	         for (int i = 0; i < salePToProfitVec.size(); i++) {
				
	        	 xyseries1.add(salePToProfitVec.get(i).x.floatValue(), salePToProfitVec.get(i).y.floatValue());
			}   

	    
	 
	        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();      
	 
	        xySeriesCollection.addSeries(xyseries1);   
	 
	        return xySeriesCollection;     
	    }
	    
	    public static JFreeChart createChart4(XYDataset dataset) {      
	        // 创建JFreeChart对象：ChartFactory.createXYLineChart      
	        JFreeChart jfreechart = ChartFactory.createXYLineChart("营销人数 - 预计成功人数关系图", // 标题      
	                "营销人数 (万)", // categoryAxisLabel （category轴，横轴，X轴标签）      
	                "预计成功人数 (万)", // valueAxisLabel（value轴，纵轴，Y轴的标签）      
	                dataset, // dataset      
	                PlotOrientation.VERTICAL,   
	                true, // legend      
	                true, // tooltips      
	                false); // URLs      
	 
	        // 使用CategoryPlot设置各种参数。以下设置可以省略。      
	        XYPlot plot = (XYPlot) jfreechart.getPlot();      
	        // 背景色 透明度      
	        plot.setBackgroundAlpha(0.5f);      
	        // 前景色 透明度      
	        plot.setForegroundAlpha(0.5f);      
	        // 其它设置可以参考XYPlot类      
	        
            jfreechart.getLegend().setItemFont(new Font("宋体", Font.BOLD+Font.PLAIN, 12));
			
	        jfreechart.setTitle(new TextTitle("营销人数 - 预计成功人数关系图",new Font("宋体",Font.BOLD+Font.ITALIC,20)));
					
			plot.setRangeGridlinePaint(Color.white);
			
			ValueAxis categoryAxis=plot.getDomainAxis();//获得横坐标
			   
	        categoryAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD+Font.PLAIN, 11));
	        
	        categoryAxis.setLabelFont(new Font("宋体",Font.BOLD,12));//设置横坐标字体       
			
			NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
					
			rangeAxis.setAutoRangeIncludesZero(true);
			
			rangeAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD+Font.PLAIN, 12)); 
			
			rangeAxis.setLabelFont(new Font("宋体", Font.BOLD+Font.PLAIN, 12));
	        	       	        
	        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)plot.getRenderer(); 
	        xylineandshaperenderer.setBaseShapesVisible(true);
	        //xylineandshaperenderer.setShapesVisible(true); //数据点可见 
	        xylineandshaperenderer.setBaseShapesFilled(true);
	        //xylineandshaperenderer.setShapesFilled(true); //数据点被填充即不是空心点
	        //xylineandshaperenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
	        //xylineandshaperenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
	        xylineandshaperenderer.setBaseItemLabelsVisible(true);// 显示每个点上的数据值的提示工具,数据标签是否可见
	        xylineandshaperenderer.setDrawOutlines(true);
	        //((ToolTipTagFragmentGenerator) xylineandshaperenderer).generateToolTipFragment("");
	 
	        return jfreechart;      
	    }
		
	    private  XYDataset createXYDataset4() { 
	    	
	    	 XYSeries xyseries1 = new XYSeries("Line-4");    
	    	 
	         for (int i = 0; i < salePToPredicPvec.size(); i++) {
				
	        	 xyseries1.add(salePToPredicPvec.get(i).x.floatValue(), salePToPredicPvec.get(i).y.floatValue());
			}   
	 
	        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();      
	 
	        xySeriesCollection.addSeries(xyseries1);   
	 
	        return xySeriesCollection;     
	    }
	    
		 
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			if (e.getSource() == bt) {
				
				//SaveResultToExcel saveHandler = new SaveResultToExcel();
				
				//saveHandler.saveToFile();
				Vector<String> fileContent = constructTxtContent();
				
				if(null == fileContent){
					System.out.println("保存文件失败");
					return;
				}
				
				SaveToTxtFile.Instance().saveContentToFile(fileContent);
			}
			
		}
		
		private Vector<String> constructTxtContent(){
			Vector<String> fileContent = new Vector<String>();
			
			String title = getTitle(rocType);
			
			if(null == title){
				return null;
			}
			
			fileContent.add(title + "建议");
			
            fileContent.add("");
            
            double totalSalePNum = 0.0D;
            
            double sucTotalPNum = 0.0D;
            
            double totalRatio = 0.0D;
            
            double totalF0 = 0.0D;
            
            double totalF1 = 0.0D;
            
            double totalF2 = 0.0D;
            
            double totalProfit = 0.0D;
			//get two points of max Profit 
			for(int i=0;i<outPutMap.get(CommonConfig.MAX_LEAF_NODE_NUM);i++){
				algorithmResultObj2 resultObj = resultVec.get(i);
				
				fileContent.add("");
				
				fileContent.add("建议营销人群" + (i+1));
				
				ProfitResultNode obj = null;
				for(int j=0;j<m_pResultVec.size();j++){
					if(resultObj.nodeNum == m_pResultVec.get(j).nodeNum){
						obj = m_pResultVec.get(j);
						break;
					}
					
				}
				
				if(null == obj){
					continue;
				}
										
				fileContent.add("节点编号：" + obj.nodeNum);
				
				fileContent.add("人群特征：" + obj.condition);
				 
                fileContent.add("营销人数：" + obj.salePNum);
                
                totalSalePNum += obj.salePNum;
                
                 treeNodeResultObj treeRootObj = GenerateTreeByLeaf.getTreeNodeByLevelVec().get(0).get(0);
                 
                 double ratio = obj.salePNum / (treeRootObj.conditionMeet + treeRootObj.conditionNotMeet);
                
                fileContent.add("人群占比：" + ratio);
                
                totalRatio += ratio;
                
                double F0 = obj.predSuccPNum / treeRootObj.conditionMeet;
                
                fileContent.add("命中率F0：" + F0);
                
                totalF0 += F0;
                
                double F1  = obj.distPNum/ treeRootObj.conditionNotMeet;
                
                fileContent.add("误判率F1：" + F1);
                
                totalF1 += F1;
                
                fileContent.add("成功率：" + obj.F2);
                
                sucTotalPNum += obj.predSuccPNum;
                
                fileContent.add("人群总利润：" + obj.profitVal);
                
                totalProfit += obj.profitVal;
                
                fileContent.add("单次营销利润：" + (obj.profitVal/obj.salePNum));
				 				 						
			}
			
			fileContent.add("");
			
			fileContent.add("合计");
			
			fileContent.add("营销人数：" + totalSalePNum);           
         
           fileContent.add("人群占比：" + totalRatio);
           
           fileContent.add("命中率F0：" + totalF0);
           
           fileContent.add("误判率F1：" + totalF1);
           
           totalF2 = sucTotalPNum / totalSalePNum;
           
           fileContent.add("成功率：" + totalF2);
           
           fileContent.add("人群总利润：" + totalProfit);
           
           fileContent.add("单次营销利润：" + (totalProfit/totalSalePNum));
						
			return fileContent;
			
		}
		
		private String getTitle(ROC_TYPE rocType){
			String title = null;
			switch(rocType){
		       case ROC_EXT:
		    	   title = "外呼营销";
		    	   break;
		       case ROC_SMS:
		    	   title ="短信营销";
		           break;
		       case ROC_CUST:
		    	   title ="客户保有";
		           break;
		       case ROC_APP:
		    	   title ="APP客户保有";
		    	   break;
		       case ROC_WARN:
		    	   title = "投诉客户预警";
		    	   break;
		       default:
		    	   System.out.println("输入ROC类型不正确！");
		    	   return null;
		 
		     }
				return title;
		}
	
}

	
