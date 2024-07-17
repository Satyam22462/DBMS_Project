import java.sql.*;
import java.util.*;

public class seller {
    ScannerClass sc = new ScannerClass();
    public static void adminLogin(Connection connection, ScannerClass sc) {
        sc.readString();
        int attempts = 0;
        int MAX_LOGIN_ATTEMPTS = 3;
        while (attempts < MAX_LOGIN_ATTEMPTS) {
            System.out.println("Enter Email:");
            String email = sc.readString();
            System.out.println("Enter Password:");
            String password = sc.readString();

            try {
                String loginQuery = "SELECT * FROM Manager WHERE ManagerEmail = ? AND password = ?";
                PreparedStatement loginStatement = connection.prepareStatement(loginQuery);
                loginStatement.setString(1, email);
                loginStatement.setString(2, password);
                ResultSet resultSet = loginStatement.executeQuery();
                int managerID = -1;
                if (resultSet.next()) {
                    managerID = resultSet.getInt("ManagerID");
                    System.out.println("Admin login successful!");
                    after_login_admin(connection,sc, managerID);
//                    return true;
                }
                else {
                    System.out.println("Invalid email or password. Please try again.");
                    attempts++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Maximum login attempts reached. Exiting...");
//        return false;
    }
    private static void after_login_admin(Connection connection,ScannerClass sc, int managerID) {
        while (true){
            System.out.println("Explore : \n1. Add Product Category\n2. Add Product\n3. Remove Product Category\n4. Remove Product\n5. Update Product Details\n6. Update Product Category Details\n7. Inventory Analysis\n8. Sneek in Inventory\n9. Add more stocks\n10. Logout");
            int aa = sc.readInt();
            if (aa == 1) {
                add_product_cat(connection, sc);
            }
            else if (aa == 2) {
//                printProductCategories(connection);
                add_product(connection,sc, managerID);
            }
            else if (aa == 3) {
                remove_product_cat(connection,sc);
            }
            else if (aa == 4) {
                while (true) {
                    System.out.println("Choose : \n1. Delete by ProductID\n2. Delete by Name\n3. Back\n");
                    int aa_1 = sc.readInt();
                    if (aa_1 == 1) {
                        remove_product_byID(connection, sc);
                    } else if (aa_1 == 2) {
                        System.out.println("Under Development");
                    }
                    else{
                        break;
                    }
                }
            }
            else if (aa == 5) {
                //currently underDevelopment
//                System.out.println("Under Development");
                System.out.println("Choose : \n1. Update Name");
                int ab = sc.readInt();

            }
            else if (aa == 6) {
                //currently underDevelopment
                System.out.println("Under Development");
            }
            else if (aa == 7) {
                performInventoryAnalysis(connection);
            }
            else if (aa == 8) {
                while (true){
                    System.out.println("Choose : \n1. View All Products\n2. View Product Categories\n3. Back");
                    int a_1 = sc.readInt();
                    if(a_1==1){
//                        System.out.println("Under Development");
                        //it fetches the output as all the product will be listed then the product stocks are shown according to warehouses present.
                        printProducts1(connection,managerID);
                    }
                    else if(a_1==2){
//                        System.out.println("Under Development");
                        //it fetches the output as all the product categories will e listed then the total product stocks are shown according to warehouse ids
                        printProductCategoriesByWarehouse(connection,managerID);
                    } else if (a_1==3) {
                        break;
                    }
                    else{
                        System.out.println("Choose a valid option!!");
                    }
                }
            }
            else if (aa==9){
//                System.out.println("Under Development");
                increaseStockQuantity(connection,sc);
            }
            else if (aa == 10) {
                return;
            }else {
                System.out.println("Choose a valid option!!!");
            }
        }
    }

    private static void increaseStockQuantity(Connection connection, ScannerClass scanner) {
        scanner.readString();
        try {
            while (true) {
                System.out.println("Enter ProductID of the product to increase stock quantity:");
                int productID = scanner.readInt();

                System.out.println("Enter the quantity to add:");
                int quantityToAdd = scanner.readInt();

                //validating inputs
                if (productID <= 0 || quantityToAdd <= 0) {
                    System.out.println("Invalid input. ProductID and quantity to add must be greater than 0.");
                    continue;
                }

                //increasing stock quantity query
                String INSERT_QUERY = "UPDATE stores SET StockQty = StockQty + ? WHERE ProductID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
                preparedStatement.setInt(1, quantityToAdd);
                preparedStatement.setInt(2, productID);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Stock quantity increased successfully!");
                } else {
                    System.out.println("Failed to increase stock quantity!");
                }
                preparedStatement.close();
                break; //exiting after successful insertion/increasing in stock
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   public static void performInventoryAnalysis(Connection connection) {
        try {
            // Retrieve data from the database
            Map<Integer, Map<Integer, Integer>> warehouseProductQuantities = getWarehouseProductQuantities(connection);

            // Aggregate data
            Map<Integer, Integer> totalProductQuantities = calculateTotalProductQuantities(warehouseProductQuantities);
            Map<Integer, Integer> totalWarehouseQuantities = calculateTotalWarehouseQuantities(warehouseProductQuantities);

            // Perform analysis
            int maxQuantityProductID = getMaxQuantityProductID(totalProductQuantities);
            int maxQuantityWarehouseID = getMaxQuantityWarehouseID(totalWarehouseQuantities);

            // Display analysis results
            displayAnalysisResults(maxQuantityProductID, maxQuantityWarehouseID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Map<Integer, Map<Integer, Integer>> getWarehouseProductQuantities(Connection connection) throws SQLException {
        //retrieving product quantities per warehouse
        Map<Integer, Map<Integer, Integer>> warehouseProductQuantities = new HashMap<>();
        String query = "SELECT WarehouseID, ProductID, StockQty FROM stores";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int warehouseID = resultSet.getInt("WarehouseID");
                int productID = resultSet.getInt("ProductID");
                int stockQty = resultSet.getInt("StockQty");
                warehouseProductQuantities.computeIfAbsent(warehouseID, k -> new HashMap<>())
                        .put(productID, stockQty);
            }
        }
        return warehouseProductQuantities;
    }

    private static Map<Integer, Integer> calculateTotalProductQuantities(Map<Integer, Map<Integer, Integer>> warehouseProductQuantities) {
        //total product quantities across all warehouses
        Map<Integer, Integer> totalProductQuantities = new HashMap<>();
        for (Map<Integer, Integer> productQuantities : warehouseProductQuantities.values()) {
            for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
                int productID = entry.getKey();
                int quantity = entry.getValue();
                totalProductQuantities.merge(productID, quantity, Integer::sum);
            }
        }
        return totalProductQuantities;
    }

    private static Map<Integer, Integer> calculateTotalWarehouseQuantities(Map<Integer, Map<Integer, Integer>> warehouseProductQuantities) {
        //total quantities per warehouse
        Map<Integer, Integer> totalWarehouseQuantities = new HashMap<>();
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : warehouseProductQuantities.entrySet()) {
            int warehouseID = entry.getKey();
            Map<Integer, Integer> productQuantities = entry.getValue();
            int totalWarehouseQuantity = productQuantities.values().stream().mapToInt(Integer::intValue).sum();
            totalWarehouseQuantities.put(warehouseID, totalWarehouseQuantity);
        }
        return totalWarehouseQuantities;
    }

private static int getMaxQuantityProductID(Map<Integer, Integer> totalProductQuantities) {
        // Find the product with the highest total quantity
        return Collections.max(totalProductQuantities.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    private static int getMaxQuantityWarehouseID(Map<Integer, Integer> totalWarehouseQuantities) {
        // Find the warehouse with the highest total quantity
        return Collections.max(totalWarehouseQuantities.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    private static void displayAnalysisResults(int maxQuantityProductID, int maxQuantityWarehouseID) {
        System.out.println("Inventory Analysis:");
        System.out.println("Product with Highest Total Quantity (Product ID): " + maxQuantityProductID);
        System.out.println("Warehouse with Highest Total Quantity (Warehouse ID): " + maxQuantityWarehouseID);
    }

    private static void add_product_cat(Connection connection, ScannerClass sc) {
        sc.readString();
        try {
            while (true) {
                System.out.println("Enter Product Category Name:");
                String productCatName = sc.readString();
                if (productCatName.trim().isEmpty()) {
                    System.out.println("Product Category Name cannot be empty. Please try again.");
                } else {
                    System.out.println("Enter Product Category Description:");
                    String productCatDescription = sc.readString();
                    //get next available ProductCatID
                    int nextProductCatID = getNextProductCatID(connection);

                    //inserting data into ProductCategory table
                    String insertQuery = "INSERT INTO ProductCategory (ProductCatID, ProductCatName, ProductCatDescription) VALUES (?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setInt(1, nextProductCatID);
                    preparedStatement.setString(2, productCatName);
                    preparedStatement.setString(3, productCatDescription);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Product category added successfully!");
                    } else {
                        System.out.println("Failed to add product category!");
                    }
                    preparedStatement.close();
                    break; //Exiting the loop after successful insertion
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        
    private static int getNextProductCatID(Connection connection) throws SQLException {
        String query = "SELECT MAX(ProductCatID) FROM ProductCategory";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1) + 1;
        }
        return 1; // If no record found, start from 1
    }
    private static void printProductCategories(Connection connection) {
        try {
            // Query to select all rows from ProductCategory table
            String query = "SELECT * FROM ProductCategory";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Print the header
            System.out.println("Product Category ID | Product Category Name | Product Category Description");
            System.out.println("--------------------------------------------------------------------------");

            // Print each row of the result set
            while (resultSet.next()) {
                int productCatID = resultSet.getInt("ProductCatID");
                String productCatName = resultSet.getString("ProductCatName");
                String productCatDescription = resultSet.getString("ProductCatDescription");

                // Print the row
                System.out.printf("%-20s  | %-20s  | %-30s%n", productCatID, productCatName, productCatDescription);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void add_product(Connection connection, ScannerClass sc, int managerID) {
        sc.readString();
        try {
            printProductCategories(connection);

            System.out.println("Enter Product Name:");
            String productName = sc.readString();

            System.out.println("Enter Product Price:");
            double productPrice = sc.readDouble();

            System.out.println("Enter Product Category ID:");
            int productCatID = sc.readInt();
            sc.readString();
            System.out.println("Enter Product Description :");
            String productDescription = sc.readString();
            System.out.println("Enter Brand Name :");
            String brandName = sc.readString();

            System.out.println("Enter Stock Quantity:");
            int stockQuantity = sc.readInt();

            //get the next available ProductID
            int nextProductID = getNextProductID(connection);

            //inserting data into Product table
            String insertQuery = "INSERT INTO Product (ProductID, ProductName, ProductPrice, ProductDescription, Brandname, ProductCatID) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, nextProductID);
            preparedStatement.setString(2, productName);
            preparedStatement.setDouble(3, productPrice);
            preparedStatement.setString(4, productDescription);
            preparedStatement.setString(5, brandName);
            preparedStatement.setInt(6, productCatID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product added successfully!");

                //update stocks in associated warehouses
                updateStockInWarehouses(connection, nextProductID, stockQuantity, managerID, sc);
            } else {
                System.out.println("Failed to add product!");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void updateStockInWarehouses(Connection connection, int productID, int stockQuantity, int managerID, ScannerClass sc) {
        try {
            String branchQuery = "SELECT BranchID FROM branch WHERE ManagerID = ?";
            PreparedStatement branchStatement = connection.prepareStatement(branchQuery);
            branchStatement.setInt(1, managerID);
            ResultSet branchResult = branchStatement.executeQuery();
            int branchID = -1;
            if (branchResult.next()) {
                branchID = branchResult.getInt("BranchID");
            }
            branchStatement.close();
            String warehouseQuery = "SELECT WarehouseID FROM Warehouse WHERE BranchID = ?";
            PreparedStatement warehouseStatement = connection.prepareStatement(warehouseQuery);
            warehouseStatement.setInt(1, branchID);
            ResultSet warehouseResult = warehouseStatement.executeQuery();
            List<Integer> warehouseIDs = new ArrayList<>();
            while (warehouseResult.next()) {
                int warehouseID = warehouseResult.getInt("WarehouseID");
                warehouseIDs.add(warehouseID);
            }
            if (warehouseIDs.isEmpty()) {
                System.out.println("No warehouses found under this branch.");
            } else {
                while (true) {
                    System.out.println("Warehouses under this branch:");
                    for (Integer id : warehouseIDs) {
                        System.out.println(id);
                    }
                    System.out.println("Select an option:");
                    System.out.println("1. Add stock to a specific warehouse\n2. Distribute stock quantity among all warehouses\n3. Back\n");
                    int option = sc.readInt();
                    if (option == 1) {
                        System.out.println("Enter the WarehouseID to add stock:");
                        int specificWarehouseID = sc.readInt();
                        if(warehouseIDs.contains(specificWarehouseID)) {
                            addStockToWarehouse(connection, productID, stockQuantity, specificWarehouseID);
                        }else{
                            System.out.println("Invalid WarehouseID");
                        }
                    } else if (option == 2) {
                        for (Integer warehouseID : warehouseIDs) {
                            addStockToWarehouse(connection, productID, stockQuantity, warehouseID);
                        }
                    } else if( option == 3){
                        System.out.println("Back");
                        return;
                    }
                    else {
                        System.out.println("Invalid option selected.");
                    }
                }
            }
            warehouseStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

private static void addStockToWarehouse(Connection connection, int productID, int stockQuantity, int warehouseID) {
        try {
            String stockUpdateQuery = "INSERT INTO stores (warehouseID, productID, stockQty, ReturnableStatus) VALUES (?, ?, ?, ?)";
            PreparedStatement stockStatement = connection.prepareStatement(stockUpdateQuery);
            stockStatement.setInt(1, warehouseID);
            stockStatement.setInt(2, productID);
            stockStatement.setInt(3, stockQuantity);
            stockStatement.setString(4, "Yes");
            stockStatement.executeUpdate();
            stockStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getNextProductID(Connection connection) throws SQLException {
        String query = "SELECT MAX(ProductID) FROM Product";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1) + 1;
        }
        return 1; // If no record found, start from 1
    }

    private static void remove_product_cat(Connection connection, ScannerClass sc){
        try{
            printProductCategories(connection);
            System.out.println("*********!! If you remove any product category all product related to that category would also be removed !!*********\n");
            //System.out.println("You need all manager's permission for this!!");

            System.out.println("Enter the ID of the Product Category you want to remove:");
            int productCatID = sc.readInt();
            sc.readString();
            System.out.println("Are you sure you want to remove this product category? (yes/no)");
            String confirmation = sc.readString().trim().toLowerCase();
            if (confirmation.equals("yes")) {
                //deleting the product category and related products
                deleteProductCategory(connection, productCatID);
            } else {
                System.out.println("Product category removal cancelled.");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void deleteProductCategory(Connection connection, int productCatID) throws SQLException {
        String deleteCategoryQuery = "DELETE FROM ProductCategory WHERE ProductCatID = ?";
        PreparedStatement deleteCategoryStatement = connection.prepareStatement(deleteCategoryQuery);
        deleteCategoryStatement.setInt(1, productCatID);
        int categoryDeleted = deleteCategoryStatement.executeUpdate();
        deleteCategoryStatement.close();

        if (categoryDeleted > 0) {
            System.out.println("Product category removed successfully!");
//            System.out.println(productsDeleted + " products related to this category were also removed.");
        } else {
            System.out.println("Failed to remove product category!");
        }
    }

  private static void printProducts(Connection connection) {
        try {
            // Query to select all rows from Product table
            String query = "SELECT * FROM Product";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Print the header
            System.out.println("Product ID | Product Name | Product Price | Product Description | Brand Name | Product Category ID");
            System.out.println("----------------------------------------------------------------------------------------------------");

            // Print each row of the result set
            while (resultSet.next()) {
                int productID = resultSet.getInt("ProductID");
                String productName = resultSet.getString("ProductName");
                double productPrice = resultSet.getDouble("ProductPrice");
                String productDescription = resultSet.getString("ProductDescription");
                String brandName = resultSet.getString("Brandname");
                int productCatID = resultSet.getInt("ProductCatID");

                // Print the row
                System.out.printf("%-10s | %-12s | %-13s | %-20s | %-10s | %-18s%n", productID, productName, productPrice, productDescription, brandName, productCatID);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printProducts1(Connection connection, int managerID) {
        try {
            // Retrieve the branch ID of the manager
            String branchQuery = "SELECT BranchID FROM branch WHERE ManagerID = ?";
            PreparedStatement branchStatement = connection.prepareStatement(branchQuery);
            branchStatement.setInt(1, managerID);
            ResultSet branchResult = branchStatement.executeQuery();

            int branchID = -1;
            if (branchResult.next()) {
                branchID = branchResult.getInt("BranchID");
            } else {
                System.out.println("No branch found for the manager.");
                return;
            }

            branchStatement.close();

            // Query to select product details along with the number of stocks left in each warehouse under the branch
            String query = "SELECT P.ProductID, P.ProductName, P.ProductPrice, P.ProductDescription, P.BrandName, P.ProductCatID, " +
                    "W.WarehouseID, COALESCE(SUM(S.stockQty), 0) AS StocksLeft " +
                    "FROM Product P " +
                    "CROSS JOIN Warehouse W " +
                    "LEFT JOIN stores S ON P.ProductID = S.productID AND W.WarehouseID = S.warehouseID " +
                    "WHERE W.BranchID = ? " +
                    "GROUP BY P.ProductID, W.WarehouseID";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, branchID);
            ResultSet resultSet = statement.executeQuery();

            // Print the header
            System.out.println("Product ID | Product Name | Product Price | Product Description | Brand Name | Product Category ID | Warehouse ID | Stocks Left");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------");

            // Print each row of the result set
            while (resultSet.next()) {
                int productID = resultSet.getInt("ProductID");
                String productName = resultSet.getString("ProductName");
                double productPrice = resultSet.getDouble("ProductPrice");
                String productDescription = resultSet.getString("ProductDescription");
                String brandName = resultSet.getString("BrandName");
                int productCatID = resultSet.getInt("ProductCatID");
                int warehouseID = resultSet.getInt("WarehouseID");
                int stocksLeft = resultSet.getInt("StocksLeft");

                // Print the row
                System.out.printf("%-10s | %-12s | %-13s | %-20s | %-10s | %-18s | %-12s | %-10s%n", productID, productName, productPrice, productDescription, brandName, productCatID, warehouseID, stocksLeft);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printProductCategoriesByWarehouse(Connection connection, int managerID) {
        try {
            //retrieving the branch ID of the manager
            String branchQuery = "SELECT BranchID FROM branch WHERE ManagerID = ?";
            PreparedStatement branchStatement = connection.prepareStatement(branchQuery);
            branchStatement.setInt(1, managerID);
            ResultSet branchResult = branchStatement.executeQuery();

            int branchID = -1;
            if (branchResult.next()) {
                branchID = branchResult.getInt("BranchID");
            } else {
                System.out.println("No branch found for the manager.");
                return;
            }

            branchStatement.close();

            //SQL Query to select product category details along with the number of stocks left in each warehouse under the branch
            String query = "SELECT PC.ProductCatID, PC.ProductCatName, " +
                    "W.WarehouseID, COALESCE(SUM(S.stockQty), 0) AS StocksLeft " +
                    "FROM productcategory PC " +
                    "CROSS JOIN Warehouse W " +
                    "LEFT JOIN Product P ON PC.ProductCatID = P.ProductCatID " +
                    "LEFT JOIN stores S ON P.ProductID = S.productID AND W.WarehouseID = S.warehouseID " +
                    "WHERE W.BranchID = ? " +
                    "GROUP BY PC.ProductCatID, W.WarehouseID";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, branchID);
            ResultSet resultSet = statement.executeQuery();

            //header
            System.out.println("Category ID | Category Name | Warehouse ID | Stocks Left");
            System.out.println("--------------------------------------------------------------------------------");

            while (resultSet.next()) {
                int categoryID = resultSet.getInt("ProductCatID");
                String categoryName = resultSet.getString("ProductCatName");
                int warehouseID = resultSet.getInt("WarehouseID");
                int stocksLeft = resultSet.getInt("StocksLeft");

                //print the row
                System.out.printf("%-11s | %-13s | %-12s | %-10s%n", categoryID, categoryName, warehouseID, stocksLeft);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void remove_product_byID(Connection connection, ScannerClass sc){
        try{
            printProducts(connection);
            System.out.println("Enter the ID of the Product you want to remove:");
            int productID = sc.readInt();
            sc.readString();
            System.out.println("Are you sure you want to remove this product ? (yes/no)");
            String confirmation = sc.readString().trim().toLowerCase();
            if (confirmation.equals("yes")) {
                deleteProductByID(connection, productID);
            } else {
                System.out.println("Product category removal cancelled.");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void deleteProductByID(Connection connection, int productID) throws SQLException{
        String deleteProductQuery = "DELETE FROM Product WHERE ProductID = ?";
        PreparedStatement deleteProductStatement = connection.prepareStatement(deleteProductQuery);
        deleteProductStatement.setInt(1, productID);
        int productDeleted = deleteProductStatement.executeUpdate();
        deleteProductStatement.close();
        if (productDeleted > 0) {
            System.out.println("Product removed successfully!");
        } else {
            System.out.println("Failed to remove product !");
        }
    }



    //rough tries

//    public static void increaseStockQuantityForProduct(int productId, int quantityToAdd) {
//        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
//            // SQL query to update stock quantity in all warehouses for the given product
//            String sql = "UPDATE stores SET StockQty = StockQty + ? WHERE ProductID = ?";
//
//            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//                pstmt.setInt(1, quantityToAdd);
//                pstmt.setInt(2, productId);
//
//                // Execute the update
//                int rowsAffected = pstmt.executeUpdate();
//                System.out.println(rowsAffected + " warehouses updated successfully.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    private static void remove_product_byName(Connection connection, ScannerClass sc){
//        sc.readString();
//        try{
//            printProducts(connection);
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

//    private static void printProducts1(Connection connection, int managerID) {
//        try {
//            // Retrieve the branch ID of the manager
//            String branchQuery = "SELECT BranchID FROM branch WHERE ManagerID = ?";
//            PreparedStatement branchStatement = connection.prepareStatement(branchQuery);
//            branchStatement.setInt(1, managerID);
//            ResultSet branchResult = branchStatement.executeQuery();
//
//            int branchID = -1;
//            if (branchResult.next()) {
//                branchID = branchResult.getInt("BranchID");
//            } else {
//                System.out.println("No branch found for the manager.");
//                return;
//            }
//
//            branchStatement.close();
//
//            // Query to select product details along with the number of stocks left in warehouses under the branch
//            String query = "SELECT P.ProductID, P.ProductName, P.ProductPrice, P.ProductDescription, P.Brandname, P.ProductCatID, SUM(S.stockQty) AS StocksLeft " +
//                    "FROM Product P " +
//                    "LEFT JOIN stores S ON P.ProductID = S.productID " +
//                    "INNER JOIN Warehouse W ON S.warehouseID = W.WarehouseID " +
//                    "WHERE W.BranchID = ? " +
//                    "GROUP BY P.ProductID";
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setInt(1, branchID);
//            ResultSet resultSet = statement.executeQuery();
//
//            // Print the header
//            System.out.println("Product ID | Product Name | Product Price | Product Description | Brand Name | Product Category ID | Stocks Left");
//            System.out.println("--------------------------------------------------------------------------------------------------------------");
//
//            // Print each row of the result set
//            while (resultSet.next()) {
//                int productID = resultSet.getInt("ProductID");
//                String productName = resultSet.getString("ProductName");
//                double productPrice = resultSet.getDouble("ProductPrice");
//                String productDescription = resultSet.getString("ProductDescription");
//                String brandName = resultSet.getString("Brandname");
//                int productCatID = resultSet.getInt("ProductCatID");
//                int stocksLeft = resultSet.getInt("StocksLeft");
//
//                // Print the row
//                System.out.printf("%-10s | %-12s | %-13s | %-20s | %-10s | %-18s | %-10s%n", productID, productName, productPrice, productDescription, brandName, productCatID, stocksLeft);
//            }
//
//            resultSet.close();
//            statement.close();
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
}


