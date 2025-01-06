package com.retail.service;

import com.retail.entity.PurchaseOrders;
import com.retail.exception.InputValidationException;
import com.retail.exception.OperationFailureException;
import com.retail.repository.CustomerRepository;
import com.retail.repository.PurchaseOrderRepository;
import com.retail.util.ResponsePojo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class PurchaseOrderService {

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    public ResponsePojo savePurchaseOrder(PurchaseOrders purchaseOrders) throws Exception {
        if (customerRepository.findById(purchaseOrders.getCustomerId()).isEmpty()) {
            throw new InputValidationException("Customer Not Found with id -"+purchaseOrders.getCustomerId());
        }
        try{
            calculateRewardsBasedOnPurchaseOrder(purchaseOrders);
            purchaseOrderRepository.save(purchaseOrders);
            return new ResponsePojo("SUCCESS", "Puchase Order Saved!");
        }catch (Exception e){
            throw new OperationFailureException(e.getLocalizedMessage());
        }



    }

    public ResponsePojo bulkProcessPurchaseOrders(MultipartFile file) throws Exception{
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                PurchaseOrders purchaseOrders = new PurchaseOrders();
                XSSFRow row= sheet.getRow(i);
                XSSFCell cell =row.getCell(0);
                if(cell==null || cell.getStringCellValue()==null){
                    throw new InputValidationException("Customer Id is Mandatory in Line no - "+i);
                }
                purchaseOrders.setCustomerId(cell.getStringCellValue());
                if (customerRepository.findById(purchaseOrders.getCustomerId()).isEmpty()) {
                    throw new InputValidationException("Customer Not Found with id -"+purchaseOrders.getCustomerId());
                }
                cell = row.getCell(1);
                purchaseOrders.setOrderId(cell.getStringCellValue());
                if(cell==null || cell.getStringCellValue()==null){
                    throw new InputValidationException("Order Id is Mandatory in Line no - "+i);
                }
                cell = row.getCell(2);
                purchaseOrders.setOrderDate(cell.getDateCellValue());
                if(cell==null || cell.getDateCellValue()==null){
                    throw new InputValidationException("Order Date is Mandatory in Line no - "+i);
                }
                cell = row.getCell(3);
                purchaseOrders.setOrderTotal(cell.getNumericCellValue());
                calculateRewardsBasedOnPurchaseOrder(purchaseOrders);
                purchaseOrderRepository.save(purchaseOrders);
            }

            return new ResponsePojo("SUCCESS", "Puchase Order Saved!");
        } catch (InputValidationException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationFailureException(e.getLocalizedMessage());
        }
    }

    public ResponsePojo fetchOrderHistory(String customerId, Date fromDate) {

        List<PurchaseOrders> purchaseOrdersList = purchaseOrderRepository.findByCustomerIdAndOrderDateGreaterThanEqual(customerId, fromDate);
        double totalOrderValue = purchaseOrdersList.stream().mapToDouble(PurchaseOrders::getOrderTotal).sum();
        double totalRewardsValue = purchaseOrdersList.stream().mapToDouble(PurchaseOrders::getTotalRewards).sum();
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("Total Orders", purchaseOrdersList.size());
        responseMap.put("Total Order Value", totalOrderValue);
        responseMap.put("Total Reward Points", totalRewardsValue);
        responseMap.put("Orders", purchaseOrdersList);
        ResponsePojo response = new ResponsePojo("SUCCESS", "Puchase Order Saved!", responseMap);
        return response;
    }

    public void calculateRewardsBasedOnPurchaseOrder(PurchaseOrders purchaseOrders) {
        if (purchaseOrders.getOrderTotal() > 100) {
            double rewardPoints = (purchaseOrders.getOrderTotal() - 100) * 2;
            purchaseOrders.setTotalRewards(rewardPoints + 50);

        } else if (purchaseOrders.getOrderTotal() >= 50 && purchaseOrders.getOrderTotal() <= 100) {
            purchaseOrders.setTotalRewards(purchaseOrders.getOrderTotal() - 50);

        }
    }
}
