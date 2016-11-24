package com.xn.ales.data.datimport.csv;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.database.DataAnalysisException;
import com.xn.alex.data.database.DatabaseConstant;
import com.xn.alex.data.database.LoadDataInFileTask;
import com.xn.alex.data.database.MySqlExecuter;
import com.xn.alex.data.ui.IPropertyListener;

public class CSVImport implements IDataImport {
    private static final int BATCH_NUM = 20000;

    private List<String> columnNames = new ArrayList<String>();

    private final List<String> missingColumnIndexList = new ArrayList<String>();

    private final String tableName;

    private IPropertyListener propertyListener;
    
    /**
     * @param tableName
     */
    public CSVImport(final String tableName) {
        super();
        this.tableName = tableName;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    @Override
    public boolean parse(final String file) {
        /* final String newFormatfile = file.replaceAll("\\\\", "\\\\\\\\");
         final long startTime = System.currentTimeMillis();
         try {
             MySqlExecuter.getMySqlExecuter().executer(new SqlTask() {

                 @Override
                 public Statement run(final Connection connection) throws SQLException {
                     final Statement statement = connection.createStatement();
                     statement.addBatch("ALTER TABLE " + tableName + " DISABLE KEYS");
                     statement.addBatch("LOAD DATA LOCAL INFILE \'" + newFormatfile + "\' " + DatabaseConstant.IGNORE
                             + " INTO TABLE " + tableName
                             + " FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\r\\n' IGNORE 1 LINES");
                     statement.addBatch("ALTER TABLE " + tableName + " ENABLE KEYS");
                     statement.executeBatch();
                     return statement;
                 }
             });
         } catch (final DataAnalysisException e) {
             System.err.println("导入数据文件 " + file + " 出错");
             return false;
         }
         System.out.println("TIme is " + (System.currentTimeMillis() - startTime));
         return true;*/
        boolean rt = true;
        final long start = System.currentTimeMillis();
        final CsvParserSettings settings = new CsvParserSettings();
        final DataRowListProcessor rowProcessor = new DataRowListProcessor(BATCH_NUM, this);
        rowProcessor.setTotalNum(getLineNumbers(file));
        //settings.setRowProcessor(new ConcurrentRowProcessor(rowProcessor));
        settings.setRowProcessor(rowProcessor);
        settings.setHeaderExtractionEnabled(true);
        settings.setLineSeparatorDetectionEnabled(true);
        settings.setEmptyValue("");
        //settings.setNullValue("");
        settings.setSkipEmptyLines(true);
        
        
        final CsvParser parser = new CsvParser(settings);
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            parser.parse(reader);
            final String[] headers = rowProcessor.getHeaders();
            processENColumn(headers);
            if (checkHeader(headers) == false) {
                rt = false;
                return rt;
            }

            // processENColumn(headers);
            final StringBuilder rows = rowProcessor.getRows();
            final ByteArrayInputStream ingputStream = new ByteArrayInputStream(String.valueOf(rows).getBytes("UTF-8"));
            load2Db(new BufferedInputStream(ingputStream, 3 * 1024 * 1024), tableName);

            final StringBuilder sb = new StringBuilder();
            for (final String value : columnNames) {
                sb.append(value);
                sb.append(" ");
            }

            /* System.out.println("Headers are " + sb.toString());*/
            if (rowProcessor.getCurrentNum() > 0) {
                System.out.println("导入数据行数: " + rowProcessor.getCurrentNum());
            }
            if (rowProcessor.getObsoleteLine() > 0) {
                System.out.println(rowProcessor.getObsoleteLine() + "行不符合要求数据被丢弃！");
            }
            System.out.println("导入数据成功");
            final long end = System.currentTimeMillis();
            System.out.println("导入时间: " + (end - start) + "ms");
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
            rt = false;
        } catch (Exception e) {
        	e.printStackTrace();
            rt = false;
        }
        finally {
            parser.stopParsing();
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException ioe) {

                }
            }
        }
        return rt;
    }

    /* (non-Javadoc)
     * @see com.xn.alex.data.datimport.IDataImport#load(java.util.List)
     */
    @Override
    public void load2Db(final InputStream resultList, final String tableName) {
        try {
            MySqlExecuter.getMySqlExecuter().executer(
                    new LoadDataInFileTask(resultList, DatabaseConstant.IGNORE, tableName));
        } catch (final DataAnalysisException e) {
            System.err.println("数据文件加载失败");
        }
    }

    public boolean checkHeader(final String[] headers) {
        for (int curCol = 0; curCol < headers.length; curCol++) {
            if ("".equals(headers[curCol])) {
                final String msg = "第" + (curCol + 1) + "列列名不能为空";
                System.out.println(msg);
                return false;
            }
        }
        return true;
    }

    public void processENColumn(final String[] headers) {
        for (final String value : headers) {
            final String columnNameCh = value.trim();
            final String columnNameEn = ConfigParser.chnToEnColumnName.get(columnNameCh);
            if (null != columnNameEn) {
                columnNames.add(columnNameEn);
            } else {
                missingColumnIndexList.add(value);
            }
        }
    }

    /**
     * @return the columnNames
     */
    public List<String> getColumnNames() {
        return columnNames;
    }

    /**
     * @param columnNames the columnNames to set
     */
    public void setColumnNames(final List<String> columnNames) {
        this.columnNames = columnNames;
    }

	@Override
	public void setPropertyListener(IPropertyListener listener) {
		this.propertyListener = listener;
		
	}

	@Override
	public IPropertyListener getPropertyListener() {
		return propertyListener;
	}

	private int getLineNumbers(final String fileName){
		long start = System.currentTimeMillis();
		int lines = 0;
		File file = new File(fileName);
		long fileLength = file.length();
		LineNumberReader rf = null;
		try{
			rf = new LineNumberReader(new FileReader(file));
			rf.skip(fileLength);
			lines = rf.getLineNumber();
		}catch(IOException e){
			
		}finally{
			if(rf != null){
				try {
					rf.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		long end = System.currentTimeMillis() - start;
		return lines;
	}

}
