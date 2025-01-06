package com.retail.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PurchaseOrders {

    @Id
    String id;
    String customerId;
    String orderId;
    Double orderTotal=0.0;
    Double totalRewards=0.0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public Double getTotalRewards() {
        return totalRewards;
    }

    public void setTotalRewards(Double totalRewards) {
        this.totalRewards = totalRewards;
    }
}
