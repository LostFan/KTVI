package org.lostfan.ktv.controller;

import org.lostfan.ktv.model.DailyRegisterModel;
import org.lostfan.ktv.view.report.DailyRegisterView;

import java.time.LocalDate;

public class DailyRegisterController {

    private DailyRegisterModel model;
    private DailyRegisterView view;

    public DailyRegisterController() {
        this.model = new DailyRegisterModel();
        this.view = new DailyRegisterView(this.model);

        this.view.setGenerateActionListener(this::generateActionPerformed);
        this.view.setExcelActionListener(this::excelActionPerformed);
    }

    private void generateActionPerformed(Object arg) {
        this.model.generate((LocalDate) arg);
    }

    private void excelActionPerformed(Object arg) {
        String result = this.model.generateExcel();
        if (result != null) {
            this.view.showExceptionWindow(result);
        }
    }
}
