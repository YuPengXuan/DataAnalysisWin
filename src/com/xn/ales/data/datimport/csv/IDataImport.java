package com.xn.ales.data.datimport.csv;

import java.util.List;

public interface IDataImport {

    public void parse(String file);

    public void load2Db(List<String[]> resultList, String tableName);

}
