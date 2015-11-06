package org.lostfan.ktv.view.model;

import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.utils.ResourceBundles;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FieldsComboBoxModel<E> implements ComboBoxModel<String> {

    private List<EntityField<E, ?>> fields;
    private Map<String, EntityField<E, ?>> titleFieldMap;

    public Object currentValue;

    public FieldsComboBoxModel(List<EntityField<E, ?>> fields) {
        this.fields = fields;
        this.titleFieldMap = this.fields.stream().collect(Collectors.toMap(
                field-> ResourceBundles.getEntityBundle().getString(field.getTitleKey()),
                Function.identity()));
        this.currentValue = null;
    }

    public EntityField<E, ?> getSelectedField() {
        if (this.currentValue == null) {
            return null;
        }
        return this.titleFieldMap.get(this.currentValue);
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
        return fields.size();
    }

    @Override
    public String getElementAt(int index) {
        return  ResourceBundles.getEntityBundle().getString(fields.get(index).getTitleKey());
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
