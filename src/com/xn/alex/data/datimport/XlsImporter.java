package com.xn.alex.data.datimport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.xn.alex.data.common.CommonConfig;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.common.CommonConfig.FILE_TYPE;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.window.MainWindow;

public class XlsImporter extends AbstractImporter {

	public XlsImporter(String fileName) {
		super(fileName, FILE_TYPE.XLS_FILE);
	}

	public XlsImporter(String fileName,String tableName){
		super(fileName, tableName,FILE_TYPE.XLS_FILE);
	}
	private InputStream is = null;

	private HSSFWorkbook xlsWorkBook = null;

	@Override
	public List<String> getColumnInfo() throws IOException {
		List<String> columnNames = new ArrayList<String>();
		InputStream localIs = getInstanceOfIs();

		if ((null == xlsWorkBook) && (FILE_TYPE.XLS_FILE == getFileType())) {
			xlsWorkBook = new HSSFWorkbook(localIs);
		}

		Sheet sheet = xlsWorkBook.getSheetAt(0);

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

				missingColumnIndexList.add(i);
			}
		}

		return columnNames;
	}

	@Override
	public void load() throws IOException {
		List<String> columnNames = new ArrayList<String>();
		final String fileName = getFileName();
		final String tableName = getTableName();

		File xlsxFile = new File(fileName);
		long fileSize = xlsxFile.length();

		if (fileSize > 8000000) {
			setLargeFile(true);

			if (false == HugeDataImport.Instance().importData(fileName,
					tableName, columnNames, missingColumnIndexList)) {
				System.out.println("导入大数据失败");

				MainWindow.treeNodeToFullPathMap.remove(MainWindow.Instance()
						.getCurrentNode().hashCode());
				return;
			}

			MainWindow.fileNameToTableMap.put(fileName, tableName);

			updateMainWindowColumnVec(columnNames);

			System.out.println("文件：" + fileName + " 导入数据库成功");
		} else {
			setLargeFile(false);
			columnNames = getColumnInfo();

			commomLoadIntoDb(fileName, columnNames);
		}

	}

	private InputStream getInstanceOfIs() throws FileNotFoundException {

		if (null == is) {
			is = new FileInputStream(getFileName());
		}

		return is;
	}

	@Override
	protected boolean loadDataIntoDatabase(String tableName,
			List<String> columnNames) {
		Vector<Vector<String>> commitValueVec = MainWindow.getJtableValueVec();

		commitValueVec.clear();

		FILE_TYPE fileType = getFileType();

		List<Integer> numbericIndexList = getNumericListColumnIndex(columnNames);

		int sheetNum = xlsWorkBook.getNumberOfSheets();;

		int obsoleteLine = 0;

		for (int i = 0; i < sheetNum; i++) {
			Sheet sheet = null;
			sheet = xlsWorkBook.getSheetAt(i);
			
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

			}

		}

		DatabaseAction.Instance().insertTable(tableName, columnNames,
				commitValueVec);

		if (obsoleteLine > 0) {

			System.out.println(obsoleteLine + "行不符合格式的数据将会被丢弃！");

		}

		return true;
	}

}
