package org.lostfan.ktv.controller;

import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.dao.impl.hsqldb.HsqldbServiceDAO;
import org.lostfan.ktv.model.ModelBase;
import org.lostfan.ktv.view.SearchViewBase;
import org.lostfan.ktv.view.TableViewBase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EntityController {

    private ModelBase model;
    private TableViewBase view;
    private ServiceDAO serviceDAO = new HsqldbServiceDAO();

    public EntityController(ModelBase model, TableViewBase view) {
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
            SearchViewBase view = new SearchViewBase(model);
            view.addFindActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    view.getTextFields();
                }
            });
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

                System.out.println("Service name:" + model.getList().get(selectedIndex));
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
                System.out.println("Service name:" + model.getList().get(selectedIndex));
            }
        }
    }
}
