package org.lostfan.ktv.view.components;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.searcher.EntitySearcherModel;
import org.lostfan.ktv.utils.DefaultContextMenu;
import org.lostfan.ktv.utils.Observer;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.EntityFilterView;
import org.lostfan.ktv.view.EntitySelectionView;
import org.lostfan.ktv.view.entity.EntityView;
import org.lostfan.ktv.view.FrameView;

public class EntityPanel extends JPanel {

    private class ModelObserver implements Observer {
        @Override
        public void update(Object args) {
            EntityPanel.this.revalidateResult();
        }
    }

    private JComboBox<String> comboBox;
    private JTextField textField;
    private JButton tableButton;
    private JButton entityButton;
    private EntitySearcherModel model;
    private EntityComboBoxModel entityComboBoxModel;
    private ViewActionListener searchActionListener;
    private FrameView parentView;

    private ModelObserver modelObserver;

    public EntityPanel(EntitySearcherModel model)
    {
        this(model, null);
    }

    public EntityPanel(EntitySearcherModel model, FrameView parentView) {


        this.comboBox = new JComboBox<String>() {
            @Override
            public void updateUI() {
                // Hide arrow button
                super.updateUI();
                UIManager.put("ComboBox.squareButton", Boolean.FALSE);
                setUI(new BasicComboBoxUI() {
                    @Override
                    protected JButton createArrowButton() {
                        JButton b = new JButton();
                        b.setBorder(BorderFactory.createEmptyBorder());
                        b.setVisible(false);
                        return b;
                    }
                });
                setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }

        };
        this.setBackground(Color.BLUE);
        this.setLayout(new BorderLayout());
        this.add(this.comboBox, BorderLayout.CENTER);

        new DefaultContextMenu().add((JTextField) this.comboBox.getEditor().getEditorComponent());
        this.comboBox.setEditable(true);

        this.parentView = parentView;


        this.tableButton = new JButton("...");
        this.tableButton.addActionListener(e -> {
            if(this.parentView == null) {
                return;
            }
            EntitySelectionView entitySelectionView;
            entitySelectionView = EntitySelectionFactory.createView(model.getEntityNameKey());
            this.parentView.openDialog(entitySelectionView);
            if (entitySelectionView.get() != null) {
                this.setSelectedEntity(entitySelectionView.get());
                ((JTextField) ((this.comboBox).getEditor().getEditorComponent())).setText(entitySelectionView.get().getName());
                this.comboBox.invalidate();
                this.comboBox.repaint();
            }
        });

        this.entityButton = new JButton();
        URL url = EntityFilterView.class.getClassLoader().getResource("images/search.png");
        if(url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image image = icon.getImage().getScaledInstance(10,10,Image.SCALE_SMOOTH);
            icon = new ImageIcon(image);
            entityButton.setIcon(icon);
        }

        this.entityButton.addActionListener(e -> {
            if (this.getSelectedEntity() != null) {
                EntityView entityView = EntityViewFactory.createForm(EntityFieldTypes.getEntityClass(model.getEntityClass()), this.getSelectedEntity().getId());
                entityView.getChangeActionListener().actionPerformed(null);
            }
        });

        JPanel buttonPanel = new JPanel(new BorderLayout(0, 0));
        buttonPanel.add(this.tableButton, BorderLayout.LINE_START);
        buttonPanel.add(this.entityButton, BorderLayout.LINE_END);
        this.add(buttonPanel, BorderLayout.LINE_END);

        this.textField = (JTextField) this.comboBox.getEditor().getEditorComponent();
        new EntityPanelController(model, this);
        this.model = model;
        entityComboBoxModel = new EntityComboBoxModel(model);
        this.comboBox.setModel(entityComboBoxModel);
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!textField.getText().isEmpty() && entityComboBoxModel.getSelectedEntity() == null) {
                    textField.requestFocusInWindow();
                }
            }
        });
        this.comboBox.setEditable(true);

        this.modelObserver = new ModelObserver();
        model.addObserver(this.modelObserver);

        this.textField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent ke) {
                // "Enter" key
                // Select the first found value
                if (ke.getKeyCode() == 10) {
                    if (entityComboBoxModel.getSelectedEntity() == null && entityComboBoxModel.getSize() > 0) {
                        comboBox.setSelectedIndex(0);
                        textField.setText(entityComboBoxModel.getSelectedName());
                    }
                    comboBox.hidePopup();
                    return;
                }

                // Result select key:
                // 37 - Left Arrow
                // 38 - Up Arrow
                // 39 - Right Arrow
                // 40 - Down Arrow
                if (ke.getKeyCode() >= 37 && ke.getKeyCode() <= 40) {
                    return;
                }

                entityComboBoxModel.setCurrentValue(textField.getText());

                // Perform search action
                // Model update triggers component revalidation
                if (searchActionListener != null) {
                    searchActionListener.actionPerformed(textField.getText());
                }
            }
        });

        this.textField.addActionListener( e -> {
            // Place into the textField the selected value
            textField.setText(entityComboBoxModel.getSelectedName());
        });
    }

    private void revalidateResult() {
        revalidate();
        if (textField.hasFocus()) {
            comboBox.hidePopup();
            comboBox.showPopup();
        }
    }

    public Entity getSelectedEntity() {
        return entityComboBoxModel.getSelectedEntity();
    }

    public Object getSelectedItem() {
        return entityComboBoxModel.getSelectedItem();
    }

    public void setSelectedId(int id){
        entityComboBoxModel.setSelectedEntity(id);
    }

    public void setSelectedEntity(Entity id){
        entityComboBoxModel.setSelectedEntity(id);
    }

    public void setSearchActionListener(ViewActionListener searchActionListener) {
        this.searchActionListener = searchActionListener;
    }

    public JComboBox getComboBox() {
        return this.comboBox;
    }

    public JButton getTableButton() {
        return this.tableButton;
    }

    public JButton getEntityButton() {
        return this.entityButton;
    }

    public void setParentView(FrameView parentView) {
       this.parentView = parentView;
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        this.comboBox.setEnabled(isEnabled);
        ((JTextField)this.comboBox.getEditor().getEditorComponent()).setDisabledTextColor(Color.BLACK);
        this.tableButton.setVisible(isEnabled);
    }

    @Override
    public boolean isEnabled() {
        return this.comboBox.isEnabled();
    }
}
