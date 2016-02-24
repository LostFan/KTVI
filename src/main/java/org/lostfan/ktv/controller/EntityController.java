package org.lostfan.ktv.controller;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.FieldSearchCriterion;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.EntityFilterView;
import org.lostfan.ktv.view.EntityTableView;
import org.lostfan.ktv.view.EntityView;
import org.lostfan.ktv.view.View;
import org.lostfan.ktv.view.components.EntityViewFactory;

import java.util.List;

public class EntityController implements MainInnerController {

    private EntityModel model;
    private EntityTableView view;
    private EntityFilterView entityFilterView;

    public EntityController(EntityModel model, EntityTableView view) {
        this.model = model;
        this.view = view;

        view.setFilterActionListener(this::filterActionPerformed);
        view.setAddActionListener(this::addActionPerformed);
        view.setChangeActionListener(this::changeActionPerformed);
        view.setDeleteActionListener(this::deleteActionPerformed);
    }

    public EntityModel getModel() {
        return model;
    }

    @Override
    public EntityTableView getView() {
        return view;
    }

    protected void filterActionPerformed(Object args) {
        if (this.entityFilterView == null) {
            this.entityFilterView = new EntityFilterView(model);
            this.entityFilterView.setFindActionListener(this::searchFindActionPerformed);
        }
        this.entityFilterView.show();
    }

    protected void searchFindActionPerformed(Object args) {
        List<FieldSearchCriterion> criteria = this.entityFilterView.getSearchCriteria();
        this.model.setSearchCriteria(criteria);
    }

    protected void addActionPerformed(Object args) {
        EntityView entityView = EntityViewFactory.createForm(model);
        entityView.setAddActionListener(args_ -> {
            Entity entity = (Entity) args_;
            ValidationResult result = model.save(entity);
            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
            entityView.hide();
        });
    }

    protected void changeActionPerformed(Object args) {
        int selectedId = (Integer) args;

        Entity entity = model.getEntity(selectedId);
        EntityView entityView = EntityViewFactory.createForm(model, entity);

        entityView.setAddActionListener(args_ -> {
            Entity entity1 = (Entity) args_;
            ValidationResult result = model.update(entity1);
            if (result.hasErrors()) {
                entityView.showErrors(result.getErrors());
                return;
            }
            entityView.hide();
        });
    }

    protected void deleteActionPerformed(Object args) {
        List<Integer> selectedIds = (List<Integer>) args;
        model.deleteEntityById(selectedIds);
    }
}
