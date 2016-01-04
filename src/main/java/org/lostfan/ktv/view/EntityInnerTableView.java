package org.lostfan.ktv.view;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.FullEntityField;
import org.lostfan.ktv.utils.Observer;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.model.EntityActionTableCellEditor;
import org.lostfan.ktv.view.model.EntityInnerTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.List;

public class EntityInnerTableView<T extends Entity> extends View {

    private class ModelObserver implements Observer {
        @Override
        public void update(Object args) {
            EntityInnerTableView.this.revalidate();
        }
    }

    private JTable table;
    private JScrollPane tableScrollPane;
    private JButton addCopyButton;
    private JButton addButton;
    private JButton restoreRowButton;
    private JButton deleteButton;
    private JButton upButton;
    private JButton downButton;
    private EntityInnerTableModel entityInnerTableModel;

    private ViewActionListener addActionListener;

    private ModelObserver modelObserver;

//    private EntityModel model;

    public EntityInnerTableView(FullEntityField fullEntityField, List<Entity> list) {
//        this.model = model;

        this.entityInnerTableModel = new EntityInnerTableModel(fullEntityField, list);
        this.table = new JTable(this.entityInnerTableModel);
        this.table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        this.table.setAutoCreateRowSorter(false);
        this.table.setFillsViewportHeight(true);

        this.addButton = new JButton(getGuiString("buttons.add"));
        this.addButton.addActionListener(e -> {
            this.entityInnerTableModel.addRow();
            revalidate();
        });

        this.addCopyButton = new JButton(getGuiString("buttons.addCopy"));
        this.addCopyButton.addActionListener(e -> {
            this.entityInnerTableModel.addRow(this.table.getSelectedRow());
            revalidate();
        });
        this.restoreRowButton = new JButton(getGuiString("buttons.restoreRows"));
        this.restoreRowButton.addActionListener(e -> {
            this.stopEditing();
            this.entityInnerTableModel.restoreRows();
            revalidate();
        });

        this.deleteButton = new JButton(getGuiString("buttons.delete"));
        this.deleteButton.addActionListener(e -> {
            this.stopEditing();
            this.entityInnerTableModel.deleteRows(this.table.getSelectedRows());
            revalidate();
        });

        buildLayout();

//        this.modelObserver = new ModelObserver();
//
//        model.addObserver(this.modelObserver);
    }

    private void buildLayout() {
        getContentPanel().setLayout(new BorderLayout(10, 10));

        // ID column values should be aligned to the left;
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        this.table.getColumnModel().getColumn(0).setCellRenderer(renderer);
        addStringActionTableCellEditorToColumns();
        this.tableScrollPane = new JScrollPane(this.table);

        getContentPanel().add(tableScrollPane, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        getContentPanel().add(rightPanel, BorderLayout.LINE_END);

        JPanel rightPanelInner = new JPanel(new GridLayout(4, 1, 0, 10));
        rightPanel.add(rightPanelInner);

        rightPanelInner.add(this.addCopyButton);
        rightPanelInner.add(this.addButton);
        rightPanelInner.add(this.restoreRowButton);
        rightPanelInner.add(this.deleteButton);
    }

    public int getSelectedIndex() {
        return this.table.getSelectedRow();
    }

    public int[] getSelectedIndexes() {
        return this.table.getSelectedRows();
    }

//    public boolean isConfirm() {
//        int optionType = JOptionPane.OK_CANCEL_OPTION;
//        int messageType = JOptionPane.QUESTION_MESSAGE;
//        Object[] selValues = {ResourceBundles.getGuiBundle().getString("buttons.yes"),
//                ResourceBundles.getGuiBundle().getString("buttons.cancel") };
//        String message = ResourceBundles.getGuiBundle().getString("window.delete") + " : "
//                + ResourceBundles.getEntityBundle().getString(model.getEntityNameKey());
//        int result = JOptionPane.showOptionDialog(null,
//                ResourceBundles.getGuiBundle().getString("message.deleteQuestion"), message,
//                optionType, messageType, null, selValues,
//                selValues[0]);
//
//        return result == 0 ? true : false;
//    }
//
//    public void setModel(EntityModel model) {
//        this.model.removeObserver(modelObserver);
//        this.model = model;
//        model.addObserver(this.modelObserver);
//
//        this.table.setModel(new EntityTableModel(model));
//        this.tableScrollPane.setViewportView(this.table);
//
//        revalidate();
//    }

    @SuppressWarnings("unchecked")
    public List<T> getEntityList() {
        return (List<T>)entityInnerTableModel.getEntityList();
    }

    public void setEntityList(List<Entity> entityList) {
        entityInnerTableModel.setEntityList(entityList);
    }

//    public EntityModel getEntityModel() {
//        return model;
//    }

    private void addStringActionTableCellEditorToColumns() {
        JTextField textField = new JTextField();
        textField.setBorder(BorderFactory.createEmptyBorder());
        textField.setEditable(false);
        DefaultCellEditor editor = new DefaultCellEditor(textField);
        editor.setClickCountToStart(1);
        for (int columnIndex=0;columnIndex< this.table.getColumnCount(); columnIndex++) {
            if(EntityFieldTypes.getEntityClass(this.table.getColumnClass(columnIndex)).isEntityClass()) {
                this.table.getColumn(this.table.getColumnName(columnIndex))
                        .setCellEditor(new EntityActionTableCellEditor(
                                editor, EntityFieldTypes.getEntityClass(this.table.getColumnClass(columnIndex))));
            }
        }
    }
    protected void revalidate() {
        addStringActionTableCellEditorToColumns();
        super.revalidate();
    }

    public void setAddActionListener(ViewActionListener addActionListener) {
        this.addActionListener = addActionListener;
    }

    public void addFindActionListener(ActionListener listener) {
        this.addCopyButton.addActionListener(listener);
    }

    public void addChangeActionListener(ActionListener listener) {
        this.restoreRowButton.addActionListener(listener);
    }

    public void addDoubleClickListener(MouseListener listener) {
        this.table.addMouseListener(listener);
    }

    public void addDeleteActionListener(ActionListener listener) {
        this.deleteButton.addActionListener(listener);
    }

    public void stopEditing() {
        if (table.isEditing()) {
            int col = table.getEditingColumn(),
                    row = table.getEditingRow();
            CellEditor cellEditor = table.getCellEditor();
            if (cellEditor != null)
                cellEditor.stopCellEditing();
            table.changeSelection(row, col, false, false);
        }
    }
}
