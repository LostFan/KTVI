package dao;

import java.time.LocalDate;
import java.util.List;

import domain.Service;

public interface ServiceDAO {

    public List<Service> getAllServices();

    public Service getService(String name);

    public void save(Service service);

    public void update(Service service);

    public void delete(Service service);

    public int getCostByDay(Service service, LocalDate date);
}
