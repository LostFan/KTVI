package org.lostfan.ktv.model.dto;

import org.lostfan.ktv.domain.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by 1 on 21.12.2016.
 */
public class RenderedServiceAndPayment {

    public RenderedServiceAndPayment(RenderedServiceExt renderedService) {
        this.date = renderedService.getDate();
        this.service = renderedService.getService();
        this.price = renderedService.getPrice().setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    public RenderedServiceAndPayment(PaymentExt payment) {
        this.date = payment.getDate();
        this.isCredit = true;
        this.service = payment.getService();
        this.price = payment.getPrice().setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    private LocalDate date;

    private boolean isCredit;

    private Service service;

    private BigDecimal price;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isCredit() {
        return isCredit;
    }

    public void setCredit(boolean credit) {
        isCredit = credit;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
