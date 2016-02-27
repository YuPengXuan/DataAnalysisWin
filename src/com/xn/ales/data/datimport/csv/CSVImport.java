package com.xn.ales.data.datimport.csv;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.database.DataAnalysisException;
import com.xn.alex.data.database.DatabaseConstant;
import com.xn.alex.data.database.LoadDataInFileTask;
import com.xn.alex.data.database.MySqlExecuter;
import com.xn.alex.data.database.SqlTask;

public class CSVImport implements IDataImport {
    private static final int BATCH_NUM = 10000;

    private List<String> columnNames = new ArrayList<String>();

    private final List<String> missingColumnIndexList = new ArrayList<String>();
    
    private final String tableName;

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
    	final String newFormatfile = file.replaceAll("\\\\", "\\\\\\\\");
        final long startTime = System.currentTimeMillis();
    	try {
			MySqlExecuter.getMySqlExecuter().executer(new SqlTask() {
				
				@Override
				public Statement run(Connection connection) throws SQLException {
					final Statement statement = connection.createStatement();
					statement.addBatch("ALTER TABLE " + tableName + " DISABLE KEYS");
					statement.addBatch("LOAD DATA LOCAL INFILE \'" +  newFormatfile + "\' " + DatabaseConstant.IGNORE + " INTO TABLE " + tableName + " FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\r\\n' IGNORE 1 LINES");
					statement.addBatch("ALTER TABLE " + tableName + " ENABLE KEYS");
					statement.executeBatch();
					return statement;
				}
			});
		} catch (DataAnalysisException e) {
			System.err.println("导入数据文件 " + file + " 出错");
			return false;
		}
    	System.out.println("TIme is " + (System.currentTimeMillis() - startTime));
    	return true;
//        boolean rt = true;
//        final long start = System.currentTimeMillis();
//        final CsvParserSettings settings = new CsvParserSettings();
//        final DataRowListProcessor rowProcessor = new DataRowListProcessor(BATCH_NUM, this);
//        //settings.setRowProcessor(new ConcurrentRowProcessor(rowProcessor));
//        settings.setRowProcessor(rowProcessor);
//        settings.setHeaderExtractionEnabled(true);
//        settings.setLineSeparatorDetectionEnabled(true);
//        settings.setEmptyValue("");
//        //settings.setNullValue("");
//        settings.setSkipEmptyLines(true);
//        
//        final CsvParser parser = new CsvParser(settings);
//        FileReader reader = null;
//        try {
//            reader = new FileReader(file);
//            parser.parse(reader);
//            final String[] headers = rowProcessor.getHeaders();
//            processENColumn(headers);
//            if (checkHeader(headers) == false) {
//                rt = false;
//                return rt;
//            }
//
//            // processENColumn(headers);
//            final List<String[]> rows = rowProcessor.getRows();
//            load2Db(rows, tableName);
//            
//            final StringBuilder sb = new StringBuilder();
//            for (final String value : columnNames) {
//                sb.append(value);
//                sb.append(" ");
//            }
//
//            System.out.println("Headers are " + sb.toString());
//            if (rows.size() > 0) {
//                System.out.println("Processed line " + rows.size());
//            }
//            if (rowProcessor.getObsoleteLine() > 0) {
//                System.out.println(rowProcessor.getObsoleteLine() + "行不符合要求数据被丢弃！");
//            }
//            System.out.println("导入数据成功");
//            final long end = System.currentTimeMillis();
//            System.out.println("Time costs " + (end - start));
//        } catch (final FileNotFoundException e) {
//            e.printStackTrace();
//            rt = false;
//        } finally {
//            parser.stopParsing();
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException ioe) {
//
//                }
//            }
//        }
//        return rt;
    }

    /* (non-Javadoc)
     * @see com.xn.alex.data.datimport.IDataImport#load(java.util.List)
     */
    @Override
    public void load2Db(final List<String[]> resultList, final String tableName) {
	  try {
		MySqlExecuter.getMySqlExecuter().executer(new LoadDataInFileTask(resultList, DatabaseConstant.IGNORE, tableName, resultList.size()));
	  } catch (DataAnalysisException e) {
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

}
