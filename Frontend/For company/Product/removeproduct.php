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
    $brand_name = $conn->real_escape_string($_POST['brand_name']);
    $product_cat_id = $conn->real_escape_string($_POST['product_cat_id']);

    // Check if the provided product name, brand name, and product category ID exist in the Product table
    $check_query = "SELECT * FROM Product WHERE ProductName = '$product_name' AND BrandName = '$brand_name' AND ProductCatID = '$product_cat_id'";
    $result = $conn->query($check_query);

    if ($result->num_rows == 0) {
        echo "Error: Product not found.";
    } else {
        // Remove the product from the Product table
        $remove_query = "DELETE FROM Product WHERE ProductName = '$product_name' AND BrandName = '$brand_name' AND ProductCatID = '$product_cat_id'";

        if ($conn->query($remove_query) === TRUE) {
            echo "Product removed successfully";
        } else {
            echo "Error removing product: " . $conn->error;
        }
    }
}

// Close connection
$conn->close();
?>
