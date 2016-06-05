package org.lostfan.ktv.controller;

import org.lostfan.ktv.model.DailyRegisterModel;
import org.lostfan.ktv.model.ReportToBankModel;
import org.lostfan.ktv.view.report.DailyRegisterView;
import org.lostfan.ktv.view.report.ReportToBankView;

import java.time.LocalDate;

public class ReportToBankController {

    private ReportToBankModel model;
    private ReportToBankView view;

    public ReportToBankController() {
        this.model = new ReportToBankModel();
        this.view = new ReportToBankView(this.model);

        this.view.setGenerateActionListener(this::generateActionPerformed);
    }

    private void generateActionPerformed(Object arg) {
        String result = this.model.generate((LocalDate) arg);
        if (result != null) {
            this.view.showExceptionWindow(result);
        }
    }
}
