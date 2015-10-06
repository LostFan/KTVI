package org.lostfan.ktv.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Service;

public class SearchServiceModel implements SearchModel {

    private List<String> fields;

    private ServiceDAO dao;

    public SearchServiceModel() {
        this.dao = DAOFactory.getDefaultDAOFactory().getServiceDAO();
        fields = new ArrayList<>();
        fields.add("Наименование");
        fields.add("Дополнительная услуга");
    }

    public List<String> getFields() {
        return fields;
    }

    public String getName() {
        return "Поиск: сервисы";
    }
}
