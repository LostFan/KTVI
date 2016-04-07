package org.lostfan.ktv.view.table;

import org.lostfan.ktv.model.entity.SubscriptionFeeModel;
import org.lostfan.ktv.utils.ViewActionListener;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class SubscriptionFeeTableView extends EntityTableView {

    private SubscriptionFeeModel model;

    private ViewActionListener countWithActionListener;
    private ViewActionListener countAllWithActionListener;
    private ViewActionListener newDateActionListener;

    public SubscriptionFeeTableView(SubscriptionFeeModel model) {
        super(model);
        this.model = model;

        JButton button = new JButton(getGuiString("buttons.countWith"));
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

        buildLayout();
    }

    private void buildLayout() {

        JButton beginButton =  new JButton("<<<");
        JButton minusYear =  new JButton("<<");
        JButton minusMonth =  new JButton("<");
        JButton plusMonth =  new JButton(">");
        JButton plusYear =  new JButton(">>");
        JButton endButton =  new JButton(">>>");
        JTextArea numberOfMonth = new JTextArea();
        numberOfMonth.setText(String.valueOf(model.getDate().getMonth().getValue()));
        numberOfMonth.setOpaque(false);
        numberOfMonth.setBackground(new Color(0,0,0,0));
        JTextArea numberOfYear = new JTextArea();
        numberOfYear.setText(String.valueOf(model.getDate().getYear()));
        numberOfYear.setOpaque(false);
        numberOfYear.setBackground(new Color(0,0,0,0));
        numberOfYear.setForeground(null);


        minusMonth.addActionListener(e -> {
            LocalDate date = model.getDate().minusMonths(1);
            numberOfMonth.setText(String.valueOf(date.getMonth().getValue()));
            numberOfYear.setText(String.valueOf(date.getYear()));
            if (newDateActionListener != null) {
                newDateActionListener.actionPerformed(date);
            }
        });
        minusYear.addActionListener(e -> {
            LocalDate date = model.getDate().minusYears(1);
            numberOfMonth.setText(String.valueOf(date.getMonth().getValue()));
            numberOfYear.setText(String.valueOf(date.getYear()));
            if (newDateActionListener != null) {
                newDateActionListener.actionPerformed(date);
            }
        });
        plusMonth.addActionListener(e -> {
            LocalDate date = model.getDate().plusMonths(1);
            numberOfMonth.setText(String.valueOf(date.getMonth().getValue()));
            numberOfYear.setText(String.valueOf(date.getYear()));
            if (newDateActionListener != null) {
                newDateActionListener.actionPerformed(date);
            }
        });
        plusYear.addActionListener(e -> {
            LocalDate date = model.getDate().plusYears(1);
            numberOfMonth.setText(String.valueOf(date.getMonth().getValue()));
            numberOfYear.setText(String.valueOf(date.getYear()));
            if (newDateActionListener != null) {
                newDateActionListener.actionPerformed(date);
            }
        });

        JPanel newPanel = new JPanel();
        getContentPanel().add(newPanel,  BorderLayout.SOUTH);
        newPanel.add(beginButton);
        newPanel.add(minusYear);
        newPanel.add(minusMonth);
        newPanel.add(numberOfMonth);
        newPanel.add(numberOfYear);
        newPanel.add(plusMonth);
        newPanel.add(plusYear);
        newPanel.add(endButton);
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

    public void setCountWithActionListener(ViewActionListener countWithActionListener) {
        this.countWithActionListener = countWithActionListener;
    }

    public void setCountAllWithActionListener(ViewActionListener countAllWithActionListener) {
        this.countAllWithActionListener = countAllWithActionListener;
    }

    public void newDateActionListener(ViewActionListener newDateActionListener) {
        this.newDateActionListener = newDateActionListener;
    }
}
