package org.lostfan.ktv.view.entity;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.model.entity.RenderedServiceEntityModel;
import org.lostfan.ktv.utils.FilePath;
import org.lostfan.ktv.utils.RenderedServicesLoader;
import org.lostfan.ktv.utils.ViewActionListener;
import org.lostfan.ktv.view.EntityInnerTableView;
import org.lostfan.ktv.view.FormView;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoadRenderedServicesView extends FormView {

    private class ModelObserver implements org.lostfan.ktv.utils.Observer {

        private Integer filesCount = 1;
        private Integer filesCounter = 1;

        @Override
        public void update(Object args) {
            Integer progress = model.getProgress();
            if (progress != null) {
                LoadRenderedServicesView.this.progressBar.setValue(progress);
                if (progress == 100) {
                    entityInnerTableView.getEntityList().clear();
                    entityInnerTableView.getEntityList().addAll(model.getRenderedServices());
                }
            }
            LoadRenderedServicesView.this.revalidate();
        }

        public void setFilesCounter(Integer filesCounter) {
            this.filesCounter = filesCounter;
        }

        public void setFilesCount(Integer filesCount) {
            this.filesCount = filesCount;
        }
    }

    private static class RenderedServiceFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String filename = f.getName().toLowerCase();
            return filename.endsWith(".txt");
        }

        @Override
        public String getDescription() {
            return "*.txt";
        }
    }

    private JFileChooser fileChooser;
    private EntityInnerTableView<RenderedService> entityInnerTableView;
    private RenderedServiceEntityModel model;
    private JButton openFileButton;
    private JButton addButton;
    private JButton cancelButton;
    private DateFormField dateField;
    private JProgressBar progressBar;

    protected ViewActionListener addActionListener;
    private ViewActionListener loadRenderedServiceFileActionListener;
    private Thread loadingThread;
    private boolean isOpened = true;

    private ModelObserver modelObserver;

    private RenderedServicesLoader renderedServicesLoader = new RenderedServicesLoader();

    public LoadRenderedServicesView(RenderedServiceEntityModel model) {
        this.model = model;
        this.model.getRenderedServices().clear();

        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileFilter(new RenderedServiceFileFilter());
        this.fileChooser.setMultiSelectionEnabled(true);

        this.modelObserver = new ModelObserver();

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);

        dateField = new DateFormField("renderedService.date");
        dateField.addValueListener(e -> {
            for (RenderedService renderedService : entityInnerTableView.getEntityList()) {
                renderedService.setDate(dateField.getValue());
            }
            this.revalidate();
        });
        addFormField(dateField);


        List<String> bankFileNames = new ArrayList<>();
        setTitle(getGuiString("window.loadSubscriptionFees"));

        this.addButton = new JButton(getGuiString("buttons.add"));
        this.addButton.addActionListener(e -> {

            if (this.addActionListener != null) {
                this.addActionListener.actionPerformed(entityInnerTableView.getEntityList());
            }
        });

        this.cancelButton = new JButton(getGuiString("buttons.cancel"));
        this.cancelButton.addActionListener(e -> {
                isOpened = false;
                hide();
        });

        this.openFileButton = new JButton(getGuiString("buttons.openFile"));
        openFileButton.addActionListener(e -> {
            Action details = fileChooser.getActionMap().get("viewTypeDetails");
            details.actionPerformed(null);
            if(FilePath.getFilePath(this.getTitle()) != null) {
                File file = new File(FilePath.getFilePath(this.getTitle()).getPath());
                fileChooser.setCurrentDirectory(file);
            }
            int ret = fileChooser.showDialog(null, getGuiString("buttons.openFile"));
            if (ret == JFileChooser.APPROVE_OPTION) {
                LoadRenderedServicesView.this.addButton.setEnabled(false);
                File[] files = fileChooser.getSelectedFiles();
                FilePath.setFilePath(this.getTitle(), fileChooser.getCurrentDirectory().getPath());
                List<RenderedService> renderedServices = new ArrayList<RenderedService>();
                renderedServices.addAll(entityInnerTableView.getEntityList());
                loadingThread = new Thread(() -> {
                    model.addObserver(this.modelObserver);
                    modelObserver.setFilesCount(files.length);
                    Integer filesCounter = 0;

                    for (File file : files) {
                        if(!isOpened) {
                            return;
                        }
                        modelObserver.setFilesCounter(filesCounter++);
                        if (bankFileNames.contains(file.getName())) {
                            if (!confirmAdding()) {
                                continue;
                            }
                        } else {
                            bankFileNames.add(file.getName());
                        }



                        List<RenderedService> newRenderedServices = renderedServicesLoader.load(file);
                        for (RenderedService renderedService : newRenderedServices) {
                            renderedService.setDate(dateField.getValue());
                        }
                        renderedServices.addAll(newRenderedServices);

                    }
                    if (LoadRenderedServicesView.this.loadRenderedServiceFileActionListener != null) {
                        LoadRenderedServicesView.this.loadRenderedServiceFileActionListener.actionPerformed(renderedServices);
                        this.entityInnerTableView.getTable().revalidate();
                    }
                    LoadRenderedServicesView.this.addButton.setEnabled(true);
                    model.removeObserver(this.modelObserver);
                });
                loadingThread.start();

            }

            this.entityInnerTableView.getTable().revalidate();
        });

        getFrame().addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
               isOpened = false;
            }
        });


        this.entityInnerTableView = new EntityInnerTableView<>(model.getLoadFullEntityField(), new ArrayList<>());

        buildLayout();

        show();
    }

    public void addRenderedServices(List<RenderedService> renderedServices) {
        entityInnerTableView.getEntityList().addAll(renderedServices);
        this.revalidate();
    }

    private void buildLayout() {
        getContentPanel().setLayout(new BorderLayout(10, 10));
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalGlue());

        panel.add(Box.createRigidArea(new Dimension(10, 10)));

        panel.add(openFileButton);
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createRigidArea(new Dimension(10, 10)));

        JPanel jPanel = new JPanel();
        jPanel.setPreferredSize(new Dimension(50, 50));
        jPanel.add(getFieldPanel());
        getContentPanel().add(jPanel, BorderLayout.NORTH);

        panel.add(this.entityInnerTableView.getContentPanel());
        getContentPanel().add(panel);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(progressBar);
        buttonPanel.add(cancelButton);
        getContentPanel().add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setAddActionListener(ViewActionListener addActionListener) {
        this.addActionListener = addActionListener;
    }

    public void setLoadRenderedServiceFileActionListener(ViewActionListener loadRenderedServiceFileActionListener) {
        this.loadRenderedServiceFileActionListener = loadRenderedServiceFileActionListener;
    }

    private boolean confirmAdding(){
        int optionType = JOptionPane.OK_CANCEL_OPTION;
        int messageType = JOptionPane.QUESTION_MESSAGE;
        Object[] selValues = { getGuiString("buttons.yes"), getGuiString("buttons.cancel") };
        String message = getGuiString("window.loadRenderedServices") + " : "
                + getEntityString(model.getEntityNameKey());
        int result = JOptionPane.showOptionDialog(null,
                getGuiString("message.alreadyExistLoadQuestion"), message,
                optionType, messageType, null, selValues,
                selValues[0]);

        return result == 0;
    }

    public void showErrors(List<org.lostfan.ktv.validation.Error> errors) {
        // TODO: add appropriate implementation of visualising the errors
        for (org.lostfan.ktv.validation.Error error : errors) {
            String message = error.getField() != null ? getEntityString(error.getField()) + " " : "";
            if (error.getMessage().equals("empty")) {
                message += "should not be empty";
            } else {
                message += error.getMessage();
            }
            JOptionPane.showMessageDialog(getFrame(), message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
