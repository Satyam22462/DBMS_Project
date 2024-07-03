<?php
$servername = "localhost";
$username = "root";
$password = "";
$database = "blinkit";

$conn = new mysqli($servername, $username, $password, $database);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$searchTerm = isset($_GET['search']) ? $_GET['search'] : '';

if (empty(trim($searchTerm))) {
    $productQuery = "SELECT p.*, pc.ProductCatName, pc.ProductCatDescription, s.StockQty, s.ReturnableStatus 
                    FROM Product p
                    LEFT JOIN ProductCategory pc ON p.ProductCatID = pc.ProductCatID
                    LEFT JOIN stores s ON p.ProductID = s.ProductID";
} else {
    $productQuery = "SELECT p.*, pc.ProductCatName, pc.ProductCatDescription, s.StockQty, s.ReturnableStatus 
                    FROM Product p
                    LEFT JOIN ProductCategory pc ON p.ProductCatID = pc.ProductCatID
                    LEFT JOIN stores s ON p.ProductID = s.ProductID
                    WHERE p.ProductName LIKE '%$searchTerm%'";
}

$productResult = $conn->query($productQuery);

$searchResults = array();
while ($row = $productResult->fetch_assoc()) {
    $searchResults[] = $row;
}

$conn->close();

echo json_encode($searchResults);
?>
