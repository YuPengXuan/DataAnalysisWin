package com.xn.alex.data.roc;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.xn.alex.data.common.CommonConfig;
import com.xn.alex.data.resultobj.algorithmResultObj2;
import com.xn.alex.data.resultobj.treeNodeResultObj;
import com.xn.alex.data.rule.GenerateTreeByLeaf;

public class ProfitModelAction4 extends ProfitModelInterface implements ActionListener{

	Vector<Vector<treeNodeResultObj>> treeNodeVec = GenerateTreeByLeaf.getTreeNodeByLevelVec();
	 
	Vector<treeNodeResultObj> treeLeafNodeVec = GenerateTreeByLeaf.getTreeLeafNodeVec();
	
	long xNotBuyTotal = treeNodeVec.get(0).get(0).conditionNotMeet;
	 
	long yBuyTotal = treeNodeVec.get(0).get(0).conditionMeet;
	
	long numTotal = xNotBuyTotal+yBuyTotal;
	
	double alfa = (double)yBuyTotal/numTotal;
	
	private JFrame frame;
	
	private JPanel contentPane;
	
	private JTextField input_A;//γ
	
	private JTextField input_B;//V
	
	private JTextField input_C;//c
	
	private JTextField input_D;//d
	
	private JTextField input_E;//F0
	
	private JTextField input_F;//F0
	
	private JTextField input_G;//F1
	
	private JTextField input_H;//F1
	
	private JTextField input_I;//F2
	
	private JTextField input_J;//F2
	
	private JTextField input_K;//N
	
	private JTextField input_L;//营销人群个数
	
	private JButton okBt;
	
	private double paraA = -1;
	
	private double paraB = -1;
	
	private double paraC = -1;
	
	private double paraD = -1;
	
	private double paraE = -1;
	
	private double paraF = -1;
	
	private double paraG = -1;
	
	private double paraH = -1;
	
	private double paraI = -1;
	
	private double paraJ = -1;
	
	private double paraK = -1;
	
	private double paraL = -1;
	
	private Vector<algorithmResultObj2> yToProfitVec = new Vector<algorithmResultObj2>();
	
	private Vector<algorithmResultObj2> yToSuccRateVec = new Vector<algorithmResultObj2>();
	
	private Vector<algorithmResultObj2> salePToProfitVec = new Vector<algorithmResultObj2>();
	
	private Vector<algorithmResultObj2> salePToPredicPvec = new Vector<algorithmResultObj2>();
		
	private Map<String, Double> outPutMap = new HashMap<String, Double>();	


	public ProfitModelAction4(){
		 
	 }
	 
	
	 public void takeAction(){
		 try{
			 
		     createWindow();
		     
		 }
		 catch(Exception e){
			 
			 System.out.print("生成利润模型失败");
			 
		 }
		 
	 }
	
		//利润模型界面
		public void createWindow(){
			
			frame = new JFrame("利润模型");
			
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			contentPane = new JPanel();
			
			contentPane.setLayout(new BorderLayout());
			
			//
	 
			JPanel topPanel = new JPanel();
			
		    GridLayout layout1 = new GridLayout(0,1);
		     
		    topPanel.setLayout(layout1);
		     
		    layout1.setVgap(15);
		     
		    layout1.setHgap(3);
		     
		    topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 0, 30));
		    
		    JLabel title = new JLabel("存量客户保有利润分析",JLabel.CENTER);
		     
		    title.setFont(new java.awt.Font("Dialog", 1, 20));
		    
		    topPanel.add(title);
		    
		    JLabel profit = new JLabel("1、利润模型",JLabel.LEFT);
		    
		    profit.setFont(new java.awt.Font("Dialog", 1, 16));
		     
		    topPanel.add(profit);
		    
		    ImageIcon imageicon1 =new ImageIcon(ClassLoader.getSystemResource("images/存量客户保有.png"));
		    
		    JLabel profit_equation = new JLabel(imageicon1);
		     
		    topPanel.add(profit_equation);
		    
		    JLabel para = new JLabel("2、变量解释", JLabel.LEFT);
		    
		    para.setFont(new java.awt.Font("Dialog", 1, 16));
		    
		    topPanel.add(para);
		    
		    JLabel a = new JLabel("N:客户总数"+"--α:流失客户占比--"+"V:客户终身价值"+"--"+"γ:挽留成功率");
		    
		    a.setFont(new java.awt.Font("Dialog", Font.ITALIC, 13));

		    topPanel.add(a);
		    
		    JLabel b = new JLabel("c:挽留激励成本--"+"d:挽留接触成本"+"--"+"F0:模型命中率"
		    +"--"+"F1:模型误判率--F2:模型成功率");
		    
		    b.setFont(new java.awt.Font("Dialog", Font.ITALIC, 13));
		    
		    topPanel.add(b);
		    
		    JLabel parameter = new JLabel("3、营销参数设置及限制条件",JLabel.LEFT);
		    
		    parameter.setFont(new java.awt.Font("Dialog", 1, 16));
		     
		    topPanel.add(parameter);
		     
		    contentPane.add(topPanel, BorderLayout.NORTH);
		    
		    //
		    
			JPanel centerPanel = new JPanel();
		     
			FlowLayout layout2 = new FlowLayout(FlowLayout.LEFT, 5, 5);
		     
			centerPanel.setLayout(layout2);
			    
		    centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 15));
		    
		    JLabel input5 = new JLabel(" 	 		N=");
		    
		    centerPanel.add(input5);
		    
		    input_K = new JTextField(""+numTotal,6);
		    
		    input_K.addActionListener(this);
		    
		    centerPanel.add(input_K);
		    /*
		    JLabel input6 = new JLabel(" 	 		α=");
		    
		    centerPanel.add(input6);
		    
		    input_L = new JTextField(""+alfa,5);
		    
		    input_L.addActionListener(this);
		    
		    centerPanel.add(input_L);	*/
		    
		    JLabel input1 = new JLabel(" 	 		γ=");
		     
		    centerPanel.add(input1);
		    
		    input_A = new JTextField("0.5",6);
		    
		    input_A.addActionListener(this);
		     
		    centerPanel.add(input_A);
		    
		    JLabel input2 = new JLabel(" 	 V=");
		     
		    centerPanel.add(input2);
		    
		    input_B = new JTextField("100",6);
		    
		    input_B.addActionListener(this);
		     
		    centerPanel.add(input_B);
		    
		    JLabel input3 = new JLabel(" 	 c=");
		     
		    centerPanel.add(input3);
		    
		    input_C = new JTextField("50",6);
		    
		    input_C.addActionListener(this);
		     
		    centerPanel.add(input_C);
		    
		    JLabel input4 = new JLabel(" 	 d=");
		     
		    centerPanel.add(input4);
		    
		    input_D = new JTextField("3",6);
		    
		    input_D.addActionListener(this);
		     
		    centerPanel.add(input_D);
		    
		    JPanel pane = new JPanel();
		    
		    BoxLayout lo = new BoxLayout(pane, BoxLayout.X_AXIS);
		    
		    pane.setLayout(lo);
		    
		    //
		    
		    JLabel lb1 = new JLabel("F0∈");
		    
		    centerPanel.add(lb1);
		    
		    JLabel lb2 = new JLabel("[");
		    
		    centerPanel.add(lb2);
		    
		    input_E = new JTextField("0",4);
		    
		    input_E.addActionListener(this);
		    
		    centerPanel.add(input_E);
		    
		    JLabel lb3 = new JLabel(",");
		    
		    centerPanel.add(lb3);
		    
		    input_F = new JTextField("1",4);
		    
		    input_F.addActionListener(this);
		    
		    centerPanel.add(input_F);
		    
		    JLabel lb4 = new JLabel("]");
		    
		    centerPanel.add(lb4);
		    
		    //
		    
		    JLabel lb5 = new JLabel("F1∈");
		    
		    centerPanel.add(lb5);
		    
		    JLabel lb6 = new JLabel("[");
		    
		    centerPanel.add(lb6);
		    
		    input_G = new JTextField("0",4);
		    
		    input_G.addActionListener(this);
		    
		    centerPanel.add(input_G);
		    
		    JLabel lb7 = new JLabel(",");
		    
		    centerPanel.add(lb7);
		    
		    input_H = new JTextField("1",4);
		    
		    input_H.addActionListener(this);
		    
		    centerPanel.add(input_H);
		    
		    JLabel lb8 = new JLabel("]");
		    
		    centerPanel.add(lb8);
		    
		    //
		    
		    JLabel lb9 = new JLabel("			F2∈");
		    
		    centerPanel.add(lb9);
		    
		    JLabel lb10 = new JLabel("[");
		    
		    centerPanel.add(lb10);
		    
		    input_I = new JTextField("0",4);
		    
		    input_I.addActionListener(this);
		    
		    centerPanel.add(input_I);
		    
		    JLabel lb11 = new JLabel(",");
		    
		    centerPanel.add(lb11);
		    
		    input_J = new JTextField("1",4);
		    
		    input_J.addActionListener(this);
		    
		    centerPanel.add(input_J);
		    
		    JLabel lb12 = new JLabel("]");
		    
		    centerPanel.add(lb12);
		    
		    JLabel lb13 = new JLabel("最终营销人群≤");
		    
		    centerPanel.add(lb13);
		    
		    input_L = new JTextField(""+(m_resultObjVec.size()-1),4);
		    
		    input_L.addActionListener(this);
		    
		    centerPanel.add(input_L);
		    
		    JLabel lb14 = new JLabel("种");
		    
		    centerPanel.add(lb14);

		    contentPane.add(centerPanel, BorderLayout.CENTER);
		    
		    
		    	     
		    
		    JPanel bottomPanel = new JPanel();
		    
		    bottomPanel.setSize(300, 600);
		     
		    bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));
		    
			   
		    okBt = new JButton("确定");
		    
		    okBt.addActionListener(this);
		     
		    bottomPanel.add(okBt);
		    
		    contentPane.add(bottomPanel, BorderLayout.SOUTH);
		    
	        //contentPane.add(bottomPanel, BorderLayout.SOUTH);
		     
		    frame.setContentPane(contentPane);

		    frame.pack();

		    frame.setLocationRelativeTo(null);
		     
		    frame.setSize(570,620);
		     
		    frame.setResizable(false);

	        frame.setVisible(true);
			
		}
	 
		 private boolean isValueValid(){
			 
			 String paraKText = input_K.getText();
			 
			 if("".equals(paraKText) || Double.parseDouble(paraKText)<0.0){
				 
				 JOptionPane.showMessageDialog(null,"请输入正确的参数N","错误信息",JOptionPane.ERROR_MESSAGE);
				 
				 return false;
			 }
			 
			 paraK = Double.parseDouble(paraKText);
			 
			 String paraLText = input_L.getText();
			 
			 if("".equals(paraLText) || Double.parseDouble(paraLText)<0.0 ||Integer.parseInt(paraLText)>m_resultObjVec.size()){
				 
				 JOptionPane.showMessageDialog(null,"请输入正确的最终营销人群个数","错误信息",JOptionPane.ERROR_MESSAGE);
				 
				 return false;
			 }
			 
			 paraL = Integer.parseInt(paraLText);
			 
			 String paraAText = input_A.getText();
			 
			 if("".equals(paraAText) || Double.parseDouble(paraAText)<0.0 || Double.parseDouble(paraAText)>1.0){
				 
				 JOptionPane.showMessageDialog(null,"请输入正确的参数γ","错误信息",JOptionPane.ERROR_MESSAGE);
				 
				 return false;
			 }
			 
			 paraA = Double.parseDouble(paraAText);
			 
			 String paraBText = input_B.getText();
			 
			 if("".equals(paraBText) || Double.parseDouble(paraBText)<0){
				 
				 JOptionPane.showMessageDialog(null,"请输入正确的参数V","错误信息",JOptionPane.ERROR_MESSAGE);
				 
				 return false;
				 
			 }
			 
			 paraB = Double.parseDouble(paraBText);
			 
			 String paraCText = input_C.getText();
			 
			 if("".equals(paraCText) || Double.parseDouble(paraCText)<0){
				 
				 JOptionPane.showMessageDialog(null,"请输入正确的参数c","错误信息",JOptionPane.ERROR_MESSAGE);
				 
				 return false;
				 
			 }
			 
			 paraC = Double.parseDouble(paraCText);
			 
			 String paraDText = input_D.getText();
			 
			 if("".equals(paraDText) || Double.parseDouble(paraDText)<0){
				 
				 JOptionPane.showMessageDialog(null,"请输入正确的参数d","错误信息",JOptionPane.ERROR_MESSAGE);
				 
				 return false;
			 }
			 
			 paraD = Double.parseDouble(paraDText);
			 
			 String paraEText = input_E.getText();
			 
			 if("".equals(paraEText) || Double.parseDouble(paraEText)<0.0 ||Double.parseDouble(paraEText)>1.0){
				 
				 JOptionPane.showMessageDialog(null,"请输入正确的F0取值","错误信息",JOptionPane.ERROR_MESSAGE);
				 
				 return false;
			 }
			 
			 paraE = Double.parseDouble(paraEText);
			 
			 String paraFText = input_F.getText();
			 
			 if("".equals(paraFText) || Double.parseDouble(paraFText)<0.0 ||Double.parseDouble(paraFText)>1.0){
				 
				 JOptionPane.showMessageDialog(null,"请输入正确的F0取值","错误信息",JOptionPane.ERROR_MESSAGE);
				 
				 return false;
			 }
			 
			 paraF = Double.parseDouble(paraFText);
			 
			 String paraGText = input_G.getText();
			 
			 if("".equals(paraGText) || Double.parseDouble(paraGText)<0.0 ||Double.parseDouble(paraGText)>1.0){
				 
				 JOptionPane.showMessageDialog(null,"请输入正确的F1取值","错误信息",JOptionPane.ERROR_MESSAGE);
				 
				 return false;
			 }
			 
			 paraG = Double.parseDouble(paraGText);
			 
			 String paraHText = input_H.getText();
			 
			 if("".equals(paraHText) || Double.parseDouble(paraHText)<0.0 ||Double.parseDouble(paraHText)>1.0){
				 
				 JOptionPane.showMessageDialog(null,"请输入正确的F1取值","错误信息",JOptionPane.ERROR_MESSAGE);
				 
				 return false;
			 }
			 
			 paraH = Double.parseDouble(paraHText);
			 
			 String paraIText = input_I.getText();
			 
			 if("".equals(paraIText) || Double.parseDouble(paraIText)<0.0 ||Double.parseDouble(paraIText)>1.0){
				 
				 JOptionPane.showMessageDialog(null,"请输入正确的F2取值","错误信息",JOptionPane.ERROR_MESSAGE);
				 
				 return false;
			 }
			 
			 paraI = Double.parseDouble(paraIText);
			 
			 String paraJText = input_J.getText();
			 
			 if("".equals(paraJText) || Double.parseDouble(paraJText)<0.0 ||Double.parseDouble(paraJText)>1.0){
				 
				 JOptionPane.showMessageDialog(null,"请输入正确的F2取值","错误信息",JOptionPane.ERROR_MESSAGE);
				 
				 return false;
			 }
			 
			 paraJ = Double.parseDouble(paraJText);
			 
			 
			 if (paraE >= paraF) {
				 
				 JOptionPane.showMessageDialog(null,"请输入正确的F0取值","错误信息",JOptionPane.ERROR_MESSAGE);
				 
				 return false;
			}
			 
			 if (paraG >= paraH) {
				 
				 JOptionPane.showMessageDialog(null,"请输入正确的F1取值","错误信息",JOptionPane.ERROR_MESSAGE);
				 
				 return false;
			}
			 
			 if (paraI >= paraJ) {
				 
				 JOptionPane.showMessageDialog(null,"请输入正确的F2取值","错误信息",JOptionPane.ERROR_MESSAGE);
				 
				 return false;
			}

			 return true;
		 }
	 
		 //计算最大利润
		 private double executeAlogrithm(double paraA, double paraB, double paraC,double paraD,double paraE,double paraF,double paraG,double paraH,double paraI,double paraJ){
			 
			 yToProfitVec.clear();
			 yToSuccRateVec.clear();
			 salePToPredicPvec.clear();
			 salePToProfitVec.clear();
			 
			 double Max = -999999999999.0;
			 
			 double P;
			 
			 double maxF0 = 0.0;
			 
			 double maxF1 = 0.0;
			 
			 double maxF2 = 0.0;
			 
			 double maxSalePeople =0.0;
			 
			 double maxsuccessPeople = 0.0;
			 
			 double maxdistPeople = 0.0;
			 
			 double salePeople = 0.0;
			 
			 double successPeople = 0.0;
			 
			 double distPeople = 0.0;
			 
			 int num = 0;
			 
			 for(int i = 0;i<m_resultObjVec.size();i++){
				 
				 algorithmResultObj2 obj = m_resultObjVec.get(i);
				 
				 double x = obj.x.doubleValue();//F1
				 
				 double y = obj.y.doubleValue();//F0
				 
				 if(x==0 && y ==0){
					 continue;
				 }
				 
				 for (int j = 0; j < treeLeafNodeVec.size(); j++) {
					if (treeLeafNodeVec.get(j).nodeNumber == obj.nodeNum) {
						
						salePeople +=treeLeafNodeVec.get(j).conditionMeet + treeLeafNodeVec.get(j).conditionNotMeet;
						successPeople +=treeLeafNodeVec.get(j).conditionMeet;
						distPeople += treeLeafNodeVec.get(j).conditionNotMeet;
						
					}
				}
				 
				 double succRate = successPeople/salePeople;//F2
				 
				 if(y>=paraE && y<=paraF && x>= paraG && x<=paraH && succRate>=paraI && succRate<= paraJ){
					 
					 if (i+1 > paraL) {
							
						 break;
					}
					 
					 P = ( paraK* alfa* y *(paraA* paraB-paraA* paraC-paraD)) - (paraK* (1-alfa)* x* (paraD+paraC));
					 
					 for(int n=0;n<m_pResultVec.size();n++){
						 if(m_pResultVec.get(n).nodeNum == obj.nodeNum){
							 m_pResultVec.get(n).profitVal = P/10000;
						 }
					 }
					 
					 algorithmResultObj2 yToProfitObj = new algorithmResultObj2();
					 
					 yToProfitObj.x = y;
					 
					 yToProfitObj.y = P/10000;
					 
					 yToProfitVec.add(yToProfitObj);
					 
					 algorithmResultObj2 yToyToSuccRateObj = new algorithmResultObj2();
					 
					 yToyToSuccRateObj.x = y;
					 
					 yToyToSuccRateObj.y = succRate;
					 				 
					 yToSuccRateVec.add(yToyToSuccRateObj);
					 
					 
					 algorithmResultObj2 salePToProfitObj = new algorithmResultObj2();
					 
					 salePToProfitObj.x = (salePeople/10000)*(paraK/numTotal);
					 salePToProfitObj.y = P/10000;
					 				 
					 salePToProfitVec.add(salePToProfitObj);
					 
					 algorithmResultObj2 salePToPredicPObj = new algorithmResultObj2();
					 
					 salePToPredicPObj.x = (salePeople/10000)*(paraK/numTotal);
					 
					 salePToPredicPObj.y = (successPeople/10000)*(paraK/numTotal);
					 				 
					 salePToPredicPvec.add(salePToPredicPObj);
								 
					 if (P>Max) {
						
						 Max =P;
						 
						 maxF0 = obj.y.doubleValue();
	                    
						 maxF1 = obj.x.doubleValue();
						 
						 maxF2 = succRate;
						 
						 num = i+1;
						 
						 maxSalePeople = salePeople;
						 
						 maxsuccessPeople = successPeople;
						 
						 maxdistPeople = distPeople;
					}
					 
				 }//
				 
				 outPutMap.put(CommonConfig.MAX_PROFIT, Max/10000);
				 
				 outPutMap.put(CommonConfig.MAX_F0, maxF0);
				 
				 outPutMap.put(CommonConfig.MAX_F1, maxF1);
				 
				 outPutMap.put(CommonConfig.MAX_F2, maxF2);
				 
				 outPutMap.put(CommonConfig.MAX_SALE_PEOPLE, (maxSalePeople/10000)*(paraK/numTotal));
				 
				 outPutMap.put(CommonConfig.MAX_PRED_PEOPLE, (maxsuccessPeople/10000)*(paraK/numTotal));
				 
				 outPutMap.put(CommonConfig.MAX_PRED_DIST_PEOPLE, (maxdistPeople/10000)*(paraK/numTotal));
				 
				 outPutMap.put(CommonConfig.MAX_LEAF_NODE_NUM, (double) num);
				 
			 }
			 /*
			 if (Max == -999999999999.0) {
				
				 JOptionPane.showMessageDialog(null,"该范围内不存在点！","提示",JOptionPane.INFORMATION_MESSAGE);
			}*/
			 
			 return Max;
		 }

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource() == input_A) {
				
				input_A.setText(input_A.getText());
			}
			
			if (e.getSource() == input_B) {
				
				input_B.setText(input_B.getText());
			}
			
			if (e.getSource() == input_C) {
				
				input_C.setText(input_C.getText());
			}
			
			if (e.getSource() == input_D) {
				
				input_D.setText(input_D.getText());
			}
			
			if (e.getSource() == input_E) {
				
				input_E.setText(input_E.getText());
			}
			
			if (e.getSource() == input_F) {
				
				input_F.setText(input_F.getText());
			}
			
			if (e.getSource() == input_G) {
				
				input_G.setText(input_G.getText());
			}
			
			if (e.getSource() == input_H) {
				
				input_H.setText(input_H.getText());
			}
			
			if (e.getSource() == input_I) {
				
				input_I.setText(input_I.getText());
			}
			
			if (e.getSource() == input_J) {
				
				input_J.setText(input_J.getText());
			}
			
			if (e.getSource() == input_K) {
				
				input_K.setText(input_K.getText());
			}
			
			if (e.getSource() == input_L) {
				
				input_L.setText(input_L.getText());
			}
			
			if (e.getSource() == okBt) {
				
				if(false == isValueValid()){
					
			    	 return;
			     }
				double a;
				a = executeAlogrithm(paraA,paraB,paraC,paraD,paraE,paraF,paraG,paraH,paraI,paraJ);
				
				if (a == -999999999999.0) {
					
					JOptionPane.showMessageDialog(null,"该范围内不存在点！","提示",JOptionPane.INFORMATION_MESSAGE);
				}
				//result2A.setText("点(a,b)为最佳点"+ProfitModelAction3.bestXPoint.size());
				else{
				ProfitGraph2 graph = new ProfitGraph2(yToProfitVec,yToSuccRateVec,salePToProfitVec,salePToPredicPvec,m_pResultVec, outPutMap, rocType, m_resultObjVec);
				
				graph.takeAction();}
				
			}
			
		}

}
