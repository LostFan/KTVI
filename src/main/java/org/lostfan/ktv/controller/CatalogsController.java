package org.lostfan.ktv.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.lostfan.ktv.model.ServiceModel;
import org.lostfan.ktv.model.SubscriberModel;
import org.lostfan.ktv.view.CatalogsView;
import org.lostfan.ktv.view.TableViewBase;

/**
 * Created by Ihar_Niakhlebau on 30-Sep-15.
 */
public class CatalogsController {

    private CatalogsView view;

    public CatalogsController(CatalogsView view) {
        this.view = view;

        this.view.addServiceActionListener(new ServiceActionListener());
        this.view.addSubscriberActionListener(new SubscriberActionListener());
    }

    private class ServiceActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ServiceModel model = new ServiceModel();
            TableViewBase view = new TableViewBase(model);
            ServiceController controller = new ServiceController(model, view);
        }
    }

    private class SubscriberActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SubscriberModel model = new SubscriberModel();
            TableViewBase view = new TableViewBase(model);
            SubscriberController controller = new SubscriberController(model, view);
        }
    }
}