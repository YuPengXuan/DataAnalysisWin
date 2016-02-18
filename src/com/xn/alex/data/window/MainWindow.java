package com.xn.alex.data.window;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JTable;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JMenuItem;

import com.xn.alex.data.action.AboutAction;
import com.xn.alex.data.action.C45Action;
import com.xn.alex.data.action.CloseAction;
import com.xn.alex.data.action.HistogramAction;
import com.xn.alex.data.action.ImportRuleAction;
import com.xn.alex.data.action.LostValueAction;
import com.xn.alex.data.action.NormalizationAction;
import com.xn.alex.data.action.OddValueAction;
import com.xn.alex.data.action.OpenAction;
import com.xn.alex.data.action.RocAction;
import com.xn.alex.data.action.SaveAction;
import com.xn.alex.data.action.SkipAction;
import com.xn.alex.data.action.WindowAction;
import com.xn.alex.data.common.CommonConfig.CURRENT_ACTION;
import com.xn.alex.data.common.CommonConfig.ROC_TYPE;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.common.SoftWareLicence;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.database.DatabaseConstant;
import com.xn.alex.data.graphics.CategoryDataSheet;
import com.xn.alex.data.listener.JButtonListener;
import com.xn.alex.data.listener.JTableListener;
import com.xn.alex.data.listener.JTreeListener;

public class MainWindow extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmOpenFile;	
	private JMenuItem mntmSaveFile;
	private JMenuItem mntmSaveRule;
	private JMenuItem mntmCloseFile;
	private JMenuItem mntmExit;
    private JMenu mnFilter;
    private JMenuItem mntmLostVal;
    private JMenuItem mntmHistogram;
    private JMenuItem mntmNormalization;
    private JMenuItem mntmRemoveOddValue;
    private JMenuItem mntmSkip;
    private JMenu mnDataMining;
    private JMenuItem mntmClusterAnalyze;    
	private JMenu mnClassAnalyze;
    private JMenuItem mntmKaFang;
    private JMenuItem mntmC4_45;
    private JMenuItem mntmZAPTree;
    private JMenuItem mntmRegAnylyze;    
	private JMenuItem mntmAssocAnalyze;
	private JMenuItem mntmTimeAnalyze;    
	private JMenu mnSmart;
    private JMenu mnSmsSale;
    private JMenuItem mntmImportRule;
    private JMenuItem mntmRoc;
    private JMenuItem mntmProfitModel;
    private JMenu mnExCallSale;
    private JMenuItem mntmImportRuleEx;
    private JMenuItem mntmRocEx;
    private JMenuItem mntmProfitModelEx;
    private JMenu mnAppCustKeep;
    private JMenuItem mntmImportRuleApp;
    private JMenuItem mntmRocApp;
    private JMenuItem mntmProfitModelApp;
    private JMenu mnCustKeep;
    private JMenuItem mntmImportRuleKeep;
    private JMenuItem mntmRocKeep;
    private JMenuItem mntmProfitModelKeep;
    private JMenu mnCpCustWarn;
    private JMenuItem mntmImportRuleWarn;
	private JMenuItem mntmRocWarn;
    private JMenuItem mntmProfitModelWarn;
    

	private JMenu mnHelp;
	private JMenuItem mntmAbout;
	
	
	private JSplitPane splitPane;
	private JSplitPane splitPane_1;
	
	private JScrollPane scrollPane;
	private DefaultMutableTreeNode rootNode;

	private JTree tree;

	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JTextArea textArea;
	private JToolBar toolBar;
	
	private JButton openButton;
	private JButton saveButton;
	private JButton closeButton;
	
	public static Map<String, String> fileNameToTableMap = new ConcurrentHashMap<String, String>();
	
	public static Map<Integer, String> treeNodeToFullPathMap = new ConcurrentHashMap<Integer, String>();
	
	private static Vector<String> columnVec = new Vector<String>();
	
	private static Vector<Vector<String>> valueVec = new Vector<Vector<String>>();
	
	private List<UpdateObject> updateObjList = new ArrayList<UpdateObject>();
	
	private static MainWindow mainWindowHandler = null;
	
	private WindowAction action;
	
	private int[] selectedColumnInds;
	
	private int[] selectedRowInds;
	
	private static CURRENT_ACTION currentAct = CURRENT_ACTION.NONE;
	
	private DefaultMutableTreeNode currentNode;
	
	public JMenuItem getMntmClusterAnalyze() {
		return mntmClusterAnalyze;
	}
	
	public JMenuItem getMntmRegAnylyze() {
		return mntmRegAnylyze;
	}
	
    public JMenuItem getMntmAssocAnalyze() {
		return mntmAssocAnalyze;
	}
    
    public JMenuItem getMntmTimeAnalyze() {
		return mntmTimeAnalyze;
	}
	
	public JMenuItem getMntmSaveFile() {
		return mntmSaveFile;
	}

	public JMenu getMnClassAnalyze() {
		return mnClassAnalyze;
	}

	public JMenuItem getMntmImportRuleEx() {
		return mntmImportRuleEx;
	}

	public JMenuItem getMntmRocEx() {
		return mntmRocEx;
	}

	public JMenuItem getMntmProfitModelEx() {
		return mntmProfitModelEx;
	}

	public JMenu getMnAppCustKeep() {
		return mnAppCustKeep;
	}

	public JMenuItem getMntmImportRuleApp() {
		return mntmImportRuleApp;
	}

	public JMenuItem getMntmRocApp() {
		return mntmRocApp;
	}

	public JMenuItem getMntmProfitModelApp() {
		return mntmProfitModelApp;
	}

	public JMenu getMnCustKeep() {
		return mnCustKeep;
	}

	public JMenuItem getMntmImportRuleKeep() {
		return mntmImportRuleKeep;
	}

	public JMenuItem getMntmRocKeep() {
		return mntmRocKeep;
	}

	public JMenuItem getMntmProfitModelKeep() {
		return mntmProfitModelKeep;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}
	
	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}
	
	public List<UpdateObject> getUpdateObjList() {
		return updateObjList;
	}

	public void setUpdateObjList(List<UpdateObject> updateObjList) {
		this.updateObjList = updateObjList;
	}
	

	public static CURRENT_ACTION getCurrentAct() {
		return currentAct;
	}

	public static void setCurrentAct(CURRENT_ACTION currentAct) {
		MainWindow.currentAct = currentAct;
	}
	
	public DefaultMutableTreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(DefaultMutableTreeNode rootNode) {
		this.rootNode = rootNode;
	}

	public DefaultMutableTreeNode getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(DefaultMutableTreeNode currentNode) {
		this.currentNode = currentNode;
	}
	
	public JMenuItem getMntmOpenFile() {
		return mntmOpenFile;
	}

	public JMenuItem getMntmImportRule() {
		return mntmImportRule;
	}

	public JMenuItem getMntmSaveRule() {
		return mntmSaveRule;
	}

	public JMenuItem getMntmLostVal() {
		return mntmLostVal;
	}

	public JMenuItem getMntmHistogram() {
		return mntmHistogram;
	}

	public JMenuItem getMntmNormalization() {
		return mntmNormalization;
	}

	public JMenuItem getMntmRemoveOddValue() {
		return mntmRemoveOddValue;
	}

	public JMenuItem getMntmSkip() {
		return mntmSkip;
	}

	public JMenu getMnSmart() {
		return mnSmart;
	}

	public JMenuItem getMntmKaFang() {
		return mntmKaFang;
	}

	public JMenuItem getMntmC4_45() {
		return mntmC4_45;
	}

	public JMenuItem getMntmZAPTree() {
		return mntmZAPTree;
	}

	public JMenuItem getMntmRoc() {
		return mntmRoc;
	}

	public JMenuItem getMntmProfitModel() {
		return mntmProfitModel;
	}
	
	public int[] getSelectedColumnInds() {
		return selectedColumnInds;
	}

	public void setSelectedColumnInds(int[] selectedColumnInds) {
		this.selectedColumnInds = selectedColumnInds;
	}
	
	public JButton getOpenButton() {
		return openButton;
	}

	public JButton getSaveButton() {
		return saveButton;
	}

	public JButton getCloseButton() {
		return closeButton;
	}
	
	public int[] getSelectedRowInds() {
		return selectedRowInds;
	}

	public void setSelectedRowInds(int[] selectedRowInds) {
		this.selectedRowInds = selectedRowInds;
	}
	
	public JMenuItem getMntmImportRuleWarn() {
		return mntmImportRuleWarn;
	}

	public JMenuItem getMntmRocWarn() {
		return mntmRocWarn;
	}

	public JMenuItem getMntmProfitModelWarn() {
		return mntmProfitModelWarn;
	}

	/**
	 * Create the frame.
	 */
	private MainWindow() {
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter(){

			public void windowClosing(WindowEvent e){				
				if(JOptionPane.showConfirmDialog(null,"是否退出")==JOptionPane.OK_OPTION){
					SoftWareLicence.Instance().setLeftTime();
				    System.exit(0);
				}
				
			}						
		});
				
	}
	
	public static MainWindow Instance(){
		
		if(null == mainWindowHandler){
			mainWindowHandler = new MainWindow();
		}
		return mainWindowHandler;
	}
	
	public void createWindow(){		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(100, 100, 450, 300);
		
		createMeanu();
		
		createPanel();
		
		addListenerForTree();
		
		addListenserForJtable();
		
		addListenserForJButton();
				
		setOutPutToTextArea();
		
		loadUnDeleteFileLastTime();
		
	}
	
    private void loadUnDeleteFileLastTime(){
		
		try{
		
		    ResultSet rs = DatabaseAction.Instance().getAllResult(DatabaseConstant.FILE_TO_TABLE);
		
		    while(rs.next()){
		    	
		    	String fileName = rs.getString(DatabaseConstant.FILE_FULL_PATH);
		    	
		    	String tableName = rs.getString(DatabaseConstant.TABLE_NAME);
		    	
                fileNameToTableMap.put(fileName, tableName);
                
                action = OpenAction.Instance();
                ((OpenAction) action).addToTreeNode(fileName , rootNode);
                
                treeNodeToFullPathMap.put(currentNode.hashCode(), fileName);
						
		    }
		
		
		    DatabaseAction.Instance().closeCurrentConnection();
		}
		catch(Exception e){
			
			DatabaseAction.Instance().closeCurrentConnection();
			
		}
	}
	
	
	private void createMeanu(){
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
	    mnFile = new JMenu("文件");
		menuBar.add(mnFile);
		
		mntmOpenFile = new JMenuItem("  导入数据");
		mnFile.add(mntmOpenFile);
		mntmOpenFile.addActionListener(this);
		
		mntmSaveFile = new JMenuItem("  保存数据");
		mnFile.add(mntmSaveFile);
		mntmSaveFile.addActionListener(this);
		
		mnFile.addSeparator();
		
		mntmCloseFile = new JMenuItem("  关闭数据");
		mnFile.add(mntmCloseFile);
		mntmCloseFile.addActionListener(this);
		
		mnFile.addSeparator();
		mntmExit = new JMenuItem("  退出软件");
		mnFile.add(mntmExit);
		mntmExit.addActionListener(this);
		
		mnFilter = new JMenu(" 数据处理");
		menuBar.add(mnFilter);
		
		mntmLostVal = new JMenuItem("  缺失值处理");
		mnFilter.add(mntmLostVal);
		mntmLostVal.addActionListener(this);
		
		mntmHistogram = new JMenuItem("  直方图");
		mnFilter.add(mntmHistogram);
		mntmHistogram.addActionListener(this);
		
		mntmRemoveOddValue = new JMenuItem("  去除奇异值");
		mnFilter.add(mntmRemoveOddValue);
		mntmRemoveOddValue.addActionListener(this);
				
		mntmNormalization = new JMenuItem("  数据归一化");
		mnFilter.add(mntmNormalization);
		mntmNormalization.addActionListener(this);
		
		mntmSkip = new JMenuItem("  预处理完成");
		mnFilter.add(mntmSkip);
		mntmSkip.addActionListener(this);
		
		mnDataMining = new JMenu("  数据挖掘");
		menuBar.add(mnDataMining);		
				
		mntmClusterAnalyze = new JMenuItem("  聚类分析");
		mnDataMining.add(mntmClusterAnalyze);
		mntmClusterAnalyze.addActionListener(this);
		
		mnClassAnalyze = new JMenu("  分类分析");
		mnDataMining.add(mnClassAnalyze);
		
		mntmKaFang = new JMenuItem("  卡方树");
		mnClassAnalyze.add(mntmKaFang);
		mntmKaFang.addActionListener(this);
		
		mntmC4_45 = new JMenuItem("  C4.5");
		mnClassAnalyze.add(mntmC4_45);
		mntmC4_45.addActionListener(this);
		
		mntmZAPTree = new JMenuItem("  ZAP树");
		mnClassAnalyze.add(mntmZAPTree);
		mntmZAPTree.addActionListener(this);
						
		mntmRegAnylyze = new JMenuItem("  回归分析");
		mnDataMining.add(mntmRegAnylyze);
		mntmRegAnylyze.addActionListener(this);
		
		mntmTimeAnalyze = new JMenuItem("  关联分析");
		mnDataMining.add(mntmTimeAnalyze);
		mntmTimeAnalyze.addActionListener(this);
		
		mntmAssocAnalyze = new JMenuItem("  时间序列分析");
		mnDataMining.add(mntmAssocAnalyze);
		mntmAssocAnalyze.addActionListener(this);
		
		mnSmart = new JMenu(" 智慧运营");
		menuBar.add(mnSmart);
		
		mnExCallSale = new JMenu(" 外呼营销");
		mnSmart.add(mnExCallSale);
		
		mntmImportRuleEx = new JMenuItem("  导入规则");
		mnExCallSale.add(mntmImportRuleEx);
		mntmImportRuleEx.addActionListener(this);
		
		mntmRocEx = new JMenuItem("  ROC曲线");
		mnExCallSale.add(mntmRocEx);
		mntmRocEx.addActionListener(this);
		
		mntmProfitModelEx = new JMenuItem("  利润分析");
		mnExCallSale.add(mntmProfitModelEx);
		mntmProfitModelEx.addActionListener(this);
		
		mnSmsSale = new JMenu(" 短信营销");
		mnSmart.add(mnSmsSale);
		
		mntmImportRule = new JMenuItem("  导入规则");
		mnSmsSale.add(mntmImportRule);
		mntmImportRule.addActionListener(this);
		
		mntmRoc = new JMenuItem("  ROC曲线");
		mnSmsSale.add(mntmRoc);
		mntmRoc.addActionListener(this);
		
		mntmProfitModel = new JMenuItem("  利润分析");
		mnSmsSale.add(mntmProfitModel);
		mntmProfitModel.addActionListener(this);
		
		mnCustKeep = new JMenu(" 客户保有");
		mnSmart.add(mnCustKeep);
		
		mntmImportRuleKeep = new JMenuItem("  导入规则");
		mnCustKeep.add(mntmImportRuleKeep);
		mntmImportRuleKeep.addActionListener(this);
		
		mntmRocKeep = new JMenuItem("  ROC曲线");
		mnCustKeep.add(mntmRocKeep);
		mntmRocKeep.addActionListener(this);
		
		mntmProfitModelKeep = new JMenuItem("  利润分析");
		mnCustKeep.add(mntmProfitModelKeep);
		mntmProfitModelKeep.addActionListener(this);
		
		mnAppCustKeep = new JMenu(" APP客户保有");
		mnSmart.add(mnAppCustKeep);
		
		mntmImportRuleApp = new JMenuItem("  导入规则");
		mnAppCustKeep.add(mntmImportRuleApp);
		mntmImportRuleApp.addActionListener(this);
		
		mntmRocApp = new JMenuItem("  ROC曲线");
		mnAppCustKeep.add(mntmRocApp);
		mntmRocApp.addActionListener(this);
		
		mntmProfitModelApp = new JMenuItem("  利润分析");
		mnAppCustKeep.add(mntmProfitModelApp);
		mntmProfitModelApp.addActionListener(this);			
		
		mnCpCustWarn = new JMenu(" 投诉客户预警");
		mnSmart.add(mnCpCustWarn);
		
		mntmImportRuleWarn = new JMenuItem("  导入规则");
		mnCpCustWarn.add(mntmImportRuleWarn);
		mntmImportRuleWarn.addActionListener(this);
		
		mntmRocWarn = new JMenuItem("  ROC曲线");
		mnCpCustWarn.add(mntmRocWarn);
		mntmRocWarn.addActionListener(this);
		
		mntmProfitModelWarn = new JMenuItem("  利润分析");
		mnCpCustWarn.add(mntmProfitModelWarn);
		mntmProfitModelWarn.addActionListener(this);			
						
		mnHelp = new JMenu(" 帮助");
		menuBar.add(mnHelp);		
		
		mntmAbout = new JMenuItem(" 关于");
		mnHelp.add(mntmAbout);
		mntmAbout.addActionListener(this);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}
	
	private void createPanel(){
		
		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.8);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(0.2);
		splitPane.setLeftComponent(splitPane_1);
		
		scrollPane = new JScrollPane();
		splitPane_1.setLeftComponent(scrollPane);
		
		rootNode = new DefaultMutableTreeNode("项目文件");
		tree = new JTree(rootNode);
		tree.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tree.setPreferredSize(new Dimension(200, HEIGHT));
		scrollPane.setViewportView(tree);
		
		scrollPane_2 = new JScrollPane();
		splitPane_1.setRightComponent(scrollPane_2);
		
		
		table = new JTable();
		DefaultTableModel dtm = new DefaultTableModel();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setModel(dtm);
		getColumnNames(columnVec);
		dtm.setDataVector(valueVec, columnVec);
		dtm.fireTableStructureChanged();
		dtm.fireTableDataChanged();
		scrollPane_2.setViewportView(table);
		
		scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane_1.setViewportView(textArea);
		
		toolBar = new JToolBar();
		openButton = generateToolBarButton("resource/open.png");
		openButton.addActionListener(this);
		saveButton = generateToolBarButton("resource/save.png");
		saveButton.addActionListener(this);
		closeButton = generateToolBarButton("resource/delete.png");
		closeButton.addActionListener(this);
		toolBar.add(openButton);
		toolBar.add(saveButton);
		toolBar.add(closeButton);
		contentPane.add(toolBar, BorderLayout.NORTH);
		
	}
	
	public static Vector<String> getJtableColumnVec(){
		
		return columnVec;
		
	}
	
	public static Vector<Vector<String>> getJtableValueVec(){
		
		return valueVec;
		
	}
	
	private JButton generateToolBarButton(String fileName){
		ImageIcon imageicon = null;
		
		if(null != fileName){		
			
		    imageicon = new ImageIcon(fileName);
		
		    Image image = imageicon.getImage();
		
		    image = image.getScaledInstance(22, 22, Image.SCALE_DEFAULT);
		
		    imageicon.setImage(image);		
		}
		
		JButton button = new JButton(imageicon);
		
		button.setMargin(new Insets(0,0,0,0));
		
		button.setBorder(BorderFactory.createRaisedBevelBorder());
		
		return button;
	}
	
	public void getColumnNames(Vector<String> columnVec){
			
		//only show 20 columns in startup when too many columns in config.xml
		for(int i=0; i<20; i++){
			
			String columnName = ConfigParser.columnVecInConfigOrder.get(i);
			
			columnVec.add(columnName);		
			
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		action = null;
        if(event.getSource() == mntmOpenFile || event.getSource() == openButton){       
        	action = OpenAction.Instance();        	
        }
        else if(event.getSource() == mntmCloseFile || event.getSource() == closeButton){
        	
        	if(currentNode == null){
        		return;
        	}
        	
        	action = CloseAction.Instance();        	       	
        }
        else if(event.getSource() == mntmSaveFile || event.getSource() == saveButton){
        	action = SaveAction.Instance();
        }
        else if(event.getSource() == mntmExit){
        	System.exit(0);
        }
        else if(event.getSource() == mntmNormalization){      	
        	action = NormalizationAction.Instance();
        }
        else if(event.getSource() == mntmRemoveOddValue){
        	action = OddValueAction.Instance();       	
        }
        else if(event.getSource() == mntmAbout){
            action = AboutAction.Instance();
        }
        else if(event.getSource() == mntmLostVal){
        	LostValueAction.Instance().takeAction();
        }
        else if(event.getSource() == mntmC4_45){
        	action = C45Action.Instance();        	
        }
        else if(event.getSource() == mntmRoc){
        	action = RocAction.Instance();
        }
        else if(event.getSource() == mntmSkip){
        	action = SkipAction.Instance();
        }
        else if(event.getSource() == mntmProfitModel){
        	CategoryDataSheet.Instance().testCategoryDataSheet();
        }
        else if(event.getSource() == mntmImportRule){
        	ImportRuleAction.Instance().setRocType(ROC_TYPE.ROC_SMS);
        	action = ImportRuleAction.Instance();
        }
        else if(event.getSource() == mntmImportRuleEx){
        	ImportRuleAction.Instance().setRocType(ROC_TYPE.ROC_EXT);
        	action = ImportRuleAction.Instance();
        }
        else if(event.getSource() == mntmImportRuleKeep){
        	ImportRuleAction.Instance().setRocType(ROC_TYPE.ROC_CUST);
        	action = ImportRuleAction.Instance();
        }
        else if(event.getSource() == mntmImportRuleApp){
        	ImportRuleAction.Instance().setRocType(ROC_TYPE.ROC_APP);
        	action = ImportRuleAction.Instance();
        }
        else if(event.getSource() == mntmImportRuleWarn){
        	ImportRuleAction.Instance().setRocType(ROC_TYPE.ROC_WARN);
        	action = ImportRuleAction.Instance();
        }
        
        else if(event.getSource() == mntmHistogram){
        	action = HistogramAction.Instance();
        }
        
                
        if(null!=action){
        	action.takeAction();
        }

		
	}
			    
    private void addListenerForTree(){
    	
    	JTreeListener.Instance().addListener();
    	
    }
    
    private void setOutPutToTextArea(){
    	
    	TextAreaOutPutControl.Instance().setTextArea(textArea);
    	
    	TextAreaOutPutControl.Instance().Initialize();
    	
    }
    
    private void addListenserForJtable(){ 
    	
    	JTableListener.Instance().addListener();   	
    	   	
    } 
    
    private void addListenserForJButton(){
    	
    	JButtonListener.Instance().addListener();
    	
    }
    
    public Vector<String> getSelectedChnColumnVec(){
    	
    	if(null == selectedColumnInds){
    		return null;
    	}
    	
    	Vector<String> selectedChnColumnVec = new Vector<String>();
    	
    	for(int i=0;i<selectedColumnInds.length;i++){
    		
    		String ChnColumnName = columnVec.get(selectedColumnInds[i]);
    		
    		if(null == ChnColumnName){
    			return null;
    		}
    		
    		selectedChnColumnVec.add(ChnColumnName);
    		
    	}
    	
    	return selectedChnColumnVec;
    	
    }
    
    public Vector<String> getSelectedEnColumnVec(){
    	
    	if(null == selectedColumnInds){
    		return null;
    	}
    	
    	Vector<String> selectedEnColumnVec = new Vector<String>();
    	     	
        for(int i=0;i<selectedColumnInds.length;i++){
    		
    		String ChnColumnName = columnVec.get(selectedColumnInds[i]);
    		
    		String EnColumnName = ConfigParser.chnToEnColumnName.get(ChnColumnName);
    		
    		if(null == EnColumnName){
    			return null;
    		}
    		
    		selectedEnColumnVec.add(EnColumnName);
    		
    	}
    	
    	return selectedEnColumnVec;
    	
    }
	
}
	
