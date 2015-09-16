package org.lostfan.ktv.dao.impl.hsqldb;

import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.ServicePrice;
import org.lostfan.ktv.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HsqldbServiceDAO implements ServiceDAO {

    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        try {
            Statement statement = ConnectionManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM \"service\"");
            while (rs.next()) {
                Service service = new Service();
                service.setId(rs.getInt("id"));
                service.setName(rs.getString("name"));
                service.setAdditionalService(rs.getBoolean("additional"));

                services.add(service);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return services;
    }

    public Service getService(int id) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void save(Service service) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void update(Service service) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void delete(Service service) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int getCostByDay(Service service, LocalDate date) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<ServicePrice> getServicePricesByServiceId(int serviceId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
