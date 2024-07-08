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
    $product_cat_name = $conn->real_escape_string($_POST['product_cat_name']);
    $product_cat_description = $conn->real_escape_string($_POST['product_cat_description']);

    // Check if the product category already exists by name (case insensitive)
    $check_query = "SELECT * FROM ProductCategory WHERE LOWER(ProductCatName) = LOWER('$product_cat_name')";
    $result = $conn->query($check_query);

    if ($result->num_rows > 0) {
        echo "Error: Product category with the same name already exists.";
    } else {
        // Insert the product category into the database
        $insert_query = "INSERT INTO ProductCategory (ProductCatName, ProductCatDescription) VALUES ('$product_cat_name', '$product_cat_description')";

        if ($conn->query($insert_query) === TRUE) {
            echo "Product category added successfully.";
        } else {
            echo "Error adding product category: " . $conn->error;
        }
    }
}

// Close connection
$conn->close();
?>
