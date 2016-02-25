package com.xn.alex.data.datimport;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Before;
import org.junit.Test;

import com.xn.ales.data.datimport.csv.DataImportFactory;
import com.xn.ales.data.datimport.csv.IDataImport;
import com.xn.alex.data.common.CommonConfig;
import com.xn.alex.data.common.ConfigParser;

public class DataImportTest {
    HugeDataImport dataImport;

    DataImport defaultDataImport;

    private static String SAMPLE_FILE = "C:\\NBI\\Prestudy\\Git-Repository\\DataAnalysisWin\\resource\\test.csv";

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

        } catch (final Exception e) {
            System.out.println("parser fail!");

            System.exit(1);
        }
    }

    @Test
    public void testCSVImport() throws Exception {
        final IDataImport csvImport = DataImportFactory.getDataImport(CommonConfig.FILE_TYPE.CSV_FILE);
        csvImport.parse(SAMPLE_FILE);

    }
}
