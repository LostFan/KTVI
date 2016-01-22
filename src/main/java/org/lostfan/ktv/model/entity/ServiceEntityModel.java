package org.lostfan.ktv.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.lostfan.ktv.dao.DAOFactory;
import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.dao.ServiceDAO;
import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.ServicePrice;
import org.lostfan.ktv.domain.TariffPrice;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.model.EntityFieldTypes;
import org.lostfan.ktv.model.dto.ServiceWithPrices;
import org.lostfan.ktv.model.transform.ServiceWithPricesTransformer;
import org.lostfan.ktv.validation.ServicePriceValidator;
import org.lostfan.ktv.validation.ValidationResult;

public class ServiceEntityModel extends BaseEntityModel<Service> {

    private List<EntityField> fields;
    ServiceWithPricesTransformer serviceWithPricesTransformer;
    ServicePriceValidator servicePriceValidator;

    public ServiceEntityModel() {

        serviceWithPricesTransformer = new ServiceWithPricesTransformer();
        servicePriceValidator = new ServicePriceValidator();

        this.fields = new ArrayList<>();
        this.fields.add(new EntityField("service.id", EntityFieldTypes.Integer, Service::getId, Service::setId, false));
        this.fields.add(new EntityField("service.name", EntityFieldTypes.String, Service::getName, Service::setName));
        this.fields.add(new EntityField("service.additional", EntityFieldTypes.Boolean, Service::isAdditionalService, Service::setAdditionalService));
        this.fields.add(new EntityField("service.change_tariff", EntityFieldTypes.Boolean, Service::isChangeTariff, Service::setChangeTariff));
        this.fields.add(new EntityField("service.consume_materials", EntityFieldTypes.Boolean, Service::isConsumeMaterials, Service::setConsumeMaterials));
        this.fields.add(new EntityField("service.connection_service", EntityFieldTypes.Boolean, Service::isConnectionService, Service::setConnectionService));
        this.fields.add(new EntityField("service.disconnection_service", EntityFieldTypes.Boolean, Service::isDisconnectionService, Service::setDisconnectionService));
    }

    @Override
    public List<EntityField> getFields() {
        return this.fields;
    }

    @Override
    public String getEntityNameKey() {
        return "services";
    }

    @Override
    public String getEntityName() {
        return "service";
    }

    @Override
    public List<EntityModel> getEntityModels() {
        return null;
    }

    @Override
    public Class getEntityClass() {
        return Service.class;
    }

    @Override
    protected ServiceDAO getDao() {
        return DAOFactory.getDefaultDAOFactory().getServiceDAO();
    }

    @Override
    public Service createNewEntity() {
        return new Service();
    }

    public void delete(ServicePrice price) {
        getDao().deleteServicePrice(price);
    }

    public ServiceWithPrices getServiceWithPrices(Integer serviceId) {
        ServiceWithPrices serviceWithPrices = serviceWithPricesTransformer.transformTo(getEntity(serviceId));
        serviceWithPrices.setArchivePrices(new ArrayList<>());
        List<ServicePrice> prices = getDao().getServicePrices(serviceId);
        // Sort by date DESC
        prices.stream()
                .sorted((price1, price2) -> {
                    if (price1.getDate().isAfter(price2.getDate())) {
                        return -1;
                    } else if (price1.getDate().equals(price2.getDate())) {
                        return 1;
                    } else {
                        return 0;
                    }
                })
                .forEach(tariffPrice -> {
                    if (tariffPrice.getDate().isAfter(LocalDate.now())) {
                        serviceWithPrices.setNewPrice(tariffPrice);
                    } else if (serviceWithPrices.getCurrentPrice() == null) {
                        serviceWithPrices.setCurrentPrice(tariffPrice);
                    } else {
                        serviceWithPrices.getArchivePrices().add(tariffPrice);
                    }
                });
        return serviceWithPrices;
    }

    public ValidationResult save(ServicePrice price) {
        // TODO: validate and save the new price
        ValidationResult result = this.servicePriceValidator.validate(price);
        if (!result.hasErrors()) {
            ServiceWithPrices tariffWithPrices = getServiceWithPrices(price.getServiceId());
            if (tariffWithPrices.getNewPrice() != null) {
                getDao().deleteServicePrice(tariffWithPrices.getNewPrice());
            }
            getDao().saveServicePrice(price);

        }
        return result;
    }
}
