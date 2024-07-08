import java.sql.*;
import java.util.*;



public class main {
    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getConnection();
        seller slr = new seller();
        ScannerClass sc = new ScannerClass();
        if (connection != null) {
            while (true) {
                System.out.println("Select any option Below : \n1. Enter as Seller/Admin\n2. Enter as Customer\n3. Exit\n");
                int a = sc.readInt();
                if (a == 1) {
                    while (true) {
                        System.out.println("Choose : \n1. Apply for Seller\n2. Enter as Seller\n3. Exit\n");
                        int a_1 = sc.readInt();
                        if (a_1 == 1) {
                            System.out.println("Under Development");
                        } else if (a_1 == 2) {
                            slr.adminLogin(connection, sc);
                        } else if (a_1 == 3) {
                            break;
                        } else {
                            System.out.println("Select a valid option");
                        }
                    }
                } else if (a == 2) {
                    while (true){
                        System.out.println("Select Option : \n1. Login\n2. SignUp\n3. Back\n");
                        int a_1 = sc.readInt();
                        if (a_1 == 1) {
                            login(connection, sc);
                        } else if (a_1 == 2) {
                            signUp(connection, sc);
                        } else if (a_1 == 3) {
                            System.out.println("Back");
                            break;
                        } else {
                            System.out.println("Choose a valid option!!");
                        }
                    }
                }
                else if (a == 3) {
                    System.out.println("Exit");
                    return;
                } else {
                    System.out.println("Choose a valid option!!");
                }
            }
        } else {
            System.out.println("Database Connection Failed!");
        }
    }

    private static void signUp(Connection connection, ScannerClass sc) {
        sc.readString();
        System.out.println("Enter First Name:");
        String fname = sc.readString();
        System.out.println("Enter Middle Name (Press Enter to skip):");
        String mname = sc.readString();
        if (mname.trim().isEmpty()) { // Check if middle name is empty
            mname = null;
        }
        System.out.println("Enter Last Name:");
        String lname = sc.readString();
        System.out.println("Enter Date of Birth (YYYY-MM-DD):");
        String dob = sc.readString();
        System.out.println("Enter Street Number:");
        String streetNo = sc.readString();
        System.out.println("Enter Landmark:");
        String landmark = sc.readString();
        System.out.println("Enter City:");
        String city = sc.readString();
        System.out.println("Enter State:");
        String state = sc.readString();
        System.out.println("Enter Pincode:");
        String pincode = sc.readString();
        System.out.println("Enter Email:");
        String email = sc.readString();
        System.out.println("Enter Password:");
        String password = sc.readString();

        try {
            // Disable auto-commit mode
//            connection.setAutoCommit(false);
//            // Begin the transaction
//            Statement stmt = connection.createStatement();
//            stmt.executeUpdate("START TRANSACTION");

            //get the next available CustomerID from the database
            String getIdQuery = "SELECT MAX(CustomerID) FROM Customer";
            PreparedStatement getIdStatement = connection.prepareStatement(getIdQuery);
            ResultSet resultSet = getIdStatement.executeQuery();
            int nextId = 1; // Default value if no rows returned
            if (resultSet.next()) {
                nextId = resultSet.getInt(1) + 1; // Increment the max id by 1
            }

            //inserting data into Customer table
            String insertCustomerQuery = "INSERT INTO Customer (CustomerID, fname, mname, lname, CustomerDOB, streetNo, landmark, city, state, pincode, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertCustomerStatement = connection.prepareStatement(insertCustomerQuery);
            insertCustomerStatement.setInt(1, nextId);
            insertCustomerStatement.setString(2, fname);
            insertCustomerStatement.setString(3, mname); // If middle name is empty, null will be passed
            insertCustomerStatement.setString(4, lname);
            insertCustomerStatement.setString(5, dob);
            insertCustomerStatement.setString(6, streetNo);
            insertCustomerStatement.setString(7, landmark);
            insertCustomerStatement.setString(8, city);
            insertCustomerStatement.setString(9, state);
            insertCustomerStatement.setString(10, pincode);
            insertCustomerStatement.setString(11, email);

            int rowsAffected = insertCustomerStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Customer details inserted successfully!");
                createCartForCustomer(connection, nextId);
            } else {
                System.out.println("Customer details insertion failed!");
            }
            insertCustomerStatement.close();

            // Commit the transaction
//            stmt.executeUpdate("COMMIT");

            //get the CustomerID of the inserted customer
            int customerId = nextId;

            //inserting data into CustomerPassword table along with CustomerID
            String insertPasswordQuery = "INSERT INTO CustomerPassword (CustomerID, email, password) VALUES (?, ?, ?)";
            PreparedStatement insertPasswordStatement = connection.prepareStatement(insertPasswordQuery);
            insertPasswordStatement.setInt(1, customerId);
            insertPasswordStatement.setString(2, email);
            insertPasswordStatement.setString(3, password);

            rowsAffected = insertPasswordStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Password inserted successfully!");
            } else {
                System.out.println("Password insertion failed!");
            }
            insertPasswordStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
//            try {
//                // Rollback the transaction if an exception occurs
//                connection.rollback();
//            } catch (SQLException rollbackException) {
//                rollbackException.printStackTrace();
//            }
        }
//        finally {
//            try {
//                // Enable auto-commit mode
//                connection.setAutoCommit(true);
//            } catch (SQLException autoCommitException) {
//                autoCommitException.printStackTrace();
//            }
//        }
    }

    
    private static void createCartForCustomer(Connection connection, int customerID) {
        try {
            //inserting data into Cart table
            String insertCartQuery = "INSERT INTO Cart (cartID, customerID, productQty, TotalCost) VALUES (?, ?, 0, 0)";
            PreparedStatement insertCartStatement = connection.prepareStatement(insertCartQuery);
            insertCartStatement.setInt(1, customerID); // Set the cartID as customerID
            insertCartStatement.setInt(2, customerID); // Set the customerID
            int rowsAffected = insertCartStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cart created successfully for CustomerID: " + customerID);
            } else {
                System.out.println("Cart creation failed for CustomerID: " + customerID);
            }
            insertCartStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void login(Connection connection, ScannerClass sc) {
        int attempts = 0;
        int MAX_LOGIN_ATTEMPTS = 3;
        while (attempts < MAX_LOGIN_ATTEMPTS) {
            sc.readString();
            System.out.println("Enter Email:");
            String email = sc.readString();
            System.out.println("Enter Password:");
            String password = sc.readString();
            try {
                String loginQuery = "SELECT c.CustomerID, c.fname, c.pincode FROM Customer c INNER JOIN CustomerPassword cp ON c.CustomerID = cp.CustomerID WHERE cp.email = ? AND cp.password = ?";
                PreparedStatement loginStatement = connection.prepareStatement(loginQuery);
                loginStatement.setString(1, email);
                loginStatement.setString(2, password);
                ResultSet resultSet = loginStatement.executeQuery();

                if (resultSet.next()) {
                    int custID = resultSet.getInt("CustomerID");
                    String customerName = resultSet.getString("fname");
                    int pincode = resultSet.getInt("pincode");
                    System.out.println("Welcome, " + customerName + "! Login successful!" + custID);
                    after_login(connection, sc, custID, pincode);
//                    matchUserWarehousePincode(connection);
//                    return;
                } else {
                    System.out.println("Invalid email or password. Please try again.");
                    attempts++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Maximum login attempts reached. Exiting...");
    }

    private static List<Integer> availableWarehousesForProduct = new ArrayList<>();

    private static void getWarehousesForProduct(Connection connection, int productID) {
        availableWarehousesForProduct.clear(); // Clear previous warehouse IDs
        try {
            // Query to retrieve warehouse IDs where the product is available
            String query = "SELECT DISTINCT WareHouseID FROM stores WHERE ProductID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productID);
            ResultSet resultSet = statement.executeQuery();

            // Add warehouse IDs to the global list
            while (resultSet.next()) {
                int warehouseID = resultSet.getInt("WareHouseID");
                availableWarehousesForProduct.add(warehouseID);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printAvailableWarehouses() {
        System.out.println("Available warehouses for the product:");
        for (int warehouseID : availableWarehousesForProduct) {
            System.out.println(warehouseID);
        }
    }

    private static void after_login(Connection connection, ScannerClass sc, int custID, int pincode) throws SQLException {
        while (true) {
            System.out.println("Explore : \n1. Product Category\n2. Products\n3. Cart\n4. View Past 5 Orders\n5. Profile\n6. Add Contact Details\n7. Logout");
            int a_2 = sc.readInt();
            if (a_2 == 1) {
                try {
                    List<Integer> productcatIDs = new ArrayList<>();
                    displayProductCategories(connection, productcatIDs);
                    System.out.println("******************************************************************");
                    while (true) {
                        System.out.println("For Visit Choose above product cat ID else 0 for exit: ");
                        int productCatID = sc.readInt();
                        if (productcatIDs.contains(productCatID)) {
                            System.out.println("You chose to visit Product Category " + productCatID);
                            List<Integer> productIDs = new ArrayList<>();
                            displayProductsByCategory(connection, productCatID, productIDs);
                            System.out.println("*********************\n*************************************************");
                            while (true) {
                                System.out.println("If You Want to Add Any Product To Cart just Enter the productID from above else enter '0':");
                                int bb = sc.readInt();
                                if (productIDs.contains(bb)) {
                                    getWarehousesForProduct(connection, bb);
                                    printAvailableWarehouses();
                                    //                            printMatchingWarehouseIDs(connection);
                                    System.out.println("Enter WareHouseID from where u want to buy : ");
                                    int bc = sc.readInt();
                                    System.out.println("Enter the quantity:");
                                    int quantity = sc.readInt();
                                    int stqty = isProductAvailableInStores(connection, bb, bc);
                                    if (stqty >= quantity) {
                                        addsTo(connection, custID, bb, quantity, bc);
                                        updateStoresStock(connection, bb, quantity, bc);
                                    } else if (stqty == 0) {
                                        System.out.println("Product is out of stock");
                                    } else {
                                        System.out.println("Please Check the available Product Qty or enter less Qty");
                                    }
                                } else if (bb == 0) {
                                    break;
                                } else {
                                    System.out.println("Enter Valid ID plz.");
                                }
                            }
                        } else if (productCatID == 0) {
                            break;
                        } else {
                            System.out.println("Invalid Product Category ID.");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (a_2 == 2) {
                while (true) {
                    System.out.println("Explore : \n1. Search Product\n2. See All Products\n3. Back ");
                    int a_3 = sc.readInt();
                    if (a_3 == 1) {
                        sc.readString();
                        System.out.println("Enter the keyword to search for:");
                        String keyword = sc.readString();
                        try {
                            List<Integer> S_productIDs = new ArrayList<>();
                            System.out.println("**********************************************************************");
                            searchProductByName(connection, keyword, S_productIDs);
                            System.out.println("**********************************************************************");
                            System.out.println("If You Want to Add Any Product To Cart just Enter the productID from above :");
                            int bb_1 = sc.readInt();
                            if (S_productIDs.contains(bb_1)) {
                                getWarehousesForProduct(connection, bb_1);
                                printAvailableWarehouses();
                                System.out.println("Enter WareHouseID from where u want to buy : ");
                                int bc = sc.readInt();

                                System.out.println("Enter the quantity:");
                                int quantity = sc.readInt();
                                int stqty = isProductAvailableInStores(connection, bb_1, bc);
                                if (stqty >= quantity) {
                                    addsTo(connection, custID, bb_1, quantity, bc);
                                    updateStoresStock(connection, bb_1, quantity, bc);

                                } else if (stqty == 0) {
                                    System.out.println("Product is out of stock");
                                } else {
                                    System.out.println("Please Check the available Product Qty or enter less Qty");
                                }

                            } else {
                                System.out.println("Enter Valid ID plz.");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else if (a_3 == 2) {
                        try {
                            List<Integer> A_productIDs = new ArrayList<>();
                            System.out.println("**********************************************************************");
                            displayAllProducts(connection, A_productIDs);
                            System.out.println("**********************************************************************");
                            System.out.println("If You Want to Add Any Product To Cart just Enter the productID from above :");
                            int bb_2 = sc.readInt();
                            if (A_productIDs.contains(bb_2)) {
                                getWarehousesForProduct(connection, bb_2);
                                printAvailableWarehouses();
                                System.out.println("Enter WareHouseID from where u want to buy : ");
                                int bc = sc.readInt();

                                System.out.println("Enter the quantity:");
                                int quantity = sc.readInt();
                                int stqty = isProductAvailableInStores(connection, bb_2, bc);
                                if (stqty >= quantity) {
                                    addsTo(connection, custID, bb_2, quantity, bc);
                                    updateStoresStock(connection, bb_2, quantity, bc);
                                } else if (stqty == 0) {
                                    System.out.println("Product is out of stock");
                                } else {
                                    System.out.println("Please Check the available Product Qty or enter less Qty");
                                }

                            } else {
                                System.out.println("Enter Valid ID plz.");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else if (a_3 == 3) {
                        break;
                    } else {
                        System.out.println("Invalid Input, Choose correct one!!!");
                    }
                }
            } else if (a_2 == 3) {
                System.out.println("*************************************************");
                printAddsToData(connection, custID);
                System.out.println("*************************************************");
                while (true) {
                    System.out.println("Choose : \n1. Remove Product\n2. Proceed to Checkout(Order)\n3. Back");
                    int ac = sc.readInt();
                    if (ac == 1) {
                        removeProduct(connection, sc, custID);
                    } else if (ac == 2) {
                        System.out.println("Confirm your payments:");
                        insertPayment(connection, sc, custID);
                    } else if (ac == 3) {
                        break;
                    } else {
                        System.out.println("Invalid Input!!");
                    }
                }
            } else if (a_2 == 4) {
                printLast5Orders(connection, custID);
            } else if (a_2 == 5) {
                //under Development
                System.out.println("Under Development");
            } else if (a_2 == 6) {
                //under development
                System.out.println("Under Development");
            } else if (a_2 == 7) {
                break;
            } else {
                System.out.println("Enter Valid Input again!!");
            }
        }
    }

    public static void printLast5Orders(Connection connection, int custID) {
        String query = "SELECT OrderID FROM Orders WHERE CartID IN (SELECT CartID FROM Cart WHERE CustomerID = ?) ORDER BY OrderID DESC LIMIT 5";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, custID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int orderID = resultSet.getInt("OrderID");
                System.out.println("Order ID: " + orderID);
                printProductsForOrder(connection, orderID);
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printProductsForOrder(Connection connection, int orderID) {
        String query = "SELECT ProductID, ProductQty, ProductPrice FROM OrderList WHERE OrderID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int productID = resultSet.getInt("ProductID");
                int productQty = resultSet.getInt("ProductQty");
                double productPrice = resultSet.getDouble("ProductPrice");
                System.out.println("Product ID: " + productID + ", Quantity: " + productQty + ", Price: " + productPrice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    private static int generateOrderID(Connection connection) throws SQLException {
        int orderID = 0;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT MAX(OrderID) AS maxOrderID FROM Orders");
            if (resultSet.next()) {
                orderID = resultSet.getInt("maxOrderID") + 1;
            }
        }
        return orderID;
    }
    
    private static void insertOrder(Connection connection, ScannerClass sc, int custID, int paymentID, String paymentMethod) throws SQLException {
        //retrieving orderId using custID(custID>cartID>totalCartAmt cartID>orderID)
        int cartID = getCartID(connection, custID);
        double totalAmt = calculateTotalAmt(connection, cartID);
        int orderID = generateOrderID(connection);
        String query = "INSERT INTO Orders (OrderID,  totalPayableAmt, cartID, paymentID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderID);
            statement.setDouble(2, totalAmt);
            statement.setInt(3, cartID);
            statement.setInt(4, paymentID);
            statement.executeUpdate();
            System.out.println("Order inserted successfully!");
//            insertOrderList(connection,orderID,cartID);
            updateOrderStatus(connection, orderID, paymentMethod);
            clearAddsTo(connection, cartID);
            clearCart(connection, cartID);
        } catch (SQLException e) {
            System.err.println("Error inserting order: " + e.getMessage());
        }
    }

    private static double calculateTotalAmt(Connection connection, int cartID) throws SQLException {
        double totalAmt = 0.0;
        String query = "SELECT TotalCost FROM cart where cartID=? ";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, cartID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalAmt = resultSet.getDouble("TotalCost");
            }
        } catch (SQLException e) {
            System.err.println("Error calculating total amount: " + e.getMessage());
        }
        return totalAmt;
    }
    
    private static int generatePaymentID(Connection connection) {
        int startingPaymentID = 100;
        int generatedPaymentID = startingPaymentID;
        try {
            String query = "SELECT MAX(paymentID) AS maxPaymentID FROM payment";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int maxPaymentID = resultSet.getInt("maxPaymentID");
                generatedPaymentID = Math.max(maxPaymentID + 1, startingPaymentID);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedPaymentID;
    }

    private static void insertPayment(Connection connection, ScannerClass sc, int customerID) {
        try {
            sc.readString();
            System.out.println("Enter payment method (Cash On Delivery, Net Banking, UPI, EMI):");
            String paymentMethod = sc.readString();
            Timestamp paymentDateAndTime = new Timestamp(System.currentTimeMillis());
            int paymentID = generatePaymentID(connection);

            // Begin transaction
            connection.setAutoCommit(false); // Disable auto-commit mode
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); // Set isolation level

            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("START TRANSACTION"); // Start the transaction

                if (!isCartEmpty(connection, customerID)) { // Check if the cart is not empty
                    String query = "INSERT INTO payment (paymentID, paymentMethod, paymentDateTime) VALUES (?, ?, ?)";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setInt(1, paymentID);
                        statement.setString(2, paymentMethod);
                        statement.setTimestamp(3, paymentDateAndTime);
                        int rowsAffected = statement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Payment details inserted successfully!");
                            insertOrder(connection, sc, customerID, paymentID, paymentMethod);
                            System.out.println("Generated PaymentID: " + paymentID);
                            stmt.executeUpdate("COMMIT"); // Commit the transaction
                        } else {
                            System.out.println("Failed to insert payment details!");
                            stmt.executeUpdate("ROLLBACK"); // Rollback the transaction
                        }
                    }
                } else {
                    System.out.println("Your cart is empty. Please add products before proceeding with the payment.");
                    stmt.executeUpdate("ROLLBACK"); // Rollback the transaction
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback(); // Rollback the transaction if an exception occurs
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true); // Enable auto-commit mode
            } catch (SQLException autoCommitException) {
                autoCommitException.printStackTrace();
            }
        }
    }

    private static boolean isCartEmpty(Connection connection, int customerID) throws SQLException {
        String query = "SELECT COUNT(*) FROM addsTo WHERE cartID IN (SELECT cartID FROM cart WHERE CustomerID = ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, customerID);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        resultSet.close();
        statement.close();
        return count == 0;
    }


    // the orderStatus is not working correctly but it's more for this submission
    //#underDevelopment
    public static void updateOrderStatus(Connection connection, int orderID, String paymentMethod) throws SQLException {
        String orderStatus = determineOrderStatus(paymentMethod);
        updateOrder(connection, orderID, orderStatus);
        if (shouldScheduleEvent(orderStatus)) {
            scheduleEvent(connection, orderID);
        }
    }

    
    private static String determineOrderStatus(String paymentMethod) {
        switch (paymentMethod) {
            case "Card":
            case "UPI":
            case "Net Banking":
                return "Confirmed";
            case "Cash On Delivery":
                return "On the way";
            default:
                return "Processing";
        }
    }

    private static boolean shouldScheduleEvent(String orderStatus) {
        return orderStatus.equals("Confirmed") || orderStatus.equals("On the way") || orderStatus.equals("Processing");
    }


    
    private static void updateOrder(Connection connection, int orderID, String orderStatus) throws SQLException {
        String query = "UPDATE Orders SET orderStatus = ? WHERE OrderID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, orderStatus);
            statement.setInt(2, orderID);
            statement.executeUpdate();
        }
    }

    private static void scheduleEvent(Connection connection, int orderID) throws SQLException {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Timestamp deliveryTime = new Timestamp(currentTime.getTime() + (60 * 1000));
        String query = "UPDATE Orders SET orderStatus = 'Delivered' WHERE OrderID = ? AND orderStatus IN ('Confirmed', 'On the way', 'Processing') AND orderTiming <= ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderID);
            statement.setTimestamp(2, deliveryTime);
            statement.executeUpdate();
        }
    }


    // conflicting transaction updating cart details after successfull transactions.

    public static void clearCart(Connection connection, int cartID) throws SQLException {
        //Disable auto-commit mode to start the transaction
        connection.setAutoCommit(false);

        try {
            //Begin the transaction explicitly
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("START TRANSACTION");

            //Execute the database operations (clearing the addsTo table and deleting the cart)
            clearAddsTo(connection, cartID);
            deleteCart(connection, cartID);

            //If no exceptions occurred, commit the transaction
            connection.commit();
        } catch (SQLException e) {
            //Log the exception or handle it as needed
            e.printStackTrace();

            //Rollback the transaction
            connection.rollback();

            //Re-throw the exception to the caller
            throw e;
        } finally {
            //Enable auto-commit mode to return to default behavior
            connection.setAutoCommit(true);
        }
    }


    
    private static void clearAddsTo(Connection connection, int cartID) throws SQLException {
            String deleteQuery = "DELETE FROM addsTo WHERE cartID = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
                statement.setInt(1, cartID);
                statement.executeUpdate();
            }
        }
    
   private static void deleteCart(Connection connection, int cartID) throws SQLException {
            String updateQuery = "UPDATE cart SET ProductQty = 0, TotalCost = 0 WHERE cartID = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
                statement.setInt(1, cartID);
                statement.executeUpdate();
            }
        }

    private static int isProductAvailableInStores(Connection connection, int productID, int warehouseID) {
        int stQty = 0;
        try {
            String query = "SELECT SUM(StockQty) AS totalStock FROM stores WHERE ProductID = ? AND WarehouseID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productID);
            statement.setInt(2, warehouseID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int stockQuantity = resultSet.getInt("totalStock");
                stQty = stockQuantity;
            } else {
                System.out.println("Product not found in the specified warehouse.");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stQty;
    }

    private static void updateStoresStock(Connection connection, int productID, int quantity, int warehouseID) {
        try {
            String updateQuery = "UPDATE stores SET StockQty = StockQty - ? WHERE ProductID = ? AND WarehouseID = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setInt(1, quantity);
            updateStatement.setInt(2, productID);
            updateStatement.setInt(3, warehouseID);
            updateStatement.executeUpdate();
            updateStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    private static void addsTo(Connection connection, int custID, int productID, int quantity, int warehouseID) {
        try {
            int cartID = getCartID(connection, custID);
            String query = "INSERT INTO addsTo (cartID, productID, productQty, warehouseID) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, cartID);
            statement.setInt(2, productID);
            statement.setInt(3, quantity);
            statement.setInt(4, warehouseID);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product added to cart successfully!");
            } else {
                System.out.println("Failed to add product to cart!");
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getCartID(Connection connection, int custID) {
        int cartID = -1;
        try {
            String query = "SELECT CartID FROM cart WHERE customerID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, custID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                cartID = resultSet.getInt("CartID");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartID;
    }

    private static void searchProductByName(Connection connection, String keyword, List<Integer> S_productIDs) throws SQLException {
        String query = "SELECT * FROM product WHERE ProductName LIKE ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, "%" + keyword + "%");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int productID = resultSet.getInt("ProductID");
            S_productIDs.add(productID);
            String productName = resultSet.getString("ProductName");
            double productPrice = resultSet.getDouble("ProductPrice");
            String productDescription = resultSet.getString("ProductDescription");
            String brandName = resultSet.getString("Brandname");
//            int productCatID = resultSet.getInt("ProductCatID");
            System.out.printf("ProductID: %d  | Product Name: %s  | Price: %.2f | Description: %s | Brand: %s |%n",
                    productID, productName, productPrice, productDescription, brandName);
//            System.out.printf("ProductID: %d | Product Name: %s | Price: %.2f | Description: %s | Brand: %s | CategoryID: %d |%n",
//                    productID, productName, productPrice, productDescription, brandName, productCatID);
        }
        statement.close();
    }
    private static void displayProductCategories(Connection connection, List<Integer> productcatIDs) {
        try {
            String query = "SELECT * FROM ProductCategory";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            System.out.println("Product Categories:");
            System.out.println("Product Category ID | Product Category Name | Product Category Description");
            System.out.println("--------------------------------------------------------------------------");
            while (resultSet.next()) {
                int categoryID = resultSet.getInt("ProductCatID");
                productcatIDs.add(categoryID);
                String categoryName = resultSet.getString("ProductCatName");
                String categoryDes = resultSet.getString("ProductCatDescription");
//                System.out.println(categoryID + ". " + categoryName);
                System.out.printf("%-20s  | %-20s  | %-30s%n", categoryID, categoryName, categoryDes);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayProductsByCategory(Connection connection, int productCatID, List<Integer> productIDs) throws SQLException {
        String query = "SELECT * FROM Product WHERE ProductCatID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, productCatID);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int productID = resultSet.getInt("ProductID");
            productIDs.add(productID);
            String productName = resultSet.getString("ProductName");
            double productPrice = resultSet.getDouble("ProductPrice");
            String productDescription = resultSet.getString("ProductDescription");
            String brandName = resultSet.getString("Brandname");
            System.out.println("Product ID: " + productID);
            System.out.println("Product Name: " + productName);
            System.out.println("Product Price: " + productPrice);
            System.out.println("Product Description: " + productDescription);
            System.out.println("Brand Name: " + brandName);
            System.out.println("---------------------");
        }
        resultSet.close();
        statement.close();
    }

    private static void displayAllProducts(Connection connection, List<Integer> A_productIDs) throws SQLException {
        String query = "SELECT * FROM product";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int productID = resultSet.getInt("ProductID");
            A_productIDs.add(productID);
            String productName = resultSet.getString("ProductName");
            double productPrice = resultSet.getDouble("ProductPrice");
            String productDescription = resultSet.getString("ProductDescription");
            String brandName = resultSet.getString("Brandname");
//            int productCatID = resultSet.getInt("ProductCatID");
            System.out.printf("ProductID: %d  | Product Name: %s  | Price: %.2f | Description: %s | Brand: %s |%n",
                    productID, productName, productPrice, productDescription, brandName);
//            System.out.printf("ProductID: %d | Product Name: %s | Price: %.2f | Description: %s | Brand: %s | CategoryID: %d |%n",
//                    productID, productName, productPrice, productDescription, brandName, productCatID);
        }
        statement.close();
    }

    private static void printAddsToData(Connection connection, int customerID) throws SQLException {
        String addsToQuery = "SELECT a.productID, p.productname, p.ProductPrice, a.productQty " +
                "FROM addsTo a " +
                "INNER JOIN product p ON a.productID = p.productID " +
                "WHERE a.cartID IN (SELECT cartID FROM cart WHERE CustomerID = ?)";
        PreparedStatement addsToStatement = connection.prepareStatement(addsToQuery);
        addsToStatement.setInt(1, customerID);
        ResultSet addsToResultSet = addsToStatement.executeQuery();
        System.out.println("Cart data for Customer ID: " + customerID);
        while (addsToResultSet.next()) {
            int productID = addsToResultSet.getInt("productID");
            String productName = addsToResultSet.getString("productname");
            double productPrice = addsToResultSet.getDouble("ProductPrice");
            int productQty = addsToResultSet.getInt("productQty");
            System.out.printf("ProductID: %d, ProductName: %s, ProductPrice: %.2f, ProductQty: %d%n",
                    productID, productName, productPrice, productQty);
        }
        String cartQuery = "SELECT productQty AS totalQuantity, TotalCost AS totalCost " +
                "FROM cart WHERE CustomerID = ?";
        PreparedStatement cartStatement = connection.prepareStatement(cartQuery);
        cartStatement.setInt(1, customerID);
        ResultSet cartResultSet = cartStatement.executeQuery();
        if (cartResultSet.next()) {
            int totalQuantity = cartResultSet.getInt("totalQuantity");
            double totalCost = cartResultSet.getDouble("totalCost");
            System.out.printf("Total Quantity: %d, Total Cost: %.2f%n", totalQuantity, totalCost);
        }
        addsToResultSet.close();
        addsToStatement.close();
        cartResultSet.close();
        cartStatement.close();
    }

    // jb ek hi product ko multiple warehouses ke through add krenge to only 1 baar hi remove kr skte h and 1 ko hi add kia to usko remove nhi kr skte
    //can be fixed just by adding a list
    private static void removeProduct(Connection connection, ScannerClass sc, int customerID) {
        try {
            System.out.println("Enter the ProductID you want to remove:");
            int productID = sc.readInt();
            if (isProductInCart(connection, customerID, productID)) {
                int a = (getWarehouseID(connection, customerID, productID));
                System.out.println("Warehouse from where you purchased : " + a);
                System.out.println("Enter the WareHouseID : ");
                int bc = sc.readInt();
                int productQtyInCart = getProductQuantityInCart(connection, customerID, productID, bc);
                // Begin transaction
                try {
                    // Start transaction
                    // Disable auto-commit mode
                    connection.setAutoCommit(false);
                    // Begin the transaction
                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate("START TRANSACTION");
                    String deleteQuery = "DELETE FROM addsTo WHERE cartID IN (SELECT cartID FROM cart WHERE CustomerID = ?) AND productID = ? AND warehouseID = ?";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                    deleteStatement.setInt(1, customerID);
                    deleteStatement.setInt(2, productID);
                    deleteStatement.setInt(3, bc);
                    int rowsAffected = deleteStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        updateCart(connection);
                        System.out.println("Product removed from cart successfully!");
                        System.out.println("Quantity removed: " + productQtyInCart);
                        addQuantityToStock(connection, productID, productQtyInCart, bc);
                        // Commit the transaction
                        stmt.executeUpdate("COMMIT");
                    } else {
                        System.out.println("Failed to remove product from cart!");
                    }
                    deleteStatement.close();
//                    connection.commit();
                }catch (SQLException e) {
                    e.printStackTrace();
                    try {
                        // Rollback the transaction if an exception occurs
                        connection.rollback();
                    } catch (SQLException rollbackException) {
                        rollbackException.printStackTrace();
                    }
                        }
                finally {
                    try {
        //                // Enable auto-commit mode
                        connection.setAutoCommit(true);
                    } catch (SQLException autoCommitException) {
                        autoCommitException.printStackTrace();
                    }
                }
            } else {
                System.out.println("Product is not in your cart.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getWarehouseID(Connection connection, int custID, int productID) {
        int warehouseID = -1;
        int cartID = getCartID(connection, custID);
        try {
            String query = "SELECT s.warehouseID " +
                    "FROM addsTo a " +
                    "JOIN stores s ON a.productID = s.productID " +
                    "WHERE a.cartID = ? AND a.productID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, cartID);
            statement.setInt(2, productID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                warehouseID = resultSet.getInt("warehouseID");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warehouseID;
    }

    private static boolean isProductInCart(Connection connection, int customerID, int productID) throws SQLException {
        String query = "SELECT COUNT(*) FROM addsTo WHERE cartID IN (SELECT cartID FROM cart WHERE CustomerID = ?) AND productID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, customerID);
        statement.setInt(2, productID);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        resultSet.close();
        statement.close();
        return count > 0;
    }

    private static int getProductQuantityInCart(Connection connection, int customerID, int productID, int warehouseID) {
        try {
            String query = "SELECT SUM(productQty) AS totalQty FROM addsTo WHERE cartID IN (SELECT cartID FROM cart WHERE CustomerID = ?) AND productID = ? AND warehouseID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, customerID);
            statement.setInt(2, productID);
            statement.setInt(3, warehouseID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("totalQty");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // If no product quantity found
    }

    private static void addQuantityToStock(Connection connection, int productID, int quantity, int warehouseID) {
        try {
            // Begin transaction
            connection.setAutoCommit(false);
            try {
                String updateQuery = "UPDATE stores SET StockQty = StockQty + ? WHERE ProductID = ? AND WarehouseID = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setInt(1, quantity);
                    updateStatement.setInt(2, productID);
                    updateStatement.setInt(3, warehouseID);
                    int rowsAffected = updateStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Stock quantity updated successfully!");
                    } else {
                        System.out.println("Failed to update stock quantity!");
                    }
                }
                // Commit transaction if everything is successful
                connection.commit();
            } catch (SQLException e) {
                // Rollback transaction if any error occurs
                connection.rollback();
                System.err.println("Transaction failed: " + e.getMessage());
            } finally {
                // Restore auto-commit mode
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void updateCart(Connection connection) {
        try {
            String query = "SELECT a.cartID, SUM(a.productQty) AS totalQty, SUM(a.productQty * p.ProductPrice) AS totalCost " +
                    "FROM addsTo a " +
                    "INNER JOIN product p ON a.productID = p.productID " +
                    "GROUP BY a.cartID";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int cartID = resultSet.getInt("cartID");
                int totalQty = resultSet.getInt("totalQty");
                double totalCost = resultSet.getDouble("totalCost");
                updateCartTable(connection, cartID, totalQty, totalCost);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void updateCartTable(Connection connection, int cartID, int totalQty, double totalCost) {
        try {
            String updateQuery = "UPDATE cart SET productQty = ?, TotalCost = ? WHERE cartID = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setInt(1, totalQty);
            updateStatement.setDouble(2, totalCost);
            updateStatement.setInt(3, cartID);
            int rowsAffected = updateStatement.executeUpdate();
            if (rowsAffected > 0) {
//                System.out.println("Cart with ID " + cartID + " updated successfully.");
            } else {
                System.out.println("Failed to update cart with ID " + cartID);
            }
            updateStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


//*********still under development****************


//rough tries

//    private static List<Integer> matchingWarehouseIDs = new ArrayList<>();
//    private static void printMatchingWarehouseIDs(Connection connection) {
//        try {
//            // Query to retrieve all warehouse IDs
//            String query = "SELECT WareHouseID FROM warehouse";
//            PreparedStatement statement = connection.prepareStatement(query);
//            ResultSet resultSet = statement.executeQuery();
//
//            // Print all warehouse IDs
//            System.out.println("All Warehouse IDs:");
//            while (resultSet.next()) {
//                int warehouseID = resultSet.getInt("WareHouseID");
//                System.out.println(warehouseID);
//            }
//
//            resultSet.close();
//            statement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    private static int isProductAvailableInStores(Connection connection, int productID, int WareHouseID) {
//        int stQty = 0;
//        try {
//            String query = "SELECT SUM(StockQty) AS totalStock FROM stores WHERE ProductID = ?";
////            String query = "SELECT StockQty FROM stores WHERE ProductID = ?";
//
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setInt(1, productID);
//            ResultSet resultSet = statement.executeQuery();
//
//            if (resultSet.next()) {
//                int stockQuantity = resultSet.getInt("totalStock");
//                stQty =  stockQuantity;
////
//            } else {
//                System.out.println("Product not found in stores.");
//            }
//
//            resultSet.close();
//            statement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return stQty;
//    }

//    private static void insertOrderList(Connection connection, int orderID, int cartID) {
//        String query = "INSERT INTO OrderList (OrderID, ProductID, ProductQty, ProductPrice) " +
//                "SELECT ?, addsTo.productID, addsTo.productQty, product.ProductPrice " +
//                "FROM addsTo " +
//                "INNER JOIN product ON addsTo.productID = product.productID " +
//                "WHERE addsTo.cartID = ?";
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, orderID);
//            statement.setInt(2, cartID);
//            statement.executeUpdate();
//            System.out.println("Order list inserted successfully!");
//        } catch (SQLException e) {
//            System.err.println("Error inserting order list: " + e.getMessage());
//        }
//    }

//    private static void insertPayment(Connection connection, ScannerClass sc, int customerID) {
//        try {
//            sc.readString();
//            System.out.println("Enter payment method (Cash On Delivery,Net Banking,UPI,EMI):");
//            String paymentMethod = sc.readString();
//            Timestamp paymentDateAndTime = new Timestamp(System.currentTimeMillis());
//            int paymentID = generatePaymentID(connection);
//            String query = "INSERT INTO payment (paymentID, paymentMethod, paymentDateTime) VALUES (?, ?, ?)";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setInt(1, paymentID);
//            statement.setString(2, paymentMethod);
//            statement.setTimestamp(3, paymentDateAndTime);
//            int rowsAffected = statement.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("Payment details inserted successfully!");
//                insertOrder(connection, sc, customerID, paymentID, paymentMethod);
//                System.out.println("Generated PaymentID: " + paymentID);
//            } else {
//                System.out.println("Failed to insert payment details!");
//            }
//            statement.close();
//        } catch (SQLException e) {
//            System.err.println("Error inserting payment details: " + e.getMessage());
//        }
//    }

//    private static void insertPayment(Connection connection, ScannerClass sc, int customerID) {
//        try {
//            if (!isCartEmpty(connection, customerID)) { // Check if the cart is not empty
//                sc.readString();
//                System.out.println("Enter payment method (Cash On Delivery, Net Banking, UPI, EMI):");
//                String paymentMethod = sc.readString();
//                Timestamp paymentDateAndTime = new Timestamp(System.currentTimeMillis());
//                int paymentID = generatePaymentID(connection);
//                String query = "INSERT INTO payment (paymentID, paymentMethod, paymentDateTime) VALUES (?, ?, ?)";
//                PreparedStatement statement = connection.prepareStatement(query);
//                statement.setInt(1, paymentID);
//                statement.setString(2, paymentMethod);
//                statement.setTimestamp(3, paymentDateAndTime);
//                int rowsAffected = statement.executeUpdate();
//                if (rowsAffected > 0) {
//                    System.out.println("Payment details inserted successfully!");
//                    insertOrder(connection, sc, customerID, paymentID, paymentMethod);
//                    System.out.println("Generated PaymentID: " + paymentID);
//                } else {
//                    System.out.println("Failed to insert payment details!");
//                }
//                statement.close();
//            } else {
//                System.out.println("Your cart is empty. Please add products before proceeding with the payment.");
//            }
//        } catch (SQLException e) {
//            System.err.println("Error inserting payment details: " + e.getMessage());
//        }
//    }


//public static void scheduleEvent(Connection connection, int orderID) throws SQLException {
//    String query = "SET @orderID = ?";
//    try (PreparedStatement statement = connection.prepareStatement(query)) {
//        statement.setInt(1, orderID);
//        statement.execute();
//    }
//
//    String createEventQuery = "CREATE EVENT IF NOT EXISTS update_order_event_30s " +
//            "ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 30 SECOND " +
//            "ON COMPLETION PRESERVE " +
//            "DO " +
//            "    UPDATE Orders " +
//            "    SET orderStatus = 'Delivered' " +
//            "    WHERE OrderID = @orderID " +
//            "      AND orderStatus IN ('Confirmed', 'On the way', 'Processing')";
//    try (PreparedStatement statement = connection.prepareStatement(createEventQuery)) {
//        statement.executeUpdate();
//    }
//}

//    public static void clearCart(Connection connection, int cartID) throws SQLException {
//        clearAddsTo(connection, cartID);
//        deleteCart(connection, cartID);
//    }

//    private static void updateStoresStock(Connection connection, int productID, int quantity) {
//        try {
//            String updateQuery = "UPDATE stores SET StockQty = StockQty - ? WHERE ProductID = ?";
//            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
//            updateStatement.setInt(1, quantity);
//            updateStatement.setInt(2, productID);
//            updateStatement.executeUpdate();
//            updateStatement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    private static void printAddsToData(Connection connection, int customerID) throws SQLException {
//        String query = "SELECT a.productID, p.productname, p.ProductPrice, a.productQty " +
//                "FROM addsTo a " +
//                "INNER JOIN product p ON a.productID = p.productID " +
//                "WHERE a.cartID IN (SELECT cartID FROM cart WHERE CustomerID = ?)";
//        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setInt(1, customerID);
//        ResultSet resultSet = statement.executeQuery();
//        System.out.println("AddsTo data for Customer ID: " + customerID);
//        while (resultSet.next()) {
//            int productID = resultSet.getInt("productID");
//            String productName = resultSet.getString("productname");
//            double productPrice = resultSet.getDouble("ProductPrice");
//            int productQty = resultSet.getInt("productQty");
//            System.out.printf("ProductID: %d, ProductName: %s, ProductPrice: %.2f, ProductQty: %d%n",
//                    productID, productName, productPrice, productQty);
//        }
//        resultSet.close();
//        statement.close();
//    }

//    private static int getProductQuantityInCart(Connection connection, int customerID, int productID) {
//        try {
//            String query = "SELECT productQty FROM addsTo WHERE cartID IN (SELECT cartID FROM cart WHERE CustomerID = ?) AND productID = ?";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setInt(1, customerID);
//            statement.setInt(2, productID);
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                return resultSet.getInt("productQty");
//            }
//            resultSet.close();
//            statement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0; // If no product quantity found
//    }

//    private static void addQuantityToStock(Connection connection, int productID, int quantity) {
//        try {
//            String updateQuery = "UPDATE stores SET StockQty = StockQty + ? WHERE ProductID = ?";
//            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
//            updateStatement.setInt(1, quantity);
//            updateStatement.setInt(2, productID);
//            int rowsAffected = updateStatement.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("Stock quantity updated successfully!");
//            } else {
//                System.out.println("Failed to update stock quantity!");
//            }
//            updateStatement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    private static void addQuantityToStock(Connection connection, int productID, int quantity, int warehouseID) {
//        try {
//            String updateQuery = "UPDATE stores SET StockQty = StockQty + ? WHERE ProductID = ? AND WarehouseID = ?";
//            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
//            updateStatement.setInt(1, quantity);
//            updateStatement.setInt(2, productID);
//            updateStatement.setInt(3, warehouseID);
//            int rowsAffected = updateStatement.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("Stock quantity updated successfully!");
//            } else {
//                System.out.println("Failed to update stock quantity!");
//            }
//            updateStatement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

