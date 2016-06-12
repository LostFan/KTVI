package org.lostfan.ktv.controller;

import org.lostfan.ktv.model.ConverterModel;
import org.lostfan.ktv.model.FileMenu;
import org.lostfan.ktv.view.entity.ConverterView;

import java.io.File;

public class ConverterController {



    private ConverterModel model;
    private ConverterView view;

    public ConverterController(FileMenu conversionType) {
        this.model = new ConverterModel(conversionType);
        this.view = new ConverterView(this.model);

        this.view.setChangeActionListener(this::convert);
    }

    private void convert(Object arg) {
        String result = this.model.convert((File[]) arg);
        if (result != null) {
            this.view.showExceptionWindow(result);
        }
    }
}
