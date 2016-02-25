package com.xn.ales.data.datimport.csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.xn.alex.data.common.ConfigParser;

public class CSVImport implements IDataImport {
    private static final int BATCH_NUM = 10000;

    final List<String> columnNames = new ArrayList<String>();

    final List<String> missingColumnIndexList = new ArrayList<String>();

    @Override
    public void parse(final String file) {
        final long start = System.currentTimeMillis();
        final CsvParserSettings settings = new CsvParserSettings();
        final DataRowListProcessor rowProcessor = new DataRowListProcessor(BATCH_NUM, this);
        //settings.setRowProcessor(new ConcurrentRowProcessor(new RowProcessor() {
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

            }
            processENColumn(headers);
            final List<String[]> rows = rowProcessor.getRows();
            load2Db(rows);
            final StringBuilder sb = new StringBuilder();
            for (final String value : columnNames) {
                sb.append(value);
                sb.append(" ");
            }
            System.out.println("Headers are " + sb.toString());
            System.out.println("Processed line " + rows.size());
            final long end = System.currentTimeMillis();
            System.out.println("Time costs " + (end - start));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            parser.stopParsing();
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException ioe) {

                }
            }
        }

    }

    /* (non-Javadoc)
     * @see com.xn.alex.data.datimport.IDataImport#load(java.util.List)
     */
    @Override
    public void load2Db(final List<String[]> resultList) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.xn.alex.data.datimport.IDataImport#checkHeader()
     */
    @Override
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

    private void processENColumn(final String[] headers) {
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
}
