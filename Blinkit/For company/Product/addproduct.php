<?php
// Establish a connection to the database
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "blinkit";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Process form data when form is submitted
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Escape user inputs for security
    $product_name = $conn->real_escape_string($_POST['product_name']);
    $product_price = $conn->real_escape_string($_POST['product_price']);
    $product_description = $conn->real_escape_string($_POST['product_description']);
    $brand_name = $conn->real_escape_string($_POST['brand_name']);
    $product_cat_id = $conn->real_escape_string($_POST['product_cat_id']);
    $product_quantity = $conn->real_escape_string($_POST['product_quantity']);
    $returnable_status = $conn->real_escape_string($_POST['returnable_status']);

    // Check if the provided product_cat_id exists in the ProductCategory table
    $check_query = "SELECT * FROM ProductCategory WHERE ProductCatID = '$product_cat_id'";
    $result = $conn->query($check_query);

    if ($result->num_rows == 0) {
        echo "Error: Product Category ID does not exist.";
    } else {
        // Attempt to insert data into the Product table
        $product_sql = "INSERT INTO Product (ProductName, ProductPrice, ProductDescription, BrandName, ProductCatID)
                        VALUES ('$product_name', '$product_price', '$product_description', '$brand_name', '$product_cat_id')";

        if ($conn->query($product_sql) === TRUE) {
            // Get the last inserted ProductID
            $last_product_id = $conn->insert_id;

            // Attempt to insert data into the stores table
            $stores_sql = "INSERT INTO stores (WareHouseID, ProductID, StockQty, ReturnableStatus)
                           VALUES (1, '$last_product_id', '$product_quantity', '$returnable_status')";

            if ($conn->query($stores_sql) === TRUE) {
                echo "New record created successfully";
            } else {
                echo "Error: " . $stores_sql . "<br>" . $conn->error;
            }
        } else {
            echo "Error: " . $product_sql . "<br>" . $conn->error;
        }
    }
}

// Close connection
$conn->close();
?>
