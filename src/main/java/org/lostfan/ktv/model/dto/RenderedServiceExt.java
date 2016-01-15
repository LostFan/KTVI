package org.lostfan.ktv.model.dto;

import org.lostfan.ktv.domain.RenderedService;
import org.lostfan.ktv.domain.Service;

public class RenderedServiceExt extends RenderedService {

    private Service service;

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
