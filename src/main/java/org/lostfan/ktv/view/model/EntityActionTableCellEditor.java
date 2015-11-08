package org.lostfan.ktv.view.model;

import java.util.List;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

import org.lostfan.ktv.model.EntityModel;
import org.lostfan.ktv.view.EntityView;

public class EntityActionTableCellEditor extends ActionTableCellEditor {

    public EntityActionTableCellEditor(TableCellEditor editor){
        super(editor);
    }

    protected void openEntityTableView(JTable table, int row, int column){
//        tableView = new EntityTableView((BaseEntityModel)newModel);
        JTextArea textArea = new JTextArea(10, 50);
        Object value = table.getValueAt(row, column);
        if(value!=null){
            textArea.setText((String)value);
            textArea.setCaretPosition(0);
        }
        int result = JOptionPane.showOptionDialog(table
                , new JScrollPane(textArea), (String)table.getColumnName(column)
                , JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if(result==JOptionPane.OK_OPTION)
            table.setValueAt(textArea.getText(), row, column);
    }

    @Override
    protected void openEntityView(JTable table, int row, int column) {
        List<EntityModel> entityModels = ((EntityTableModel)table.getModel()).getEntityModel().getEntityModels();
        for (EntityModel entityModel : entityModels) {
            if(entityModel.getEntityClass() == ((EntityTableModel)table.getModel()).getColumnClass(column)) {

                new EntityView(entityModel);
            }
        }
        ;
    }
}
