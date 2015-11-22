package org.lostfan.ktv.controller;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.FieldSearchCriterion;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.model.entity.EntityTableModel;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.EntityInnerTableView;
import org.lostfan.ktv.view.EntitySearchView;
import org.lostfan.ktv.view.EntityTableView;
import org.lostfan.ktv.view.EntityView;
import org.lostfan.ktv.view.components.EntityViewFactory;

import java.util.ArrayList;
import java.util.HashMap;
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
                Entity entity = entityView.getEntity();
                ValidationResult result = model.getValidator().validate(entity);
                if (result.hasErrors()) {
                    entityView.showErrors(result.getErrors());
                    return;
                }
//                if(!isSaveInnerTable(entityView)) {
//                    return;
//                }
                model.save(entity);
//                saveInnerTable(entityView, entity.getId());
                entityView.hide();
            });
        }
    }

    private class ChangeActionListener implements ViewActionListener {

        @Override
        public void actionPerformed(Object args) {
            int selectedId = (Integer) args;

            Entity entity = model.getFullEntity(selectedId);
            EntityView entityView = new EntityView(model, entity);
            Map<EntityModel, EntityInnerTableView> entityInnerTableViews = new HashMap<>();
            if(EntityTableModel.class.isInstance(model)) {
                for (EntityModel entityModel : ((EntityTableModel) model).getTableModels()) {
                    EntityInnerTableView entityInnerTableView =
                            new EntityInnerTableView(entityModel, entity == null ? null : entity.getId());
                    entityInnerTableViews.put(entityModel, entityInnerTableView);
                    entityView.addInnerTable(entityInnerTableView);
                }
            }

            entityView.setAddActionListener(args_ -> {

                Entity entity1 = entityView.getEntity();
                ValidationResult result = model.getValidator().validate(entity1);
                if (result.hasErrors()) {
                    entityView.showErrors(result.getErrors());
                    return;
                }

                if(!isSaveInnerTable(entityInnerTableViews, entityView)) {
                    return;
                }

                model.save(entity1);


                saveInnerTable(entityInnerTableViews , entity1.getId());
                entityView.hide();
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
    private boolean isSaveInnerTable(Map<EntityModel, EntityInnerTableView> entityModelEntityInnerTableViewMap, EntityView entityView) {
        if(entityModelEntityInnerTableViewMap.size() == 0) {
            return true;
        }
        for (EntityModel entityModel : entityModelEntityInnerTableViewMap.keySet()) {
            for (Object o : entityModelEntityInnerTableViewMap.get(entityModel).getEntityList()) {
                ValidationResult innerResult = entityModel.getValidator().validate((Entity) o);
                if (innerResult.hasErrors()) {
                    entityView.showErrors(innerResult.getErrors());
                    return false;
                }
            }
        }
        return true;
    }

    private void saveInnerTable(Map<EntityModel, EntityInnerTableView> entityModelEntityInnerTableViewMap, Integer id) {

        if(entityModelEntityInnerTableViewMap.size() == 0) {
            return;
        }
        for (EntityModel entityModel : entityModelEntityInnerTableViewMap.keySet()) {
            List<Entity>  entitiesInModel = entityModel.getList();
            System.out.println(entitiesInModel.size());
            for (Entity innerEntity : entitiesInModel) {
                if(!entityModelEntityInnerTableViewMap.get(entityModel).getEntityList().contains(innerEntity)) {
                    entityModel.deleteEntityById(innerEntity.getId());
                }
            }
            for (Object o : entityModelEntityInnerTableViewMap.get(entityModel).getEntityList()) {
                entityModel.getParentField().set((Entity)o, id);
                entityModel.save((Entity)o);
            }
        }
    }




//    private boolean isSaveInnerTable(EntityView entityView) {
//        List<EntityModel> entityModels = model.getTableModels();
//        if(entityModels == null) {
//            return true;
//        }
//        for (EntityModel entityModel : entityModels) {
//            List<Entity> entities = entityView.getTableEntities().get(entityModel);
//            for (Entity innerEntity : entities) {
//                ValidationResult innerResult = entityModel.getValidator().validate(innerEntity);
//                if (innerResult.hasErrors()) {
//                    entityView.showErrors(innerResult.getErrors());
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//    private void saveInnerTable(EntityView entityView, Integer id) {
//        List<EntityModel> entityModels = model.getTableModels();
//        if(entityModels == null) {
//            return;
//        }
//        for (EntityModel entityModel : entityModels) {
//            List<Entity> entities = entityView.getTableEntities().get(entityModel);
//            List<Entity>  entitiesInModel = entityModel.getList();
//            for (Entity innerEntity : entitiesInModel) {
//                if(!entities.contains(innerEntity)) {
//                    entityModel.deleteEntityById(innerEntity.getId());
//                }
//            }
//            for (Entity innerEntity : entities) {
//                entityModel.getParentField().set(innerEntity, id);
//                entityModel.save(innerEntity);
//            }
//        }
//    }
}
