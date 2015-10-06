package org.lostfan.ktv.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.lostfan.ktv.model.ServiceModel;
import org.lostfan.ktv.view.CatalogsView;
import org.lostfan.ktv.view.MainView;
import org.lostfan.ktv.view.TableViewBase;

/**
 * Created by Ihar_Niakhlebau on 30-Sep-15.
 */
public class MainController {

    private MainView view;

    public MainController(MainView view) {
        this.view = view;

        this.view.addDocumentActionListener(new DocumentActionListener());
        this.view.addReportActionListener(new ReportActionListener());
    }

    private class DocumentActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            CatalogsView view = new CatalogsView();
            CatalogsController controller = new CatalogsController(view);
        }
    }

    private class ReportActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Report action");
        }
    }
}
