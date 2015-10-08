package org.lostfan.ktv.controller;

import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.dao.impl.hsqldb.HsqldbServiceDAO;
import org.lostfan.ktv.model.FieldSearchCriterion;
import org.lostfan.ktv.model.Model;
import org.lostfan.ktv.view.EntitySearchView;
import org.lostfan.ktv.view.EntityTableView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EntityController {

    private Model model;
    private EntityTableView view;
    private ServiceDAO serviceDAO = new HsqldbServiceDAO();

    public EntityController(Model model, EntityTableView view) {
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
            EntitySearchView entitySearchView = new EntitySearchView(model);
            entitySearchView.addFindActionListener(new SearchFindActionListener(entitySearchView));
        }
    }

    private class SearchFindActionListener implements ActionListener {
        EntitySearchView entitySearchView;
        public SearchFindActionListener(EntitySearchView entitySearchView) {
            this.entitySearchView = entitySearchView;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            List<FieldSearchCriterion> criteria = this.entitySearchView.getSearchCriteria();
            model.setSearchCriteria(criteria);
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
