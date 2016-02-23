package com.xn.alex.data.roc;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.xn.alex.data.common.CommonConfig.ROC_TYPE;
import com.xn.alex.data.resultobj.ProfitResultNode;

public abstract class ProfitModelInterface {
	
	public abstract void takeAction();	
	
	protected ROC_TYPE rocType;
	
    protected Vector<ProfitResultNode> m_pResultVec;

	public Vector<ProfitResultNode> getpResultVec() {
		return m_pResultVec;
	}

	public void setpResultVec(Vector<ProfitResultNode> m_pResultVec) {
		this.m_pResultVec = m_pResultVec;
	}

	public ROC_TYPE getRocType() {
		return rocType;
	}

	public void setRocType(ROC_TYPE rocType) {
		this.rocType = rocType;
	}
	
    protected JFrame frame;
	
    protected JPanel contentPane;
    
    protected JPanel topPanel;
    
    protected JPanel centerPanel;
	
    protected JTextField input_γ;
    
    protected JTextField input_V;
	
    protected JTextField input_c;
	
    protected JTextField input_d;
	
    protected JTextField input_b;
	
    protected JTextField input_F0_1;
	
    protected JTextField input_F0_2;
	
    protected JTextField input_F1_1;
	
    protected JTextField input_F1_2;
	
    protected JTextField input_F2_1;
	
    protected JTextField input_F2_2;
	
    protected JTextField input_N;
	
    protected JTextField input_saleP;
	
    protected JButton okBt;
    
    protected double para_γ = 0;//γ
	
    protected double para_V = 0;//V
	
    protected double para_c = 0;//c
	
    protected double para_d = 0;//d
	
    protected double para_b = 0;//b
	
    protected double para_F0_1 = 0;//F0
	
    protected double para_F0_2 = 0;//F0
	
    protected double para_F1_1 = 0;//F1
	
    protected double para_F1_2 = 0;//F1
	
    protected double para_F2_1 = 0;//F2
	
    protected double para_F2_2 = 0;//F2
	
    protected double para_N = 0;//N
	
    protected double para_saleP = 0;//营销人群数

}
