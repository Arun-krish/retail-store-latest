package com.retail.service;

import com.retail.entity.PurchaseOrders;
import com.retail.exception.InputValidationException;
import com.retail.exception.OperationFailureException;
import com.retail.repository.CustomerRepository;
import com.retail.repository.PurchaseOrderRepository;
import com.retail.util.ApplicationConstants;
import com.retail.util.DateUtils;
import com.retail.util.ResponsePojo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Slf4j
public class PurchaseOrderService {

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    /**
     * To save individual Purchase Order
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
            purchaseOrderRepository.save(purchaseOrders);
            return new ResponsePojo(ApplicationConstants.SUCCESS, "Purchase Order Saved!",purchaseOrders);
        } catch (Exception e) {
            log.error("Failed to Save Purchase Order ", e);
            throw new OperationFailureException(e.getLocalizedMessage());
        }


    }

    /**
     * To capture Purchase order file and process rewards based on orders
     * @param file
     * @return
     * @throws Exception
     */
    public ResponsePojo bulkProcessPurchaseOrders(MultipartFile file) throws Exception {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            log.info("Total rows found {}", sheet.getPhysicalNumberOfRows());
            List<PurchaseOrders> purchaseOrdersList=new ArrayList<>();
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                PurchaseOrders purchaseOrders = new PurchaseOrders();
                XSSFRow row = sheet.getRow(i);
                XSSFCell cell = row.getCell(0);
                if (isEmptyCell(cell, ApplicationConstants.STRING)) {
                    throw new InputValidationException("Customer Id is Mandatory in Row no - " + i);
                }
                purchaseOrders.setCustomerId(cell.getStringCellValue());
                if (customerRepository.findById(purchaseOrders.getCustomerId()).isEmpty()) {
                    throw new InputValidationException("Invalid Customer ID -" + purchaseOrders.getCustomerId());
                }
                cell = row.getCell(1);
                if (isEmptyCell(cell,  ApplicationConstants.DATE)) {
                    throw new InputValidationException("Order Date is Mandatory in Row no - " + i);
                }
                purchaseOrders.setOrderDate(cell.getDateCellValue());

                cell = row.getCell(2);
                if (isEmptyCell(cell,  ApplicationConstants.NUMERIC)) {
                    throw new InputValidationException("Order Total is Mandatory in Row no - " + i);
                }
                purchaseOrders.setOrderTotal(cell.getNumericCellValue());
                calculateRewardsBasedOnPurchaseOrder(purchaseOrders);
                generateOrderNumber(purchaseOrders);
                purchaseOrderRepository.save(purchaseOrders);
                purchaseOrdersList.add(purchaseOrders);
            }
            log.info("Uploaded file has been Processed!");
            return new ResponsePojo(ApplicationConstants.SUCCESS, "Purchase Order Saved!",purchaseOrdersList);
        } catch (InputValidationException e) {
            log.error("Invalid Input Received ", e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to Upload Purchase Orders ", e);
            throw new OperationFailureException(e.getLocalizedMessage());
        }
    }

    /**
     * To generate Purchase Order number
     * @param purchaseOrders
     */
    void generateOrderNumber(PurchaseOrders purchaseOrders){
        int puchaseOrdersListSize=purchaseOrderRepository.findAll().size();
        purchaseOrders.setOrderId("O"+(puchaseOrdersListSize+1));
    }

    /**
     * To validate Empty cells in Excel
     * @param cell
     * @param cellType
     * @return
     */
    boolean isEmptyCell(XSSFCell cell, String cellType) {

        return (cell == null) ||
                (cell.getCellType() == CellType.STRING && !StringUtils.hasLength(cell.getStringCellValue().trim())) ||
                (cellType.equalsIgnoreCase( ApplicationConstants.DATE) && cell.getDateCellValue() == null);
    }

    /**
     * To fetch order history based on Customer Id and From date
     * @param customerId
     * @param fromDate
     * @return
     * @throws Exception
     */
    public ResponsePojo fetchOrderHistory(String customerId, Date fromDate,Date toDate,boolean lastThreeMonths) throws Exception {
        if (customerRepository.findById(customerId).isEmpty()) {
            throw new InputValidationException("Invalid Customer ID -" + customerId);
        }
        List<PurchaseOrders> purchaseOrdersList;
        if(fromDate != null && toDate !=null){
            purchaseOrdersList = purchaseOrderRepository.findByCustomerIdAndOrderDateBetween(customerId, DateUtils.subractOneDayFromGivenDate(fromDate), DateUtils.addOneDayFromGivenDate(toDate));
        }else if(fromDate != null){
            purchaseOrdersList = purchaseOrderRepository.findByCustomerIdAndOrderDateGreaterThanEqual(customerId,fromDate);
        }else if(lastThreeMonths){
            purchaseOrdersList = purchaseOrderRepository.findByCustomerIdAndOrderDateGreaterThanEqual(customerId, DateUtils.fetchThreeMonthsBackDateFromCurrentDate());
        }else{
            throw new InputValidationException("Kindly Choose Time-Frame(From Date/To Date/Last 3 Months)" );
        }
        try{

            if(purchaseOrdersList.isEmpty()){
                return new ResponsePojo( ApplicationConstants.SUCCESS, "No Order Details Found!");
            }else{
                double totalOrderValue = purchaseOrdersList.stream().mapToDouble(PurchaseOrders::getOrderTotal).sum();
                double totalRewardsValue = purchaseOrdersList.stream().mapToDouble(PurchaseOrders::getTotalRewards).sum();
                Map<String, Object> responseMap = new LinkedHashMap<>();
                responseMap.put(ApplicationConstants.TOTAL_ORDERS, purchaseOrdersList.size());
                responseMap.put(ApplicationConstants.TOTAL_ORDERS_VALUE, totalOrderValue);
                responseMap.put(ApplicationConstants.TOTAL_REWARD_POINTS, totalRewardsValue);
                responseMap.put(ApplicationConstants.ORDERS, purchaseOrdersList);
                return new ResponsePojo(ApplicationConstants.SUCCESS, "Details Fetched Successfully!", responseMap);
            }
        }catch (Exception e){
            log.error("Failed to Fetch Purchase Orders ", e);
            throw new OperationFailureException(e.getLocalizedMessage());
        }

    }





    /**
     * To calculate Reward points based on Purchase order total
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
