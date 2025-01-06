package com.retail.service;

import com.retail.entity.PurchaseOrders;
import com.retail.repository.PurchaseOrderRepository;
import com.retail.util.ResponsePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderService {

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    public ResponsePojo savePurchaseOrder(PurchaseOrders purchaseOrders) {

    }

    public void calculateRewardsBasedOnPurchaseOrder(PurchaseOrders purchaseOrders) {
        if (purchaseOrders.getOrderTotal() > 100) {
            double rewardPoints = (purchaseOrders.getOrderTotal() - 100) * 2;
            purchaseOrders.setOrderTotal(rewardPoints + 50);

        } else if (purchaseOrders.getOrderTotal() >= 50 && purchaseOrders.getOrderTotal() <= 100) {
            purchaseOrders.setOrderTotal(purchaseOrders.getOrderTotal()-50);

        }
    }
}
