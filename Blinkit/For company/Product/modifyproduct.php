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
    $existing_product_name = $conn->real_escape_string($_POST['existing_product_name']);
    $existing_brand_name = $conn->real_escape_string($_POST['existing_brand_name']);
    $existing_product_cat_id = $conn->real_escape_string($_POST['existing_product_cat_id']);
    $existing_product_price = $conn->real_escape_string($_POST['existing_product_price']);
    $existing_product_description = $conn->real_escape_string($_POST['existing_product_description']);
    $new_product_name = $conn->real_escape_string($_POST['new_product_name']);
    $new_brand_name = $conn->real_escape_string($_POST['new_brand_name']);
    $new_product_cat_id = $conn->real_escape_string($_POST['new_product_cat_id']);
    $new_product_price = $conn->real_escape_string($_POST['new_product_price']);
    $new_product_description = $conn->real_escape_string($_POST['new_product_description']);

    // Check if the provided existing data exist in the Product table
    $check_query = "SELECT * FROM Product WHERE ProductName = '$existing_product_name' AND BrandName = '$existing_brand_name' AND ProductCatID = '$existing_product_cat_id' AND ProductPrice = '$existing_product_price'";
    $result = $conn->query($check_query);

    if ($result->num_rows == 0) {
        echo "Error: Product not found.";
    } else {
        // Update the product with the provided details
        $update_query = "UPDATE Product SET ProductName = '$new_product_name', BrandName = '$new_brand_name', ProductCatID = '$new_product_cat_id', ProductPrice = '$new_product_price', ProductDescription = '$new_product_description' WHERE ProductName = '$existing_product_name' AND BrandName = '$existing_brand_name' AND ProductCatID = '$existing_product_cat_id' AND ProductPrice = '$existing_product_price'";

        if ($conn->query($update_query) === TRUE) {
            echo "Product modified successfully";
        } else {
            echo "Error updating product: " . $conn->error;
        }
    }
}

// Close connection
$conn->close();
?>
