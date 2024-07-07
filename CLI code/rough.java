public class rough {
//    ***signup for customer*****/
//    private static void signUp(Connection connection, ScannerClass sc) {
//        sc.readString();
//        System.out.println("Enter First Name:");
//        String fname = sc.readString();
//        System.out.println("Enter Middle Name (Press Enter to skip):");
//        String mname = sc.readString();
//        if (mname.trim().isEmpty()) { // Check if middle name is empty
//            mname = null;
//        }
//        System.out.println("Enter Last Name:");
//        String lname = sc.readString();
//        System.out.println("Enter Date of Birth (YYYY-MM-DD):");
//        String dob = sc.readString();
//        System.out.println("Enter Street Number:");
//        String streetNo = sc.readString();
//        System.out.println("Enter Landmark:");
//        String landmark = sc.readString();
//        System.out.println("Enter City:");
//        String city = sc.readString();
//        System.out.println("Enter State:");
//        String state = sc.readString();
//        System.out.println("Enter Pincode:");
//        String pincode = sc.readString();
//        System.out.println("Enter Email:");
//        String email = sc.readString();
//        System.out.println("Enter Password:");
//        String password = sc.readString();
//
//        try {
//            // Get the next available CustomerID from the database
//            String getIdQuery = "SELECT MAX(CustomerID) FROM Customer";
//            PreparedStatement getIdStatement = connection.prepareStatement(getIdQuery);
//            ResultSet resultSet = getIdStatement.executeQuery();
//            int nextId = 1; // Default value if no rows returned
//            if (resultSet.next()) {
//                nextId = resultSet.getInt(1) + 1; // Increment the max id by 1
//            }
//
//            // Inserting data into Customer table
//            String insertQuery = "INSERT INTO Customer (CustomerID, fname, mname, lname, CustomerDOB, streetNo, landmark, city, state, pincode, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
//            preparedStatement.setInt(1, nextId);
//            preparedStatement.setString(2, fname);
//            preparedStatement.setString(3, mname); // If middle name is empty, null will be passed
//            preparedStatement.setString(4, lname);
//            preparedStatement.setString(5, dob);
//            preparedStatement.setString(6, streetNo);
//            preparedStatement.setString(7, landmark);
//            preparedStatement.setString(8, city);
//            preparedStatement.setString(9, state);
//            preparedStatement.setString(10, pincode);
//            preparedStatement.setString(11, email);
//
//            int rowsAffected = preparedStatement.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("Sign up successful!");
//            } else {
//                System.out.println("Sign up failed!");
//            }
//            preparedStatement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }



//****triger for addsto to cart******/
//    public static void updateCart(Connection connection) {
//        try {
//            Statement stmt = connection.createStatement();
//
//            // Query to update cart data based on addsTo and product tables
//            String updateQuery = "UPDATE cart c " +
//                    "SET c.productQty = (SELECT SUM(a.productQty) FROM addsTo a WHERE a.cartID = c.cartID), " +
//                    "    c.TotalCost = (SELECT SUM(a.productQty * p.ProductPrice) " +
//                    "                   FROM addsTo a INNER JOIN product p ON a.productID = p.productID " +
//                    "                   WHERE a.cartID = c.cartID) " +
//                    "WHERE c.cartID IN (SELECT DISTINCT cartID FROM addsTo)";
//
//            // Execute the update query
//            int rowsAffected = stmt.executeUpdate(updateQuery);
//
//            // Display the number of rows affected
//            System.out.println(rowsAffected + " row(s) updated in cart table.");
//
//            // Close the statement
//            stmt.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    private static void updateCart(Connection connection) {
//        try {
//            // Query to calculate productQty and TotalCost for each cartID
//            String query = "SELECT a.cartID, SUM(a.productQty) AS totalQty, SUM(a.productQty * p.ProductPrice) AS totalCost " +
//                    "FROM addsTo a " +
//                    "INNER JOIN product p ON a.productID = p.productID " +
//                    "GROUP BY a.cartID";
//            PreparedStatement statement = connection.prepareStatement(query);
//            ResultSet resultSet = statement.executeQuery();
//
//            // Update cart table with calculated values
//            while (resultSet.next()) {
//                int cartID = resultSet.getInt("cartID");
//                int totalQty = resultSet.getInt("totalQty");
//                double totalCost = resultSet.getDouble("totalCost");
//
//                // Update cart table with calculated values
//                updateCartTable(connection, cartID, totalQty, totalCost);
//            }
//
//            resultSet.close();
//            statement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Method to update cart table with calculated values
//    private static void updateCartTable(Connection connection, int cartID, int totalQty, double totalCost) {
//        try {
//            String updateQuery = "UPDATE cart SET productQty = ?, TotalCost = ? WHERE cartID = ?";
//            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
//            updateStatement.setInt(1, totalQty);
//            updateStatement.setDouble(2, totalCost);
//            updateStatement.setInt(3, cartID);
//            int rowsAffected = updateStatement.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("Cart with ID " + cartID + " updated successfully.");
//            } else {
//                System.out.println("Failed to update cart with ID " + cartID);
//            }
//            updateStatement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    private static void add_product_cat(Connection connection, ScannerClass sc) {
//        sc.readString();
//        try {
//            System.out.println("Enter Product Category Name:");
//            String productCatName = sc.readString();
//            System.out.println("Enter Product Category Description:");
//            String productCatDescription = sc.readString();
//
//            // Get the next available ProductCatID
//            int nextProductCatID = getNextProductCatID(connection);
//
//            // Inserting data into ProductCategory table
//            String insertQuery = "INSERT INTO ProductCategory (ProductCatID, ProductCatName, ProductCatDescription) VALUES (?, ?, ?)";
//            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
//            preparedStatement.setInt(1, nextProductCatID);
//            preparedStatement.setString(2, productCatName);
//            preparedStatement.setString(3, productCatDescription);
//
//            int rowsAffected = preparedStatement.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("Product category added successfully!");
//            } else {
//                System.out.println("Failed to add product category!");
//            }
//            preparedStatement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }



    //    ****************************Admin/ seller's work*******************************************************
//    private static void adminLogin(Connection connection, ScannerClass sc) {
//        sc.readString();
//        int attempts = 0;
//        int MAX_LOGIN_ATTEMPTS = 3;
//        while (attempts < MAX_LOGIN_ATTEMPTS) {
//            System.out.println("Enter Email:");
//            String email = sc.readString();
//            System.out.println("Enter Password:");
//            String password = sc.readString();
//
//            try {
//                String loginQuery = "SELECT * FROM Manager WHERE ManagerEmail = ? AND password = ?";
//                PreparedStatement loginStatement = connection.prepareStatement(loginQuery);
//                loginStatement.setString(1, email);
//                loginStatement.setString(2, password);
//                ResultSet resultSet = loginStatement.executeQuery();
//
//                if (resultSet.next()) {
//                    System.out.println("Admin login successful!");
//                    after_login_admin(connection,sc);
////                    return true;
//                } else {
//                    System.out.println("Invalid email or password. Please try again.");
//                    attempts++;
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("Maximum login attempts reached. Exiting...");
////        return false;
//    }
//    private static void after_login_admin(Connection connection,ScannerClass sc) {
//        while (true){
//            System.out.println("Explore : \n1. Add Product Category\n2. Add Product\n3. Remove Product Category\n4. Remove Product\n5. Update Product Details\n6. Update Product Category Details\n7. Inventory Analysis\n8. Logout");
//            int aa = sc.readInt();
//            if (aa == 1) {
//                add_product_cat(connection, sc);
//            }
//            else if (aa == 2) {
////                printProductCategories(connection);
//                add_product(connection,sc);
//            }
//            else if (aa == 3) {
//                remove_product_cat(connection,sc);
//            }
//            else if (aa == 4) {
//                System.out.println("Choose : \n1. Delete by ProductID\n2. Delete by Name\n");
//                int aa_1 = sc.readInt();
//                if(aa_1==1) {
//                    remove_product_byID(connection, sc);
//                }
//                else if(aa_1==2){
//                    remove_product_byName(connection,sc);
//                }
//            } else if (aa == 5) {
//                //currently underDevelopment
//                System.out.println("Under Development");
//            } else if (aa == 6) {
//                //currently underDevelopment
//                System.out.println("Under Development");
//            } else if (aa == 7) {
//                performInventoryAnalysis(connection);
//            }
//            else if (aa == 8) {
//                return;
//            }else {
//                System.out.println("Choose a valid option!!!");
//            }
//        }
//    }
//
//    public static void performInventoryAnalysis(Connection connection) {
//        try {
//            // Retrieve data from the database
//            Map<Integer, Map<Integer, Integer>> warehouseProductQuantities = getWarehouseProductQuantities(connection);
//
//            // Aggregate data
//            Map<Integer, Integer> totalProductQuantities = calculateTotalProductQuantities(warehouseProductQuantities);
//            Map<Integer, Integer> totalWarehouseQuantities = calculateTotalWarehouseQuantities(warehouseProductQuantities);
//
//            // Perform analysis
//            int maxQuantityProductID = getMaxQuantityProductID(totalProductQuantities);
//            int maxQuantityWarehouseID = getMaxQuantityWarehouseID(totalWarehouseQuantities);
//
//            // Display analysis results
//            displayAnalysisResults(maxQuantityProductID, maxQuantityWarehouseID);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static Map<Integer, Map<Integer, Integer>> getWarehouseProductQuantities(Connection connection) throws SQLException {
//        // Retrieve product quantities per warehouse from the database
//        Map<Integer, Map<Integer, Integer>> warehouseProductQuantities = new HashMap<>();
//        String query = "SELECT WarehouseID, ProductID, StockQty FROM stores";
//        try (Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(query)) {
//            while (resultSet.next()) {
//                int warehouseID = resultSet.getInt("WarehouseID");
//                int productID = resultSet.getInt("ProductID");
//                int stockQty = resultSet.getInt("StockQty");
//                warehouseProductQuantities.computeIfAbsent(warehouseID, k -> new HashMap<>())
//                        .put(productID, stockQty);
//            }
//        }
//        return warehouseProductQuantities;
//    }
//
//    private static Map<Integer, Integer> calculateTotalProductQuantities(Map<Integer, Map<Integer, Integer>> warehouseProductQuantities) {
//        // Calculate total product quantities across all warehouses
//        Map<Integer, Integer> totalProductQuantities = new HashMap<>();
//        for (Map<Integer, Integer> productQuantities : warehouseProductQuantities.values()) {
//            for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
//                int productID = entry.getKey();
//                int quantity = entry.getValue();
//                totalProductQuantities.merge(productID, quantity, Integer::sum);
//            }
//        }
//        return totalProductQuantities;
//    }
//
//    private static Map<Integer, Integer> calculateTotalWarehouseQuantities(Map<Integer, Map<Integer, Integer>> warehouseProductQuantities) {
//        // Calculate total quantities per warehouse
//        Map<Integer, Integer> totalWarehouseQuantities = new HashMap<>();
//        for (Map.Entry<Integer, Map<Integer, Integer>> entry : warehouseProductQuantities.entrySet()) {
//            int warehouseID = entry.getKey();
//            Map<Integer, Integer> productQuantities = entry.getValue();
//            int totalWarehouseQuantity = productQuantities.values().stream().mapToInt(Integer::intValue).sum();
//            totalWarehouseQuantities.put(warehouseID, totalWarehouseQuantity);
//        }
//        return totalWarehouseQuantities;
//    }
//
//    private static int getMaxQuantityProductID(Map<Integer, Integer> totalProductQuantities) {
//        // Find the product with the highest total quantity
//        return Collections.max(totalProductQuantities.entrySet(), Map.Entry.comparingByValue()).getKey();
//    }
//
//    private static int getMaxQuantityWarehouseID(Map<Integer, Integer> totalWarehouseQuantities) {
//        // Find the warehouse with the highest total quantity
//        return Collections.max(totalWarehouseQuantities.entrySet(), Map.Entry.comparingByValue()).getKey();
//    }
//
//    private static void displayAnalysisResults(int maxQuantityProductID, int maxQuantityWarehouseID) {
//        System.out.println("Inventory Analysis:");
//        System.out.println("Product with Highest Total Quantity (Product ID): " + maxQuantityProductID);
//        System.out.println("Warehouse with Highest Total Quantity (Warehouse ID): " + maxQuantityWarehouseID);
//    }
//
//    private static void add_product_cat(Connection connection, ScannerClass sc) {
//        sc.readString();
//        try {
//            while (true) {
//                System.out.println("Enter Product Category Name:");
//                String productCatName = sc.readString();
//                if (productCatName.trim().isEmpty()) {
//                    System.out.println("Product Category Name cannot be empty. Please try again.");
//                } else {
//                    // Proceed with adding product category
//                    System.out.println("Enter Product Category Description:");
//                    String productCatDescription = sc.readString();
//
//                    // Get the next available ProductCatID
//                    int nextProductCatID = getNextProductCatID(connection);
//
//                    // Inserting data into ProductCategory table
//                    String insertQuery = "INSERT INTO ProductCategory (ProductCatID, ProductCatName, ProductCatDescription) VALUES (?, ?, ?)";
//                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
//                    preparedStatement.setInt(1, nextProductCatID);
//                    preparedStatement.setString(2, productCatName);
//                    preparedStatement.setString(3, productCatDescription);
//
//                    int rowsAffected = preparedStatement.executeUpdate();
//                    if (rowsAffected > 0) {
//                        System.out.println("Product category added successfully!");
//                    } else {
//                        System.out.println("Failed to add product category!");
//                    }
//                    preparedStatement.close();
//                    break; // Exit the loop after successful insertion
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    private static int getNextProductCatID(Connection connection) throws SQLException {
//        String query = "SELECT MAX(ProductCatID) FROM ProductCategory";
//        PreparedStatement statement = connection.prepareStatement(query);
//        ResultSet resultSet = statement.executeQuery();
//        if (resultSet.next()) {
//            return resultSet.getInt(1) + 1;
//        }
//        return 1; // If no record found, start from 1
//    }
//    private static void printProductCategories(Connection connection) {
//        try {
//            // Query to select all rows from ProductCategory table
//            String query = "SELECT * FROM ProductCategory";
//            PreparedStatement statement = connection.prepareStatement(query);
//            ResultSet resultSet = statement.executeQuery();
//
//            // Print the header
//            System.out.println("Product Category ID | Product Category Name | Product Category Description");
//            System.out.println("--------------------------------------------------------------------------");
//
//            // Print each row of the result set
//            while (resultSet.next()) {
//                int productCatID = resultSet.getInt("ProductCatID");
//                String productCatName = resultSet.getString("ProductCatName");
//                String productCatDescription = resultSet.getString("ProductCatDescription");
//
//                // Print the row
//                System.out.printf("%-20s  | %-20s  | %-30s%n", productCatID, productCatName, productCatDescription);
//            }
//
//            resultSet.close();
//            statement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    private static void add_product(Connection connection, ScannerClass sc) {
//        sc.readString();
//        try {
//            printProductCategories(connection); // Print existing product categories
//
//            System.out.println("Enter Product Name:");
//            String productName = sc.readString();
//
//            System.out.println("Enter Product Price:");
//            double productPrice = sc.readDouble();
//
//            System.out.println("Enter Product Category ID:");
//            int productCatID = sc.readInt();
//            sc.readString();
//            System.out.println("Enter Product Description :");
//            String productDescription = sc.readString();
//            System.out.println("Enter Brand Name :");
//            String brandName = sc.readString();
//
//            // Get the next available ProductID
//            int nextProductID = getNextProductID(connection);
//
//            // Inserting data into Product table
//            String insertQuery = "INSERT INTO Product (ProductID, ProductName, ProductPrice, ProductDescription, Brandname, ProductCatID) VALUES (?, ?, ?, ?, ?, ?)";
//            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
//            preparedStatement.setInt(1, nextProductID);
//            preparedStatement.setString(2, productName);
//            preparedStatement.setDouble(3, productPrice);
//            preparedStatement.setString(4, productDescription);
//            preparedStatement.setString(5, brandName);
//            preparedStatement.setInt(6, productCatID);
//
//            int rowsAffected = preparedStatement.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("Product added successfully!");
//            } else {
//                System.out.println("Failed to add product!");
//            }
//            preparedStatement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    private static int getNextProductID(Connection connection) throws SQLException {
//        String query = "SELECT MAX(ProductID) FROM Product";
//        PreparedStatement statement = connection.prepareStatement(query);
//        ResultSet resultSet = statement.executeQuery();
//        if (resultSet.next()) {
//            return resultSet.getInt(1) + 1;
//        }
//        return 1; // If no record found, start from 1
//    }
//    private static void remove_product_cat(Connection connection, ScannerClass sc){
//        try{
//            printProductCategories(connection);
//            System.out.println("*********!! If you remove any product category all product related to that category would also be removed !!*********\n");
//    //        System.out.println("You need all manager's permission for this!!");
//
//            System.out.println("Enter the ID of the Product Category you want to remove:");
//            int productCatID = sc.readInt();
//            sc.readString();
//            System.out.println("Are you sure you want to remove this product category? (yes/no)");
//            String confirmation = sc.readString().trim().toLowerCase();
//            if (confirmation.equals("yes")) {
//                // Delete the product category and related products
//                deleteProductCategory(connection, productCatID);
//            } else {
//                System.out.println("Product category removal cancelled.");
//            }
//        }catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    private static void deleteProductCategory(Connection connection, int productCatID) throws SQLException {
//        String deleteCategoryQuery = "DELETE FROM ProductCategory WHERE ProductCatID = ?";
//        PreparedStatement deleteCategoryStatement = connection.prepareStatement(deleteCategoryQuery);
//        deleteCategoryStatement.setInt(1, productCatID);
//        int categoryDeleted = deleteCategoryStatement.executeUpdate();
//        deleteCategoryStatement.close();
//
//        if (categoryDeleted > 0) {
//            System.out.println("Product category removed successfully!");
////            System.out.println(productsDeleted + " products related to this category were also removed.");
//        } else {
//            System.out.println("Failed to remove product category!");
//        }
//    }
//    private static void printProducts(Connection connection) {
//        try {
//            // Query to select all rows from Product table
//            String query = "SELECT * FROM Product";
//            PreparedStatement statement = connection.prepareStatement(query);
//            ResultSet resultSet = statement.executeQuery();
//
//            // Print the header
//            System.out.println("Product ID | Product Name | Product Price | Product Description | Brand Name | Product Category ID");
//            System.out.println("----------------------------------------------------------------------------------------------------");
//
//            // Print each row of the result set
//            while (resultSet.next()) {
//                int productID = resultSet.getInt("ProductID");
//                String productName = resultSet.getString("ProductName");
//                double productPrice = resultSet.getDouble("ProductPrice");
//                String productDescription = resultSet.getString("ProductDescription");
//                String brandName = resultSet.getString("Brandname");
//                int productCatID = resultSet.getInt("ProductCatID");
//
//                // Print the row
//                System.out.printf("%-10s | %-12s | %-13s | %-20s | %-10s | %-18s%n", productID, productName, productPrice, productDescription, brandName, productCatID);
//            }
//            resultSet.close();
//            statement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    private static void remove_product_byID(Connection connection, ScannerClass sc){
//        try{
//            printProducts(connection);
//            System.out.println("Enter the ID of the Product you want to remove:");
//            int productID = sc.readInt();
//            sc.readString();
//            System.out.println("Are you sure you want to remove this product ? (yes/no)");
//            String confirmation = sc.readString().trim().toLowerCase();
//            if (confirmation.equals("yes")) {
//                deleteProductByID(connection, productID);
//            } else {
//                System.out.println("Product category removal cancelled.");
//            }
//        }catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    private static void deleteProductByID(Connection connection, int productID) throws SQLException{
//        String deleteProductQuery = "DELETE FROM Product WHERE ProductID = ?";
//        PreparedStatement deleteProductStatement = connection.prepareStatement(deleteProductQuery);
//        deleteProductStatement.setInt(1, productID);
//        int productDeleted = deleteProductStatement.executeUpdate();
//        deleteProductStatement.close();
//        if (productDeleted > 0) {
//            System.out.println("Product removed successfully!");
//        } else {
//            System.out.println("Failed to remove product !");
//        }
//    }
//    private static void remove_product_byName(Connection connection, ScannerClass sc){
//        sc.readString();
//        try{
//            System.out.println("Enter the Name of the Product you want to remove:");
//            String productName = sc.readString();
//            deleteProductByName(connection, productName,sc);
//        }catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    private static void deleteProductByName(Connection connection, String productName, ScannerClass sc) throws SQLException{
//        List<Integer> productIDs = new ArrayList<>();
//        SearchProductByName(connection,productName, productIDs);
//        removeproductbyID(connection,sc, productIDs);
//        productIDs.clear();
//    }
//
//    private static void removeproductbyID(Connection connection, ScannerClass sc, List<Integer> productIDs) {
//        try {
//            System.out.println("Enter the ID of the Product you want to remove:");
//            int productID = sc.readInt();
//
//            // Check if the entered product ID exists in the list of retrieved product IDs
//            if (productIDs.contains(productID)) {
//                sc.readString(); // Consume the newline character from previous input
//                System.out.println("Are you sure you want to remove this product? (yes/no)");
//                String confirmation = sc.readString().trim().toLowerCase();
//                if (confirmation.equals("yes")) {
//                    deleteProductByID(connection, productID);
//                } else {
//                    System.out.println("Product removal cancelled.");
//                }
//            } else {
//                System.out.println("Invalid product ID. Please enter a valid ID.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    private static void SearchProductByName(Connection connection, String productName, List<Integer> productIDs) throws SQLException {
//        try {
//            // Retrieve all products with the given product name
//            String query = "SELECT * FROM Product WHERE ProductName LIKE ?";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setString(1, "%" + productName + "%");
//            ResultSet resultSet = statement.executeQuery();
//
//            // Print the header
//            System.out.println("Product ID | Product Name | Product Price | Product Description | Brand Name | Product Category ID");
//            System.out.println("---------------------------------------------------------------------------------------------------");
//
//            // Print each row of the result set and delete the product
//            while (resultSet.next()) {
//                int productID = resultSet.getInt("ProductID");
//                productIDs.add(productID);
//                String retrievedProductName = resultSet.getString("ProductName");
//                double productPrice = resultSet.getDouble("ProductPrice");
//                String productDescription = resultSet.getString("ProductDescription");
//                String brandName = resultSet.getString("Brandname");
//                int productCatID = resultSet.getInt("ProductCatID");
//
//                // Print the row
//                System.out.printf("%-10s | %-12s | %-13s | %-20s | %-10s | %-18s%n", productID, retrievedProductName, productPrice, productDescription, brandName, productCatID);
//            }
//            resultSet.close();
//            statement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}


    //    private static void updateStockInWarehouses(Connection connection, int productID, int stockQuantity, int managerID) {
//        try {
//            // Retrieve manager's branch
//            String branchQuery = "SELECT BranchID FROM branch WHERE ManagerID = ?";
//            PreparedStatement branchStatement = connection.prepareStatement(branchQuery);
//            branchStatement.setInt(1, managerID);
//            ResultSet branchResult = branchStatement.executeQuery();
//
//            int branchID = -1;
//            if (branchResult.next()) {
//                branchID = branchResult.getInt("BranchID");
//            }
//
//            branchStatement.close();
//
//            // Retrieve associated warehouses
//            String warehouseQuery = "SELECT WarehouseID FROM Warehouse WHERE BranchID = ?";
//            PreparedStatement warehouseStatement = connection.prepareStatement(warehouseQuery);
//            warehouseStatement.setInt(1, branchID);
//            ResultSet warehouseResult = warehouseStatement.executeQuery();
//
//            // Update stock in each warehouse
//            while (warehouseResult.next()) {
//                int warehouseID = warehouseResult.getInt("WarehouseID");
//                String stockUpdateQuery = "INSERT INTO stores (warehouseID, productID, stockQty, ReturnableStatus) VALUES (?, ?, ?, ?)";
//                PreparedStatement stockStatement = connection.prepareStatement(stockUpdateQuery);
//                stockStatement.setInt(1, warehouseID);
//                stockStatement.setInt(2, productID);
//                stockStatement.setInt(3, stockQuantity);
//                stockStatement.setString(4, "Yes");
//                stockStatement.executeUpdate();
//                stockStatement.close();
//            }
//
//            warehouseStatement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }


}
