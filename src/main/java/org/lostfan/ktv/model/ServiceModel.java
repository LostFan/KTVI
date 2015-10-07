package org.lostfan.ktv.model;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Service;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ServiceModel implements ModelBase<Service> {

    private class ServiceTableModel implements TableModel {

        @Override
        public int getRowCount() {
            return getList().size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return getValueAt(0, columnIndex).getClass();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return columnValues.get(columnIndex).apply(getList().get(rowIndex));
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }
    }

    private class ServiceComboBoxModel implements ComboBoxModel<String> {

        public Object currentValue;
        public int currentIndex = 0;

        @Override
        public void setSelectedItem(Object anItem) {
            currentValue = anItem;
        }

        @Override
        public Object getSelectedItem() {

            System.out.println(getList().size());
            Function<Service, Integer> f = Service::getId;
            System.out.println("currentIndex " + currentIndex);
            Predicate<Service> d = service -> columnValues.get(currentIndex).apply(service).toString().equals("1");
            Stream<Service> s = services.stream().filter(d);
            List newlist = Arrays.asList(s.toArray());
            System.out.println(newlist.size());
            return currentValue == null ? columnNames[0] : currentValue;

        }

        @Override
        public int getSize() {
            return columnNames.length;
        }

        @Override
        public String getElementAt(int index) {

            return columnNames[index];
        }

        @Override
        public void addListDataListener(ListDataListener l) {

        }

        @Override
        public void removeListDataListener(ListDataListener l) {

        }
    }

    private List<String> fields;

    private ServiceDAO dao;
    private List<Service> services;

    public String[] columnNames;
    public List<Function<Service, Object>> columnValues;

    public ServiceModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getServiceDAO();
        fields = new ArrayList<>();
        fields.add("Наименование");
        fields.add("Дополнительная услуга");

        this.columnNames = new String[] {"ID", "Name", "Additional"};
        this.columnValues = new ArrayList<>(3);
        this.columnValues.add(Service::getId);
        this.columnValues.add(Service::getName);
        this.columnValues.add(Service::isAdditionalService);
    }

    public List<Service> getList() {
        if (this.services == null) {
            this.services = this.dao.getAllServices();
        }

        return this.services;
    }

    public TableModel getTableModel() {
        return new ServiceTableModel();
    }

    @Override
    public String getEntityName() {
        return "Услуги";
    }

    public ComboBoxModel<String> getFields() {
        return new ServiceComboBoxModel();
    }

    private class MyIdPredicate {
        Predicate<Service> servicePredicate;

        public MyIdPredicate(int x) {
            servicePredicate = (service -> service.getId() == x);
        }
    }

    public List getSearchList(List<String> fields, List values) {
//        List<String> columnNames = new ArrayList<String>();
//        columnNames.add("Id");
//        columnNames.add("Name");
//        columnNames.add("Additional");
//        List<Function<Service, Object>> columnValues = new ArrayList<>();
//        columnValues.add(Service::getId);
//        columnValues.add(Service::getName);
//        columnValues.add(Service::isAdditionalService);
//        Stream<Service> stream= getList().stream();
//        for (int i = 0; i < fields.size(); i++) {
//            stream = stream.filter(columnNames.(fields.get(i)))
//        return n
//        }
        return null;
    }
}
