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
    $branchID = $_POST['branchID'];
    $managerID = $_POST['managerID'];

    // Check if the provided Branch ID and Manager ID exist
    $sql = "SELECT * FROM Branch WHERE BranchID = $branchID AND ManagerID = $managerID";
    $result = $conn->query($sql);

    // If both Branch ID and Manager ID exist, proceed to remove the branch
    if ($result->num_rows > 0) {
        // Remove branch
        $sqlDelete = "DELETE FROM Branch WHERE BranchID = $branchID AND ManagerID = $managerID";

        if ($conn->query($sqlDelete) === TRUE) {
            echo "<p>Branch removed successfully!</p>";
        } else {
            echo "Error deleting record: " . $conn->error;
        }
    } else {
        echo "<p>Either Branch ID or Manager ID does not exist. Please enter valid IDs.</p>";
    }
}

// Close the database connection
$conn->close();
?>
