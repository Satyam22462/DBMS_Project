<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
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

    // Check if all required parameters are provided
    if (!isset($_POST['existing_product_name']) || !isset($_POST['existing_product_cat_id']) || !isset($_POST['existing_product_cat_description']) || !isset($_POST['new_product_cat_name']) || !isset($_POST['new_product_cat_description']) || !isset($_POST['new_product_cat_id'])) {
        echo "Error: Missing parameters.";
        exit();
    }

    // Escape user inputs for security
    $existing_product_name = $conn->real_escape_string($_POST['existing_product_name']);
    $existing_product_cat_id = $conn->real_escape_string($_POST['existing_product_cat_id']);
    $existing_product_cat_description = $conn->real_escape_string($_POST['existing_product_cat_description']);
    $new_product_cat_name = $conn->real_escape_string($_POST['new_product_cat_name']);
    $new_product_cat_description = $conn->real_escape_string($_POST['new_product_cat_description']);
    $new_product_cat_id = $conn->real_escape_string($_POST['new_product_cat_id']);

    // Check if the provided product category exists
    $check_query = "SELECT * FROM ProductCategory WHERE ProductCatID = '$existing_product_cat_id' AND ProductCatDescription = '$existing_product_cat_description' AND ProductCatName = '$existing_product_name'";
    $result = $conn->query($check_query);

    if ($result->num_rows == 0) {
        echo "Error: Product category does not exist.";
    } else {
        // Update the product category with the provided details
        $update_query = "UPDATE ProductCategory SET ProductCatID = '$new_product_cat_id', ProductCatName = '$new_product_cat_name', ProductCatDescription = '$new_product_cat_description' WHERE ProductCatID = '$existing_product_cat_id'";

        if ($conn->query($update_query) === TRUE) {
            echo "Product category modified successfully";
        } else {
            echo "Error updating product category: " . $conn->error;
        }
    }

    // Close connection
    $conn->close();
}
?>
