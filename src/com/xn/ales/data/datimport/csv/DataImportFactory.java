package com.xn.ales.data.datimport.csv;

import java.util.ArrayList;
import java.util.List;

import com.xn.alex.data.common.CommonConfig.FILE_TYPE;
import com.xn.alex.data.common.ConfigParser;

public class DataImportFactory {

    private DataImportFactory() {

    }

    public static IDataImport getDataImport(final FILE_TYPE type, final String tableName) {
        IDataImport dataImport = null;
        if (type == null) {
            System.out.println("File type is null!");
        } else {
            if (type == FILE_TYPE.CSV_FILE) {
                dataImport = new CSVImport(tableName);
            } else {
                System.out.println("File type is not supported!");
            }
        }
        return dataImport;
    }

    public static List<String> getNumericListColumnIndex(final List<String> columnNameVec) {

        final List<String> numericIndexList = new ArrayList<String>();

        for (final String column : columnNameVec) {

            if (false == ConfigParser.columnInfoMap.get(column).mValueType.contains("VARCHAR")) {

                numericIndexList.add(column);
            }

        }

        return numericIndexList;

    }

}
