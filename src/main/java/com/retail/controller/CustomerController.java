package com.retail.controller;

import com.retail.entity.Customers;
import com.retail.service.CustomerService;
import com.retail.util.ResponsePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping(value = "/saveCustomer")
    ResponseEntity<ResponsePojo> saveCustomer(@RequestBody Customers customers) {
        return new ResponseEntity<>(customerService.saveCustomer(customers), HttpStatus.OK);
    }
}
