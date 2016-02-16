package org.lostfan.ktv.view.model;

import java.awt.*;
import java.time.LocalDate;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.utils.DefaultContextMenu;
import org.lostfan.ktv.view.DialogView;
import org.lostfan.ktv.view.EntitySelectionView;
import org.lostfan.ktv.view.components.DatePickerField;
import org.lostfan.ktv.view.components.EntityPanel;
import org.lostfan.ktv.view.components.EntityPanelFactory;
import org.lostfan.ktv.view.components.EntitySelectionFactory;

public class DateTableCellEditor implements TableCellEditor {

    private TableCellEditor editor;
    private EntityFieldTypes entityFieldTypes;


    protected JTable table;
    protected int row, column;
    private DatePickerField datePickerField;

    public DateTableCellEditor(TableCellEditor editor, EntityFieldTypes entityFieldTypes){

        this.entityFieldTypes = entityFieldTypes;
        this.editor = editor;
    }

    public Component getTableCellEditorComponent(JTable table, Object value
            , boolean isSelected, int row, int column){
        LocalDate date = (LocalDate) value;
        datePickerField = new DatePickerField(date);
        datePickerField.getComponent(0).setPreferredSize(new Dimension(0 ,table.getRowHeight(row)));
        datePickerField.getComponent(1).setPreferredSize(new Dimension(15, table.getRowHeight(row)));

        datePickerField.getComponent(1).setEnabled(false);

        SwingUtilities.invokeLater(new Thread() {

            @Override
            public void run() {
                try {
                    sleep(50);
                } catch (InterruptedException ex) {
                }

                SwingUtilities.invokeLater(() -> {
                    datePickerField.getComponent(1).setEnabled(true);
                });

            }
        });

        return datePickerField;
    }

    public Object getCellEditorValue(){
        return this.datePickerField.getValue();
    }

    public boolean isCellEditable(EventObject anEvent){
        return editor.isCellEditable(anEvent);
    }

    public boolean shouldSelectCell(EventObject anEvent){
        return editor.shouldSelectCell(anEvent);
    }

    public boolean stopCellEditing(){
        return editor.stopCellEditing();
    }

    public void cancelCellEditing(){
        editor.cancelCellEditing();
    }

    public void addCellEditorListener(CellEditorListener l){
        editor.addCellEditorListener(l);
    }

    public void removeCellEditorListener(CellEditorListener l){
        editor.removeCellEditorListener(l);
    }

}

