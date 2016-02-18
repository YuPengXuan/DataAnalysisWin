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

	//������ - ����ֵ
	 public void takeAction(){
		 
		 try{
			 
		     createWindow();
		     
		 }
		 catch(Exception e){
			 
			 System.out.print("�����������ʧ��");
			 //JOptionPane.showMessageDialog(null,"�÷�Χ�ڲ����ڵ㣡","��ʾ",JOptionPane.INFORMATION_MESSAGE);
		 }
		 
	 }
	 
	 //������ - ����ֵ
	 public void createWindow(){
		 
		frame = new JFrame("��������");
		
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
		
		JLabel a = new JLabel("�������");
		
		a.setFont(new Font("Dialog",1,20));
		
		contentPane2.add(a);
		
		JLabel b = new JLabel("�������:");
		
		contentPane2.add(b);
		
		JLabel k = new JLabel(df.format(outPutMap.get(CommonConfig.MAX_PROFIT))+"��");
		
		contentPane2.add(k);
		
		JLabel c = new JLabel("F0: "+df.format(outPutMap.get(CommonConfig.MAX_F0)));
		
		contentPane2.add(c);
		
		JLabel d = new JLabel("F1: "+df.format(outPutMap.get(CommonConfig.MAX_F1)));
		
		contentPane2.add(d);
		
		JLabel e = new JLabel("F2: "+df.format(outPutMap.get(CommonConfig.MAX_F2)));
		
		contentPane2.add(e);
		
		JLabel f = new JLabel("Ӫ������: ");
		
		contentPane2.add(f);
		
		JLabel l = new JLabel(df.format(outPutMap.get(CommonConfig.MAX_SALE_PEOPLE))+"��");
		
		contentPane2.add(l);
		
		JLabel g = new JLabel("Ԥ�Ƴɹ�����: ");
		
		contentPane2.add(g);
		
		JLabel h = new JLabel(df.format(outPutMap.get(CommonConfig.MAX_PRED_PEOPLE))+"��");
		
		contentPane2.add(h);
		
		JLabel i = new JLabel("Ԥ�ƴ�������: ");
		
		contentPane2.add(i);
		
		JLabel j = new JLabel(df.format(outPutMap.get(CommonConfig.MAX_PRED_DIST_PEOPLE))+"��");
		
		contentPane2.add(j);
		
		
		JLabel m = new JLabel("�漰Ҷ�ӽڵ������");

		contentPane2.add(m);
		
		JLabel n = new JLabel(""+df2.format(outPutMap.get(CommonConfig.MAX_LEAF_NODE_NUM)));
		
		contentPane2.add(n);

		bt = new JButton("���Ӫ������");
		
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
	        // ����JFreeChart����ChartFactory.createXYLineChart      
	        JFreeChart jfreechart = ChartFactory.createXYLineChart("������ - ����ֵ��ϵͼ", // ����      
	                "������ - F0", // categoryAxisLabel ��category�ᣬ���ᣬX���ǩ��      
	                "����ֵ - P (��)", // valueAxisLabel��value�ᣬ���ᣬY��ı�ǩ��      
	                dataset, // dataset      
	                PlotOrientation.VERTICAL,   
	                true, // legend      
	                true, // tooltips      
	                false); // URLs      
	 
	        // ʹ��CategoryPlot���ø��ֲ������������ÿ���ʡ�ԡ�      
	        XYPlot plot = (XYPlot) jfreechart.getPlot();      
	        // ����ɫ ͸����      
	        plot.setBackgroundAlpha(0.5f);      
	        // ǰ��ɫ ͸����      
	        plot.setForegroundAlpha(0.5f);      
	        // �������ÿ��Բο�XYPlot��      
	        
	        
	        jfreechart.getLegend().setItemFont(new Font("����", Font.BOLD+Font.PLAIN, 12));
			
	        jfreechart.setTitle(new TextTitle("������ - ����ֵ��ϵͼ",new Font("����",Font.BOLD+Font.ITALIC,20)));
					
			plot.setRangeGridlinePaint(Color.white);
			
			ValueAxis categoryAxis=plot.getDomainAxis();//��ú�����
			   
	        categoryAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD+Font.PLAIN, 11));
	        
	        categoryAxis.setLabelFont(new Font("����",Font.BOLD,12));//���ú���������       
			
			NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
					
			rangeAxis.setAutoRangeIncludesZero(true);
			
			rangeAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD+Font.PLAIN, 12)); 
			
			rangeAxis.setLabelFont(new Font("����", Font.BOLD+Font.PLAIN, 12));
	        
	        
	        
	        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)plot.getRenderer(); 
	        xylineandshaperenderer.setBaseShapesVisible(true);
	        xylineandshaperenderer.setBaseShapesFilled(true);
	        //xylineandshaperenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
	        //xylineandshaperenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
	        xylineandshaperenderer.setBaseItemLabelsVisible(true);// ��ʾÿ�����ϵ�����ֵ����ʾ����,���ݱ�ǩ�Ƿ�ɼ�
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
	        // ����JFreeChart����ChartFactory.createXYLineChart      
	        JFreeChart jfreechart = ChartFactory.createXYLineChart("������ - �ɹ��ʹ�ϵͼ", // ����      
	                "������ - F0", // categoryAxisLabel ��category�ᣬ���ᣬX���ǩ��      
	                "�ɹ��� - F2", // valueAxisLabel��value�ᣬ���ᣬY��ı�ǩ��      
	                dataset, // dataset      
	                PlotOrientation.VERTICAL,   
	                true, // legend      
	                true, // tooltips      
	                false); // URLs      
	 
	        // ʹ��CategoryPlot���ø��ֲ������������ÿ���ʡ�ԡ�      
	        XYPlot plot = (XYPlot) jfreechart.getPlot();      
	        // ����ɫ ͸����      
	        plot.setBackgroundAlpha(0.5f);      
	        // ǰ��ɫ ͸����      
	        plot.setForegroundAlpha(0.5f);      
	        // �������ÿ��Բο�XYPlot��      
	        
            jfreechart.getLegend().setItemFont(new Font("����", Font.BOLD+Font.PLAIN, 12));
			
	        jfreechart.setTitle(new TextTitle("������ - �ɹ��ʹ�ϵͼ",new Font("����",Font.BOLD+Font.ITALIC,20)));
					
			plot.setRangeGridlinePaint(Color.white);
			
			ValueAxis categoryAxis=plot.getDomainAxis();//��ú�����
			   
	        categoryAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD+Font.PLAIN, 11));
	        
	        categoryAxis.setLabelFont(new Font("����",Font.BOLD,12));//���ú���������       
			
			NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
					
			rangeAxis.setAutoRangeIncludesZero(true);
			
			rangeAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD+Font.PLAIN, 12)); 
			
			rangeAxis.setLabelFont(new Font("����", Font.BOLD+Font.PLAIN, 12));
	        
	        
	        
	        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)plot.getRenderer(); 
	        xylineandshaperenderer.setBaseShapesVisible(true);
	        xylineandshaperenderer.setBaseShapesFilled(true);
	        //xylineandshaperenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
	        //xylineandshaperenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
	        xylineandshaperenderer.setBaseItemLabelsVisible(true);// ��ʾÿ�����ϵ�����ֵ����ʾ����,���ݱ�ǩ�Ƿ�ɼ�
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
	        // ����JFreeChart����ChartFactory.createXYLineChart      
	        JFreeChart jfreechart = ChartFactory.createXYLineChart("Ӫ������ - ����ֵ��ϵͼ", // ����      
	                "Ӫ������ (��)", // categoryAxisLabel ��category�ᣬ���ᣬX���ǩ��      
	                "����ֵ - P (��)", // valueAxisLabel��value�ᣬ���ᣬY��ı�ǩ��      
	                dataset, // dataset      
	                PlotOrientation.VERTICAL,   
	                true, // legend      
	                true, // tooltips      
	                false); // URLs      
	 
	        // ʹ��CategoryPlot���ø��ֲ������������ÿ���ʡ�ԡ�      
	        XYPlot plot = (XYPlot) jfreechart.getPlot();      
	        // ����ɫ ͸����      
	        plot.setBackgroundAlpha(0.5f);      
	        // ǰ��ɫ ͸����      
	        plot.setForegroundAlpha(0.5f);      
	        // �������ÿ��Բο�XYPlot��      
	        
            jfreechart.getLegend().setItemFont(new Font("����", Font.BOLD+Font.PLAIN, 12));
			
	        jfreechart.setTitle(new TextTitle("ROC����ͼ",new Font("Ӫ������ - ����ֵ��ϵͼ",Font.BOLD+Font.ITALIC,20)));
					
			plot.setRangeGridlinePaint(Color.white);
			
			ValueAxis categoryAxis=plot.getDomainAxis();//��ú�����
			   
	        categoryAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD+Font.PLAIN, 11));
	        
	        categoryAxis.setLabelFont(new Font("����",Font.BOLD,12));//���ú���������       
			
			NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
					
			rangeAxis.setAutoRangeIncludesZero(true);
			
			rangeAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD+Font.PLAIN, 12)); 
			
			rangeAxis.setLabelFont(new Font("����", Font.BOLD+Font.PLAIN, 12));
	        
	        
	        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)plot.getRenderer(); 
	        xylineandshaperenderer.setBaseShapesVisible(true);
	        xylineandshaperenderer.setBaseShapesFilled(true);
	        //xylineandshaperenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
	        //xylineandshaperenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
	        xylineandshaperenderer.setBaseItemLabelsVisible(true);// ��ʾÿ�����ϵ�����ֵ����ʾ����,���ݱ�ǩ�Ƿ�ɼ�
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
	        // ����JFreeChart����ChartFactory.createXYLineChart      
	        JFreeChart jfreechart = ChartFactory.createXYLineChart("Ӫ������ - Ԥ�Ƴɹ�������ϵͼ", // ����      
	                "Ӫ������ (��)", // categoryAxisLabel ��category�ᣬ���ᣬX���ǩ��      
	                "Ԥ�Ƴɹ����� (��)", // valueAxisLabel��value�ᣬ���ᣬY��ı�ǩ��      
	                dataset, // dataset      
	                PlotOrientation.VERTICAL,   
	                true, // legend      
	                true, // tooltips      
	                false); // URLs      
	 
	        // ʹ��CategoryPlot���ø��ֲ������������ÿ���ʡ�ԡ�      
	        XYPlot plot = (XYPlot) jfreechart.getPlot();      
	        // ����ɫ ͸����      
	        plot.setBackgroundAlpha(0.5f);      
	        // ǰ��ɫ ͸����      
	        plot.setForegroundAlpha(0.5f);      
	        // �������ÿ��Բο�XYPlot��      
	        
            jfreechart.getLegend().setItemFont(new Font("����", Font.BOLD+Font.PLAIN, 12));
			
	        jfreechart.setTitle(new TextTitle("Ӫ������ - Ԥ�Ƴɹ�������ϵͼ",new Font("����",Font.BOLD+Font.ITALIC,20)));
					
			plot.setRangeGridlinePaint(Color.white);
			
			ValueAxis categoryAxis=plot.getDomainAxis();//��ú�����
			   
	        categoryAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD+Font.PLAIN, 11));
	        
	        categoryAxis.setLabelFont(new Font("����",Font.BOLD,12));//���ú���������       
			
			NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
					
			rangeAxis.setAutoRangeIncludesZero(true);
			
			rangeAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD+Font.PLAIN, 12)); 
			
			rangeAxis.setLabelFont(new Font("����", Font.BOLD+Font.PLAIN, 12));
	        	       	        
	        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)plot.getRenderer(); 
	        xylineandshaperenderer.setBaseShapesVisible(true);
	        //xylineandshaperenderer.setShapesVisible(true); //���ݵ�ɼ� 
	        xylineandshaperenderer.setBaseShapesFilled(true);
	        //xylineandshaperenderer.setShapesFilled(true); //���ݵ㱻��伴���ǿ��ĵ�
	        //xylineandshaperenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
	        //xylineandshaperenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
	        xylineandshaperenderer.setBaseItemLabelsVisible(true);// ��ʾÿ�����ϵ�����ֵ����ʾ����,���ݱ�ǩ�Ƿ�ɼ�
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
					System.out.println("�����ļ�ʧ��");
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
			
			fileContent.add(title + "����");
			
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
				
				fileContent.add("����Ӫ����Ⱥ" + (i+1));
				
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
										
				fileContent.add("�ڵ��ţ�" + obj.nodeNum);
				
				fileContent.add("��Ⱥ������" + obj.condition);
				 
                fileContent.add("Ӫ��������" + obj.salePNum);
                
                totalSalePNum += obj.salePNum;
                
                 treeNodeResultObj treeRootObj = GenerateTreeByLeaf.getTreeNodeByLevelVec().get(0).get(0);
                 
                 double ratio = obj.salePNum / (treeRootObj.conditionMeet + treeRootObj.conditionNotMeet);
                
                fileContent.add("��Ⱥռ�ȣ�" + ratio);
                
                totalRatio += ratio;
                
                double F0 = obj.predSuccPNum / treeRootObj.conditionMeet;
                
                fileContent.add("������F0��" + F0);
                
                totalF0 += F0;
                
                double F1  = obj.distPNum/ treeRootObj.conditionNotMeet;
                
                fileContent.add("������F1��" + F1);
                
                totalF1 += F1;
                
                fileContent.add("�ɹ��ʣ�" + obj.F2);
                
                sucTotalPNum += obj.predSuccPNum;
                
                fileContent.add("��Ⱥ������" + obj.profitVal);
                
                totalProfit += obj.profitVal;
                
                fileContent.add("����Ӫ������" + (obj.profitVal/obj.salePNum));
				 				 						
			}
			
			fileContent.add("");
			
			fileContent.add("�ϼ�");
			
			fileContent.add("Ӫ��������" + totalSalePNum);           
         
           fileContent.add("��Ⱥռ�ȣ�" + totalRatio);
           
           fileContent.add("������F0��" + totalF0);
           
           fileContent.add("������F1��" + totalF1);
           
           totalF2 = sucTotalPNum / totalSalePNum;
           
           fileContent.add("�ɹ��ʣ�" + totalF2);
           
           fileContent.add("��Ⱥ������" + totalProfit);
           
           fileContent.add("����Ӫ������" + (totalProfit/totalSalePNum));
						
			return fileContent;
			
		}
		
		private String getTitle(ROC_TYPE rocType){
			String title = null;
			switch(rocType){
		       case ROC_EXT:
		    	   title = "���Ӫ��";
		    	   break;
		       case ROC_SMS:
		    	   title ="����Ӫ��";
		           break;
		       case ROC_CUST:
		    	   title ="�ͻ�����";
		           break;
		       case ROC_APP:
		    	   title ="APP�ͻ�����";
		    	   break;
		       case ROC_WARN:
		    	   title = "Ͷ�߿ͻ�Ԥ��";
		    	   break;
		       default:
		    	   System.out.println("����ROC���Ͳ���ȷ��");
		    	   return null;
		 
		     }
				return title;
		}
	
}

	
