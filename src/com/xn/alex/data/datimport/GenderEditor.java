package com.xn.alex.data.datimport;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;

public class GenderEditor extends JComboBox<Object> implements TableCellEditor{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1323120799222703602L;

    private EventListenerList listenerList = new EventListenerList();

    private ChangeEvent changeEvent = new ChangeEvent(this);
    
    public GenderEditor(){
              super();
              addItem("字符串型");
              addItem("数值型");            
    }
    public void addCellEditorListener(CellEditorListener l) {
              listenerList.add(CellEditorListener.class,l);
    }
    public void removeCellEditorListener(CellEditorListener l) {
              listenerList.remove(CellEditorListener.class,l);
    }
    private void fireEditingStopped(){
              CellEditorListener listener;
              Object[] listeners = listenerList.getListenerList();
              for(int i = 0; i < listeners.length; i++){
                       if(listeners[i]== CellEditorListener.class){
 
                                listener= (CellEditorListener)listeners[i+1];
           
                                listener.editingStopped(changeEvent);
                       }
              }
    }
    public void cancelCellEditing() {        
    }

    public boolean stopCellEditing() {
              //System.out.println("编辑其中一个单元格，再点击另一个单元格时，调用。");
              fireEditingStopped();
              return true;
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                       boolean isSelected, int row, int column) {
              boolean isMale = ((Boolean)value).booleanValue();
              setSelectedIndex(isMale? 0 : 1);
              return this;
    }

    public boolean isCellEditable(EventObject anEvent) {
              return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
              return true;
    }


    public Object getCellEditorValue() {
              return new Boolean(getSelectedIndex() == 0 ? true : false);
    }

}
