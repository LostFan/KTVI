package org.lostfan.ktv.model;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CriteriaComboBoxModel implements ComboBoxModel<String> {

    private Object currentValue;
    private Map<String, SearchCriteria> criteriaMap;
    private List<SearchCriteria> criteria;

    public CriteriaComboBoxModel(List<SearchCriteria> criteria) {
        this.criteriaMap = criteria.stream().collect(Collectors.toMap(SearchCriteria::getTitle, Function.identity()));
        this.criteria = criteria;
        this.currentValue = criteria.get(0).getTitle();
    }

    public SearchCriteria getSelectedCriterion() {
        if (this.currentValue == null) {
            return null;
        }
        return this.criteriaMap.get(this.currentValue);
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
        return criteriaMap.size();
    }

    @Override
    public String getElementAt(int index) {
        return criteria.get(index).getTitle();
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
