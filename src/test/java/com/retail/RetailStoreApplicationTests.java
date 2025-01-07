package com.retail;

import com.retail.controller.CustomerController;
import com.retail.entity.Customers;
import com.retail.entity.PurchaseOrders;
import com.retail.repository.CustomerRepository;
import com.retail.repository.PurchaseOrderRepository;
import com.retail.service.CustomerService;
import com.retail.service.PurchaseOrderService;
import com.retail.util.ApplicationConstants;
import com.retail.util.ResponsePojo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class RetailStoreApplicationTests {

    @Autowired
    PurchaseOrderService purchaseOrderService;

   /* @Mock
    PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    CustomerRepository customerRepository;*/

    @Mock
    CustomerService mockCustomerService;

    @InjectMocks
    CustomerController customerController;

    @Test
    void testCreateCustomer() throws Exception {

        when(mockCustomerService.saveCustomer(any())).thenReturn(new ResponsePojo(ApplicationConstants.SUCCESS, "Customer Saved Successfully!",any()));
        // when
        ResponseEntity<ResponsePojo> response = customerController.saveCustomer(any());

        // then
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(ApplicationConstants.SUCCESS, response.getBody().getStatus())
        );
    }

    @Test
    void testRewardPointsScenarios() {
        //Scenario 1 - Order Total is 120 hence reward should be 90
        PurchaseOrders orders = new PurchaseOrders("C1", "O1", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0);
        purchaseOrderService.calculateRewardsBasedOnPurchaseOrder(orders);
        assertThat(orders.getTotalRewards()).isEqualTo(90);

        //Scenario 2 - Order Total is 100 hence reward should be 50
        orders = new PurchaseOrders("C1", "O1", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 100.0);
        purchaseOrderService.calculateRewardsBasedOnPurchaseOrder(orders);
        assertThat(orders.getTotalRewards()).isEqualTo(50);

        //Scenario 3 - Order Total is 75 hence reward should be 25
        orders = new PurchaseOrders("C1", "O1", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 75.0);
        purchaseOrderService.calculateRewardsBasedOnPurchaseOrder(orders);
        assertThat(orders.getTotalRewards()).isEqualTo(25);

        //Scenario 4 - Order Total is 50 hence reward should be 0
        orders = new PurchaseOrders("C1", "O1", new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 50.0);
        purchaseOrderService.calculateRewardsBasedOnPurchaseOrder(orders);
        assertThat(orders.getTotalRewards()).isEqualTo(0);
    }

   /* @Test
    void testOrderFetchHistory() throws Exception {

        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customers("name", "987654321")));
        when(purchaseOrderRepository.findByCustomerIdAndOrderDateBetween("customerId", convertStringToDate("2025-01-01"),
                convertStringToDate("2025-01-05"))).thenReturn(Arrays.asList(
                new PurchaseOrders("C1", "O1", convertStringToDate("2025-01-01").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0),
                new PurchaseOrders("C1", "O1", convertStringToDate("2025-01-05").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0)));

        when(purchaseOrderRepository.findByCustomerIdAndOrderDateGreaterThanEqual("customerId", convertStringToDate("2025-01-01"))).thenReturn(Arrays.asList(
                new PurchaseOrders("C1", "O1", convertStringToDate("2025-01-01").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0),
                new PurchaseOrders("C1", "O1", convertStringToDate("2025-01-05").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0),
                new PurchaseOrders("C1", "O1", convertStringToDate("2025-01-10").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0)));

        assertThat(purchaseOrderService.fetchOrderHistory("customerId", convertStringToDate("2025-01-01"),
                convertStringToDate("2025-01-05"), false)).isEqualTo(new ResponsePojo(ApplicationConstants.SUCCESS, "Details Fetched Successfully!", Arrays.asList(
                new PurchaseOrders("C1", "O1", convertStringToDate("2025-01-01").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0),
                new PurchaseOrders("C1", "O1", convertStringToDate("2025-01-05").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 120.0))));
    }

    Date convertStringToDate(String date) throws ParseException {
        Date convertedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        return convertedDate;
    }*/
}
