package org.lostfan.ktv.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

import org.lostfan.ktv.model.SearchModel;
import org.lostfan.ktv.model.TableModelBase;

/**
 * Created by Ihar_Niakhlebau on 30-Sep-15.
 */
public class SearchViewBase extends JFrame {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;
    private JFrame frame;
    private JScrollPane scrollPane;
    private List<JTextField> textFields;
    private List<JComboBox> comboBoxes;
    private JButton addButton;
    private JButton findButton;
    private JButton cancelButton;
    private List<JButton> removeButtons;
    private List<String> comboBoxFields;

    public SearchViewBase(SearchModel model) {
        this.frame = new JFrame(model.getName());
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.textFields = new ArrayList<JTextField>();
        this.comboBoxes = new ArrayList<JComboBox>();
        this.removeButtons = new ArrayList<JButton>();
        this.comboBoxFields = model.getFields();
        this.addButton = new JButton("Добавить условие");
        this.findButton = new JButton("Найти");
        this.cancelButton = new JButton("Отмена");



        buildLayout();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void buildLayout() {
        frame.setSize(new Dimension(WIDTH, HEIGHT));
        frame.setLocationRelativeTo(null);

        frame.setLayout(new BorderLayout(10, 10));
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel labPanel = new JPanel();
        this.scrollPane = new JScrollPane(labPanel);
        labPanel.setLayout(new GridBagLayout());
        frame.setLayout(new BorderLayout());
        addButton.setFocusable(false);
        frame.add(this.scrollPane, BorderLayout.CENTER);
        JPanel butPanel = new JPanel();
        butPanel.add(addButton);
        frame.add(butPanel, BorderLayout.SOUTH);

        this.addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTextField field = new JTextField(20);
                JComboBox comboBox = new JComboBox(comboBoxFields.toArray());
                JButton button =  new JButton();
                URL url = SearchViewBase.class.getClassLoader().getResource("images/remove.png");
                if(url != null) {
                    ImageIcon icon = new ImageIcon(url);
                    Image image = icon.getImage().getScaledInstance(10,10,Image.SCALE_SMOOTH);
                    icon = new ImageIcon(image);
                    button.setIcon(icon);
                }


                textFields.add(field);
                comboBoxes.add(comboBox);
                removeButtons.add(button);

                JPanel jPanel = new JPanel();
                jPanel.setLayout(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();

                c.insets = new Insets(0,10,10,10);
                jPanel.add(comboBox, c);
                jPanel.add(field, c);
                jPanel.add(button, c);

                c.gridy = textFields.size();
                c.anchor = GridBagConstraints.NORTH;

                labPanel.add(jPanel, c);
                labPanel.revalidate();
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        textFields.remove(field);
                        comboBoxes.remove(comboBox);
                        removeButtons.remove(button);
                        labPanel.remove(jPanel);
                        labPanel.repaint();
                        labPanel.revalidate();
                    }
                });
            }
        });
    }

    public void addFindActionListener(ActionListener listener) {
        this.findButton.addActionListener(listener);
    }

    public void addCancelActionListener(ActionListener listener) {
        this.cancelButton.addActionListener(listener);
    }

    public void addAddActionListener(ActionListener listener) {
        this.addButton.addActionListener(listener);
    }

    public void closeForm() {
        this.frame.dispatchEvent(new WindowEvent(this.frame, WindowEvent.WINDOW_CLOSING));
    }

}
