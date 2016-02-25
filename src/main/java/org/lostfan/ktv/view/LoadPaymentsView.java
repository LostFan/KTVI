package org.lostfan.ktv.view;


import org.lostfan.ktv.controller.PaymentController;
import org.lostfan.ktv.domain.Payment;
import org.lostfan.ktv.model.entity.PaymentEntityModel;
import org.lostfan.ktv.utils.ViewActionListener;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoadPaymentsView extends FormView {

    private static class PaymentFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String filename = f.getName().toLowerCase();
            return filename.endsWith(".210") || filename.endsWith(".dat");
        }

        @Override
        public String getDescription() {
            return "*.210,*.dat";
        }
    }

    private JFileChooser fileChooser;
    private EntityInnerTableView<Payment> entityInnerTableView;
    private PaymentEntityModel model;
    private JButton openFileButton;
    private JButton addButton;
    private JButton cancelButton;

    protected ViewActionListener addActionListener;
    private ViewActionListener loadPaymentFileListener;

    public LoadPaymentsView(PaymentEntityModel model) {
        this.model = model;

        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileFilter(new PaymentFileFilter());
        this.fileChooser.setMultiSelectionEnabled(true);

        List<String> bankFileNames = new ArrayList<>();
        setTitle(getGuiString("window.loadPayments"));

        this.addButton = new JButton(getGuiString("buttons.add"));
        this.addButton.addActionListener(e -> {

            if (this.addActionListener != null) {
                this.addActionListener.actionPerformed(entityInnerTableView.getEntityList());
            }
        });

        this.cancelButton = new JButton(getGuiString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> hide());

        this.openFileButton = new JButton(getGuiString("buttons.openFile"));
        openFileButton.addActionListener(e -> {
            Action details = fileChooser.getActionMap().get("viewTypeDetails");
            details.actionPerformed(null);
            int ret = fileChooser.showDialog(null, getGuiString("buttons.openFile"));
            if (ret == JFileChooser.APPROVE_OPTION) {
                File[] files = fileChooser.getSelectedFiles();
                List<Payment> payments = entityInnerTableView.getEntityList();
                for (File file : files) {
                    if(bankFileNames.contains(file.getName())
                            || !LoadPaymentsView.this.model.getPaymentsByBankFileName(file.getName()).isEmpty()) {
                        if(!confirmAdding()) {
                            continue;
                        }
                    } else {
                        bankFileNames.add(file.getName());
                    }
                    if (LoadPaymentsView.this.loadPaymentFileListener != null) {
                        LoadPaymentsView.this.loadPaymentFileListener.actionPerformed(new PaymentController.FileAndPayments(file, payments));
                    }
//                    payments.addAll(LoadPaymentsView.this.model.
//                            createPayments(file, payments));
                }

            }
            this.entityInnerTableView.getTable().revalidate();
        });

        this.entityInnerTableView = new EntityInnerTableView<>(model.getLoadFullEntityField(), new ArrayList<>());

        buildLayout();

        show();
    }

    public void addPayments(List<Payment> payments) {
        entityInnerTableView.getEntityList().addAll(payments);
        entityInnerTableView.revalidate();
    }

    private void buildLayout() {
        getContentPanel().setLayout(new BorderLayout(10, 10));
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalGlue());

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

    public void setLoadPaymentFileListener(ViewActionListener loadPaymentFileListener) {
        this.loadPaymentFileListener = loadPaymentFileListener;
    }

    private boolean confirmAdding(){
        int optionType = JOptionPane.OK_CANCEL_OPTION;
        int messageType = JOptionPane.QUESTION_MESSAGE;
        Object[] selValues = { getGuiString("buttons.yes"), getGuiString("buttons.cancel") };
        String message = getGuiString("window.loadPayments") + " : "
                + getEntityString(model.getEntityNameKey());
        int result = JOptionPane.showOptionDialog(null,
                getGuiString("message.alreadyExistLoadQuestion"), message,
                optionType, messageType, null, selValues,
                selValues[0]);

        return result == 0;
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
