<?php
// Establish connection to your database (replace these values with your actual database credentials)
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

// Check if the form is submitted
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Retrieve form data
    $currentWarehouseID = $_POST['currentWarehouseID'];
    $currentBranchID = $_POST['currentBranchID'];

    // Check if the provided current Warehouse ID and Branch ID exist
    $sqlWarehouse = "SELECT * FROM warehouse WHERE WareHouseID = $currentWarehouseID AND BranchID = $currentBranchID";
    $resultWarehouse = $conn->query($sqlWarehouse);

    // If current Warehouse ID and Branch ID exist, proceed to remove the warehouse record
    if ($resultWarehouse->num_rows > 0) {
        // Delete warehouse record
        $sqlDelete = "DELETE FROM warehouse WHERE WareHouseID = $currentWarehouseID AND BranchID = $currentBranchID";

        if ($conn->query($sqlDelete) === TRUE) {
            echo "<p>Warehouse record removed successfully!</p>";
        } else {
            echo "Error deleting record: " . $conn->error;
        }
    } else {
        echo "<p>Either current Warehouse ID or Branch ID does not exist. Please enter valid IDs.</p>";
    }
}

// Close the database connection
$conn->close();
?>
