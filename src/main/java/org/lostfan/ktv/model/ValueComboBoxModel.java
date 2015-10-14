package org.lostfan.ktv.model;

import javax.swing.*;
import javax.swing.event.ListDataListener;

/**
 * Created by Ihar_Niakhlebau on 14-Oct-15.
 */
public class ValueComboBoxModel<T> extends DefaultComboBoxModel<String> {

    private Object currentValue;
    private EntityComboBoxModel<T> model;

    public ValueComboBoxModel(EntityComboBoxModel<T> model) {
        this.model = model;
    }

    public ValueComboBoxModel(EntityComboBoxModel<T> model, String currentValue) {
        this.model = model;
        this.currentValue = (Object) currentValue;
    }

    @Override
    public void setSelectedItem(Object anItem) {
            currentValue = anItem;
    }

    @Override
    public Object getSelectedItem() {
            return this.currentValue;
    }

    @Override
    public int getSize() {
        return this.model.getList().size();
    }

    @Override
    public String getElementAt(int index) {
        Object value = this.model.getFields().get(1).get(this.model.getList().get(index));        return (String) value;
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
