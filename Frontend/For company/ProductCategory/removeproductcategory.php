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
    if (!isset($_POST['product_cat_id']) || !isset($_POST['product_cat_name']) || !isset($_POST['product_cat_description'])) {
        echo "Error: Missing parameters.";
        exit();
    }

    // Escape user inputs for security
    $product_cat_id = $conn->real_escape_string($_POST['product_cat_id']);
    $product_cat_name = $conn->real_escape_string($_POST['product_cat_name']);
    $product_cat_description = $conn->real_escape_string($_POST['product_cat_description']);

    // Check if the provided product category exists
    $check_query = "SELECT * FROM ProductCategory WHERE ProductCatID = '$product_cat_id' AND ProductCatName = '$product_cat_name' AND ProductCatDescription = '$product_cat_description'";
    $result = $conn->query($check_query);

    if ($result->num_rows == 0) {
        echo "Error: Product category does not exist.";
    } else {
        // Delete the product category from the database
        $delete_query = "DELETE FROM ProductCategory WHERE ProductCatID = '$product_cat_id' AND ProductCatName = '$product_cat_name' AND ProductCatDescription = '$product_cat_description'";

        if ($conn->query($delete_query) === TRUE) {
            echo "Product category removed successfully";
        } else {
            echo "Error removing product category: " . $conn->error;
        }
    }

    // Close connection
    $conn->close();
}
?>
