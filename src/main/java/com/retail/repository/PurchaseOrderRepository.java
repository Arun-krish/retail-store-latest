package com.retail.repository;

import com.retail.entity.PurchaseOrders;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PurchaseOrderRepository extends MongoRepository<PurchaseOrders, String> {
}
