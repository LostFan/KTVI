package org.lostfan.ktv.view;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.model.EntityTableModel;

public class EntityTableView {

    private class ActionButton {
        JButton button;
        /**
         * A button is active if any table row is selected
         */
        boolean entityRequired;

        public ActionButton(JButton button, boolean entityRequired) {
            this.button = button;
            this.entityRequired = entityRequired;
        }
    }

    private JPanel contentPanel;
    private JPanel buttonsPanel;
    private JTable table;
    private JScrollPane tableScrollPane;

    private List<ActionButton> buttons;

    private EntityModel model;

    private ViewActionListener findActionListener;
    private ViewActionListener addActionListener;
    private ViewActionListener changeActionListener;
    private ViewActionListener deleteActionListener;

    public EntityTableView(EntityModel<? extends Entity> model) {
        this.model = model;

        this.buttons = new ArrayList<>();

        this.table = new JTable(new EntityTableModel<>(model));
        this.table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        this.table.setAutoCreateRowSorter(true);
        this.table.setFillsViewportHeight(true);
        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EntityTableView view = EntityTableView.this;
                // Dbl Click at the table row
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && view.changeActionListener != null) {
                    int actualIndex = EntityTableView.this.table.convertRowIndexToModel(view.table.getSelectedRow());
                    view.changeActionListener.actionPerformed(((Entity)EntityTableView.this.model.getList().get(actualIndex)).getId());
                }
            }
        });
        this.table.getSelectionModel().addListSelectionListener(e -> {
            boolean rowsSelected = this.table.getSelectedRowCount() > 0;
            for (ActionButton actionButton : buttons) {
                if (!actionButton.entityRequired) {
                    continue;
                }
                actionButton.button.setEnabled(rowsSelected);
            }
        });

        JButton button = new JButton(getString("buttons.find"));
        button.addActionListener(e -> {
            if (this.findActionListener != null) {
                this.findActionListener.actionPerformed(null);
            }
        });
        addButton(button, false);

        button = new JButton(getString("buttons.add"));
        button.addActionListener(e -> {
            if (this.addActionListener != null) {
                this.addActionListener.actionPerformed(null);
            }
        });
        addButton(button, false);

        button = new JButton(getString("buttons.changeSelected"));
        button.addActionListener(e -> {
            int selectedId = getSelectedEntityId();
            if (selectedId != -1 && this.changeActionListener != null) {
                this.changeActionListener.actionPerformed(selectedId);
            }
        });
        addButton(button, true);

        button = new JButton(getString("buttons.delete"));
        button.addActionListener(e -> {
            List<Integer> selectedIds = getSelectedEntityIds();
            if (selectedIds.size() != 0 && confirmDeletion() && this.deleteActionListener != null) {
                this.deleteActionListener.actionPerformed(selectedIds);
            }
        });
        addButton(button, true);

        this.buttonsPanel = new JPanel();

        buildLayout();
    }

    private void buildLayout() {
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

        this.buttonsPanel.setLayout(new GridLayout(4, 1, 0, 10));
        rightPanel.add(this.buttonsPanel);

        for (ActionButton actionButton : this.buttons) {
            this.buttonsPanel.add(actionButton.button);
        }
    }

    protected void addButton(JButton button, boolean entityRequired) {
        this.buttons.add(new ActionButton(button, entityRequired));
        if (entityRequired) {
            button.setEnabled(false);
        }
        if (this.buttonsPanel != null) {
            this.buttonsPanel.add(button);
            GridLayout layout = (GridLayout)this.buttonsPanel.getLayout();
            layout.setRows(layout.getRows() + 1);
        }
    }

    public JPanel getContentPanel() {
        return this.contentPanel;
    }

    private boolean confirmDeletion() {
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

        return result == 0;
    }

    private void addStringActionTableCellEditorToColumns() {
        JTextField textField = new JTextField();
        textField.setBorder(BorderFactory.createEmptyBorder());
        textField.setEditable(false);
        DefaultCellEditor editor = new DefaultCellEditor(textField);
        editor.setClickCountToStart(1);
        for (int columnIndex=0;columnIndex< table.getColumnCount(); columnIndex++) {
            if(((EntityField)this.model.getFields().get(columnIndex)).getType().isEntityClass()) {
//                this.table.getColumn(table.getColumnName(columnIndex)).setCellEditor(new EntityActionTableCellEditor(editor));
            }
        }
    }

    private void revalidate() {
        addStringActionTableCellEditorToColumns();
        this.contentPanel.invalidate();
        this.contentPanel.repaint();
    }

    public void setFindActionListener(ViewActionListener findActionListener) {
        this.findActionListener = findActionListener;
    }

    public void setAddActionListener(ViewActionListener addActionListener) {
        this.addActionListener = addActionListener;
    }

    public void setChangeActionListener(ViewActionListener changeActionListener) {
        this.changeActionListener = changeActionListener;
    }

    public void setDeleteActionListener(ViewActionListener deleteActionListener) {
        this.deleteActionListener = deleteActionListener;
    }

    protected String getString(String key) {
        return ResourceBundles.getGuiBundle().getString(key);
    }

    protected int getSelectedEntityId() {
        int selectedRow = this.table.getSelectedRow();
        if (selectedRow != -1) {
            int actualIndex = this.table.convertRowIndexToModel(selectedRow);
            return ((Entity)this.model.getList().get(actualIndex)).getId();
        }
        return selectedRow;
    }

    protected List<Integer> getSelectedEntityIds() {
        int[] selectedRows = this.table.getSelectedRows();
        if (selectedRows.length != 0) {
            List<Integer> selectedIds = IntStream.of(selectedRows).boxed().map(rowNumber -> {
                int actualIndex = table.convertRowIndexToModel(rowNumber);
                return ((Entity) this.model.getList().get(actualIndex)).getId();
            }).collect(Collectors.toList());
            return selectedIds;
        }
        return Collections.emptyList();
    }
}
