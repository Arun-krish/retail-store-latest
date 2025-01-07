package com.retail.service;

import com.retail.entity.Customers;
import com.retail.exception.OperationFailureException;
import com.retail.repository.CustomerRepository;
import com.retail.util.ApplicationConstants;
import com.retail.util.ResponsePojo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    /**
     * To Save Customers
     * @param customer
     * @return
     * @throws Exception
     */
    public ResponsePojo saveCustomer(Customers customer) throws Exception {
        try {
            customer.setCreatedOn(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            customerRepository.save(customer);
            return new ResponsePojo(ApplicationConstants.SUCCESS, "Customer Saved Successfully!",customer);
        } catch (Exception ex) {
            log.error("Failed to Save Customer ", ex);
            throw new OperationFailureException(ex.getLocalizedMessage());
        }
    }
}
