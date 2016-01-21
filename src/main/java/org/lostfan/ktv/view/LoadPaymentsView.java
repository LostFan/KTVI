package org.lostfan.ktv.view;


import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.model.entity.PaymentEntityModel;
import org.lostfan.ktv.utils.ViewActionListener;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoadPaymentsView extends FrameView {

    private JFileChooser fileOpen;
    private EntityInnerTableView<Payment> entityInnerTableView;
    private PaymentEntityModel model;
    private JButton openFileButton;
    private JButton addButton;
    private JButton cancelButton;

    protected ViewActionListener addActionListener;
    protected ViewActionListener cancelActionListener;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 550;

    public LoadPaymentsView(PaymentEntityModel model) {

        this.model = model;
        fileOpen = new JFileChooser();
        List<Entity> payments = new ArrayList<>();
        setTitle(getGuiString("window.loadPayments"));
        setSize(WIDTH, HEIGHT);


        openFileButton = new JButton(getGuiString("buttons.openFile"));

        this.addButton = new JButton(getGuiString("buttons.add"));
        this.addButton.addActionListener(e -> {

            if (this.addActionListener != null) {
                this.addActionListener.actionPerformed(entityInnerTableView.getEntityList());
            }
        });

        this.cancelButton = new JButton(getGuiString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> {
            if (this.cancelActionListener != null) {
                this.cancelActionListener.actionPerformed(null);
            }
            hide();
        });


        openFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileOpen.setFileFilter(new FileFilter() {

                    public String getDescription() {
                        return "*.210";
                    }

                    public boolean accept(File f) {
                        if (f.isDirectory()) {
                            return true;
                        } else {
                            String filename = f.getName().toLowerCase();
                            return filename.endsWith(".210");
                        }
                    }
                });
                int ret = fileOpen.showDialog(null, getGuiString("buttons.openFile"));
                BufferedReader br = null;
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileOpen.getSelectedFile();
                    try {
                        br = new BufferedReader(new FileReader(file));
                        String sCurrentLine;
                        while ((sCurrentLine = br.readLine()) != null) {
                            String str[] = sCurrentLine.split("\\^");

                            try {
                                payments.addAll(LoadPaymentsView.this.model.createPayments(Integer.parseInt(str[2]), createDate(str[9]), Integer.parseInt(str[6].split("\\.")[0]), payments));
                            } catch (Exception ex) {
                                continue;
                            }

                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    LoadPaymentsView.this.entityInnerTableView.setEntityList(payments);
                    payments.clear();
//                    label.setText(label.getText() + "\n" + file.getName());
                }
            }
        });

        this.entityInnerTableView = new EntityInnerTableView<>(model.getLoadFullEntityField(), new ArrayList<>());

        buildLayout();

        show();
    }

    private void buildLayout() {
        getContentPanel().setLayout(new BorderLayout(10, 10));
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalGlue());

//        final JLabel label = new JLabel("Выбранный файл");
//        label.setAlignmentX(CENTER_ALIGNMENT);
//        panel.add(label);

        panel.add(Box.createRigidArea(new Dimension(10, 10)));

        panel.add(openFileButton);
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createRigidArea(new Dimension(10, 10)));

        panel.add(this.entityInnerTableView.getContentPanel());
        getContentPanel().add(panel);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        getContentPanel().add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setAddActionListener(ViewActionListener addActionListener) {
        this.addActionListener = addActionListener;
    }

    public void setCancelActionListener(ViewActionListener cancelActionListener) {
        this.cancelActionListener = cancelActionListener;
    }

    private LocalDate createDate(String s) {
        return LocalDate.of(Integer.parseInt(s.substring(0, 4)), Integer.parseInt(s.substring(4, 6)), Integer.parseInt(s.substring(6, 8)));
    }

    public void showErrors(List<org.lostfan.ktv.validation.Error> errors) {
        // TODO: add appropriate implementation of visualising the errors
        for (org.lostfan.ktv.validation.Error error : errors) {
            String message = error.getField() != null ? getEntityString(error.getField()) + " " : "";
            if (error.getMessage().equals("empty")) {
                message += "should not be empty";
            } else {
                message += error.getMessage();
            }
            JOptionPane.showMessageDialog(getFrame(), message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}