package com.retail;

import com.retail.entity.PurchaseOrders;
import com.retail.service.PurchaseOrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RetailStoreApplicationTests {

    @Autowired
    PurchaseOrderService purchaseOrderService;

    @Test
    void testRewardPointsScenarios() {
        //Scenario 1 - Order Total is 120 hence reward should be 90
        PurchaseOrders orders = new PurchaseOrders("C1", "O1", new Date(), 120.0);
        purchaseOrderService.calculateRewardsBasedOnPurchaseOrder(orders);
        assertThat(orders.getTotalRewards()).isEqualTo(90);

        //Scenario 2 - Order Total is 100 hence reward should be 50
        orders = new PurchaseOrders("C1", "O1", new Date(), 100.0);
        purchaseOrderService.calculateRewardsBasedOnPurchaseOrder(orders);
        assertThat(orders.getTotalRewards()).isEqualTo(50);

        //Scenario 3 - Order Total is 75 hence reward should be 25
        orders = new PurchaseOrders("C1", "O1", new Date(), 75.0);
        purchaseOrderService.calculateRewardsBasedOnPurchaseOrder(orders);
        assertThat(orders.getTotalRewards()).isEqualTo(25);

        //Scenario 4 - Order Total is 50 hence reward should be 0
        orders = new PurchaseOrders("C1", "O1", new Date(), 50.0);
        purchaseOrderService.calculateRewardsBasedOnPurchaseOrder(orders);
        assertThat(orders.getTotalRewards()).isEqualTo(0);
    }

}
