package org.lostfan.ktv.view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.entity.BaseEntityModel;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.utils.Observer;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.model.EntitySelectionModel;
import org.lostfan.ktv.view.model.EntityTableModel;

public class EntitySelectionView<T> {

    private class ModelObserver implements Observer {
        @Override
        public void update(Object args) {
            EntitySelectionView.this.revalidate();
        }
    }

    public static final int WIDTH = 750;
    public static final int HEIGHT = 500;

    private JDialog frame;
    private JPanel contentPanel;
    private JTable table;
    private JScrollPane tableScrollPane;
    private JButton cancelButton;
    private JButton chooseButton;

    private Entity selectedEntity = null;


    private ViewActionListener chooseActionListener;

    private ModelObserver modelObserver;

    private BaseEntityModel model;

    public EntitySelectionView(BaseEntityModel model) {
        this.model = model;

        this.frame = new JDialog(new JFrame(), getString("buttons.search") + ": " +
                ResourceBundles.getEntityBundle().getString(model.getEntityNameKey()), true);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.table = new JTable(new EntitySelectionModel<>(model));
        this.table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        this.table.setAutoCreateRowSorter(true);
        this.table.setFillsViewportHeight(true);

        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EntitySelectionView view = EntitySelectionView.this;
                // Dbl Click at the table row
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    selectedEntity = (Entity) model.getList().get(EntitySelectionView.this.table.convertRowIndexToModel(EntitySelectionView.this.getSelectedIndex()));
                    frame.setVisible(false);
                }
            }
        });


        this.chooseButton = new JButton(getString("buttons.choose"));
        this.chooseButton.addActionListener(e -> {
            if (EntitySelectionView.this.getSelectedIndex() != -1) {
                selectedEntity = (Entity) model.getList().get(EntitySelectionView.this.table.convertRowIndexToModel(EntitySelectionView.this.getSelectedIndex()));
                frame.setVisible(false);
            }
        });

        this.cancelButton = new JButton(getString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> {
                frame.setVisible(false);
        });


        buildLayout();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        this.modelObserver = new ModelObserver();

        model.addObserver(this.modelObserver);
    }

    private void buildLayout() {

        frame.setSize(new Dimension(WIDTH, HEIGHT));
        frame.setLocationRelativeTo(null);

        frame.setLayout(new BorderLayout(10, 10));
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        frame.setLayout(new BorderLayout());
//        new EntitySelectionController(model, this);
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

        rightPanelInner.add(this.chooseButton);
        rightPanelInner.add(this.cancelButton);


        frame.add(this.contentPanel, BorderLayout.CENTER);
    }

    public int getSelectedIndex() {
        return this.table.getSelectedRow();
    }

    public Entity getSelectedEntity() {
        return this.selectedEntity;
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

    public void setModel(BaseEntityModel model) {
        this.model.removeObserver(modelObserver);
        this.model = model;
        model.addObserver(this.modelObserver);

        this.table.setModel(new EntityTableModel(model));
        this.tableScrollPane.setViewportView(this.table);

        revalidate();
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
//        addStringActionTableCellEditorToColumns();
        this.contentPanel.invalidate();
        this.contentPanel.repaint();
    }

    public void setChooseActionListener(ViewActionListener chooseActionListener) {
        this.chooseActionListener = chooseActionListener;
    }

    public void addCancelActionListener(ActionListener listener) {
        this.cancelButton.addActionListener(listener);
    }

    public void addDoubleClickListener(MouseListener listener) {
        this.table.addMouseListener(listener);
    }

    private String getString(String key) {
        return ResourceBundles.getGuiBundle().getString(key);
    }
}
