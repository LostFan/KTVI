package org.lostfan.ktv.view;

import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.validation.NotNullValidator;
import org.lostfan.ktv.validation.ValidationResult;

import javax.swing.*;
import java.awt.*;

public class DeleteEntitiesByDateView extends FormView {

    private EntityModel model;
    private DateFormField dateField;
    private JButton deleteButton;
    private JButton cancelButton;

    private ViewActionListener deleteActionListener;

    public DeleteEntitiesByDateView(EntityModel model) {
        this.model = model;
        setTitle(getEntityString(model.getEntityNameKey()) + " : " + getGuiString("buttons.deleteByDate"));
        dateField = new DateFormField("entity.date");
        addFormField(dateField);

        this.deleteButton = new JButton(getGuiString("buttons.delete"));
        this.deleteButton.addActionListener(e -> {
            this.clearErrors();
            if(!confirmAdding()) {
                return;
            }
            if (this.deleteActionListener != null) {
                this.deleteActionListener.actionPerformed(dateField.getValue());
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        getContentPanel().add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setDeleteActionListener(ViewActionListener deleteActionListener) {
        this.deleteActionListener = deleteActionListener;
    }

    public boolean confirmAdding(){
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
}
