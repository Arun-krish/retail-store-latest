package com.retail.repository;

import com.retail.entity.PurchaseOrders;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface PurchaseOrderRepository extends MongoRepository<PurchaseOrders, String> {

    List<PurchaseOrders> findByCustomerIdAndOrderDateBetween(String customerId, Date fromDate,Date toDate);


    List<PurchaseOrders> findByCustomerIdAndOrderDateGreaterThanEqual(String customerId, Date fromDate);
}
