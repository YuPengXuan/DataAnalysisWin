package com.xn.alex.data.roc;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.xn.alex.data.common.CommonConfig.ROC_TYPE;
import com.xn.alex.data.roc.ProfitGraph.CHART;

public class ProfitResources {
		
	public static String getTitle(ROC_TYPE rocType){
		String title = null;
		switch(rocType){
			case ROC_EXT:
				title = "4G外呼营销利润分析";
				break;
			
			case ROC_SMS:
				title = "4G短信营销利润分析";
				break;
			
			case ROC_CUST:
				title = "存量客户保有利润分析";
				break;
				
			case ROC_APP:
				title = "APP客户保有利润分析";
				break;
			
			case ROC_WARN:
				title = "投诉客户预警利润分析";
				break;
				
			default:
				title = null;
				break;			
		}
		
		return title;		
	}
	
	public static ImageIcon getImageIcon(ROC_TYPE rocType){
		ImageIcon image = null;
		switch(rocType){
			case ROC_EXT:
				image = new ImageIcon(ClassLoader.getSystemResource("images/4G外呼营销.png"));
				break;
			
			case ROC_SMS:
				image = new ImageIcon(ClassLoader.getSystemResource("images/4G短信营销.png"));
				break;
			
			case ROC_CUST:
				image = new ImageIcon(ClassLoader.getSystemResource("images/存量客户保有.png"));
				break;
				
			case ROC_APP:
				image = new ImageIcon(ClassLoader.getSystemResource("images/APP客户保有.png"));
				break;
			
			case ROC_WARN:
				image = new ImageIcon(ClassLoader.getSystemResource("images/存量客户保有.png"));
				break;
				
			default:
				image = null;
				break;			
		}
		
		return image;	
		 
	}
	
	public static JLabel getJLabela(ROC_TYPE rocType){
		JLabel labela = null;
		switch(rocType){
		case ROC_EXT:		
		case ROC_SMS:
			labela = new JLabel("N：客户总数--α：历史成功营销客户占比--V：产品单价--c：营销优惠");
			break;
		
		case ROC_CUST:
			labela = new JLabel("N:客户总数"+"--α:流失客户占比--"+"V:客户终身价值"+"--"+"γ:挽留成功率");
			break;
			
		case ROC_APP:
			labela = new JLabel("N:APP客户总数"+"--"+"V:APP客户价值"+"--"+"c:挽留激励成本"+"--"+"γ:挽留成功率");
			break;
		
		case ROC_WARN:
			labela = new JLabel("N:客户总数"+"--α:投诉客户占比--"+"V:投诉处理成本"+"--"+"γ:关怀成功率");
			break;
			
		default:
			labela = null;
			break;			
	    }
		
		return labela;
	}
	
	public static JLabel getJLabelb(ROC_TYPE rocType){
		JLabel labelb = null;
		switch(rocType){
		case ROC_EXT:
			labelb = new JLabel("d：外呼接触成本--b：外呼打扰代价--F0：模型命中率--F1：模型误判率--F2：模型成功率");
			break;
		case ROC_SMS:
			
			labelb= new JLabel("d：短信接触成本--b：短信打扰代价--F0：模型命中率--F1：模型误判率--F2：模型成功率");
			break;
		
		case ROC_CUST:
			labelb = new JLabel("c:挽留激励成本--"+"d:挽留接触成本"+"--"+"F0:模型命中率"+"--"+"F1:模型误判率--F2:模型成功率");
			break;
			
		case ROC_APP:
			labelb = new JLabel("α:APP易流失客户占比"+"--"+"F0:模型命中率"+"--"+"F1:模型误判率--F2:模型成功率");
			break;
		
		case ROC_WARN:
			labelb = new JLabel("c:关怀激励成本--"+"d:关怀接触成本"+"--"+"F0:模型命中率"+"--"+"F1:模型误判率--F2:模型成功率");
			break;
			
		default:
			labelb = null;
			break;			
	    }
		
		return labelb;
	}
	
	public static String getChartTitle(CHART chartType){
		String chartTitle = "";
		
		switch(chartType){
		case F0_TO_PROFIT:
			chartTitle = "命中率 - 利润值关系图";
			break;
		
		case F0_TO_SUCCRATE:
			chartTitle = "命中率 - 成功率关系图";
			break;
		
		case SALEP_TO_PROFIT:
			chartTitle = "营销人数 - 利润值关系图";
			break;
			
		case SALTEP_TO_SUCCP:
			chartTitle = "营销人数 - 预计成功人数关系图";
			break;		
			
		default:
			chartTitle = "";
			break;			
	    }
		
		return chartTitle;
	}

}
