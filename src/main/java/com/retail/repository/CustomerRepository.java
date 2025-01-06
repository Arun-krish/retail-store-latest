package com.retail.repository;

import com.retail.entity.Customers;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customers,String> {
}
