package org.lostfan.ktv.controller;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.FieldSearchCriterion;
import org.lostfan.ktv.model.EntityModel;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.EntitySearchView;
import org.lostfan.ktv.view.EntityTableView;
import org.lostfan.ktv.view.EntityView;

import java.util.List;
import java.util.Map;

public class EntityController {

    private EntityModel model;

    public EntityController(EntityModel model, EntityTableView view) {
        this.model = model;

        view.setFindActionListener(new FindActionListener());
        view.setAddActionListener(new AddActionListener());
        view.setChangeActionListener(new ChangeActionListener());
        view.setDeleteActionListener(new DeleteActionListener());

    }

    public void setModel(EntityModel model) {
        this.model = model;
    }

    private class FindActionListener implements ViewActionListener {
        @Override
        public void actionPerformed(Object args) {
            EntitySearchView entitySearchView = new EntitySearchView(model);
            entitySearchView.setFindActionListener(new SearchFindActionListener(entitySearchView));
        }
    }

    private class SearchFindActionListener implements ViewActionListener {

        EntitySearchView entitySearchView;

        public SearchFindActionListener(EntitySearchView entitySearchView) {
            this.entitySearchView = entitySearchView;
            List<FieldSearchCriterion> criteria = this.entitySearchView.getSearchCriteria();
            model.setSearchCriteria(criteria);
        }

        @Override
        public void actionPerformed(Object args) {
//            List<FieldSearchCriterion> criteria = this.entitySearchView.getSearchCriteria();
//            model.setSearchCriteria(criteria);
        }
    }

    private class AddActionListener implements ViewActionListener {

        @Override
        public void actionPerformed(Object args) {
            EntityView entityView = new EntityView(model);
            entityView.setAddActionListener(args_ -> {
                Map<String, Object> values = entityView.getValues();
                model.saveOrEditEntity(values);
            });
        }
    }

    private class ChangeActionListener implements ViewActionListener {

        @Override
        public void actionPerformed(Object args) {
            int selectedIndex = (Integer) args;
            EntityView entityView = new EntityView(model, (Entity) model.getList().get(selectedIndex));
            entityView.setAddActionListener(args_ -> {
                Map<String, Object> values = entityView.getValues();
                model.saveOrEditEntity(values);
            });
        }
    }

    private class DeleteActionListener implements ViewActionListener {

        @Override
        public void actionPerformed(Object args) {
            List<Integer> selectedIndexes = (List<Integer>) args;
            model.deleteEntityByRow(selectedIndexes);
        }
    }
}
