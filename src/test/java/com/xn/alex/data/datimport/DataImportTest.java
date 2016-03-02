package com.xn.alex.data.datimport;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.xn.ales.data.datimport.csv.DataImportFactory;
import com.xn.ales.data.datimport.csv.IDataImport;
import com.xn.alex.data.common.CommonConfig;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.database.DataAnalysisException;
import com.xn.alex.data.database.MySqlExecuter;
import com.xn.alex.data.database.SqlTask;
import com.xn.alex.data.login.LoginAction;

public class DataImportTest {
    HugeDataImport dataImport;

//    DataImport defaultDataImport;

    private static String SAMPLE_FILE = "D:\\projects\\data\\test1.csv";

    @Before
    public void before() throws Exception {
        dataImport = HugeDataImport.Instance();
//        defaultDataImport = new DataImport(SAMPLE_FILE, null, CommonConfig.FILE_TYPE.CSV_FILE);
        try {
            final ConfigParser cParser = ConfigParser.Instance();
            //parse EventDecoderCounter.xml                 
            final SAXParserFactory factory = SAXParserFactory.newInstance();

            final SAXParser parser = factory.newSAXParser();
            final File confFile = new File("config/config.xml");
            parser.parse(confFile, cParser);

            LoginAction.Instance().setLoginUserName("root");
            LoginAction.Instance().setLoginPassword("shroot");
            MySqlExecuter mySqlExecuter = MySqlExecuter.getMySqlExecuter();
            
            final StringBuilder createTableDefine = new StringBuilder("CREATE TABLE test.test (id INTEGER PRIMARY KEY");
            createTableDefine.append(", COLUMN" + 0 + " INTEGER NULL");
            for(int i = 1; i < 74; i ++) {
            	createTableDefine.append(", COLUMN" + i + " VARCHAR(255) NULL");
            }
            createTableDefine.append(") ENGINE MyISAM");
            
            mySqlExecuter.executer(new SqlTask() {
				
				@Override
				public Statement run(Connection connection) throws SQLException {
					final Statement statement = (Statement) connection.createStatement();
					statement.addBatch("CREATE DATABASE IF NOT EXISTS test");
					statement.addBatch("USE test");
					statement.addBatch("DROP TABLE IF EXISTS test.test");
					statement.addBatch(createTableDefine.toString());
    				statement.executeBatch();
					return statement;
				}
			});
        } catch (final Exception e) {
            System.out.println("parser fail!");

            System.exit(1);
        }
    }

    @Test
    public void testCSVImport() throws Exception {
        final IDataImport csvImport = DataImportFactory.getDataImport(CommonConfig.FILE_TYPE.CSV_FILE, "test.test");
        csvImport.parse(SAMPLE_FILE);
    }
    
    @After
    public void after() throws DataAnalysisException {
    	MySqlExecuter.getMySqlExecuter().executer(new SqlTask() {
			
			@Override
			public Statement run(Connection connection) throws SQLException {
				final Statement statement = (Statement) connection.createStatement();
				statement.addBatch("DROP DATABASE IF EXISTS test");
				statement.executeBatch();
				return statement;
			}
		});
    }
    
}
