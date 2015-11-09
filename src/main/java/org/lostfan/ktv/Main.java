package org.lostfan.ktv;
import java.awt.GridLayout;
//from w  ww. j  av a2  s .  c  om
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

public class Main {
    JFrame frame = new JFrame("Test");
    JComboBox comboBox = new JComboBox();

    public Main() {
        comboBox.addItem("One");
        comboBox.addItem("Two");
        comboBox.addItem("Three");
        JButton button = new JButton("Show Selected");
        button.addActionListener(e ->System.out.println("Selected item: " + comboBox.getSelectedItem()));
        JButton button1 = new JButton("Append Items");
        button1.addActionListener(e ->appendCbItem());
        JButton button2 = new JButton("Reduce Items");
        button2.addActionListener(e ->reduceCbItem());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 1));
        frame.add(comboBox);
        frame.add(button);
        frame.add(button1);
        frame.add(button2);
        frame.pack();
        frame.setVisible(true);
        selectFirstItem();
    }
    public void appendCbItem() {
        comboBox.addItem("Four");
        comboBox.addItem("Five");
        comboBox.addItem("Six");
        comboBox.setSelectedItem("Six");
        requestCbFocus();
    }

    public void reduceCbItem() {
        comboBox.removeItem("Four");
        comboBox.removeItem("Five");
        comboBox.removeItem("Six");
        selectFirstItem();
    }

    public void selectFirstItem() {
        comboBox.setSelectedIndex(0);
        requestCbFocus();
    }

    public void requestCbFocus() {
        comboBox.requestFocus();
        comboBox.requestFocusInWindow();
    }

    public static void main(String[] args) {
        new Main();
    }
}
