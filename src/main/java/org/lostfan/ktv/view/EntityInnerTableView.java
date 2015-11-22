package org.lostfan.ktv.view;

import org.lostfan.ktv.controller.EntityInnerTableController;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.utils.Observer;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.model.EntityActionTableCellEditor;
import org.lostfan.ktv.view.model.EntityInnerTableModel;
import org.lostfan.ktv.view.model.EntityTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.List;

public class EntityInnerTableView<T> {

    private class ModelObserver implements Observer {
        @Override
        public void update(Object args) {
            EntityInnerTableView.this.revalidate();
        }
    }

    private JPanel contentPanel;
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

    private EntityModel model;

    public EntityInnerTableView(EntityModel model, Object foreignId) {
        this.model = model;

        this.entityInnerTableModel = new EntityInnerTableModel(model, foreignId);
        this.table = new JTable(this.entityInnerTableModel);
        this.table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        this.table.setAutoCreateRowSorter(false);
        this.table.setFillsViewportHeight(true);


        this.addButton = new JButton(getString("buttons.add"));
        this.addButton.addActionListener(e -> {
            this.entityInnerTableModel.addRow();
            revalidate();
        });

        this.addCopyButton = new JButton(getString("buttons.addCopy"));
        this.addCopyButton.addActionListener(e -> {
            this.entityInnerTableModel.addRow(this.table.getSelectedRow());
            revalidate();
        });
        this.restoreRowButton = new JButton(getString("buttons.restoreRows"));
        this.restoreRowButton.addActionListener(e -> {
            this.entityInnerTableModel.restoreRows();
            revalidate();
        });

        this.deleteButton = new JButton(getString("buttons.delete"));
        this.deleteButton.addActionListener(e -> {
            this.entityInnerTableModel.deleteRows(this.table.getSelectedRows());
            revalidate();
        });

        buildLayout();

        this.modelObserver = new ModelObserver();

        model.addObserver(this.modelObserver);
    }

    private void buildLayout() {
        new EntityInnerTableController(model, this);
        this.contentPanel = new JPanel(new BorderLayout(10, 10));

        // ID column values should be aligned to the left;
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        this.table.getColumnModel().getColumn(0).setCellRenderer(renderer);
        addStringActionTableCellEditorToColumns();
        this.tableScrollPane = new JScrollPane(this.table);

        this.contentPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.contentPanel.add(rightPanel, BorderLayout.LINE_END);

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


    public JPanel getContentPanel() {
        return this.contentPanel;
    }

    public boolean isConfirm() {
        int optionType = JOptionPane.OK_CANCEL_OPTION;
        int messageType = JOptionPane.QUESTION_MESSAGE;
        Object[] selValues = {ResourceBundles.getGuiBundle().getString("buttons.yes"),
                ResourceBundles.getGuiBundle().getString("buttons.cancel") };
        String message = ResourceBundles.getGuiBundle().getString("window.delete") + " : "
                + ResourceBundles.getEntityBundle().getString(model.getEntityNameKey());
        int result = JOptionPane.showOptionDialog(null,
                ResourceBundles.getGuiBundle().getString("message.deleteQuestion"), message,
                optionType, messageType, null, selValues,
                selValues[0]);

        return result == 0 ? true : false;
    }

    public void setModel(EntityModel model) {
        this.model.removeObserver(modelObserver);
        this.model = model;
        model.addObserver(this.modelObserver);

        this.table.setModel(new EntityTableModel(model));
        this.tableScrollPane.setViewportView(this.table);

        revalidate();
    }

    public List<T> getEntityList() {
        return entityInnerTableModel.getEntityList();
    }

    public EntityModel getEntityModel() {
        return model;
    }

    private void addStringActionTableCellEditorToColumns() {
        JTextField textField = new JTextField();
        textField.setBorder(BorderFactory.createEmptyBorder());
        textField.setEditable(false);
        DefaultCellEditor editor = new DefaultCellEditor(textField);
        editor.setClickCountToStart(1);
        for (int columnIndex=0;columnIndex< this.table.getColumnCount(); columnIndex++) {
            if(EntityFieldTypes.getEntityClass(this.table.getColumnClass(columnIndex)).isEntityClass()) {
                this.table.getColumn(this.table.getColumnName(columnIndex)).setCellEditor(new EntityActionTableCellEditor(editor, EntityFieldTypes.getEntityClass(this.table.getColumnClass(columnIndex))));
            }
        }
    }
    private void revalidate() {
        addStringActionTableCellEditorToColumns();
        this.contentPanel.invalidate();
        this.contentPanel.repaint();
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

    private String getString(String key) {
        return ResourceBundles.getGuiBundle().getString(key);
    }
}
