package com.xn.alex.data.roc;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.xn.alex.data.resultobj.treeNodeResultObj;
import com.xn.alex.data.rule.GenerateTreeByLeaf;

public class ProfitModelAction extends ProfitModelInterface implements ActionListener{

	@Override
	public void takeAction() {
		// TODO Auto-generated method stub
		createWindow();
		
		addProfitFormularToDialog();
		
		addTotalNumTextField();
		
		addParaγIfNeed();
		
		addParameterVcd();
		
		addParameterbIfNeed();
		
		addF0F1F2label();
		
		addProfitButton();
	}
	
	//利润模型界面
	private void createWindow(){
				
	    frame = new JFrame("利润模型");
				
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
		contentPane = new JPanel();
				
		contentPane.setLayout(new BorderLayout());
						 
		topPanel = new JPanel();
				
		GridLayout layout1 = new GridLayout(0,1);
			     
		topPanel.setLayout(layout1);
			     
		layout1.setVgap(15);
			     
		layout1.setHgap(3);
			     
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 0, 30));
		
		String diagTitle = ProfitResources.getTitle(rocType);
		
		if(null == diagTitle){
			System.out.println("没有这种ROC类型" + rocType);
			return;
		}
			    
		JLabel title = new JLabel(diagTitle,JLabel.CENTER);
			     
		title.setFont(new java.awt.Font("Dialog", 1, 20));
			    
		topPanel.add(title);
			    
		JLabel profit = new JLabel("1、利润模型",JLabel.LEFT);
			    
		profit.setFont(new java.awt.Font("Dialog", 1, 16));
			     
		topPanel.add(profit);
	}
	
	private void addProfitFormularToDialog(){
		
		ImageIcon imageicon = ProfitResources.getImageIcon(rocType);
		
		JLabel profit_equation = new JLabel(imageicon);
	     
	    topPanel.add(profit_equation);
	    
	    JLabel para = new JLabel("2、变量解释", JLabel.LEFT);
	    
	    para.setFont(new java.awt.Font("Dialog", 1, 16));
	    
	    topPanel.add(para);
	    
	    JLabel a = ProfitResources.getJLabela(rocType);
	    
	    a.setFont(new java.awt.Font("Dialog", Font.ITALIC, 13));

	    topPanel.add(a);
	    
	    JLabel b = ProfitResources.getJLabelb(rocType);
	    
	    b.setFont(new java.awt.Font("Dialog", Font.ITALIC, 13));
	    
	    topPanel.add(b);
	    
	    JLabel parameter = new JLabel("3、营销参数设置及限制条件",JLabel.LEFT);
	    
	    parameter.setFont(new java.awt.Font("Dialog", 1, 16));
	     
	    topPanel.add(parameter);
	     
	    contentPane.add(topPanel, BorderLayout.NORTH);
	}
	
	private void addTotalNumTextField(){
		
		centerPanel = new JPanel();
	     
		FlowLayout layout2 = new FlowLayout(FlowLayout.LEFT, 5, 5);
	     
		centerPanel.setLayout(layout2);
		    
	    centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 15));
	    
	    JLabel input5 = new JLabel(" 	 		N=");
	    
	    centerPanel.add(input5);
	    
	    treeNodeResultObj rootNode = GenerateTreeByLeaf.getTreeNodeByLevelVec().get(0).get(0);
	    
	    long numTotal = rootNode.conditionMeet + rootNode.conditionNotMeet;
	    
	    input_N = new JTextField(""+numTotal,6);
	    
	    input_N.addActionListener(this);
	    
	    centerPanel.add(input_N);
		
	}
	
	private void addParaγIfNeed(){
		switch(rocType){		
		    case ROC_CUST:			
		    case ROC_APP:		
		    case ROC_WARN:
		    	addParameterγ();
			    break;
			
		    default:			
			    break;			
		}
	}
	
	private void addParameterγ(){
		JLabel inputγ = new JLabel(" 	 		γ=");
	     
	    centerPanel.add(inputγ);
	    
	    input_γ = new JTextField("0.5",6);
	    
	    input_γ.addActionListener(this);
	     
	    centerPanel.add(input_γ);
	}
	
	private void addParameterVcd(){		
		JLabel inputV = new JLabel(" 	 		V=");
	     
	    centerPanel.add(inputV);
	    
	    input_V = new JTextField("100",6);
	    
	    input_V.addActionListener(this);
	     
	    centerPanel.add(input_V);
	    
	    JLabel inputc = new JLabel(" 	 c=");
	     
	    centerPanel.add(inputc);
	    
	    input_c = new JTextField("50",6);
	    
	    input_c.addActionListener(this);
	     
	    centerPanel.add(input_c);
	    
	    JLabel inputd = new JLabel(" 	 d=");
	     
	    centerPanel.add(inputd);
	    
	    input_d = new JTextField("3",6);
	    
	    input_d.addActionListener(this);
	     
	    centerPanel.add(input_d);
		
	}
	
	private void addParameterbIfNeed(){
		switch(rocType){
		    case ROC_EXT:			
		    case ROC_SMS:
		    	addParameterb();
		    	break;
		    default:
		    	break;			
		}				
	}
	
	private void addParameterb(){
					    
	    JLabel inputb = new JLabel(" 	 b=");
	     
	    centerPanel.add(inputb);
	    
	    input_b = new JTextField("10",6);
	    
	    input_b.addActionListener(this);
	     
	    centerPanel.add(input_b);
	    	   		
	}
	        
    private void addF0F1F2label(){
        JPanel pane = new JPanel();
	    
	    BoxLayout lo = new BoxLayout(pane, BoxLayout.X_AXIS);
	    
	    pane.setLayout(lo);
    	
        JLabel lb1 = new JLabel("F0∈");
	    
	    centerPanel.add(lb1);
	    
	    JLabel lb2 = new JLabel("[");
	    
	    centerPanel.add(lb2);
	    
	    input_F0_1 = new JTextField("0",4);
	    
	    input_F0_1.addActionListener(this);
	    
	    centerPanel.add(input_F0_1);
	    
	    JLabel lb3 = new JLabel(",");
	    
	    centerPanel.add(lb3);
	    
	    input_F0_2 = new JTextField("1",4);
	    
	    input_F0_2.addActionListener(this);
	    
	    centerPanel.add(input_F0_2);
	    
	    JLabel lb4 = new JLabel("]");
	    
	    centerPanel.add(lb4);
	    
	    //
	    
	    JLabel lb5 = new JLabel("F1∈");
	    
	    centerPanel.add(lb5);
	    
	    JLabel lb6 = new JLabel("[");
	    
	    centerPanel.add(lb6);
	    
	    input_F1_1 = new JTextField("0",4);
	    
	    input_F1_1.addActionListener(this);
	    
	    centerPanel.add(input_F1_1);
	    
	    JLabel lb7 = new JLabel(",");
	    
	    centerPanel.add(lb7);
	    
	    input_F1_2 = new JTextField("1",4);
	    
	    input_F1_2.addActionListener(this);
	    
	    centerPanel.add(input_F1_2);
	    
	    JLabel lb8 = new JLabel("]");
	    
	    centerPanel.add(lb8);
	    
	    //
	    
	    JLabel lb9 = new JLabel("			F2∈");
	    
	    centerPanel.add(lb9);
	    
	    JLabel lb10 = new JLabel("[");
	    
	    centerPanel.add(lb10);
	    
	    input_F2_1 = new JTextField("0",4);
	    
	    input_F2_1.addActionListener(this);
	    
	    centerPanel.add(input_F2_1);
	    
	    JLabel lb11 = new JLabel(",");
	    
	    centerPanel.add(lb11);
	    
	    input_F2_2 = new JTextField("1",4);
	    
	    input_F2_2.addActionListener(this);
	    
	    centerPanel.add(input_F2_2);
	    
	    JLabel lb12 = new JLabel("]");
	    
	    centerPanel.add(lb12);
	    
	    JLabel lb13 = new JLabel("最终营销人群≤");
	    
	    centerPanel.add(lb13);
	    
	    input_saleP = new JTextField(""+(m_pResultVec.size()-1),4);
	    
	    input_saleP.addActionListener(this);
	    
	    centerPanel.add(input_saleP);
	    
	    JLabel lb14 = new JLabel("种");
	    
	    centerPanel.add(lb14);

	    contentPane.add(centerPanel, BorderLayout.CENTER);
    }
	
	private void addProfitButton(){
		JPanel bottomPanel = new JPanel();
	    
	    bottomPanel.setSize(310, 800);
	     
	    bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));
	    
		   
	    okBt = new JButton("确定");
	    
	    okBt.addActionListener(this);
	     
	    bottomPanel.add(okBt);
	    
	    contentPane.add(bottomPanel, BorderLayout.SOUTH);
	     
	    frame.setContentPane(contentPane);

	    frame.pack();

	    frame.setLocationRelativeTo(null);
	     
	    frame.setSize(590,620);
	     
	    frame.setResizable(false);

        frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == input_γ) {			
			input_γ.setText(input_γ.getText());			
		}
		
		if (e.getSource() == input_V) {
			input_V.setText(input_V.getText());
		}
		
		if (e.getSource() == input_c) {
			input_c.setText(input_c.getText());
		}
		
		if (e.getSource() == input_d) {
			input_d.setText(input_d.getText());
		}
		
		if (e.getSource() == input_b) {
			input_b.setText(input_b.getText());
		}
		
		if (e.getSource() == input_F0_1) {
			input_F0_1.setText(input_F0_1.getText());
		}
		
        if (e.getSource() == input_F0_2) {
        	input_F0_2.setText(input_F0_2.getText());
		}
		
        if (e.getSource() == input_F1_1) {
        	input_F1_1.setText(input_F1_1.getText());
		}
		
        if (e.getSource() == input_F1_2) {
        	input_F1_2.setText(input_F1_2.getText());
		}
		
        if (e.getSource() == input_F2_1) {
        	input_F2_1.setText(input_F2_1.getText());
		}
		
        if (e.getSource() == input_F2_2) {
        	input_F2_2.setText(input_F2_2.getText());
		}
		
		if (e.getSource() == input_N) {
			input_N.setText(input_N.getText());
		}
		
		if (e.getSource() == input_b) {
			input_b.setText(input_b.getText());
		}
		
        if (e.getSource() == input_saleP) {
        	input_saleP.setText(input_saleP.getText());
		}
        
        if (e.getSource() == okBt){
        	if(false == isValueValid()){				
		    	 return;
		    }
        	
        	ProfitCalculate calHandler = new ProfitCalculate();
        	calHandler.m_pResultVec = m_pResultVec;
        	calHandler.para_b = para_b;
        	calHandler.para_c = para_c;
        	calHandler.para_d = para_d;
        	calHandler.para_F0_1 = para_F0_1;
        	calHandler.para_F0_2 = para_F0_2;
        	calHandler.para_F1_1 = para_F1_1;
        	calHandler.para_F1_2 = para_F1_2;
        	calHandler.para_F2_1 = para_F2_1;
        	calHandler.para_F2_2 = para_F2_2;
        	calHandler.rocType = rocType;
        	calHandler.para_N = para_N;
        	calHandler.para_saleP = para_saleP;
        	calHandler.para_V = para_V;
        	calHandler.para_γ = para_γ;
        	calHandler.takeAction();

        }
		
	}
	
	private boolean isValueValid(){
		 para_N = Double.parseDouble(input_N.getText());
		 if(para_N<0.0){
			 
			 JOptionPane.showMessageDialog(null,"请输入正确的参数N","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
		 }
		 
		 para_saleP = Double.parseDouble(input_saleP.getText());
		 if(para_saleP<0.0 ||para_saleP>m_pResultVec.size()){
			 
			 JOptionPane.showMessageDialog(null,"请输入正确的最终营销人群个数","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
		 }
		 
		 if(input_γ!=null){
			 para_γ = Double.parseDouble(input_γ.getText());
		 }
		 if(para_γ<0.0 || para_γ>1.0){
			 
			 JOptionPane.showMessageDialog(null,"请输入正确的参数γ","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
		 }
		 
		 para_V = Double.parseDouble(input_V.getText());
		 if(para_V<0){
			 
			 JOptionPane.showMessageDialog(null,"请输入正确的参数V","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
			 
		 }
		 
		 para_c = Double.parseDouble(input_c.getText());
		 if(para_c<0){
			 
			 JOptionPane.showMessageDialog(null,"请输入正确的参数c","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
			 
		 }
		 
		 para_d = Double.parseDouble(input_d.getText());
		 if(para_d<0){
			 
			 JOptionPane.showMessageDialog(null,"请输入正确的参数d","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
		 }
		 
		 para_F0_1 = Double.parseDouble(input_F0_1.getText());
		 if(para_F0_1<0.0 ||para_F0_1>1.0){
			 
			 JOptionPane.showMessageDialog(null,"请输入正确的F0取值","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
		 }
		 
		 para_F0_2 = Double.parseDouble(input_F0_2.getText());
		 if(para_F0_2<0.0 ||para_F0_2>1.0){
			 
			 JOptionPane.showMessageDialog(null,"请输入正确的F0取值","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
		 }
		 	
		 para_F1_1 = Double.parseDouble(input_F1_1.getText());
		 if(para_F1_1<0.0 ||para_F1_1>1.0){
			 
			 JOptionPane.showMessageDialog(null,"请输入正确的F1取值","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
		 }
		 	
		 para_F1_2 = Double.parseDouble(input_F1_2.getText());
		 if(para_F1_2<0.0 ||para_F1_2>1.0){
			 
			 JOptionPane.showMessageDialog(null,"请输入正确的F1取值","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
		 }		 
		 
		 para_F2_1 = Double.parseDouble(input_F2_1.getText());
		 if(para_F2_1<0.0 ||para_F2_1>1.0){
			 
			 JOptionPane.showMessageDialog(null,"请输入正确的F2取值","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
		 }
		 	
		 para_F2_2 = Double.parseDouble(input_F2_2.getText());
		 if(para_F2_2<0.0 ||para_F2_2>1.0){
			 
			 JOptionPane.showMessageDialog(null,"请输入正确的F2取值","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
		 }
		 
		 if(input_b!=null){
		     para_b = Double.parseDouble(input_b.getText());
		 }
		 if(para_b<0.0 ){
			 
			 JOptionPane.showMessageDialog(null,"请输入正确的b取值","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
		 }
		 		 
		 if (para_F0_1 >= para_F0_2) {
			 
			 JOptionPane.showMessageDialog(null,"请输入正确的F0取值","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
		}
		 
		 if (para_F1_1 >= para_F1_2) {
			 
			 JOptionPane.showMessageDialog(null,"请输入正确的F1取值","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
		}
		 
		 if (para_F2_1 >= para_F2_2) {
			 
			 JOptionPane.showMessageDialog(null,"请输入正确的F2取值","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
		}
		 
		return true;
	}

}
