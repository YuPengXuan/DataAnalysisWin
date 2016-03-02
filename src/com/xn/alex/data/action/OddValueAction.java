package com.xn.alex.data.action;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.common.CommonConfig.CURRENT_ACTION;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.datimport.DataExport;
import com.xn.alex.data.window.MainWindow;

public class OddValueAction extends WindowAction {

	private static OddValueAction oddValueActionHandler = null;

	private JDialog frame;

	private JPanel contentPane;

	private JCheckBox largerChecker;

	private JCheckBox lessChecker;

	private JCheckBox equalChecker;

	private JButton OKBt;

	private JButton cancelBt;

	private JTextField largerCheckerTextField;

	private JTextField lessCheckerTextField;

	private JTextField equalCheckerTextField;

	private Map<String, String> conditionMap = new ConcurrentHashMap<String, String>();

	private OddValueAction() {

	}

	public static OddValueAction Instance() {

		if (null == oddValueActionHandler) {
			oddValueActionHandler = new OddValueAction();
		}

		return oddValueActionHandler;
	}

	public void takeAction() {

		Vector<String> colName = MainWindow.Instance().getSelectedEnColumnVec();

		if (null == colName || 0 == colName.size()) {

			JOptionPane.showMessageDialog(null, "请先选择要处理的数据列", "错误信息",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (false == createChoiceDialog()) {
			System.out.println("构筑显示界面失败!");

			return;
		}

	}

	private boolean createChoiceDialog() {

		try {

			frame = new JDialog(MainWindow.Instance(), "去除奇异值");

			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			contentPane = new JPanel();

			contentPane.setLayout(new BorderLayout());

			JPanel leftPanel = new JPanel(new GridLayout(0, 1));

			leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 5));

			largerChecker = new JCheckBox("删除大于右边数值的行(>)");

			leftPanel.add(largerChecker);

			lessChecker = new JCheckBox("删除小于右边数值的行(<)");

			leftPanel.add(lessChecker);

			equalChecker = new JCheckBox("删除等于右边数值的行(=)");

			leftPanel.add(equalChecker);

			contentPane.add(leftPanel, BorderLayout.WEST);

			GridLayout layout = new GridLayout(0, 1);

			layout.setVgap(15);

			JPanel rightPanel = new JPanel(layout);

			rightPanel
					.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 20));

			largerCheckerTextField = new JTextField(3);

			rightPanel.add(largerCheckerTextField);

			lessCheckerTextField = new JTextField(3);

			rightPanel.add(lessCheckerTextField);

			equalCheckerTextField = new JTextField(3);

			rightPanel.add(equalCheckerTextField);

			contentPane.add(rightPanel, BorderLayout.CENTER);

			JPanel bottomPanel = new JPanel();

			GridLayout layout2 = new GridLayout(0, 2);

			layout2.setHgap(30);

			bottomPanel.setLayout(layout2);

			bottomPanel.setBorder(BorderFactory
					.createEmptyBorder(0, 40, 30, 40));

			OKBt = new JButton("确定");
			OKBt.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					// TODO Auto-generated method stub
					conditionMap.clear();

					Vector<String> selectedColumnVec = MainWindow.Instance()
							.getSelectedEnColumnVec();

					if (null == selectedColumnVec) {
						System.out.println("删除奇异值失败");
						return;
					}

					if (false == isInputConditionValid()) {
						System.out.println("输入条件有误");
						return;

					}

					String fileName = MainWindow.treeNodeToFullPathMap
							.get(MainWindow.Instance().getCurrentNode()
									.hashCode());

					String tableName = MainWindow.fileNameToTableMap
							.get(fileName);

					for (int i = 0; i < selectedColumnVec.size(); i++) {

						String columnName = selectedColumnVec.get(i);

						int j = 0;

						String condition = "";

						for (Map.Entry<String, String> entry : conditionMap
								.entrySet()) {

							if (true == ConfigParser.columnInfoMap
									.get(columnName).mValueType
									.contains("VARCHAR")) {

								condition = condition + columnName
										+ entry.getKey() + "'"
										+ entry.getValue() + "'";
							} else {
								condition = condition + columnName
										+ entry.getKey() + entry.getValue();
							}

							j++;

							if (j != conditionMap.size()) {
								condition = condition + " and ";
							}

						}

						DatabaseAction.Instance().deleteData(tableName,
								condition);

					}

					MainWindow.setCurrentAct(CURRENT_ACTION.JTABLE_EDIT);

					DataExport dataExport = new DataExport(fileName, MainWindow
							.Instance().getTable());

					dataExport.run();

					MainWindow.setCurrentAct(CURRENT_ACTION.NONE);

					frame.dispose();

				}

			});

			bottomPanel.add(OKBt);

			cancelBt = new JButton("取消");
			cancelBt.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					// TODO Auto-generated method stub
					frame.dispose();
				}

			});

			bottomPanel.add(cancelBt);

			contentPane.add(bottomPanel, BorderLayout.SOUTH);

			frame.setContentPane(contentPane);

			frame.pack();

			frame.setLocationRelativeTo(MainWindow.Instance());

			frame.setPreferredSize(new Dimension(300, 250));
			frame.setSize(300, 250);

			frame.setResizable(false);

			frame.setModal(true);
			frame.setVisible(true);

		} catch (Exception e) {

			e.printStackTrace();

			return false;
		}

		return true;

	}


	private boolean isInputConditionValid() {

		Pattern pattern = Pattern.compile("^-?([0-9]*.[0-9]*?)$");

		String largerNumInStr = "-1";

		String lessNumberInStr = "-1";

		String equalInStr = "-1";

		if (largerChecker.isSelected()
				&& null != largerCheckerTextField.getText()) {

			largerNumInStr = largerCheckerTextField.getText();

			Matcher isNum = pattern.matcher(largerNumInStr);

			if (false == isNum.matches()) {

				JOptionPane.showMessageDialog(null, "请在大于条件中输入正确数字", "错误信息",
						JOptionPane.ERROR_MESSAGE);

				return false;

			}

			conditionMap.put(">", largerNumInStr);

		}

		if (lessChecker.isSelected() && null != lessCheckerTextField.getText()) {

			lessNumberInStr = lessCheckerTextField.getText();

			Matcher isNum = pattern.matcher(lessNumberInStr);

			if (false == isNum.matches()) {

				JOptionPane.showMessageDialog(null, "请在小于条件中输入正确数字", "错误信息",
						JOptionPane.ERROR_MESSAGE);

				return false;

			}

			conditionMap.put("<", lessNumberInStr);

		}

		if (equalChecker.isSelected()) {

			equalInStr = equalCheckerTextField.getText();

			if (null != equalInStr) {
				conditionMap.put("=", equalInStr);
			}

		}

		if (largerChecker.isSelected() && lessChecker.isSelected()) {

			if (Double.parseDouble(lessNumberInStr) <= Double
					.parseDouble(largerNumInStr)) {

				JOptionPane.showMessageDialog(null, "请输入输入正确范围数字", "错误信息",
						JOptionPane.ERROR_MESSAGE);

				return false;

			}

		}

		if (largerChecker.isSelected() && lessChecker.isSelected()
				&& equalChecker.isSelected()) {

			JOptionPane.showMessageDialog(null, "三个条件不能同时选择", "错误信息",
					JOptionPane.ERROR_MESSAGE);

			return false;

		}

		if (largerChecker.isSelected() && equalChecker.isSelected()) {
			if (Double.parseDouble(largerNumInStr) != Double
					.parseDouble(equalInStr)) {
				JOptionPane.showMessageDialog(null, "大于和等于数字必须相同", "错误信息",
						JOptionPane.ERROR_MESSAGE);

				return false;
			}
			conditionMap.remove(">");
			conditionMap.remove("=");
			conditionMap.put(">=", largerNumInStr);

		}

		if (lessChecker.isSelected() && equalChecker.isSelected()) {
			if (Double.parseDouble(lessNumberInStr) != Double
					.parseDouble(equalInStr)) {
				JOptionPane.showMessageDialog(null, "小于和等于数字必须相同", "错误信息",
						JOptionPane.ERROR_MESSAGE);

				return false;
			}
			conditionMap.remove("<");
			conditionMap.remove("=");
			conditionMap.put("<=", lessNumberInStr);

		}

		return true;
	}

}
