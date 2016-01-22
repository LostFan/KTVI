package org.lostfan.ktv.model.transform;

import org.lostfan.ktv.domain.Service;
import org.lostfan.ktv.domain.Tariff;
import org.lostfan.ktv.model.dto.ServiceWithPrices;
import org.lostfan.ktv.model.dto.TariffWithPrices;

public class ServiceWithPricesTransformer implements EntityTransformer<Service, ServiceWithPrices> {

    @Override
    public ServiceWithPrices transformTo(Service entity) {
        ServiceWithPrices serviceWithPrices = new ServiceWithPrices();
        serviceWithPrices.setId(entity.getId());
        serviceWithPrices.setName(entity.getName());
        serviceWithPrices.setAdditionalService(entity.isAdditionalService());
        serviceWithPrices.setConnectionService(entity.isConnectionService());
        serviceWithPrices.setDisconnectionService(entity.isDisconnectionService());
        serviceWithPrices.setChangeTariff(entity.isChangeTariff());
        serviceWithPrices.setConsumeMaterials(entity.isConsumeMaterials());
        return serviceWithPrices;
    }
}
