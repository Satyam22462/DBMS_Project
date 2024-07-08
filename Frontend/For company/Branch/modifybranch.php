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
    $currentBranchID = $_POST['currentBranchID'];
    $currentManagerID = $_POST['currentManagerID'];
    $newStreetNo = $_POST['newStreetNo'];
    $newLandmark = $_POST['newLandmark'];
    $newCity = $_POST['newCity'];
    $newState = $_POST['newState'];
    $newPincode = $_POST['newPincode'];
    $newManagerID = $_POST['newManagerID'];
    $newContact = $_POST['newContact'];

    // Check if the provided current Branch ID exists in the Branch table
    $sqlBranch = "SELECT * FROM Branch WHERE BranchID = $currentBranchID AND ManagerID = $currentManagerID";
    $resultBranch = $conn->query($sqlBranch);

    // Check if the provided new Manager ID exists in the Manager table
    $sqlManager = "SELECT * FROM Manager WHERE ManagerID = $newManagerID";
    $resultManager = $conn->query($sqlManager);

    // If current Branch ID and Manager ID exist, and new Manager ID also exists, proceed to modify the branch details
    if ($resultBranch->num_rows > 0 && $resultManager->num_rows > 0) {
        // Update branch details
        $sqlUpdate = "UPDATE Branch SET streetNo = '$newStreetNo', landmark = '$newLandmark', city = '$newCity', 
                      state = '$newState', pincode = $newPincode, ManagerID = $newManagerID, contact = $newContact 
                      WHERE BranchID = $currentBranchID";

        if ($conn->query($sqlUpdate) === TRUE) {
            echo "<p>Branch details modified successfully!</p>";
        } else {
            echo "Error updating record: " . $conn->error;
        }
    } else {
        echo "<p>Either current Branch ID or Manager ID does not exist, or new Manager ID does not exist. Please enter valid IDs.</p>";
    }
}

// Close the database connection
$conn->close();
?>
