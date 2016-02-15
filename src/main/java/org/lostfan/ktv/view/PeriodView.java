package org.lostfan.ktv.view;

import java.awt.*;
import javax.swing.*;

import org.lostfan.ktv.model.PeriodModel;
import org.lostfan.ktv.utils.ViewActionListener;

public class PeriodView extends FormView{

    private PeriodModel model;
    private DateFormField dateField;
    private JButton addButton;
    private JButton cancelButton;

    private ViewActionListener changeActionListener;

    public PeriodView(PeriodModel model) {
        this.model = model;
        setTitle(getGuiString(model.getName()));
        dateField = new DateFormField("renderedService.date");
        dateField.setValue(model.getPeriod());
        addFormField(dateField);

        this.addButton = new JButton(getGuiString("buttons.change"));
        this.addButton.addActionListener(e -> {
            if (this.changeActionListener != null) {
                this.changeActionListener.actionPerformed(dateField.getValue());
            }
            hide();
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
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        getContentPanel().add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setChangeActionListener(ViewActionListener changeActionListener) {
        this.changeActionListener = changeActionListener;
    }
}
