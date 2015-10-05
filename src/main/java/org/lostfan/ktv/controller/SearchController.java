package org.lostfan.ktv.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.lostfan.ktv.model.TableModelBase;
import org.lostfan.ktv.view.CatalogsView;
import org.lostfan.ktv.view.MainView;
import org.lostfan.ktv.view.SearchViewBase;

/**
 * Created by Ihar_Niakhlebau on 30-Sep-15.
 */
public class SearchController {

    private SearchViewBase view;
    private TableModelBase tableModelBase;

    public SearchController(SearchViewBase view) {
        this.view = view;

        this.view.addAddActionListener(new AddActionListener());
        this.view.addFindActionListener(new FindActionListener());
        this.view.addCancelActionListener(new CancelActionListener());
//        this.view.addReportActionListener(new ReportActionListener());
    }
    private class FindActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Find action");
        }
    }

    private class AddActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.addFilterField();
        }
    }

    private class CancelActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.closeForm();
        }
    }

//    private class DocumentActionListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            CatalogsView view = new CatalogsView();
//            CatalogsController controller = new CatalogsController(view);
//        }
//    }
//
//    private class ReportActionListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            System.out.println("Report action");
//        }
//    }
}
