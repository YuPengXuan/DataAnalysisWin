package com.xn.ales.data.datimport.csv;

import java.io.InputStream;

public interface IDataImport {

    public boolean parse(String file);

    public void load2Db(InputStream resultList, String tableName);

}
