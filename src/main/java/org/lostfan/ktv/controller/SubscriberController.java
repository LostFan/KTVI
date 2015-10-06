package org.lostfan.ktv.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.dao.impl.hsqldb.HsqldbSubscriberDAO;
import org.lostfan.ktv.model.SubscriberModel;
import org.lostfan.ktv.view.TableViewBase;

public class SubscriberController {

    private SubscriberModel model;
    private TableViewBase view;
    private SubscriberDAO subscriberDAO = new HsqldbSubscriberDAO();

    public SubscriberController(SubscriberModel model, TableViewBase view) {
        this.model = model;
        this.view = view;

        this.view.addFindActionListener(new FindActionListener());
        this.view.addAddActionListener(new AddActionListener());
        this.view.addChangeActionListener(new ChangeActionListener());
        this.view.addDeleteActionListener(new DeleteActionListener());
    }

    private class FindActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Find action");
        }
    }

    private class AddActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Add action");
        }
    }

    private class ChangeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.print("Change action.");
            int selectedIndex = view.getSelectedIndex();
            if (selectedIndex == -1) {
                System.out.println("No selection");
            } else {

                System.out.println("Service name:" + model.getList().get(selectedIndex).getName());
            }
        }
    }

    private class DeleteActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.print("Delete action.");
            int selectedIndex = view.getSelectedIndex();
            if (selectedIndex == -1) {
                System.out.println("No selection");
            } else {
                System.out.println("Service name:" + model.getList().get(selectedIndex).getName());
            }
        }
    }
}
