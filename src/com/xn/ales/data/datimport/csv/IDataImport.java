package com.xn.ales.data.datimport.csv;

import java.io.InputStream;

import com.xn.alex.data.ui.IPropertyListener;

public interface IDataImport {

    public boolean parse(String file);

    public void load2Db(InputStream resultList, String tableName);
    
    public void setPropertyListener(IPropertyListener listener);
    
    public IPropertyListener getPropertyListener();
    

}
