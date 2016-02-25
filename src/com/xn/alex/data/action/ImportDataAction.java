package com.xn.alex.data.action;

import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.xn.alex.data.common.CommonConfig.FILE_TYPE;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.database.DatabaseConstant;
import com.xn.alex.data.datimport.DataImport;
import com.xn.alex.data.graphics.TreeDataSheet;
import com.xn.alex.data.resultobj.treeNodeResultObj;
import com.xn.alex.data.rule.GenerateTreeByLeaf;

public class ImportDataAction extends WindowAction {

    private static ImportDataAction importDataActionHandler = null;

    private ImportDataAction() {

    }

    public static ImportDataAction Instance() {

        if (null == importDataActionHandler) {
            importDataActionHandler = new ImportDataAction();
        }

        return importDataActionHandler;
    }

    @Override
    public void takeAction() {

        final JFileChooser fileChooser = new JFileChooser();

        openAFileDialog(fileChooser);

        final File selectedFile = fileChooser.getSelectedFile();

        if (null == selectedFile) {
            return;
        }

        System.out.println("开始导入数据...");

        final String fullFileName = selectedFile.toString();

        final FILE_TYPE fileType = getFileType(fullFileName);

        if (FILE_TYPE.INVALID_FILE == fileType) {
            System.out.println("文件类型不支持");
            return;
        }
        final String tableName = DatabaseConstant.TREE_DATA_IMP_TABLE;
        final DataImport dataImportHandler = new DataImport(fullFileName, tableName, fileType);
        dataImportHandler.loadDataForTree();

        final Vector<String> tableColumnVec = getTableColumnName();

        if (tableColumnVec.size() == 0) {
            return;
        }

        if (false == getResultForTreeNode(tableColumnVec)) {
            return;
        }

        TreeDataSheet.Instance().refresh();

    }

    private Vector<String> getTableColumnName() {
        final Vector<String> columnNames = new Vector<String>();

        try {

            final String tableName = "information_schema.columns";

            final String selecedCol = "COLUMN_NAME";

            final String condition = "table_name='" + DatabaseConstant.TREE_DATA_IMP_TABLE + "'";

            final ResultSet rs = DatabaseAction.Instance().getOneResult(tableName, selecedCol, condition);

            final ResultSetMetaData data = rs.getMetaData();

            while (rs.next()) {

                for (int i = 1; i <= data.getColumnCount(); i++) {
                    final String val = rs.getString(i);

                    columnNames.add(val);
                }
            }

            DatabaseAction.Instance().closeCurrentConnection();
        } catch (final Exception e) {
            e.printStackTrace();

            DatabaseAction.Instance().closeCurrentConnection();

            columnNames.clear();
        }

        return columnNames;
    }

    private boolean getResultForTreeNode(final Vector<String> tableColumnVec) {

        final treeNodeResultObj resultTree = GenerateTreeByLeaf.getResultNode();

        if (null == resultTree) {
            return false;
        }

        //others

        final Vector<Vector<treeNodeResultObj>> treeNodeResultObjVec = GenerateTreeByLeaf.getTreeNodeByLevelVec();

        final int level = treeNodeResultObjVec.size();

        for (int i = level - 1; i >= 0; i--) {
            final Vector<treeNodeResultObj> levelObjVec = treeNodeResultObjVec.get(i);
            if (false == loopAndSetDataIntoTree(levelObjVec, tableColumnVec)) {
                System.out.println("计算错误！");
                return false;
            }
        }
        return true;

    }

    private boolean loopAndSetDataIntoTree(final Vector<treeNodeResultObj> levelObjVec,
            final Vector<String> tableColumnVec) {
        for (int i = 0; i < levelObjVec.size(); i++) {
            final treeNodeResultObj obj = levelObjVec.get(i);

            if (obj.isLeaf == true) {
                if (false == isColumnExist(obj.chnColumnName, tableColumnVec)) {
                    System.out.println("导入数据列信息和规则信息不匹配");
                    return false;
                }

                final String condition = obj.sql;

                final String tableName = DatabaseConstant.TREE_DATA_IMP_TABLE;

                final int count = DatabaseAction.Instance().getCountFromTable(tableName, condition);

                final float probality = Float.parseFloat(obj.Probability);

                final int resultNum = (int) (count * probality + 0.5);

                if ("1".equals(obj.Prediction)) {
                    obj.conditionMeet = resultNum;
                    obj.conditionNotMeet = count - obj.conditionMeet;
                } else {
                    obj.conditionNotMeet = resultNum;
                    obj.conditionMeet = count - obj.conditionNotMeet;
                }

                updateObjInfo(obj);

                continue;
            }

            final Vector<treeNodeResultObj> childVec = obj.childNodeVec;

            obj.conditionMeet = 0;

            obj.conditionNotMeet = 0;

            for (int j = 0; j < childVec.size(); j++) {
                final treeNodeResultObj childObj = childVec.get(j);

                if (childObj == null) {
                    continue;
                }

                if (childObj.conditionMeet == -1 || childObj.conditionNotMeet == -1) {
                    return false;
                }

                obj.conditionMeet += childObj.conditionMeet;

                obj.conditionNotMeet += childObj.conditionNotMeet;

            }

            obj.Prediction = obj.conditionMeet > obj.conditionNotMeet ? "1" : "0";

            final long value = obj.conditionMeet > obj.conditionNotMeet ? obj.conditionMeet : obj.conditionNotMeet;

            obj.Probability = String.valueOf((float) value / (obj.conditionMeet + obj.conditionNotMeet));

            updateObjInfo(obj);
        }

        return true;
    }

    private boolean isColumnExist(final Vector<String> chnColumnName, final Vector<String> tableColumnVec) {
        for (int i = 0; i < chnColumnName.size(); i++) {
            final String chnColName = chnColumnName.get(i);

            final String enColName = ConfigParser.chnToEnColumnName.get(chnColName);

            if (null == enColName) {
                return false;
            }

            if (false == tableColumnVec.contains(enColName)) {
                return false;
            }
        }

        return true;
    }

    private void updateObjInfo(final treeNodeResultObj obj) {
        if (null == obj) {
            return;
        }

        obj.nodeInfo = "节点号：" + obj.nodeNumber + "\n";

        obj.nodeInfo += "预测结果：" + obj.Prediction + "\n";

        obj.nodeInfo += "概率：" + obj.Probability + "\n";

        obj.nodeInfo += "节点属性：" + obj.condition;

    }

    private void openAFileDialog(final JFileChooser fileChooser) {

        fileChooser.setCurrentDirectory(new File("."));

        fileChooser.setAcceptAllFileFilterUsed(false);

        final String[][] fileENames = { { ".DBF", "数据库文件（*.DBF）" }, { ".xls", "MS-Excel 2003 文件(*.xls)" },
                { ".xlsx", "MS-Excel 2007 文件(*.xlsx)" } };

        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(final File file) {
                return true;
            }

            @Override
            public String getDescription() {
                return "";
            }
        });

        for (final String[] fileEName : fileENames) {

            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

                @Override
                public boolean accept(final File file) {

                    if (file.getName().endsWith(fileEName[0]) || file.isDirectory()) {

                        return true;
                    }

                    return false;
                }

                @Override
                public String getDescription() {

                    return fileEName[1];
                }

            });
        }

        fileChooser.showDialog(null, null);
    }

    private FILE_TYPE getFileType(final String fileName) {

        final int pos = fileName.lastIndexOf(".");

        if (-1 == pos) {
            return FILE_TYPE.INVALID_FILE;
        }

        final String fileExtensionName = fileName.substring(pos + 1);

        if ("xlsx".equals(fileExtensionName.toLowerCase())) {
            return FILE_TYPE.XLSX_FILE;
        } else if ("xls".equals(fileExtensionName.toLowerCase())) {
            return FILE_TYPE.XLS_FILE;
        } else if ("dbf".equals(fileExtensionName.toLowerCase())) {
            return FILE_TYPE.DBF_FILE;
        } else if ("csv".equals(fileExtensionName.toLowerCase())) {
            return FILE_TYPE.CSV_FILE;
        }

        return FILE_TYPE.INVALID_FILE;
    }

}
