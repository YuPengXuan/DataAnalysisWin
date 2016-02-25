package com.xn.ales.data.datimport.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.xn.alex.data.common.CommonConfig.FILE_TYPE;
import com.xn.alex.data.common.ConfigParser;

public class DataImportFactory {

    private DataImportFactory() {

    }

    public static IDataImport getDataImport(final FILE_TYPE type) {
        IDataImport dataImport = null;
        if (type == null) {
            System.out.println("File type is null!");
        } else {
            if (type == FILE_TYPE.CSV_FILE) {
                dataImport = new CSVImport();
            } else {
                System.out.println("File type is not supported!");
            }
        }
        return dataImport;
    }

    public static List<Integer> getNumericListColumnIndex(final Vector<String> columnNameVec) {

        final List<Integer> numericIndexList = new ArrayList<Integer>();

        for (int i = 0; i < columnNameVec.size(); i++) {

            final String columnName = columnNameVec.get(i);

            if (false == ConfigParser.columnInfoMap.get(columnName).mValueType.contains("VARCHAR")) {

                numericIndexList.add(i);

            }

        }

        return numericIndexList;

    }

}
