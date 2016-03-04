package com.xn.ales.data.datimport.csv;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.List;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.RowProcessor;
import com.xn.alex.data.common.CommonConfig;

public class DataRowListProcessor implements RowProcessor {

    private static final String INVALID_VALUE = "#NULL!";

    private static final String REGEX = "-?[0-9]+.*[0-9]*";

    private StringBuilder rows;

    private String[] headers;

    private final int batchNum;

    private final IDataImport dataImport;

    private List<String> numbericIndexList;

    private final int obsoleteLine = 0;

    private int rowNum = 1;

    private int totalNum = 0;
    
    private int currentNum = 0;

    public DataRowListProcessor(final int batchNum, final IDataImport dataImport) {
        super();
        this.batchNum = batchNum;
        this.dataImport = dataImport;
        rows = new StringBuilder();
        headers = null;
        numbericIndexList = null;
    }

    @Override
    public void processStarted(final ParsingContext context) {
        //System.out.println("Started to process rows of data.");
    }

    @Override
    public void rowProcessed(final String[] row, final ParsingContext context) {
        if (headers == null) {
            headers = context.headers();
            ((CSVImport) dataImport).processENColumn(headers);
            numbericIndexList = DataImportFactory.getNumericListColumnIndex(((CSVImport) dataImport).getColumnNames());
        }
        // boolean obsolete = false;
        //for (String value : row) {
        for (int i = 0; i < row.length; i++) {
            if (row[i] == null || row[i].equals(INVALID_VALUE)) {
                row[i] = String.valueOf(CommonConfig.IVALID_VALUE);
            }
            rows.append(row[i]);
            if (i != row.length - 1) {
                rows.append(",");
            }
            /*if (numbericIndexList.contains(value)) {
                final Boolean strResult = value.matches(REGEX);
                if (strResult == false) {
                    obsolete = true;
                }
            }*/
        }
        rows.append("\r\n");
        rowNum++;
        currentNum++;
        /*   if (obsolete == true) {
               obsoleteLine++;
           } else {
        rows.add(row);
        }*/
        if (rowNum == batchNum) {
            final ByteArrayInputStream ingputStream = new ByteArrayInputStream(String.valueOf(rows).getBytes());
            dataImport.load2Db(new BufferedInputStream(ingputStream, 3 * 1024 * 1024),
                    ((CSVImport) dataImport).getTableName());
            //System.out.println("Processed line " + rowNum);
            dataImport.getPropertyListener().valueChanged("progress", (int)((1.0f * currentNum / totalNum) * 100));
            rows = new StringBuilder();
            rowNum = 0;
        }
    }

    @Override
    public void processEnded(final ParsingContext context) {
        headers = context.headers();
    }

    public StringBuilder getRows() {
        return rows;
    }

    public String[] getHeaders() {
        return headers;
    }

    /**
     * @return the obsoleteLine
     */
    public int getObsoleteLine() {
        return obsoleteLine;
    }

    /**
     * @return the rowNum
     */
    public int getRowNum() {
        return rowNum;
    }

    /**
     * @return the totalNum
     */
    public int getTotalNum() {
        return totalNum;
    }
    
    public int getCurrentNum(){
    	return currentNum;
    }
    public void setTotalNum(int totalNum){
    	this.totalNum = totalNum;
    }

}
