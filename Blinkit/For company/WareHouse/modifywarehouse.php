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
    $newStreetNo = $_POST['newStreetNo'];
    $newLandmark = $_POST['newLandmark'];
    $newCity = $_POST['newCity'];
    $newState = $_POST['newState'];
    $newPincode = $_POST['newPincode'];
    $newBranchID = $_POST['newBranchID'];
    $newContact = $_POST['newContact'];

    // Check if the provided current Warehouse ID and Branch ID exist
    $sqlWarehouse = "SELECT * FROM warehouse WHERE WareHouseID = $currentWarehouseID AND BranchID = $currentBranchID";
    $resultWarehouse = $conn->query($sqlWarehouse);

    // Check if the provided new Branch ID exists
    $sqlBranch = "SELECT * FROM branch WHERE BranchID = $newBranchID";
    $resultBranch = $conn->query($sqlBranch);

    // If current Warehouse ID and Branch ID exist, and new Branch ID also exists, proceed to modify the warehouse details
    if ($resultWarehouse->num_rows > 0 && $resultBranch->num_rows > 0) {
        // Update warehouse details
        $sqlUpdate = "UPDATE warehouse SET streetNo = '$newStreetNo', landmark = '$newLandmark', city = '$newCity', 
                      state = '$newState', pincode = $newPincode, BranchID = $newBranchID, contact = $newContact 
                      WHERE WareHouseID = $currentWarehouseID AND BranchID = $currentBranchID";

        if ($conn->query($sqlUpdate) === TRUE) {
            echo "<p>Warehouse details modified successfully!</p>";
        } else {
            echo "Error updating record: " . $conn->error;
        }
    } else {
        echo "<p>Either current Warehouse ID or Branch ID does not exist, or new Branch ID does not exist. Please enter valid IDs.</p>";
    }
}

// Close the database connection
$conn->close();
?>
