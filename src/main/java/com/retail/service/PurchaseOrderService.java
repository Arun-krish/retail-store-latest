package com.retail.service;

import com.retail.entity.PurchaseOrders;
import com.retail.repository.PurchaseOrderRepository;
import com.retail.util.ResponsePojo;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

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

    public ResponsePojo savePurchaseOrder(PurchaseOrders purchaseOrders) {
        try {
            calculateRewardsBasedOnPurchaseOrder(purchaseOrders);
            purchaseOrderRepository.save(purchaseOrders);
            return new ResponsePojo("SUCCESS","Puchase Order Saved!");
        } catch (Exception e) {
            return new ResponsePojo("FAILURE","Failed to Save Puchase Order");

        }
    }

    public ResponsePojo bulkProcessPurchaseOrders() {
        try {

            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classloader.getResourceAsStream("Retail_Purchase_Orders.xlsx");
            XSSFWorkbook workbook=new XSSFWorkbook(inputStream);

            XSSFSheet sheet=workbook.getSheetAt(0);
            System.out.println(sheet.getPhysicalNumberOfRows());
            for(int i=1;i<sheet.getPhysicalNumberOfRows();i++) {
                PurchaseOrders purchaseOrders=new PurchaseOrders();
                XSSFCell cell=sheet.getRow(i).getCell(0);
                purchaseOrders.setCustomerId(cell.getStringCellValue());
                cell=sheet.getRow(i).getCell(1);
                purchaseOrders.setOrderId(cell.getStringCellValue());
                cell=sheet.getRow(i).getCell(2);
                purchaseOrders.setOrderDate(cell.getDateCellValue());
                cell=sheet.getRow(i).getCell(3);
                purchaseOrders.setOrderTotal(cell.getNumericCellValue());
                calculateRewardsBasedOnPurchaseOrder(purchaseOrders);
                purchaseOrderRepository.save(purchaseOrders);
            }

            return new ResponsePojo("SUCCESS","Puchase Order Saved!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponsePojo("FAILURE","Failed to Save Puchase Order");

        }
    }

    public ResponsePojo fetchOrderHistory(String customerId, Date fromDate){

        List<PurchaseOrders> purchaseOrdersList=purchaseOrderRepository.findByCustomerIdAndOrderDateGreaterThanEqual(customerId, fromDate);
        double totalOrderValue=purchaseOrdersList.stream().mapToDouble(PurchaseOrders::getOrderTotal).sum();
        double totalRewardsValue=purchaseOrdersList.stream().mapToDouble(PurchaseOrders::getTotalRewards).sum();
        Map<String,Object> responseMap=new HashMap<>();
        responseMap.put("Total Orders",purchaseOrdersList.size());
        responseMap.put("Total Order Value",totalOrderValue);
        responseMap.put("Total Reward Points",totalRewardsValue);
        responseMap.put("Orders",purchaseOrdersList);
        ResponsePojo response= new ResponsePojo("SUCCESS","Puchase Order Saved!",responseMap);
        return response;
    }

    public void calculateRewardsBasedOnPurchaseOrder(PurchaseOrders purchaseOrders) {
        if (purchaseOrders.getOrderTotal() > 100) {
            double rewardPoints = (purchaseOrders.getOrderTotal() - 100) * 2;
            purchaseOrders.setTotalRewards(rewardPoints + 50);

        } else if (purchaseOrders.getOrderTotal() >= 50 && purchaseOrders.getOrderTotal() <= 100) {
            purchaseOrders.setTotalRewards(purchaseOrders.getOrderTotal()-50);

        }
    }
}
