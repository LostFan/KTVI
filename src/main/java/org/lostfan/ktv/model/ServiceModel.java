package org.lostfan.ktv.model;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Service;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ServiceModel {

    private class ServiceTableModel implements TableModel {

        public String[] columnNames;
        public List<Function<Service, Object>> columnValues;

        public ServiceTableModel() {
            this.columnNames = new String[] {"ID", "Name", "Additional"};
            this.columnValues = new ArrayList<>(3);
            this.columnValues.add(Service::getId);
            this.columnValues.add(Service::getName);
            this.columnValues.add(Service::isAdditionalService);

        }

        @Override
        public int getRowCount() {
            return getServiceList().size();
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
            return columnValues.get(columnIndex).apply(getServiceList().get(rowIndex));
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

    private ServiceDAO dao;
    private List<Service> services;

    public ServiceModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getServiceDAO();
    }

    public List<Service> getServiceList() {
        if (this.services == null) {
            this.services = this.dao.getAllServices();
        }

        return this.services;
    }

    public TableModel getServiceTableModel() {
        return new ServiceTableModel();
    }
}
