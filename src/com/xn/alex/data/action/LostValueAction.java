package com.xn.alex.data.action;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JRadioButton;

import com.xn.alex.data.common.CommonConfig.CURRENT_ACTION;
import com.xn.alex.data.common.CommonConfig;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.datimport.DataExport;
import com.xn.alex.data.window.MainWindow;

public class LostValueAction extends WindowAction {

	private static LostValueAction lostValueActionHandler = null;

	private JDialog dialog;

	private JButton btOk;

	private JButton btCancel;

	private JRadioButton deleteBt;

	private JRadioButton fillDefBt;

	private LostValueAction() {

	}

	public static LostValueAction Instance() {

		if (null == lostValueActionHandler) {
			lostValueActionHandler = new LostValueAction();
		}

		return lostValueActionHandler;
	}

	public void takeAction() {

		if (false == createDialog()) {
			return;
		}

		addButtonListener();

	}

	private boolean createDialog() {

		try {

			dialog = new JDialog();

			dialog.setTitle("缺失值处理");

			Container contentPane = dialog.getContentPane();

			contentPane.setLayout(null);

			ButtonGroup bg = new ButtonGroup();

			deleteBt = new JRadioButton("删除字段为空的数据");

			deleteBt.setFont(new Font("宋体", Font.PLAIN, 12));

			deleteBt.setBounds(40, 15, 200, 30);

			fillDefBt = new JRadioButton("将空字段填上默认值");

			fillDefBt.setFont(new Font("宋体", Font.PLAIN, 12));

			fillDefBt.setBounds(40, 45, 200, 30);

			bg.add(deleteBt);

			bg.add(fillDefBt);

			deleteBt.setSelected(true);

			btOk = new JButton("确定");

			btOk.setBounds(30, 90, 70, 30);
			btOk.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					// TODO Auto-generated method stub
					if (true == deleteBt.isSelected()) {
						deleteNullValueRow();
					} else if (true == fillDefBt.isSelected()) {
						fillDefaultValueToNullRow();
					}
					dialog.dispose();
				}

			});

			btCancel = new JButton("取消");

			btCancel.setBounds(120, 90, 70, 30);
			btCancel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					// TODO Auto-generated method stub
					dialog.dispose();
				}

			});

			contentPane.add(deleteBt);

			contentPane.add(fillDefBt);

			contentPane.add(btOk);

			contentPane.add(btCancel);

			dialog.setModal(true);
			dialog.setPreferredSize(new Dimension(230, 180));
			dialog.setSize(230, 180);
			dialog.setResizable(false);
			dialog.setLocationRelativeTo(MainWindow.Instance());
			dialog.pack();
			dialog.setVisible(true);

		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private void addButtonListener() {

	}

	private void deleteNullValueRow() {

		try {

			StringBuffer condition = new StringBuffer("");

			Vector<String> chnColumnNameVec = MainWindow.getJtableColumnVec();

			for (int i = 0; i < chnColumnNameVec.size(); i++) {
				String chnColumnName = chnColumnNameVec.get(i);

				String enColumnName = ConfigParser.chnToEnColumnName
						.get(chnColumnName);

				if (null == enColumnName) {
					continue;
				}

				condition.append(enColumnName);

				if (true == ConfigParser.columnInfoMap.get(enColumnName).mValueType
						.contains("VARCHAR")) {
					condition.append("='" + CommonConfig.IVALID_VALUE + "'");
				} else {
					condition.append("=" + CommonConfig.IVALID_VALUE + "");
				}

				if (i != chnColumnNameVec.size() - 1) {
					condition.append(" or ");
				}

			}

			String fileName = MainWindow.treeNodeToFullPathMap.get(MainWindow
					.Instance().getCurrentNode().hashCode());

			if (null == fileName) {
				return;
			}

			String dataTable = MainWindow.fileNameToTableMap.get(fileName);

			if (null == dataTable) {
				return;
			}

			if (true == DatabaseAction.Instance().deleteData(dataTable,
					condition.toString())) {

				DataExport dataExport = new DataExport(fileName, MainWindow
						.Instance().getTable());

				MainWindow.setCurrentAct(CURRENT_ACTION.JTABLE_EDIT);

				dataExport.run();

				MainWindow.setCurrentAct(CURRENT_ACTION.NONE);
			}
		} catch (Exception e) {
			System.out.println("删除缺失值失败！");
		}

	}

	private void fillDefaultValueToNullRow() {

		try {

			String fileName = MainWindow.treeNodeToFullPathMap.get(MainWindow
					.Instance().getCurrentNode().hashCode());

			if (null == fileName) {
				return;
			}

			String tableName = MainWindow.fileNameToTableMap.get(fileName);

			if (null == tableName) {
				return;
			}

			Vector<String> chnColumnNameVec = MainWindow.getJtableColumnVec();

			Vector<String> columnNameVec = new Vector<String>();

			Vector<String> valueVec = new Vector<String>();

			String condition = "";

			for (int i = 0; i < chnColumnNameVec.size(); i++) {

				String chnColumnName = chnColumnNameVec.get(i);

				String enColumnName = ConfigParser.chnToEnColumnName
						.get(chnColumnName);

				columnNameVec.clear();

				columnNameVec.add(enColumnName);

				String defVal = ConfigParser.columnInfoMap.get(enColumnName).mDefValue;

				valueVec.clear();

				valueVec.add(defVal);

				condition = "";

				if (true == ConfigParser.columnInfoMap.get(enColumnName).mValueType
						.contains("VARCHAR")) {
					condition = enColumnName + "='-1'";
				} else {
					condition = enColumnName + "=-1";
				}

				DatabaseAction.Instance().updateColumn(tableName,
						columnNameVec, valueVec, condition);

			}

			DataExport dataExport = new DataExport(fileName, MainWindow
					.Instance().getTable());

			MainWindow.setCurrentAct(CURRENT_ACTION.JTABLE_EDIT);

			dataExport.run();

			MainWindow.setCurrentAct(CURRENT_ACTION.NONE);

		} catch (Exception e) {
			System.out.println("填充默认值失败！");
		}

	}

}
