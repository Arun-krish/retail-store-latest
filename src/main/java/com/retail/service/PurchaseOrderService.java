package com.retail.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.retail.entity.Customers;
import com.retail.entity.PurchaseOrders;
import com.retail.exception.InputValidationException;
import com.retail.exception.OperationFailureException;
import com.retail.repository.CustomerRepository;
import com.retail.repository.PurchaseOrderRepository;
import com.retail.util.ApplicationConstants;
import com.retail.util.DateUtils;
import com.retail.util.ResponsePojo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PurchaseOrderService {

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    CustomerRepository customerRepository;



    /**
     * To save individual Purchase Order
     *
     * @param purchaseOrders
     * @return
     * @throws Exception
     */
    public ResponsePojo savePurchaseOrder(PurchaseOrders purchaseOrders) throws Exception {
        if (customerRepository.findById(purchaseOrders.getCustomerId()).isEmpty()) {
            throw new InputValidationException("Invalid Customer ID -" + purchaseOrders.getCustomerId());
        }
        try {
            calculateRewardsBasedOnPurchaseOrder(purchaseOrders);
            generateOrderNumber(purchaseOrders);
            purchaseOrders.setCreatedOn(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            purchaseOrderRepository.save(purchaseOrders);
            return new ResponsePojo(ApplicationConstants.SUCCESS, "Purchase Order Saved!", purchaseOrders);
        } catch (Exception e) {
            log.error("Failed to Save Purchase Order ", e);
            throw new OperationFailureException(e.getLocalizedMessage());
        }


    }

    /**
     * To process rewards based on bulk Purchase Orders
     *
     * @return
     * @throws Exception
     */
    public ResponsePojo bulkProcessPurchaseOrders() throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            TypeReference<List<PurchaseOrders>> typeReference = new TypeReference<>() {
            };
            InputStream inputStream = TypeReference.class.getResourceAsStream("/json/Retail_Purchase_Orders.json");
            List<PurchaseOrders> purchaseOrdersList = mapper.readValue(inputStream, typeReference);

            String invalidCustomerId = purchaseOrdersList.stream().map(PurchaseOrders::getCustomerId).filter(customerId -> {
                return customerRepository.findById(customerId).isEmpty();
            }).collect(Collectors.joining(","));
            if (StringUtils.hasLength(invalidCustomerId)) {
                throw new InputValidationException("Invalid Customer ID -" + invalidCustomerId);
            }
            purchaseOrdersList.forEach(order -> {
                generateOrderNumber(order);
                calculateRewardsBasedOnPurchaseOrder(order);
                order.setCreatedOn(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                purchaseOrderRepository.save(order);
            });

            return new ResponsePojo(ApplicationConstants.SUCCESS, "Purchase Order Saved!", purchaseOrdersList);
        } catch (InputValidationException e) {
            log.error("Invalid Input Received ", e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to Process Purchase Orders ", e);
            throw new OperationFailureException(e.getLocalizedMessage());
        }
    }


    /**
     * To generate Purchase Order number
     *
     * @param purchaseOrders
     */
    void generateOrderNumber(PurchaseOrders purchaseOrders) {
        int puchaseOrdersListSize = purchaseOrderRepository.findAll().size();
        purchaseOrders.setOrderId("O" + (puchaseOrdersListSize + 1));
    }



    /**
     * To fetch order history based on Customer Id and From date/To date/Last 3 months
     *
     * @param customerId
     * @param fromDate
     * @return
     * @throws Exception
     */
    public ResponsePojo fetchOrderHistory(String customerId, Date fromDate, Date toDate, boolean lastThreeMonths) throws Exception {
        Optional<Customers> customersOptional=customerRepository.findById(customerId);
        if (customersOptional.isEmpty()) {
            throw new InputValidationException("Invalid Customer ID -" + customerId);
        }
        List<PurchaseOrders> purchaseOrdersList;
        if (fromDate != null && toDate != null) {
            purchaseOrdersList = purchaseOrderRepository.findByCustomerIdAndOrderDateBetween(customerId, DateUtils.subractOneDayFromGivenDate(fromDate), DateUtils.addOneDayFromGivenDate(toDate));
        } else if (fromDate != null) {
            purchaseOrdersList = purchaseOrderRepository.findByCustomerIdAndOrderDateGreaterThanEqual(customerId, fromDate);
        } else if (lastThreeMonths) {
            purchaseOrdersList = purchaseOrderRepository.findByCustomerIdAndOrderDateGreaterThanEqual(customerId, DateUtils.fetchThreeMonthsBackDateFromCurrentDate());
        } else {
            throw new InputValidationException("Kindly Choose Time-Frame(From Date/To Date/Last 3 Months)");
        }
        try {

            if (purchaseOrdersList.isEmpty()) {
                return new ResponsePojo(ApplicationConstants.SUCCESS, "No Order Details Found!");
            } else {
                double totalOrderValue = purchaseOrdersList.stream().mapToDouble(PurchaseOrders::getOrderTotal).sum();
                double totalRewardsValue = purchaseOrdersList.stream().mapToDouble(PurchaseOrders::getTotalRewards).sum();
                Map<String, Object> responseMap = new LinkedHashMap<>();
                responseMap.put(ApplicationConstants.CUSTOMER_NAME, customersOptional.get().getName());
                responseMap.put(ApplicationConstants.CUSTOMER_MOBILE, customersOptional.get().getMobile());
                responseMap.put(ApplicationConstants.TOTAL_ORDERS, purchaseOrdersList.size());
                responseMap.put(ApplicationConstants.TOTAL_ORDERS_VALUE, totalOrderValue);
                responseMap.put(ApplicationConstants.TOTAL_REWARD_POINTS, totalRewardsValue);
                responseMap.put(ApplicationConstants.ORDERS, purchaseOrdersList);
                return new ResponsePojo(ApplicationConstants.SUCCESS, "Details Fetched Successfully!", responseMap);
            }
        } catch (Exception e) {
            log.error("Failed to Fetch Purchase Orders ", e);
            throw new OperationFailureException(e.getLocalizedMessage());
        }

    }


    /**
     * To calculate Reward points based on Purchase order total
     *
     * @param purchaseOrders
     */
    public void calculateRewardsBasedOnPurchaseOrder(PurchaseOrders purchaseOrders) {
        if (purchaseOrders.getOrderTotal() > 100) {
            double rewardPoints = (purchaseOrders.getOrderTotal() - 100) * 2;
            purchaseOrders.setTotalRewards(rewardPoints + 50);

        } else if (purchaseOrders.getOrderTotal() > 50 && purchaseOrders.getOrderTotal() <= 100) {
            purchaseOrders.setTotalRewards(purchaseOrders.getOrderTotal() - 50);
        }
    }
}
