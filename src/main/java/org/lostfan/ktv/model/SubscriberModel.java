package org.lostfan.ktv.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Subscriber;
import org.lostfan.ktv.domain.Subscriber;

public class SubscriberModel implements TableModelBase {

    private class SubscriberTableModel implements TableModel {

        public String[] columnNames;
        public List<Function<Subscriber, Object>> columnValues;

        public SubscriberTableModel() {
            this.columnNames = new String[] {"ID",  "Account",  "Name" , "Balance"};
            this.columnValues = new ArrayList<>(4);
            this.columnValues.add(Subscriber::getId);
            this.columnValues.add(Subscriber::getAccount);
            this.columnValues.add(Subscriber::getName);
            this.columnValues.add(Subscriber::getBalance);

        }

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

    private SubscriberDAO dao;
    private List<Subscriber> services;

    public SubscriberModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
    }

    public List<Subscriber> getList() {
        if (this.services == null) {
            this.services = this.dao.getAllSubscribers();
        }

        return this.services;
    }

    public TableModel getTableModel() {
        return new SubscriberTableModel();
    }

    public String getTableName() {
        return "Абоненты";
    }
}
