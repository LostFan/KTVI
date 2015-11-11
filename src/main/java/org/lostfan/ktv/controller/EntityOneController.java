package org.lostfan.ktv.controller;

import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.entity.EntityModel;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.EntityView;

public class EntityOneController {

    private EntityModel model;
    private EntityView view;

    public EntityOneController(EntityModel model, EntityView view) {
        this.model = model;
        this.view = view;

        this.view.setChangeActionListener(new ChangeActionListener());
    }

    public void setModel(EntityModel model) {
        this.model = model;
    }


    private class ChangeActionListener implements ViewActionListener {

        @Override
        public void actionPerformed(Object args) {

            view.setAddActionListener(args_ -> {
                Entity entity = view.getEntity();
                ValidationResult result = model.getValidator().validate(entity);
                if (result.hasErrors()) {
                    view.showErrors(result.getErrors());
                    return;
                }

                model.save(entity);
                view.hide();
            });
        }
    }
}
