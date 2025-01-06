package com.retail.service;

import com.retail.entity.Customers;
import com.retail.repository.CustomerRepository;
import com.retail.util.ResponsePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;


    public ResponsePojo saveCustomer(Customers customer) {
        try {
            customerRepository.save(customer);
            return new ResponsePojo("SUCCESS","Customer Saved!");
        } catch (Exception e) {
            return new ResponsePojo("FAILURE","Failed to Save Customer");

        }
    }
}
