package org.lostfan.ktv.view.entity;

import org.lostfan.ktv.model.ConverterModel;
import org.lostfan.ktv.utils.FilePath;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.FormView;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class ConverterView extends FormView {

    private static class ConverterFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String filename = f.getName().toLowerCase();
            return filename.endsWith(".210") || filename.endsWith(".202");
        }

        @Override
        public String getDescription() {
            return "*.210,*.202";
        }
    }

//    private ConverterModel model;
    private JFileChooser fileChooser;
    private JButton addButton;
    private JButton cancelButton;

    private ViewActionListener changeActionListener;

    public ConverterView(ConverterModel model) {
//        this.model = model;

        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileFilter(new ConverterFileFilter());
        this.fileChooser.setMultiSelectionEnabled(true);

        setTitle(getGuiString(model.getName()));

        this.addButton = new JButton(getGuiString("buttons.change"));
        this.addButton.addActionListener(e -> {
            if (this.changeActionListener != null) {
                Action details = fileChooser.getActionMap().get("viewTypeDetails");
                details.actionPerformed(null);
                if(FilePath.getFilePath(this.getTitle()) != null) {
                    File file = new File(FilePath.getFilePath(this.getTitle()).getPath());
                    fileChooser.setCurrentDirectory(file);
                }
                int ret = fileChooser.showDialog(null, getGuiString("buttons.openFile"));
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File[] files = fileChooser.getSelectedFiles();
                    FilePath.setFilePath(this.getTitle(), fileChooser.getCurrentDirectory().getPath());
                    this.changeActionListener.actionPerformed(files);
                }

            }
            hide();
        });
        this.cancelButton = new JButton(getGuiString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> {
            hide();
        });

        buildLayout();
        show();
    }

    private void buildLayout() {
        getContentPanel().setLayout(new BorderLayout(10, 10));
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        getContentPanel().add(getFieldPanel(), BorderLayout.PAGE_START);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        getContentPanel().add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setChangeActionListener(ViewActionListener changeActionListener) {
        this.changeActionListener = changeActionListener;
    }

    public void showExceptionWindow(String message) {
        int optionType = JOptionPane.OK_OPTION;
        int messageType = JOptionPane.WARNING_MESSAGE;
        Object[] selValues = {getGuiString("buttons.ok")};
        String attention = getGuiString("message.attention");
        JOptionPane.showOptionDialog(null,
                getGuiString(message), attention,
                optionType, messageType, null, selValues,
                selValues[0]);
    }
}
