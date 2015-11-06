package org.lostfan.ktv.view.model;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

/**
 * Created by Ihar_Niakhlebau on 30-Oct-15.
 */
public abstract class ActionTableCellEditor implements TableCellEditor {

    private TableCellEditor editor;
    private JButton viewTableEntitiesButton = new JButton();
    private JButton viewEntityButton = new JButton();


    protected JTable table;
    protected int row, column;

    public ActionTableCellEditor(TableCellEditor editor){
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
        viewTableEntitiesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.cancelCellEditing();
                openEntityTableView(table, row, column);
            }
        });

        // ui-tweaking
        viewTableEntitiesButton.setFocusable(false);
        viewTableEntitiesButton.setFocusPainted(false);
        viewTableEntitiesButton.setMargin(new Insets(0, 0, 0, 0));

        viewEntityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.cancelCellEditing();
                openEntityView(table, row, column);
            }
        });

        viewEntityButton.setFocusable(false);
        viewEntityButton.setFocusPainted(false);
        viewEntityButton.setMargin(new Insets(0, 0, 0, 0));
    }

    public Component getTableCellEditorComponent(JTable table, Object value
            , boolean isSelected, int row, int column){
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(editor.getTableCellEditorComponent(table, value, isSelected, row, column));
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
        return editor.getCellEditorValue();
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

