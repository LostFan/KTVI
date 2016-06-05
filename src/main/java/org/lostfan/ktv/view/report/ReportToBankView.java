package org.lostfan.ktv.view.report;


import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.ReportToBankModel;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.validation.NotNullValidator;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.FormView;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ReportToBankView extends FormView {

    private NotNullValidator validator = new NotNullValidator();
    private JButton addButton;
    private JButton cancelButton;

    private ReportToBankModel model;

    private ViewActionListener generateActionListener;

    private DateFormField dateField;

    public ReportToBankView(ReportToBankModel model) {
        this(model, null);
    }

    public ReportToBankView(ReportToBankModel model, Entity entity) {
        this.model = model;

        setTitle(getEntityString(model.getEntityNameKey()));

        dateField = new DateFormField("renderedService.date");
        addFormField(dateField);

        this.addButton = new JButton(getGuiString("buttons.generateReport"));

        this.addButton.addActionListener(e -> {
            ValidationResult validationResult = ValidationResult.createEmpty();
            validator.validate(dateField.getValue(), dateField.getFieldKey(),
                    validationResult);

            if (validationResult.hasErrors()) {
                this.showErrors(validationResult.getErrors());
                return;
            }
            this.clearErrors();
            if(generateActionListener != null) {
                generateActionListener.actionPerformed(dateField.getValue());
            }
        });


        this.cancelButton = new JButton(getGuiString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> {
            hide();
        });


        buildLayout();

        show();
    }

    private void buildLayout() {
        getContentPanel().setLayout(new BorderLayout(10, 10));
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        getContentPanel().add(getFieldPanel(), BorderLayout.PAGE_START);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        getContentPanel().add(buttonPanel, BorderLayout.SOUTH);
    }


    public void setGenerateActionListener(ViewActionListener generateActionListener) {
        this.generateActionListener = generateActionListener;
    }

    public void showExceptionWindow(String message) {
        int optionType = JOptionPane.OK_OPTION;
        int messageType = JOptionPane.WARNING_MESSAGE;
        Object[] selValues = {getGuiString("buttons.ok")};
        String attention = getGuiString("message.attention");
        JOptionPane.showOptionDialog(null,
                getGuiString(message), attention,
                optionType, messageType, null, selValues,
                selValues[0]);
    }
}