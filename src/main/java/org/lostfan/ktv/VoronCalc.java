package org.lostfan.ktv;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import javax.swing.*;

/**
 * Created by Ihar_Niakhlebau on 17-Aug-15.
 */
public class VoronCalc extends JFrame {
    private int voron = 0;
    private JLabel countLabel;
    private JButton addCrow;
    private JButton removeCrow;

    public VoronCalc(){
        super("Crow calculator");
        //Подготавливаем компоненты объекта
        countLabel = new JLabel("Crows:" + voron);
        addCrow = new JButton("Add Crow");
        removeCrow = new JButton("Remove Crow");

        //Подготавливаем временные компоненты
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        //Расставляем компоненты по местам
        add(countLabel, BorderLayout.NORTH); //О размещении компонент поговорим позже

        buttonsPanel.add(addCrow);
        buttonsPanel.add(removeCrow);
        buttonsPanel.add(new JTable());

        add(buttonsPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addCrow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {

                voron = voron+1;     //Добавляем одну ворону
                updateCrowCounter(); //Сообщаем приложению, что количество ворон изменилось
            }
        });
    }

    private void updateCrowCounter() {
        countLabel.setText("Crows:" + voron);
    }

    public static void main(String[] args) {
//        createEmployees();
        showNodes();
//        Connection connection = null;
//        Statement statement = null;
//        try {
//            Class.forName("org.hsqldb.jdbcDriver").newInstance();
//            String url = "jdbc:hsqldb:hsqldb\\demoDatabase";
//            connection = DriverManager.getConnection(url, "SA", "");
//            connection.setAutoCommit(false);
//
//            statement = connection.createStatement();
//            statement.execute("CREATE TABLE employees (id IDENTITY , email VARCHAR(20))");
//            String update1 = "UPDATE employees SET email = 'a@b.com' WHERE email = 'a@a.com'";
//            statement.executeUpdate(update1);
//            Savepoint savepoint1 = connection.setSavepoint("savepoint1");
//
//            String update2 = "UPDATE employees SET email = 'b@b.com' WHERE email = 'b@c.com'";
//            statement.executeUpdate(update2);
//            Savepoint savepoint2 = connection.setSavepoint("savepoint2");
//
//            String update3 = "UPDATE employees SET email = 'c@c.com' WHERE email = 'c@d.com'";
//            statement.executeUpdate(update3);
//            Savepoint savepoint3 = connection.setSavepoint("savepoint3");
//
//            String update4 = "UPDATE employees SET email = 'd@d.com' WHERE email = 'd@e.com'";
//            statement.executeUpdate(update4);
//            Savepoint savepoint4 = connection.setSavepoint("savepoint4");
//
//            String update5 = "UPDATE employees SET email = 'e@e.com' WHERE email = 'e@f.com'";
//            statement.executeUpdate(update5);
//            Savepoint savepoint5 = connection.setSavepoint("savepoint5");
//
//            connection.rollback(savepoint3);
//            connection.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (statement != null) {
//                try {
//                    statement.close();
//                } catch (SQLException e) {
//                } // nothing we can do
//            }
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                } // nothing we can do
//            }
//        }
//        ApplicationContext context =
//                new AnnotationConfigApplicationContext(PersistenceConfig.class);
//        for (String s : context.getBeanDefinitionNames()) {
//            System.out.println(s);
//        }
//        VoronCalc p = context.getBean(VoronCalc.class);
//        p.setVisible(true);
//        p.pack();
//        VoronCalc app = new VoronCalc();
//        app.setVisible(true);
//        app.pack(); //Эта команда подбирает оптимальный размер в зависимости от содержимого окна
    }

    public static void createEmployees(){
                Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            String url = "jdbc:hsqldb:hsqldb\\demoDatabase";
            connection = DriverManager.getConnection(url, "SA", "");
            connection.setAutoCommit(false);

            statement = connection.createStatement();
            statement.execute("CREATE TABLE employees (id IDENTITY , email VARCHAR(20))");
            String update1 = "UPDATE employees SET email = 'a@b.com' WHERE email = 'a@a.com'";
            statement.executeUpdate(update1);
            Savepoint savepoint1 = connection.setSavepoint("savepoint1");

            String update2 = "UPDATE employees SET email = 'b@b.com' WHERE email = 'b@c.com'";
            statement.executeUpdate(update2);
            Savepoint savepoint2 = connection.setSavepoint("savepoint2");

            String update3 = "UPDATE employees SET email = 'c@c.com' WHERE email = 'c@d.com'";
            statement.executeUpdate(update3);
            Savepoint savepoint3 = connection.setSavepoint("savepoint3");

            String update4 = "UPDATE employees SET email = 'd@d.com' WHERE email = 'd@e.com'";
            statement.executeUpdate(update4);
            Savepoint savepoint4 = connection.setSavepoint("savepoint4");

            String update5 = "UPDATE employees SET email = 'e@e.com' WHERE email = 'e@f.com'";
            statement.executeUpdate(update5);
            Savepoint savepoint5 = connection.setSavepoint("savepoint5");

            connection.rollback(savepoint3);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                } // nothing we can do
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                } // nothing we can do
            }
        }
    }

    public static void showNodes(){
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            String url = "jdbc:hsqldb:hsqldb\\demoDatabase";
            connection = DriverManager.getConnection(url, "SA", "");
            connection.setAutoCommit(false);

            statement = connection.createStatement();
            String query = "SELECT email FROM employees";
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println(resultSet.getFetchSize());
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1) );
            }
        } catch (Exception e) {
            System.out.println("Statement not created");
            e.printStackTrace();
        }
    }
}
