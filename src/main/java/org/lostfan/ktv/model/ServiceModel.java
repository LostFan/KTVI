package org.lostfan.ktv.model;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Service;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServiceModel extends BaseModel<Service> {

    private class ServiceTableModel implements TableModel {

        @Override
        public int getRowCount() {
            return getList().size();
        }

        @Override
        public int getColumnCount() {
            return fields.size();
        }

        @Override
        public String getColumnName(int columnIndex) {
            return fields.get(columnIndex).getTitle();
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
            return fields.get(columnIndex).get(getList().get(rowIndex));
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

    private List<EntityField<Service, ?>> fields;

    private ServiceDAO dao;
    private List<Service> services;

    public ServiceModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getServiceDAO();
        fields = new ArrayList<>();


        this.fields = new ArrayList<>();
        this.fields.add(new EntityField<>("ID", EntityField.Types.Integer, Service::getId, Service::setId));
        this.fields.add(new EntityField<>("Name", EntityField.Types.String, Service::getName, Service::setName));
        this.fields.add(new EntityField<>("Additional", EntityField.Types.Boolean, Service::isAdditionalService, Service::setAdditionalService));
    }

    @Override
    public List<EntityField<Service, ?>> getFields() {
        return this.fields;
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

    @Override
    public FieldsComboBoxModel<Service> getFieldComboBoxModel() {
        return new FieldsComboBoxModel<>(fields);
    }

    private class MyIdPredicate {
        Predicate<Service> servicePredicate;

        public MyIdPredicate(int x) {
            servicePredicate = (service -> service.getId() == x);
        }
    }

    @Override
    public void setSearchCriteria(List<FieldSearchCriterion<Service>> criteria) {
        super.setSearchCriteria(criteria);

        this.services = this.dao.getAllServices();
        Stream<Service> stream = this.services.stream();
        for (FieldSearchCriterion<Service> serviceFieldSearchCriterion : criteria) {
            stream = stream.filter(serviceFieldSearchCriterion.buildPredicate());
        }

        this.services = stream.collect(Collectors.toList());
        this.notifyObservers(null);
    }
}
