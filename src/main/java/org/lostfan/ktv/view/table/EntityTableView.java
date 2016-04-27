package org.lostfan.ktv.view.table;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.validation.Error;
import org.lostfan.ktv.view.View;
import org.lostfan.ktv.view.model.EntityTableModel;

public class EntityTableView extends View {

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

    class MultiKeyPressListener implements KeyListener {

        // Set of currently pressed keys
        private final Set<Integer> pressed = new HashSet<>();

        @Override
        public synchronized void keyPressed(KeyEvent e) {
            pressed.add(e.getKeyCode());
            // "Delete" key
            if (e.getKeyCode() == 127) {
                List<Integer> selectedIds = getSelectedEntityIds();
                if (selectedIds.size() != 0 && confirmDeletion() && EntityTableView.this.deleteActionListener != null) {
                    EntityTableView.this.deleteActionListener.actionPerformed(selectedIds);
                }
            }
            // "Insert" key
            if (e.getKeyCode() == 155) {
                if (EntityTableView.this.addActionListener != null) {
                    EntityTableView.this.addActionListener.actionPerformed(null);
                }
            }
            // More than one key is currently pressed.
            // Iterate over pressed to get the keys.
            if (pressed.size() > 1) {
                // "ALT + F2" keys
                if (pressed.contains(18) && pressed.contains(113)) {
                    int selectedId = getSelectedEntityId();
                    if (selectedId != -1 && EntityTableView.this.changeActionListener != null) {
                        EntityTableView.this.changeActionListener.actionPerformed(selectedId);
                    }
                }
                pressed.clear();
            }
        }

        @Override
        public synchronized void keyReleased(KeyEvent e) {
            pressed.remove(e.getKeyCode());
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }

    private JPanel buttonsPanel;
    protected JPanel tablePanel;
    protected JTable table;

    private List<ActionButton> buttons;

    private EntityModel model;

    private ViewActionListener filterActionListener;
    private ViewActionListener addActionListener;
    private ViewActionListener changeActionListener;
    private ViewActionListener deleteActionListener;

    public EntityTableView(EntityModel<? extends Entity> model) {
        this.model = model;

        this.buttons = new ArrayList<>();

        this.table = new JTable(new EntityTableModel<>(model));
        this.table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        this.table.setRowSelectionAllowed(false);
        this.table.setAutoCreateRowSorter(true);
        this.table.setFillsViewportHeight(true);
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

        this.tablePanel = new JPanel();
        this.tablePanel.setLayout(new BorderLayout(10, 10));

        JButton button = new JButton(getGuiString("buttons.filter"));
        button.addActionListener(e -> {
            if (this.filterActionListener != null) {
                this.filterActionListener.actionPerformed(null);
            }
        });
        addButton(button, false);

        button = new JButton(getGuiString("buttons.add"));
        button.addActionListener(e -> {
            if (this.addActionListener != null) {
                this.addActionListener.actionPerformed(null);
            }
        });
        addButton(button, false);

        button = new JButton(getGuiString("buttons.changeSelected"));
        button.addActionListener(e -> {
            int selectedId = getSelectedEntityId();
            if (selectedId != -1 && this.changeActionListener != null) {
                this.changeActionListener.actionPerformed(selectedId);
            }
        });
        addButton(button, true);

        button = new JButton(getGuiString("buttons.delete"));
        button.addActionListener(e -> {
            List<Integer> selectedIds = getSelectedEntityIds();
            if (selectedIds.size() != 0 && confirmDeletion() && this.deleteActionListener != null) {
                this.deleteActionListener.actionPerformed(selectedIds);
            }
        });
        addButton(button, true);

        this.buttonsPanel = new JPanel();

        this.table.addKeyListener(new MultiKeyPressListener());

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
        System.out.println(model.getEntityName());
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        getContentPanel().add(tablePanel, BorderLayout.CENTER);


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
    }

    protected void revalidate() {
        addStringActionTableCellEditorToColumns();
        super.revalidate();
    }

    public void setFilterActionListener(ViewActionListener filterActionListener) {
        this.filterActionListener = filterActionListener;
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

    public void errorWindow(Iterable<Error> errors) {
        int optionType = JOptionPane.OK_CANCEL_OPTION;
        int messageType = JOptionPane.ERROR_MESSAGE;
        Object[] selValues = { getGuiString("buttons.ok") };
        String message = getGuiString("window.deleteFailed");
        StringBuffer stringBuffer = new StringBuffer();
        for (Error error : errors) {
            if (error.getField() == null) {
                String err = getGuiString(error.getMessage());
                if (error.getParams().length != 0) {
                    err = String.format(err, error.getParams());
                }
                stringBuffer.append(err);
                stringBuffer.append("\n");
            }
        }
        JOptionPane.showOptionDialog(null,
                stringBuffer, message,
                optionType, messageType, null, selValues,
                selValues[0]);
    }
}
