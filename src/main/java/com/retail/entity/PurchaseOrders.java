package com.retail.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("purchase_orders")
@Data
@NoArgsConstructor
public class PurchaseOrders {
    public PurchaseOrders(String customerId, String orderId, Date orderDate, Double orderTotal) {
        this.customerId = customerId;
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderTotal = orderTotal;
    }

    @Id
    String id;
    @NotNull
    String customerId;
    @NotNull
    String orderId;
    @NotNull
    Date orderDate;
    Double orderTotal = 0.0;
    Double totalRewards = 0.0;

}
