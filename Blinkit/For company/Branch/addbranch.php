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
    $streetNo = $_POST['streetNo'];
    $landmark = $_POST['landmark'];
    $city = $_POST['city'];
    $state = $_POST['state'];
    $pincode = $_POST['pincode'];
    $managerID = $_POST['ManagerID'];
    $contact = $_POST['contact'];

    // Check if the provided Manager ID exists in the Manager table
    $sql = "SELECT ManagerID FROM Manager WHERE ManagerID = $managerID";
    $result = $conn->query($sql);

    if ($result->num_rows == 0) {
        echo "<p>Manager ID does not exist. Please enter a valid Manager ID.</p>";
    } else {
        // If manager ID exists, insert data into the Branch table
        $sql = "INSERT INTO Branch (StreetNo, Landmark, City, State, Pincode, ManagerID, Contact) 
                VALUES ('$streetNo', '$landmark', '$city', '$state', '$pincode', $managerID, '$contact')";

        if ($conn->query($sql) === TRUE) {
            echo "<p>Branch added successfully!</p>";
        } else {
            echo "Error: " . $sql . "<br>" . $conn->error;
        }
    }
}

// Close the database connection
$conn->close();
?>
