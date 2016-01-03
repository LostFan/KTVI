package org.lostfan.ktv.view;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.entity.SubscriptionFeeModel;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.model.SubscriptionFeeTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SubscriptionFeeTableView extends View {

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

    private JPanel buttonsPanel;
    private JTable table;

    private List<ActionButton> buttons;

    private SubscriptionFeeModel model;

    private ViewActionListener findActionListener;
    private ViewActionListener countWithActionListener;
    private ViewActionListener countAllWithActionListener;

    public SubscriptionFeeTableView(SubscriptionFeeModel model) {
        this.model = model;

        this.buttons = new ArrayList<>();

        this.table = new JTable(new SubscriptionFeeTableModel(model));
        this.table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        this.table.setAutoCreateRowSorter(true);
        this.table.setFillsViewportHeight(true);
//        this.table.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                SubscriptionFeeTableView view = SubscriptionFeeTableView.this;
//                // Dbl Click at the table row
//                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && view.countAllWithActionListener != null) {
//                    int actualIndex = SubscriptionFeeTableView.this.table.convertRowIndexToModel(view.table.getSelectedRow());
//                    view.countAllWithActionListener.actionPerformed((SubscriptionFeeTableView.this.model.getList().get(actualIndex)).getId());
//                }
//            }
//        });
        this.table.getSelectionModel().addListSelectionListener(e -> {
            boolean rowsSelected = this.table.getSelectedRowCount() > 0;
            for (ActionButton actionButton : buttons) {
                if (!actionButton.entityRequired) {
                    continue;
                }
                actionButton.button.setEnabled(rowsSelected);
            }
        });

        JButton button = new JButton(getGuiString("buttons.find"));
        button.addActionListener(e -> {
            if (this.findActionListener != null) {
                this.findActionListener.actionPerformed(null);
            }
        });
        addButton(button, false);

        button = new JButton(getGuiString("buttons.countWith"));
        button.addActionListener(e -> {
            if (this.countWithActionListener != null) {
                this.countWithActionListener.actionPerformed(null);
            }
        });
        addButton(button, false);

        button = new JButton(getGuiString("buttons.countAllWith"));
        button.addActionListener(e -> {
            int selectedId = getSelectedEntityId();
            if (selectedId != -1 && this.countAllWithActionListener != null) {
                this.countAllWithActionListener.actionPerformed(selectedId);
            } else {
                this.countAllWithActionListener.actionPerformed(null);
            }
        });
        addButton(button, false);

        this.buttonsPanel = new JPanel();

        buildLayout();
    }

    private void buildLayout() {
        getContentPanel().setLayout(new BorderLayout(10, 10));

        // ID column values should be aligned to the left;
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        this.table.getColumnModel().getColumn(0).setCellRenderer(renderer);
        addStringActionTableCellEditorToColumns();
        JScrollPane tableScrollPane = new JScrollPane(this.table);

        getContentPanel().add(tableScrollPane, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        getContentPanel().add(rightPanel, BorderLayout.LINE_END);

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

    private boolean confirmDeletion() {
        int optionType = JOptionPane.OK_CANCEL_OPTION;
        int messageType = JOptionPane.QUESTION_MESSAGE;
        Object[] selValues = { getGuiString("buttons.yes"), getGuiString("buttons.cancel") };
        String message = getGuiString("window.delete") + " : "
                + getEntityString(model.getEntityNameKey());
        int result = JOptionPane.showOptionDialog(null,
                getGuiString("message.deleteQuestion"), message,
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

    protected void revalidate() {
        addStringActionTableCellEditorToColumns();
        super.revalidate();
    }

    public void setFindActionListener(ViewActionListener findActionListener) {
        this.findActionListener = findActionListener;
    }

    public void setCountWithActionListener(ViewActionListener countWithActionListener) {
        this.countWithActionListener = countWithActionListener;
    }

    public void setCountAllWithActionListener(ViewActionListener countAllWithActionListener) {
        this.countAllWithActionListener = countAllWithActionListener;
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

    protected JButton getButton(String name) {
        for (ActionButton actionButton : buttons) {
            if(name.equals(actionButton.button.getText())) {
                return actionButton.button;
            }
        }
        return null;
    }
}
