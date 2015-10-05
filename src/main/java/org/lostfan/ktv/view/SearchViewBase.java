package org.lostfan.ktv.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

import org.lostfan.ktv.model.TableModelBase;

/**
 * Created by Ihar_Niakhlebau on 30-Sep-15.
 */
public class SearchViewBase extends JFrame {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;
    private JFrame frame;
    private JScrollPane scrollPane;
    private JTextField textField;
    private static List<JTextField> textFields;
    private List<JComboBox> comboBoxes;
    private JButton addButton;
    private JButton findButton;
    private JButton cancelButton;
    private int filterLength;

    public SearchViewBase(TableModelBase model) {
        this.frame = new JFrame(model.getTableName());
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textField = new JTextField(20);
        this.textFields = new ArrayList<JTextField>();
        this.comboBoxes = new ArrayList<JComboBox>();

//        for(int i = 0; i < filterLength; i++) {
//            this.textFields[i] = new JTextField(20);
//
//
//            String[] items = {
//                    "Элемент списка 1",
//                    "Элемент списка 2",
//                    "Элемент списка 3"
//            };
//            comboBoxes[i] = new JComboBox(items);
//
//        }
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

//
//        JPanel centerPanel = new JPanel();
//        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
//
//
//
////        JPanel centerPanelInner = new JPanel();
////        System.out.println(centerPanelInner.getLayout());
////        centerPanelInner.setLayout(new GridBagLayout());
////        this.scrollPane = new JScrollPane(centerPanelInner);
//        GridBagConstraints c = new GridBagConstraints();
////        System.out.println(this.comboBoxes.size());
////        for(int i = 0; i < 1; i++) {
////            c.fill = GridBagConstraints.HORIZONTAL;
////            c.insets = new Insets(0, 0, 0, 0);
////            c.gridx = 0;
////            c.gridy = i;
////            c.ipadx = 0;
////            System.out.println("step"+i);
////            centerPanelInner.add(this.comboBoxes.get(i), c);
////            c.insets = new Insets(10, 20, 10, 20);
////            c.gridx = 1;
////            c.gridy = i;
////            c.ipadx = 100;
////            System.out.println("step"+i);
////            centerPanelInner.add(this.textFields.get(i), c);
//////            c.insets = new Insets(10, 20, 10, 20);
//////            c.gridx = 2;
//////            c.gridy = i;
//////            centerPanelInner.add(this.addButton[i], c);
////        }
//
////        centerPanel.add(centerPanelInner);
//
//
//        JPanel centerDownPanelInner = new JPanel();
//
//        centerDownPanelInner.setLayout(new GridBagLayout());
//        final JPanel labPanel = new JPanel();
//        c = new GridBagConstraints();
//        c.gridx = 0;
//        c.gridy = 0;
//        centerDownPanelInner.add( new JPanel(), c);
//        c.insets = new Insets(10, 20, 10, 0);
//        c.gridx = 1;
//        c.gridy = 0;
//        centerDownPanelInner.add(this.cancelButton , c);
//        c.insets = new Insets(10, 20, 10, 0);
//        c.gridx = 2;
//        c.gridy = 0;
//        centerDownPanelInner.add(this.addButton , c);
//
//        final JScrollPane scrollPane = new JScrollPane(labPanel);
//        labPanel.setLayout(new GridBagLayout());
//        frame.add(labPanel);
//        centerPanel.add(centerDownPanelInner);
//        frame.add(centerPanel);

        this.addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int number = textFields.size() + 1;
                JTextField field = new JTextField(20);
                String[] items = {
                    "Элемент списка 1",
                    "Элемент списка 2",
                    "Элемент списка 3"
            };
                JComboBox comboBox = new JComboBox(items);
                textFields.add(field);
                comboBoxes.add(comboBox);

                JPanel jPanel = new JPanel();
                jPanel.setLayout(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();
                jPanel.add(comboBox);
                jPanel.add(field);
                c.gridy = textFields.size();
                c.anchor = GridBagConstraints.NORTH;
                labPanel.add(jPanel, c);
//                labPanel.add(field);
                labPanel.revalidate();
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

    public void addFilterField() {


    }


}
