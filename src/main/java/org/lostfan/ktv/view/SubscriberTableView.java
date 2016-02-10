package org.lostfan.ktv.view;

import java.awt.*;
import javax.swing.*;

import org.lostfan.ktv.model.entity.SubscriberEntityModel;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.components.TextField;

public class SubscriberTableView extends EntityTableView {

    private ViewActionListener findActionListener;

    public SubscriberTableView(SubscriberEntityModel model) {
        super(model);


        JPanel searchPanel = new JPanel();
        JLabel label = new JLabel(getGuiString("label.search"));
        searchPanel.add(label);
        TextField textField = new TextField(15);
        textField.addTextChangeListener(e ->  {
            if (findActionListener != null) {
                findActionListener.actionPerformed(textField.getText());
            }
        });
        searchPanel.add(textField);

        JButton findButton = new JButton(getGuiString("buttons.find"));

        findButton.addActionListener(e -> {
            if (findActionListener != null) {
                findActionListener.actionPerformed(textField.getText());
            }
        }
        );
        searchPanel.add(findButton);

        this.tablePanel.add(searchPanel, BorderLayout.NORTH);
    }

    public void setFindActionListener(ViewActionListener findActionListener) {
        this.findActionListener = findActionListener;
    }


}
