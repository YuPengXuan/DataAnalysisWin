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
				title = "4G���Ӫ���������";
				break;
			
			case ROC_SMS:
				title = "4G����Ӫ���������";
				break;
			
			case ROC_CUST:
				title = "�����ͻ������������";
				break;
				
			case ROC_APP:
				title = "APP�ͻ������������";
				break;
			
			case ROC_WARN:
				title = "Ͷ�߿ͻ�Ԥ���������";
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
				image = new ImageIcon(ClassLoader.getSystemResource("images/4G���Ӫ��.png"));
				break;
			
			case ROC_SMS:
				image = new ImageIcon(ClassLoader.getSystemResource("images/4G����Ӫ��.png"));
				break;
			
			case ROC_CUST:
				image = new ImageIcon(ClassLoader.getSystemResource("images/�����ͻ�����.png"));
				break;
				
			case ROC_APP:
				image = new ImageIcon(ClassLoader.getSystemResource("images/APP�ͻ�����.png"));
				break;
			
			case ROC_WARN:
				image = new ImageIcon(ClassLoader.getSystemResource("images/�����ͻ�����.png"));
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
			labela = new JLabel("N���ͻ�����--������ʷ�ɹ�Ӫ���ͻ�ռ��--V����Ʒ����--c��Ӫ���Ż�");
			break;
		
		case ROC_CUST:
			labela = new JLabel("N:�ͻ�����"+"--��:��ʧ�ͻ�ռ��--"+"V:�ͻ������ֵ"+"--"+"��:�����ɹ���");
			break;
			
		case ROC_APP:
			labela = new JLabel("N:APP�ͻ�����"+"--"+"V:APP�ͻ���ֵ"+"--"+"c:���������ɱ�"+"--"+"��:�����ɹ���");
			break;
		
		case ROC_WARN:
			labela = new JLabel("N:�ͻ�����"+"--��:Ͷ�߿ͻ�ռ��--"+"V:Ͷ�ߴ���ɱ�"+"--"+"��:�ػ��ɹ���");
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
			labelb = new JLabel("d������Ӵ��ɱ�--b��������Ŵ���--F0��ģ��������--F1��ģ��������--F2��ģ�ͳɹ���");
			break;
		case ROC_SMS:
			
			labelb= new JLabel("d�����ŽӴ��ɱ�--b�����Ŵ��Ŵ���--F0��ģ��������--F1��ģ��������--F2��ģ�ͳɹ���");
			break;
		
		case ROC_CUST:
			labelb = new JLabel("c:���������ɱ�--"+"d:�����Ӵ��ɱ�"+"--"+"F0:ģ��������"+"--"+"F1:ģ��������--F2:ģ�ͳɹ���");
			break;
			
		case ROC_APP:
			labelb = new JLabel("��:APP����ʧ�ͻ�ռ��"+"--"+"F0:ģ��������"+"--"+"F1:ģ��������--F2:ģ�ͳɹ���");
			break;
		
		case ROC_WARN:
			labelb = new JLabel("c:�ػ������ɱ�--"+"d:�ػ��Ӵ��ɱ�"+"--"+"F0:ģ��������"+"--"+"F1:ģ��������--F2:ģ�ͳɹ���");
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
			chartTitle = "";
			break;			
	    }
		
		return chartTitle;
	}

}
