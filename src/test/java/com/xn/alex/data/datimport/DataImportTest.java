package test.java.com.xn.alex.data.datimport;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Before;
import org.junit.Test;

import com.xn.ales.data.datimport.csv.DataImportFactory;
import com.xn.ales.data.datimport.csv.IDataImport;
import com.xn.alex.data.common.CommonConfig;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.database.DataBaseConnection;
import com.xn.alex.data.database.DatabaseOperation;
import com.xn.alex.data.database.SqlExecuter;
import com.xn.alex.data.datimport.HugeDataImport;
import com.xn.alex.data.login.LoginAction;
import com.xn.alex.data.datimport.DataImport;

public class DataImportTest {
    HugeDataImport dataImport;

    DataImport defaultDataImport;

    private static String SAMPLE_FILE = "D:\\projects\\data\\test1.csv";

    @Before
    public void before() throws Exception {
        dataImport = HugeDataImport.Instance();
        defaultDataImport = new DataImport(SAMPLE_FILE, null, CommonConfig.FILE_TYPE.CSV_FILE);
        try {
            final ConfigParser cParser = ConfigParser.Instance();
            //parse EventDecoderCounter.xml                 
            final SAXParserFactory factory = SAXParserFactory.newInstance();

            final SAXParser parser = factory.newSAXParser();
            final File confFile = new File("config/config.xml");
            parser.parse(confFile, cParser);

            LoginAction.Instance().setLoginUserName("root");
            LoginAction.Instance().setLoginPassword("shroot");
            final StringBuilder createTableDefine = new StringBuilder("CREATE TABLE test.test (id DOUBLE PRIMARY KEY");
            
            for(int i = 0; i < 74; i ++) {
            	createTableDefine.append(", COLUMN" + i + " VARCHAR(255) NULL");
            }
            createTableDefine.append(") ENGINE MyISAM");
            
            DatabaseOperation.executer(new DataBaseConnection(), new SqlExecuter() {
    			
    			@Override
    			public void executer(Connection connection) throws SQLException {
    				Statement statement = connection.createStatement();
    				statement.executeUpdate("CREATE DATABASE IF NOT EXISTS test");
    			}
    		});
            
            DatabaseOperation.executer(new DataBaseConnection(), new SqlExecuter() {
    			
        			@Override
        			public void executer(Connection connection) throws SQLException {
        				Statement statement = connection.createStatement();
        				statement.executeUpdate("DROP TABLE IF EXISTS test.test");
        			}
        	});
            
            DatabaseOperation.executer(new DataBaseConnection(), new SqlExecuter() {
    			
    			@Override
    			public void executer(Connection connection) throws SQLException {
    				Statement statement = connection.createStatement();
    				statement.executeUpdate(createTableDefine.toString());
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
}
