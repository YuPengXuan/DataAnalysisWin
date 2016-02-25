package com.xn.alex.data.datimport;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class GenderRenderer extends JComboBox<Object> implements TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6361888426070700845L;
	
	public GenderRenderer(){
        super();
        addItem("字符串型");
        addItem("数值型");
    }

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		// TODO Auto-generated method stub
		if(isSelected){
            setForeground(table.getForeground());
            super.setBackground(table.getBackground());
        }else{
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        boolean isNumeric = ((Boolean)value).booleanValue();
        setSelectedIndex(isNumeric? 0 : 1);
        return this;
	}

}
