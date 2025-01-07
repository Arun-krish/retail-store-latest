# Retail Store Reward Program
This application is used to generate Reward points for Purchase orders.

# Features
- Save Customers
- Save Purchase orders based on Customers
- Bulk upload Purchase orders
- Calculte Reward Points based on Purchase Orders
- Fetch Customer based Order history

# Tech Stack
- Java 17
- Spring Boot 3.4
- MongoDB 5.2

# API Details
- **To save Customer** -'http://localhost:8080/api/customer/saveCustomer' \
  --data '{
    "name":"AK1",
    "mobile":"9876543210"
- **To save Purchase Order** -'http://localhost:8080/api/purchaseOrder/savePurchaseOrder'
    --data '{
    "customerId": "677be0ef6ffe6034a2e95c63",
    "orderTotal": 100,
    "orderDate": "2025-01-01"
  }'  
- **To Bulk process Purchase Orders** -http://localhost:8080/api/purchaseOrder/bulkProcessPurchaseOrders
- **To Fetch Order history** --location 'http://localhost:8080/api/purchaseOrder/fetchOrderHistory' \
      --header 'customerId: 677be0ef6ffe6034a2e95c63' \
      --header 'fromDate: 10/01/2024' \
      --header 'toDate: 10/01/2024' \
      --header 'lastThreeMonths: true'
