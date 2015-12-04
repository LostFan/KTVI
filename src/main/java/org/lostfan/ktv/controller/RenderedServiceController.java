package org.lostfan.ktv.controller;

import java.util.List;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.FieldSearchCriterion;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.EntitySearchView;
import org.lostfan.ktv.view.EntityTableView;
import org.lostfan.ktv.view.EntityView;
import org.lostfan.ktv.view.components.EntityViewFactory;

public class RenderedServiceController {

    private RenderedServiceEntityModel model;

    public RenderedServiceController(RenderedServiceEntityModel model, EntityTableView view) {
        this.model = model;

        view.setFindActionListener(new FindActionListener());
        view.setAddActionListener(new AddActionListener());
        view.setChangeActionListener(new ChangeActionListener());
        view.setDeleteActionListener(new DeleteActionListener());
    }

    public void setModel(RenderedServiceEntityModel model) {
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
            ((EntityModel) model).setSearchCriteria(criteria);
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
            EntityView entityView = EntityViewFactory.createForm(model);
            entityView.setAddActionListener(args_ -> {
                RenderedService entity = (RenderedService) args_;
                ValidationResult result = model.save(entity);
                if (result.hasErrors()) {
                    entityView.showErrors(result.getErrors());
                    return;
                }
                model.save(entity);
                entityView.hide();
            });
        }
    }

    private class ChangeActionListener implements ViewActionListener {

        @Override
        public void actionPerformed(Object args) {
            int selectedId = (Integer) args;

            RenderedService entity = model.getEntity(selectedId);
            EntityView entityView = EntityViewFactory.createRenderedServiceForm(model, entity);


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
