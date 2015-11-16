package org.lostfan.ktv.view.model;

import java.awt.*;
import java.net.URL;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.utils.DefaultContextMenu;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.EntitySelectionView;
import org.lostfan.ktv.view.EntityView;
import org.lostfan.ktv.view.components.EntityComboBox;
import org.lostfan.ktv.view.components.EntityComboBoxFactory;
import org.lostfan.ktv.view.components.EntityModelFactory;
import org.lostfan.ktv.view.components.EntitySelectionFactory;

/**
 * Created by Ihar_Niakhlebau on 30-Oct-15.
 */
public abstract class ActionTableCellEditor implements TableCellEditor {

    private TableCellEditor editor;
    private JButton viewTableEntitiesButton = new JButton();
    private JButton viewEntityButton = new JButton();
    private EntityFieldTypes entityFieldTypes;
    private ViewActionListener changeActionListener;


    protected JTable table;
    protected int row, column;
    private EntityComboBox entityComboBox;

    public ActionTableCellEditor(TableCellEditor editor, EntityFieldTypes entityFieldTypes){

        this.entityFieldTypes = entityFieldTypes;
        URL urlTableEntities = ActionTableCellEditor.class.getClassLoader().getResource("images/ellipsis.gif");

        if(urlTableEntities != null) {
            ImageIcon ELLIPSIS_ICON = new ImageIcon(urlTableEntities);
            Image image = ELLIPSIS_ICON.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
            ELLIPSIS_ICON = new ImageIcon(image);
            viewTableEntitiesButton.setIcon(ELLIPSIS_ICON);
        }

        URL urlEntity = ActionTableCellEditor.class.getClassLoader().getResource("images/search.png");

        if(urlEntity != null) {
            ImageIcon ELLIPSIS_ICON = new ImageIcon(urlEntity);
            Image image = ELLIPSIS_ICON.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
            ELLIPSIS_ICON = new ImageIcon(image);
            viewEntityButton.setIcon(ELLIPSIS_ICON);
        }

        this.editor = editor;
        viewTableEntitiesButton.addActionListener(e -> {

            EntitySelectionView entitySelectionView = EntitySelectionFactory.createForm(entityFieldTypes);
            if (entitySelectionView.getSelectedEntity() != null) {
                this.entityComboBox.setSelectedEntity(entitySelectionView.getSelectedEntity());
                ((JTextField) (entityComboBox.getEditor().getEditorComponent())).setText(entitySelectionView.getSelectedEntity().getName());
                this.entityComboBox.invalidate();
                this.entityComboBox.repaint();
            }
            editor.stopCellEditing();

        });

        // ui-tweaking
        viewTableEntitiesButton.setFocusable(false);
        viewTableEntitiesButton.setFocusPainted(false);
        viewTableEntitiesButton.setMargin(new Insets(0, 0, 0, 0));

        viewEntityButton.addActionListener(e -> {
            editor.cancelCellEditing();
            EntityView entityView = EntityModelFactory.createForm(entityFieldTypes, this.entityComboBox.getSelectedEntity().getId());
//            entityView.changeActionListener.actionPerformed(null);
        });

        viewEntityButton.setFocusable(false);
        viewEntityButton.setFocusPainted(false);
        viewEntityButton.setMargin(new Insets(0, 0, 0, 0));
    }

    public Component getTableCellEditorComponent(JTable table, Object value
            , boolean isSelected, int row, int column){
        Entity entity = (Entity) value;
        JPanel panel = new JPanel(new BorderLayout());
//        panel.add(editor.getTableCellEditorComponent(table, value, isSelected, row, column));
        this.entityComboBox = EntityComboBoxFactory.createComboBox(entityFieldTypes);
        panel.add(this.entityComboBox);
        new DefaultContextMenu().add((JTextField)  this.entityComboBox.getEditor().getEditorComponent());
        if(entity != null) {
            this.entityComboBox.setSelectedEntity(entity);
            ((JTextField) (this.entityComboBox.getEditor().getEditorComponent())).setText(entity.getName());
        }
        JPanel panelButtons = new JPanel(new BorderLayout());
        panelButtons.add(viewTableEntitiesButton, BorderLayout.WEST);
        panelButtons.add(viewEntityButton, BorderLayout.EAST);
        panel.add(panelButtons, BorderLayout.EAST);
        this.table = table;
        this.row = row;
        this.column = column;
        return panel;
    }

    public Object getCellEditorValue(){
        return this.entityComboBox.getSelectedEntity();
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

    protected abstract void openEntityTableView(JTable table, int row, int column);

    protected abstract void openEntityView(JTable table, int row, int column);
}

