/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2016 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.xn.ales.data.datimport.csv;

import java.util.List;

/**
 * @author ewanggu
 * @since 2016
 *
 */
public interface IDataImport {

    public void parse(String file);

    public void load2Db(List<String[]> resultList);

    public boolean checkHeader(String[] headers);

}
