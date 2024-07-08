<?php
// Check if the form is submitted
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Retrieve form data
    $streetNo = $_POST['streetNo'];
    $landmark = $_POST['landmark'];
    $city = $_POST['city'];
    $state = $_POST['state'];
    $pincode = $_POST['pincode'];
    $BranchID = $_POST['BranchID'];
    $contact = $_POST['contact'];

    // Validate pincode (6 digits)
    if (!preg_match('/^[0-9]{6}$/', $pincode)) {
        echo "Invalid pincode format. Please enter 6 digits.";
        exit;
    }

    // Validate contact number (10 to 12 digits)
    if (!preg_match('/^[0-9]{10,12}$/', $contact)) {
        echo "Invalid contact number format. Please enter 10 to 12 digits.";
        exit;
    }

    // Establish a database connection
    $servername = "localhost";
    $username = "root";
    $password = "";
    $dbname = "blinkit"; // Update with your actual database name

    $conn = new mysqli($servername, $username, $password, $dbname);

    // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    // Check if BranchID exists in the Branch table
    $branchCheckQuery = "SELECT * FROM Branch WHERE BranchID = '$BranchID'";
    $result = $conn->query($branchCheckQuery);

    if ($result->num_rows == 0) {
        echo "Error: Branch ID does not exist.";
        exit;
    }

    // Insert data into warehouse table
    $sql = "INSERT INTO warehouse (streetNo, landmark, city, state, pincode, BranchID, contact)
            VALUES ('$streetNo', '$landmark', '$city', '$state', '$pincode', '$BranchID', '$contact')";

    if ($conn->query($sql) === TRUE) {
        echo "New record created successfully";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }

    // Close connection
    $conn->close();
}
?>
