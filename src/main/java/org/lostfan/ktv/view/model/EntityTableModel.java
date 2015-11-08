package org.lostfan.ktv.view.model;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.dao.SubscriberDAO;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.BaseEntityModel;
import org.lostfan.ktv.model.EntityModel;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.utils.ResourceBundles;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class EntityTableModel<T extends Entity> implements TableModel {

    private BaseEntityModel<T> model;
    private SubscriberDAO subscriberDAO;
    private ServiceDAO serviceDAO;

    public EntityTableModel(BaseEntityModel<T> model) {
        this.model = model;
        this.subscriberDAO = DAOFactory.getDefaultDAOFactory().getSubscriberDAO();
        this.serviceDAO = DAOFactory.getDefaultDAOFactory().getServiceDAO();
    }

    @Override
    public int getRowCount() {
        return this.model.getList().size();
    }

    @Override
    public int getColumnCount() {
        return this.model.getFields().size();
    }

    @Override
    public String getColumnName(int columnIndex) {
    return ResourceBundles.getEntityBundle().getString(
            this.model.getFields().get(columnIndex).getTitleKey());
}

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return this.model.getFields().get(columnIndex).getType().getClazz();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
//        System.out.println(this.model.getFields().get(columnIndex).getType().isEntityClass());
        return this.model.getFields().get(columnIndex).getType().isEntityClass() ? true : false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = this.model.getFields().get(columnIndex).get(this.model.getList().get(rowIndex));
        EntityFieldTypes thisType = this.model.getFields().get(columnIndex).getType();
        if(thisType.getClazz() == Integer.class && value ==null) {
            return 0;
        }
        if( thisType.isEntityClass()) {
            value =  ((Entity) thisType.getDAO().get((Integer) value)).getName();
        }

        return value;
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

    public EntityModel getEntityModel() {
        return this.model;
    }
}
