package org.lostfan.ktv.view.model;

import java.awt.*;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.utils.DefaultContextMenu;
import org.lostfan.ktv.view.DialogView;
import org.lostfan.ktv.view.EntitySelectionView;
import org.lostfan.ktv.view.components.EntityPanel;
import org.lostfan.ktv.view.components.EntityPanelFactory;
import org.lostfan.ktv.view.components.EntitySelectionFactory;

public class EntityTableCellEditor implements TableCellEditor {

    private TableCellEditor editor;
    private EntityFieldTypes entityFieldTypes;


    protected JTable table;
    protected int row, column;
    private EntityPanel entityPanel;

    public EntityTableCellEditor(TableCellEditor editor, EntityFieldTypes entityFieldTypes){

        this.entityFieldTypes = entityFieldTypes;
        this.editor = editor;
    }

    public Component getTableCellEditorComponent(JTable table, Object value
            , boolean isSelected, int row, int column){
        Entity entity = (Entity) value;
        JPanel panel = new JPanel(new BorderLayout());
        this.entityPanel = EntityPanelFactory.createEntityPanel(entityFieldTypes);

        new DefaultContextMenu().add((JTextField) this.entityPanel.getComboBox().getEditor().getEditorComponent());
        if(entity != null) {
            this.entityPanel.setSelectedEntity(entity);
            ((JTextField) (this.entityPanel.getComboBox().getEditor().getEditorComponent())).setText(entity.getName());
        }
        entityPanel.getTableButton().addActionListener(e -> {
            EntitySelectionView entitySelectionView = EntitySelectionFactory.createForm(entityFieldTypes);
            DialogView.open(entitySelectionView);
            if (entitySelectionView.get() != null) {
                this.entityPanel.setSelectedEntity(entitySelectionView.get());
                ((JTextField) (entityPanel.getComboBox().getEditor().getEditorComponent())).setText(entitySelectionView.get().getName());
                this.entityPanel.invalidate();
                this.entityPanel.repaint();
            }
            editor.stopCellEditing();

        });
        this.table = table;
        this.row = row;
        this.column = column;

        //TODO Need to find best solution
        entityPanel.getTableButton().setPreferredSize(new Dimension(15,table.getRowHeight(row)));
        entityPanel.getEntityButton().setPreferredSize(new Dimension(15,table.getRowHeight(row)));
        entityPanel.getTableButton().setEnabled(false);
        entityPanel.getEntityButton().setEnabled(false);
        panel.add(this.entityPanel);

        SwingUtilities.invokeLater(new Thread() {

            @Override
            public void run() {
                try {
                    sleep(50);
                } catch (InterruptedException ex) {
                }

                SwingUtilities.invokeLater(() -> {
                    entityPanel.getTableButton().setEnabled(true);
                    entityPanel.getEntityButton().setEnabled(true);
                });

            }
        });
        return panel;
    }

    public Object getCellEditorValue(){
        return this.entityPanel.getSelectedEntity();
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

