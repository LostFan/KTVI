package org.lostfan.ktv.controller;

import org.lostfan.ktv.model.EntityComboBoxModel;
import org.lostfan.ktv.model.FieldSearchCriterion;
import org.lostfan.ktv.model.EntityModel;
import org.lostfan.ktv.view.ComboBoxView;
import org.lostfan.ktv.view.EntitySearchView;
import org.lostfan.ktv.view.EntityTableView;
import org.lostfan.ktv.view.EntityView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EntityController {

    private EntityModel model;
    private EntityTableView view;

    public EntityController(EntityModel model, EntityTableView view) {
        this.model = model;
        this.view = view;

        this.view.addFindActionListener(new FindActionListener());
        this.view.addAddActionListener(new AddActionListener());
        this.view.addChangeActionListener(new ChangeActionListener());
        this.view.addDeleteActionListener(new DeleteActionListener());
        this.view.addDoubleClickListener(new ChangeMouseDoubleClickListener());

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
            List<FieldSearchCriterion> criteria = this.entitySearchView.getSearchCriteria();
            model.setSearchCriteria(criteria);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
//            List<FieldSearchCriterion> criteria = this.entitySearchView.getSearchCriteria();
//            model.setSearchCriteria(criteria);
        }
    }

    private class AddActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            EntityView entityView = new EntityView(model);
            entityView.addAddActionListener(e1 -> {
                Map<String, Object> values = entityView.getValues();
                model.saveOrEditEntity(values);
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
                    Map<String, Object> values = entityView.getValues();
                    model.saveOrEditEntity(values);
                });

            }
        }
    }

    private class ChangeMouseDoubleClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int selectedIndex = view.getSelectedIndex();
                EntityView entityView = new EntityView(model, model.getList().get(selectedIndex));
                entityView.addAddActionListener(e1 -> {
                    Map<String, Object> values = entityView.getValues();
                    model.saveOrEditEntity(values);
                });

            }
        }
    }



    private class DeleteActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            int selectedIndex = view.getSelectedIndex();
            if (selectedIndex == -1) {
                System.out.println("No selection");
                return;
            }
            if(view.isConfirm()) {
                model.deleteEntityByRow(IntStream.of(view.getSelectedIndexes()).boxed().collect(Collectors.toList()));
            }
        }
    }
}
