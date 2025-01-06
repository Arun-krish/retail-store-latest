package com.retail.controller;

import com.retail.entity.PurchaseOrders;
import com.retail.service.PurchaseOrderService;
import com.retail.util.ResponsePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
@RequestMapping("/api/purchaseOrder")
public class PurchaseOrderController {


    @Autowired
    PurchaseOrderService purchaseOrderService;

    /**
     * To save individual Purchase Order
     * @param purchaseOrders
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/savePurchaseOrder")
    ResponseEntity<ResponsePojo> savePurchaseOrder(@RequestBody PurchaseOrders purchaseOrders) throws Exception {
        return new ResponseEntity<>(purchaseOrderService.savePurchaseOrder(purchaseOrders), HttpStatus.OK);
    }

    /**
     * To capture Purchase order file and process rewards based on orders
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/bulkProcessPurchaseOrders")
    ResponseEntity<ResponsePojo> bulkProcessPurchaseOrders(@RequestBody MultipartFile file)  throws Exception{
        return new ResponseEntity<>(purchaseOrderService.bulkProcessPurchaseOrders(file), HttpStatus.OK);
    }

    /**
     * To fetch order history based on Customer Id and From date
     * @param customerId
     * @param fromDate
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/fetchOrderHistory")
    ResponseEntity<ResponsePojo> fetchOrderHistory(@RequestHeader(required = true) String customerId,
                                                   @RequestHeader(required = false) Date fromDate,
                                                   @RequestHeader(required = false) Date toDate,
                                                   @RequestHeader(required = false) boolean lastThreeMonths) throws Exception{
        return new ResponseEntity<>(purchaseOrderService.fetchOrderHistory(customerId,fromDate,toDate,lastThreeMonths), HttpStatus.OK);
    }
}
