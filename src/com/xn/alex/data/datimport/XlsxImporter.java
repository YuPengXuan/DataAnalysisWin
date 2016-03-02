package com.xn.alex.data.datimport;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.xn.alex.data.common.CommonConfig;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.common.CommonConfig.FILE_TYPE;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.ui.ProgressBar;
import com.xn.alex.data.window.MainWindow;

public class XlsxImporter extends AbstractImporter {

	public XlsxImporter(String fileName) {
		super(fileName, FILE_TYPE.XLSX_FILE);
		// TODO Auto-generated constructor stub
	}

	public XlsxImporter(String fileName, String tableName) {
		super(fileName, tableName, FILE_TYPE.XLSX_FILE);
	}

	private InputStream is = null;

	private XSSFWorkbook xssWorkBook = null;

	@Override
	public List<String> getColumnInfo() throws IOException {
		List<String> columnNames = new ArrayList<String>();
		InputStream localIs = getInstanceOfIs();

		if ((null == xssWorkBook) && (FILE_TYPE.XLSX_FILE == getFileType())) {

			xssWorkBook = new XSSFWorkbook(new BufferedInputStream(localIs));

		}

		Sheet sheet = xssWorkBook.getSheetAt(0);

		if (sheet == null) {
			return columnNames;
		}

		Row row = sheet.getRow(0);

		if (row == null) {

			System.out.println("文件格式有误，第一行数据列名为空！");

			return columnNames;
		}

		int cellNum = row.getLastCellNum();

		for (int i = 0; i < cellNum; i++) {

			if (row.getCell(i) == null) {
				continue;
			}

			Cell cell = row.getCell(i);

			String columnNameCh = cell.toString();

			String columnNameEn = ConfigParser.chnToEnColumnName
					.get(columnNameCh);

			if (null != columnNameEn) {
				columnNames.add(columnNameEn);
			} else {

				columnNames.add(columnNameCh);
				missingColumnIndexList.add(i);

				MissColumnIndToChnNameMap.put(i, columnNameCh);
			}
		}
		return columnNames;
	}

	@Override
	public void load() throws IOException {
		final String fileName = getFileName();
		final String tableName = getTableName();

		File xlsxFile = new File(fileName);
		long fileSize = xlsxFile.length();

		if (fileSize > 8000000) {
			final List<String> columnNames = new ArrayList<String>();
			setLargeFile(true);

			if (false == getColumnInfoAndData(fileName, columnNames,
					missingColumnIndexList, MissColumnIndToChnNameMap,
					tableName)) {

				System.out.println("导入大数据失败");

				MainWindow.treeNodeToFullPathMap.remove(MainWindow.Instance()
						.getCurrentNode().hashCode());

				return;

			}

			if (MissColumnIndToChnNameMap.size() != 0) {
				setNeedChooseColType(true);

				NewColumnHandler colHandTh = new NewColumnHandler(columnNames,
						MissColumnIndToChnNameMap, missingColumnIndexList,
						this, tableName);
				colHandTh.setFileType(getFileType());
				// colHandTh.start();
				colHandTh.run();
				if(!colHandTh.isFinished()){
					return;
				}
			}

			setNeedChooseColType(false);

			final ProgressBar progresBar = new ProgressBar(MainWindow.Instance(),"加载文件"){

				@Override
				public void onRun() throws Exception {
					if (false == HugeDataImport.Instance().importData(fileName,
							tableName, columnNames, missingColumnIndexList)) {
						System.out.println("导入大数据失败");

						MainWindow.treeNodeToFullPathMap.remove(MainWindow.Instance()
								.getCurrentNode().hashCode());

						throw new Exception("导入大数据失败");
					}

					MainWindow.fileNameToTableMap.put(fileName, tableName);

					updateMainWindowColumnVec(columnNames);

					System.out.println("文件：" + fileName + " 导入数据库成功");
				}

				@Override
				public void onClose() {
					this.dispose();
				}

				@Override
				public void onException(Exception e) {
					JOptionPane.showMessageDialog(this, e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
					this.dispose();
				}
				
			};
			

		} else {
			setLargeFile(false);

			List<String> columnNames = getColumnInfo();

			if (MissColumnIndToChnNameMap.size() != 0) {

				setNeedChooseColType(true);

				NewColumnHandler colHandTh = new NewColumnHandler(columnNames,
						MissColumnIndToChnNameMap, missingColumnIndexList,
						this, tableName);
				colHandTh.setFileType(getFileType());
				// colHandTh.start();
				colHandTh.run();

				if (!colHandTh.isFinished()) {
					return;
				}
			}

			commomLoadIntoDb(fileName, columnNames);
		}

	}

	@Override
	protected boolean loadDataIntoDatabase(final String tableName,
			final List<String> columnNames) {
		
		final String fileName = getFileName();
		ProgressBar progressBar = new ProgressBar(MainWindow.Instance(),"加载文件"){

			@Override
			public void onRun() throws Exception {
				setProgress(0,"开始加载文件:" + fileName);
				
				Vector<Vector<String>> commitValueVec = MainWindow.getJtableValueVec();

				commitValueVec.clear();

				FILE_TYPE fileType = getFileType();

				List<Integer> numbericIndexList = getNumericListColumnIndex(columnNames);

				int sheetNum = xssWorkBook.getNumberOfSheets();

				int obsoleteLine = 0;

				int progress = 0;
				for (int i = 0; i < sheetNum; i++) {

					Sheet sheet = null;
					
					sheet = xssWorkBook.getSheetAt(i);
					
					if (sheet == null)
						continue;

					for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {

						Row row = sheet.getRow(rowNum);
						if (null == row || missingColumnIndexList.contains(rowNum)) {
							continue;
						}

						Vector<String> tmpVec = new Vector<String>();

						boolean isAddThisRow = true;

						int cellNum = row.getLastCellNum();

						for (int j = 0; j < cellNum; j++) {

							Cell cell = row.getCell(j);

							String cellValue = null;

							if (null == cell || "#NULL!".equals(cell.toString())) {

								// String columnName = columnNames.get(i);

								// String defVal =
								// ConfigParser.columnInfoMap.get(columnName).mDefValue;

								// cellValue = defVal;

								// cellValue = "-1";
								cellValue = String.valueOf(CommonConfig.IVALID_VALUE);

							} else {

								if (true == numbericIndexList.contains(j)
										&& cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {

									isAddThisRow = false;

									obsoleteLine++;

									break;

								}
								cellValue = cell.toString();

							}

							tmpVec.add(cellValue);

						}

						if (true == isAddThisRow) {

							commitValueVec.add(tmpVec);

						}

						isAddThisRow = true;
						
						if(rowNum % (sheet.getLastRowNum() / 100) == 0){
							setProgress(progress++);	
						}
						
					}

				}
				
				DatabaseAction.Instance().insertTable(tableName, columnNames,
						commitValueVec);

				setProgress(100,"导入数据完成.");
				if (obsoleteLine > 0) {

					System.out.println(obsoleteLine + "行不符合格式的数据将会被丢弃！");

				}
			}

			@Override
			public void onClose() {
				this.dispose();
			}

			@Override
			public void onException(Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}
			
		};
		

		return true;
	}

	private InputStream getInstanceOfIs() throws FileNotFoundException {

		if (null == is) {
			is = new FileInputStream(getFileName());
		}

		return is;
	}

	private boolean getColumnInfoAndData(String fileName,
			List<String> columnNames, List<Integer> missColumnIndexList,
			Map<Integer, String> MissColumnIndToChnNameMap, String tableName) {

		InputStream sheet = null;

		boolean isSucc = true;

		try {
			OPCPackage pkg = OPCPackage.open(fileName);
			XSSFReader r = new XSSFReader(pkg);
			SharedStringsTable sst = r.getSharedStringsTable();

			XMLReader parser = XMLReaderFactory
					.createXMLReader("org.apache.xerces.parsers.SAXParser");
			ContentHandler handler = new SaxHeaderParser(sst, columnNames,
					missColumnIndexList, MissColumnIndToChnNameMap);
			parser.setContentHandler(handler);

			Iterator<InputStream> sheets = r.getSheetsData();
			while (sheets.hasNext()) {
				System.out.println("开始读取列名...:\n");
				sheet = sheets.next();
				InputSource sheetSource = new InputSource(sheet);
				parser.parse(sheetSource);
				sheet.close();
				System.out.println("");
			}
		} catch (Exception e) {
			if (!"".equals(e.getMessage())) {
				e.printStackTrace();
				isSucc = false;
			}
			try {
				sheet.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		System.out.println("结束读取列名.\n");

		return isSucc;
	}
}
