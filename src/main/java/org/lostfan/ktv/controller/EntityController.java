package org.lostfan.ktv.controller;

import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.dao.impl.hsqldb.HsqldbServiceDAO;
import org.lostfan.ktv.model.FieldSearchCriterion;
import org.lostfan.ktv.model.EntityModel;
import org.lostfan.ktv.model.FieldValue;
import org.lostfan.ktv.view.EntitySearchView;
import org.lostfan.ktv.view.EntityTableView;
import org.lostfan.ktv.view.EntityView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EntityController {

    private EntityModel model;
    private EntityTableView view;
    private ServiceDAO serviceDAO = new HsqldbServiceDAO();

    public EntityController(EntityModel model, EntityTableView view) {
        this.model = model;
        this.view = view;

        this.view.addFindActionListener(new FindActionListener());
        this.view.addAddActionListener(new AddActionListener());
        this.view.addChangeActionListener(new ChangeActionListener());
        this.view.addDeleteActionListener(new DeleteActionListener());
    }

    public void setModel(EntityModel model) {
        this.model = model;
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
            EntityView entityView = new EntityView(model);
            entityView.addAddActionListener(e1 -> {
                List<FieldValue> fieldValues = entityView.getValues();
                model.saveOrEditEntity(fieldValues);
            });
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
                EntityView entityView = new EntityView(model, model.getList().get(selectedIndex));
                entityView.addAddActionListener(e1 -> {
                    List<FieldValue> fieldValues = entityView.getValues();
                    model.saveOrEditEntity(fieldValues);
                });

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
