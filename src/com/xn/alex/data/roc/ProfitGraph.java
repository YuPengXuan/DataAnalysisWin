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
import java.util.Collections;
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

import com.xn.alex.data.common.CommonConfig.ROC_TYPE;
import com.xn.alex.data.file.SaveToTxtFile;
import com.xn.alex.data.resultobj.ProfitResultNode;
import com.xn.alex.data.resultobj.treeNodeResultObj;
import com.xn.alex.data.rule.GenerateTreeByLeaf;

public class ProfitGraph implements ActionListener{
	
	private int maxProfitVecInd = 0;
	
	private ROC_TYPE rocType;
	
	private Vector<ProfitResultNode> showVec;
	
    private JFrame frame;
	
	private JPanel contentPane1;
	
	private JPanel contentPane2;
	
	private JButton exportBt;
	
	private enum CHART{F0_TO_PROFIT, F0_TO_SUCCRATE, SALEP_TO_PROFIT,SALTEP_TO_SUCCP};
	
	public ProfitGraph(Vector<ProfitResultNode> showVec,ROC_TYPE rocType, int maxProfitVecInd){
		this.showVec = showVec;
		this.rocType = rocType; 
		this.maxProfitVecInd = maxProfitVecInd;
	}

	
	public void show(){
		
        frame = new JFrame("��������");
		 
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.setLayout(new BorderLayout());
		
		contentPane1 = new JPanel();
		
		contentPane1.setLayout(new GridLayout(0, 2));
		
		//create 4 chart
		for(int i=0;i<4;i++){
			XYDataset xyDataSet = createXYDataset(i);
			
			JFreeChart chart = createChart(xyDataSet,i);
			 
			ChartPanel localChartPanel = new ChartPanel(chart, false);
			 
			contentPane1.add(localChartPanel);
			
			frame.add(contentPane1,BorderLayout.CENTER);
		}
		
		ProfitResultNode obj = showVec.get(maxProfitVecInd);
		
        DecimalFormat df = new DecimalFormat("#0.0000");
		
		DecimalFormat df2 = new DecimalFormat("#.#");
		
		contentPane2 = new JPanel();
		
		contentPane2.setLayout(new BoxLayout(contentPane2, 1));
		
		JLabel a = new JLabel("�������");
		
		a.setFont(new Font("Dialog",1,20));
		
		contentPane2.add(a);
		
		JLabel b = new JLabel("�������:");
		
		contentPane2.add(b);
		
		JLabel k = new JLabel(df.format(obj.profitVal)+"��");
		
		contentPane2.add(k);
		
		JLabel c = new JLabel("F0: "+df.format(obj.F0));
		
		contentPane2.add(c);
		
		JLabel d = new JLabel("F1: "+df.format(obj.F1));
		
		contentPane2.add(d);
		
		JLabel e = new JLabel("F2: "+df.format(obj.F2));
		
		contentPane2.add(e);
		
		JLabel f = new JLabel("Ӫ������: ");
		
		contentPane2.add(f);
		
		JLabel l = new JLabel(df.format(obj.salePNum/10000)+"��");
		
		contentPane2.add(l);
		
		JLabel g = new JLabel("Ԥ�Ƴɹ�����: ");
		
		contentPane2.add(g);
		
		JLabel h = new JLabel(df.format(obj.predSuccPNum/10000)+"��");
		
		contentPane2.add(h);
		
		JLabel i = new JLabel("Ԥ�ƴ�������: ");
		
		contentPane2.add(i);
		
		JLabel j = new JLabel(df.format(obj.distPNum/10000)+"��");
		
		contentPane2.add(j);
		
		
		JLabel m = new JLabel("�漰Ҷ�ӽڵ������");

		contentPane2.add(m);
		
		JLabel n = new JLabel(""+df2.format(maxProfitVecInd+1));
		
		contentPane2.add(n);

		exportBt = new JButton("���Ӫ������");
		
		exportBt.addActionListener(this);
		
		contentPane2.add(exportBt);
		
		frame.add(contentPane2,BorderLayout.EAST);
		
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
	
	private JFreeChart createChart(XYDataset xyDataSet,int i){
		CHART chartType = CHART.values()[i];
		
		// ����JFreeChart����ChartFactory.createXYLineChart      
        JFreeChart jfreechart = ChartFactory.createXYLineChart("Ӫ������ - ����ֵ��ϵͼ", // ����      
        		getXlabel(chartType), // categoryAxisLabel ��category�ᣬ���ᣬX���ǩ��      
        		getYlabel(chartType), // valueAxisLabel��value�ᣬ���ᣬY��ı�ǩ��      
                xyDataSet, // dataset      
                PlotOrientation.VERTICAL,   
                true, // legend      
                true, // tooltips      
                false); 
        // ʹ��CategoryPlot���ø��ֲ������������ÿ���ʡ�ԡ�      
        XYPlot plot = (XYPlot) jfreechart.getPlot();      
        // ����ɫ ͸����      
        plot.setBackgroundAlpha(0.5f);      
        // ǰ��ɫ ͸����      
        plot.setForegroundAlpha(0.5f);      
        // �������ÿ��Բο�XYPlot��      
        
        jfreechart.getLegend().setItemFont(new Font("����", Font.BOLD+Font.PLAIN, 12));
		
        jfreechart.setTitle(new TextTitle("ROC����ͼ",new Font(getChartTitle(chartType),Font.BOLD+Font.ITALIC,20)));
				
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
        xylineandshaperenderer.setBaseItemLabelsVisible(true);// ��ʾÿ�����ϵ�����ֵ����ʾ����,���ݱ�ǩ�Ƿ�ɼ�
        xylineandshaperenderer.setDrawOutlines(true);            
        return jfreechart;		
	}
	
	private String getXlabel(CHART chartType){
		String xLabel = "";
		switch(chartType){
	        case F0_TO_PROFIT:
	        case F0_TO_SUCCRATE:
	        	xLabel = "������ - F0";
	    	    break;
	        case SALEP_TO_PROFIT:
	        case SALTEP_TO_SUCCP:
	        	xLabel = "Ӫ������ (��)";
	    	    break;
	        default:
	    	    return xLabel;
	    }		
		return xLabel;		
	}
	
	private String getYlabel(CHART chartType){
		String yLabel = "";
		switch(chartType){
	        case F0_TO_PROFIT:
	        case SALEP_TO_PROFIT:
	        	yLabel = "����ֵ - P (��)";
		        break;
	        case F0_TO_SUCCRATE:
	        	yLabel = "�ɹ��� - F2";
	    	    break;
	        case SALTEP_TO_SUCCP:
	        	yLabel = "Ԥ�Ƴɹ����� (��)";
	    	    break;
	        default:
	    	    return yLabel;
	    }		
		return yLabel;		
	}
	
	private String getChartTitle(CHART chartType){
		String chartTitle = "";
		switch(chartType){
	        case F0_TO_PROFIT:
	        	chartTitle = "������ - ����ֵ��ϵͼ";
		        break;
	        case F0_TO_SUCCRATE:
	        	chartTitle = "������ - �ɹ��ʹ�ϵͼ";
	    	    break;
	        case SALEP_TO_PROFIT:
	        	chartTitle = "Ӫ������ - ����ֵ��ϵͼ";
	    	    break;
	        case SALTEP_TO_SUCCP:
	        	chartTitle = "Ӫ������ - Ԥ�Ƴɹ�������ϵͼ";
	    	    break;
	        default:
	    	    return chartTitle;
	    }		
		return chartTitle;		
	}
			
	private XYDataset createXYDataset(int chartType){		
		CHART chart = CHART.values()[chartType];
		
		XYSeries xyseries = null; 
		
		switch(chart){
		    case F0_TO_PROFIT:
		    	xyseries = new XYSeries("Line-1");
			    break;
		    case F0_TO_SUCCRATE:
		    	xyseries = new XYSeries("Line-2");
		    	break;
		    case SALEP_TO_PROFIT:
		    	xyseries = new XYSeries("Line-3");
		    	break;
		    case SALTEP_TO_SUCCP:
		    	xyseries = new XYSeries("Line-4");
		    	break;
		    default:
		    	return null;
		}
		
		addDataToXySeries(chart, xyseries);
				
		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();      
		 
        xySeriesCollection.addSeries(xyseries);   
 
        return xySeriesCollection;     
	}
	
	private void addDataToXySeries(CHART chartType, XYSeries xyseries){
		for(int i =0;i<showVec.size();i++){
			ProfitResultNode obj = showVec.get(i);
			
			if(obj.isShowToPGraph == false){
				continue;
			}
			
			switch(chartType){
		        case F0_TO_PROFIT:
		    	    xyseries.add(obj.F0, obj.profitVal);
			        break;
		        case F0_TO_SUCCRATE:
		    	    xyseries.add(obj.F0, obj.F2);;
		    	    break;
		        case SALEP_TO_PROFIT:
		    	    xyseries.add(obj.salePNum/10000, obj.profitVal);;
		    	    break;
		        case SALTEP_TO_SUCCP:
		    	    xyseries.add(obj.salePNum/10000, obj.predSuccPNum/10000);;
		    	break;
		    default:
		    	return;
			}
			
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if (event.getSource() == exportBt) {
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
		
		DecimalFormat df = new DecimalFormat("0.0000");
		
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
		
        Vector<ProfitResultNode> descVec = new Vector<ProfitResultNode>();
        for(int i=0;i<=maxProfitVecInd;i++){
        	descVec.add(showVec.get(i));
        }
        
        Collections.sort(descVec, new ProfitObjOrderByDesc());
        
		for(int i=0;i<descVec.size();i++){
			fileContent.add("");
			
			fileContent.add("����Ӫ����Ⱥ" + (i+1));
			
			ProfitResultNode obj =  descVec.get(i);
			
			fileContent.add("�ڵ��ţ�" + (int)obj.nodeNum);
			
			fileContent.add("��Ⱥ������" + obj.condition);
			 
            fileContent.add("Ӫ��������" + (int)(obj.exportSalePNum));
            
            totalSalePNum += obj.salePNum;
            
             treeNodeResultObj treeRootObj = GenerateTreeByLeaf.getTreeNodeByLevelVec().get(0).get(0);
             
             double ratio = obj.salePNum / (treeRootObj.conditionMeet + treeRootObj.conditionNotMeet);
            
            fileContent.add("��Ⱥռ�ȣ�" + df.format(ratio));
            
            totalRatio += ratio;
            
            fileContent.add("������F0��" + df.format(obj.exportF0));
            
            totalF0 += obj.F0;
            
            fileContent.add("������F1��" + df.format(obj.exportF1));
            
            totalF1 += obj.F1;
            
            fileContent.add("�ɹ��ʣ�" + df.format(obj.exportF2));
            
            sucTotalPNum += obj.predSuccPNum;
            
            fileContent.add("��Ⱥ������" + (double)obj.exportProfitVal/(double)10000);
            
            totalProfit += obj.profitVal;
            
            fileContent.add("����Ӫ������" + ((double)obj.profitVal/((double)obj.salePNum/(double)10000)));
			 				 						
		}
		
		fileContent.add("");
		
		fileContent.add("�ϼ�");
		
		fileContent.add("Ӫ��������" + (int)totalSalePNum);           
     
       fileContent.add("��Ⱥռ�ȣ�" + df.format(totalRatio));
       
       fileContent.add("������F0��" + df.format(totalF0));
       
       fileContent.add("������F1��" + df.format(totalF1));
       
       totalF2 = (double)sucTotalPNum / (double)totalSalePNum;
       
       fileContent.add("�ɹ��ʣ�" + df.format(totalF2));
       
       fileContent.add("��Ⱥ������" + (double)totalProfit/(double)10000);
       
       fileContent.add("����Ӫ������" + ((double)totalProfit/(double)totalSalePNum));
					
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
