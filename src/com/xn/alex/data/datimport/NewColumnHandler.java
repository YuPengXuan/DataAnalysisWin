package com.xn.alex.data.datimport;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.xn.ales.data.datimport.csv.DataImportFactory;
import com.xn.ales.data.datimport.csv.IDataImport;
import com.xn.alex.data.common.CommonConfig;
import com.xn.alex.data.common.CommonConfig.FILE_TYPE;
import com.xn.alex.data.common.ConfigElement;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.common.ConfigParser.DataColumnInfo;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.window.MainWindow;

public class NewColumnHandler{
	
    protected JFrame frame;
	
    protected JPanel contentPane;
    
    protected JPanel topPanel;
    
    protected JPanel bottomPanel;
    
    protected JScrollPane topScrolPanel;
    
    protected JButton okBt;
    
    protected JButton cancelBt;
    
    protected JTable table;
    
    private volatile boolean isFinished = false;
    
    private Vector<String> m_columnNames;
    
    private Map<Integer, String> m_issColumnIndToChnNameMap;
    
    private List<Integer> m_missingColumnIndexList;
    
    private String m_tableName;
    
    private DataImport m_dataImprtHandler;
    
    private FILE_TYPE fileType;
         
    public FILE_TYPE getFileType() {
		return fileType;
	}


	public void setFileType(FILE_TYPE fileType) {
		this.fileType = fileType;
	}


	public boolean isFinished() {
		return isFinished;
	}


	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	
	public NewColumnHandler(Vector<String> columnNames, Map<Integer, String> MissColumnIndToChnNameMap, List<Integer> missingColumnIndexList, DataImport dataImprtHandler, String tableName){
		m_columnNames = columnNames;
		m_issColumnIndToChnNameMap = MissColumnIndToChnNameMap;
		m_missingColumnIndexList = missingColumnIndexList;
		m_dataImprtHandler = dataImprtHandler;
		m_tableName = tableName;
	}
	
	public void run(){
		if(false == ColumnCheck(m_columnNames)){
			return;
		}
				
		HandleNewColumnType(m_columnNames,m_issColumnIndToChnNameMap, m_missingColumnIndexList, m_tableName);
		
		m_issColumnIndToChnNameMap.clear();
		
	}	
	
	private boolean ColumnCheck(Vector<String> columnNames){
		String primaryKey = "customerID";				        
		if(columnNames.contains(primaryKey) == false){	
			System.out.println("缺乏客户编号！");
			JOptionPane.showMessageDialog(null,"缺乏客户编号！","错误信息",JOptionPane.ERROR_MESSAGE);		    
		    return false;
		    	
	    } 	
		
		
		return true;
	}


	public void HandleNewColumnType(Vector<String> columnNames, Map<Integer, String> MissColumnIndToChnNameMap, List<Integer> missingColumnIndexList, String tableName){    	
    	
    	createColumnSelectWindow(MissColumnIndToChnNameMap);
    	
    	addListenerForButton(tableName, columnNames, missingColumnIndexList);
    	
    }

		
	
	public void createColumnSelectWindow(Map<Integer, String> MissColumnIndToChnNameMap){
		frame = new JFrame("列数据类型选择");
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
		contentPane = new JPanel();
				
		contentPane.setLayout(new BorderLayout());
					
		topPanel = new JPanel();
		
		topScrolPanel = new JScrollPane(); 
		
		GridLayout layout1 = new GridLayout(0,1);
	     
		topPanel.setLayout(layout1);
		
		TableValues tv = new TableValues(MissColumnIndToChnNameMap);
		
		table = new JTable(tv); 
		
		table.setRowHeight(30);
		
		TableColumnModel tcm= table.getColumnModel();
		
        TableColumn tc = tcm.getColumn(TableValues.NUMERIC_TYPE);
        
        GenderRenderer cellRenderer = new GenderRenderer(); 
       
        tc.setCellRenderer(cellRenderer);
        
        tc.setCellEditor(new GenderEditor());
		
		topScrolPanel.setViewportView(table);
		
		topPanel.add(topScrolPanel);
								
		contentPane.add(topPanel, BorderLayout.CENTER);
		
		bottomPanel = new JPanel();
		
        GridLayout Layout2 = new GridLayout(0,2);
	    
	    bottomPanel.setLayout(Layout2);
	    
	    Layout2.setHgap(30);
		
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));
		
		okBt = new JButton("确定");
	     
	    bottomPanel.add(okBt);
	     
	    cancelBt = new JButton("取消");
	     
	    bottomPanel.add(cancelBt);
	     
	    contentPane.add(bottomPanel, BorderLayout.SOUTH);
		
		frame.setContentPane(contentPane);

		frame.pack();		
		     
		frame.setSize(320, 400);
		
		frame.setLocationRelativeTo(null);
		     
		frame.setResizable(false);

	    frame.setVisible(true);
		
	}
	
	private void addListenerForButton(final String tableName, final Vector<String> columnNames, final List<Integer> missingColumnIndexList){
		okBt.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent Event) {
				// TODO Auto-generated method stub
				int row = table.getRowCount();				
				
				for(int i=0;i<row;i++){
					String columnName = table.getValueAt(i, 0).toString();
					
					String value = table.getValueAt(i, 1).toString();
					
					String valueType = null;
					
					if("true".equals(value)){
						valueType = "VARCHAR(100)";
					}
					else{
						valueType = "FLOAT";
					}					
					
					updateConfigFile(columnName,valueType, columnNames, missingColumnIndexList);					
															
					//System.out.println("row:" + i + " colunName:" + columnName + " value:" + value);
					
				}

				if(false == createTable(columnNames,tableName)){
					frame.dispose();
					return;
				}
				
				frame.dispose();
				
				loadHugeData(tableName, columnNames, missingColumnIndexList);
							
			}			
			
		});
		
		cancelBt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent Event){
				// TODO Auto-generated method stub
				frame.dispose();
			}
		});
	}
	
	private void updateConfigFile(String columnName, String valType, Vector<String> columnNames, List<Integer> missingColumnIndexList){
		long maxGenId = ConfigParser.Instance().getMaxGenId();
		
		long newGenId = maxGenId + 1;
		
		ConfigParser.Instance().setMaxGenId(newGenId);
		
		String newDatabaseColName = CommonConfig.GEN_COLUMN_NAME_PREFIX + newGenId;
		
		ConfigElement cfgElement = new ConfigElement();
		
		cfgElement.mExcelColumnName = columnName;
		
		cfgElement.mValueType = valType;
		
		cfgElement.mDefValue = "0";
		
		cfgElement.mDatabaseName = newDatabaseColName;
		
		ConfigParser.columnInfoMap.put(newDatabaseColName, cfgElement);
		
		ConfigParser.chnToEnColumnName.put(columnName, newDatabaseColName);
		
		ConfigParser.columnVecInConfigOrder.add(columnName);
		
		if(cfgElement.mValueType.contains("VARCHAR")){			
	    	Pattern pat = Pattern.compile("VARCHAR\\((\\d+)\\)");
	    	
	    	Matcher macher = pat.matcher(cfgElement.mValueType);
	    	
	    	while(macher.find()){	    		
	    		String sizeStr = macher.group(1);
	    		
	    		int size = Integer.parseInt(sizeStr);
	    		
	    		ConfigParser.columnNameToSizeMap.put(columnName, size);
	    	}
	    }
		
		int index = columnNames.indexOf(columnName);
		
		columnNames.set(index, newDatabaseColName);
		
		int listInd = missingColumnIndexList.indexOf(index);
		
		missingColumnIndexList.remove(listInd);				
		
	}
	
	private boolean createTable(Vector<String> columnNames, String tableName){
		String primaryKey = "customerID";
		
		List<DataColumnInfo> columnNameToType = ConfigParser.Instance().getColumnNameToTypeMap(columnNames);
		
		if(false == DatabaseAction.Instance().createTable(tableName, columnNameToType, primaryKey)){
			return false;
		}
		
		return true;
		
	}
	
	private void loadHugeData(String tableName, Vector<String> columnNames, List<Integer> missingColumnIndexList){
		String fileName = m_dataImprtHandler.getfileName();
		
	    boolean isSuccess = false;
	    
	    if(FILE_TYPE.CSV_FILE == getFileType()){
	    	final IDataImport csvImport = DataImportFactory.getDataImport(CommonConfig.FILE_TYPE.CSV_FILE, tableName);
	    	
	    	isSuccess = csvImport.parse(fileName);
	    }
	    else{
	    	isSuccess = HugeDataImport.Instance().importData(fileName, tableName, columnNames, missingColumnIndexList);
	    }
		
		if(false == isSuccess){
		    System.out.println("导入大数据失败");
		
		    MainWindow.treeNodeToFullPathMap.remove(MainWindow.Instance().getCurrentNode().hashCode());
		
		    return;
	 }
	
     MainWindow.fileNameToTableMap.put(fileName, tableName);
	
     m_dataImprtHandler.updateMainWindowColumnVec(columnNames);
	
	 System.out.println("文件：" + fileName +" 导入数据库成功");
	}
	
}
