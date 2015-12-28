package org.lostfan.ktv.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.searcher.EntitySearcherModel;
import org.lostfan.ktv.utils.Observer;
import org.lostfan.ktv.utils.ResourceBundles;
import org.lostfan.ktv.view.model.EntitySearchTableModel;
import org.lostfan.ktv.view.model.EntitySelectionModel;

public class EntitySelectionView extends FrameView {

    private class ModelObserver implements Observer {
        @Override
        public void update(Object args) {
            EntitySelectionView.this.revalidate();
        }
    }

    public static final int WIDTH = 750;
    public static final int HEIGHT = 500;

    private JTable table;
    private JScrollPane tableScrollPane;
    private JButton cancelButton;
    private JButton chooseButton;

    private Entity selectedEntity = null;

    private ModelObserver modelObserver;

    private EntitySearcherModel model;

    public EntitySelectionView(EntitySearcherModel model) {
        this.model = model;

        setTitle(getGuiString("buttons.search") + ": " + getEntityString(model.getEntityNameKey()));
        getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
                    hide();
                }
            }
        });

        this.chooseButton = new JButton(getGuiString("buttons.choose"));
        this.chooseButton.addActionListener(e -> {
            if (EntitySelectionView.this.getSelectedIndex() != -1) {
                selectedEntity = (Entity) model.getList().get(EntitySelectionView.this.table.convertRowIndexToModel(EntitySelectionView.this.getSelectedIndex()));
                hide();
            }
        });

        this.cancelButton = new JButton(getGuiString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> {
                hide();
        });

        buildLayout();
        this.modelObserver = new ModelObserver();
        model.addObserver(this.modelObserver);

        getFrame().setLocationRelativeTo(null);
        show();
    }

    private void buildLayout() {

        setSize(WIDTH, HEIGHT);

        getContentPanel().setLayout(new BorderLayout(10, 10));
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

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

        rightPanelInner.add(this.chooseButton);
        rightPanelInner.add(this.cancelButton);
    }

    public int getSelectedIndex() {
        return this.table.getSelectedRow();
    }

    public Entity getSelectedEntity() {
        return this.selectedEntity;
    }

    public void setModel(EntitySearcherModel model) {
        this.model.removeObserver(modelObserver);
        this.model = model;
        model.addObserver(this.modelObserver);

        this.table.setModel(new EntitySearchTableModel<>(model));
        this.tableScrollPane.setViewportView(this.table);

        revalidate();
    }

    private void addStringActionTableCellEditorToColumns() {
        JTextField textField = new JTextField();
        textField.setBorder(BorderFactory.createEmptyBorder());
        textField.setEditable(false);
        DefaultCellEditor editor = new DefaultCellEditor(textField);
        editor.setClickCountToStart(1);
    }
}
