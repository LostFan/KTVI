package org.lostfan.ktv;

import org.lostfan.ktv.controller.MainController;
import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.impl.hsqldb.HsqldbDaoFactory;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.MainModel;
import org.lostfan.ktv.utils.ConnectionManager;
import org.lostfan.ktv.utils.HsqldbConnectionManager;
import org.lostfan.ktv.validation.ValidationResult;
import org.lostfan.ktv.view.FormView;
import org.lostfan.ktv.view.MainView;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {
        ConnectionManager.setManager(new HsqldbConnectionManager());
        DAOFactory.setDefaultDAOFactory(new HsqldbDaoFactory());

        // Close connections and save all the changes
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                ConnectionManager.getManager().close();
            }
        });

        MainModel model = new MainModel();
        MainView view = new MainView(model);
        MainController controller = new MainController(model, view);


        new TestFormView();

        view.show();
    }

    public static class TestFormView extends FormView {

        public TestFormView() {
            setTitle("Test FormView");
            setSize(400, 500);
            getFrame().setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            StringFormField nameField = new StringFormField("subscriber.name");
            IntegerFormField sizeField = new IntegerFormField("payment.price");
            addFormField(nameField);
            addFormField(sizeField);

            JButton addButton = new JButton("Validate");
            getContentPanel().add(addButton);
            addButton.addActionListener(args -> {
                ValidationResult errors = ValidationResult.createEmpty();
                if (nameField.getValue().equals("")) {
                    errors.addError("errors.empty", "subscriber.name");
                }
                if (sizeField.getValue() == null) {
                    errors.addError("errors.empty", "payment.price");
                } else if (sizeField.getValue() < 0) {
                    errors.addError("errors.negative", "payment.price");
                }
                showErrors(errors);
            });
            show();
        }
    }

    public static class TestDto {
        private int size;
        private String name;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
