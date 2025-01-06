package com.retail.controller;

import com.retail.entity.Customers;
import com.retail.entity.PurchaseOrders;
import com.retail.service.CustomerService;
import com.retail.service.PurchaseOrderService;
import com.retail.util.ResponsePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/purchaseOrder")
public class PurchaseOrderController {


    @Autowired
    PurchaseOrderService purchaseOrderService;

    @PostMapping(value = "/savePurchaseOrder")
    ResponseEntity<ResponsePojo> saveCustomer(@RequestBody PurchaseOrders purchaseOrders) {
        return new ResponseEntity<>(purchaseOrderService.savePurchaseOrder(purchaseOrders), HttpStatus.OK);
    }

    @GetMapping(value = "/bulkProcessPurchaseOrders")
    ResponseEntity<ResponsePojo> bulkProcessPurchaseOrders() {
        return new ResponseEntity<>(purchaseOrderService.bulkProcessPurchaseOrders(), HttpStatus.OK);
    }

    @GetMapping(value = "/fetchOrderHistory")
    ResponseEntity<ResponsePojo> fetchOrderHistory(@RequestHeader String customerId,@RequestHeader Date fromDate) {
        return new ResponseEntity<>(purchaseOrderService.fetchOrderHistory(customerId,fromDate), HttpStatus.OK);
    }
}
