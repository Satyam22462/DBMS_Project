<?php
$firstname = $_POST['first_name'];
$secondname = $_POST['second_name'];
$lastname = $_POST['last_name'];
$CustDOB = $_POST['CustDOB'];
$age = $_POST['age'];
$street_number = $_POST['street_number'];
$landmark = $_POST['landmark'];
$city = $_POST['city'];
$pincode = $_POST['pincode'];
$state = $_POST['state'];
$contact = $_POST['contact'];

// Establishing connection to MySQL
$conn = new mysqli("localhost", "root", "", "blinkit");
if($conn->connect_error){
    echo "$conn->connect_error";
    die("Connection Failed : ". $conn->connect_error);
} else {
    // Inserting data into the Customer table
    $stmt = $conn->prepare("INSERT INTO Customer (fname, mname, lname, CustomerDOB, streetNo, landmark, city, state, pincode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("ssssssssi", $firstname, $secondname, $lastname, $CustDOB, $street_number, $landmark, $city, $state, $pincode);
    $execval = $stmt->execute();
    
    if ($execval === FALSE) {
        echo "Error inserting into Customer table: " . $conn->error;
        $stmt->close();
        $conn->close();
        die(); // Exit script if insertion fails
    }
    
    // Retrieving the auto-generated CustomerID
    $customerID = $conn->insert_id;

    // Inserting data into the customerContact table
    $stmt = $conn->prepare("INSERT INTO customerContact (CustomerID, contact) VALUES (?, ?)");
    $stmt->bind_param("is", $customerID, $contact);
    $execval = $stmt->execute();
    
    if ($execval === FALSE) {
        echo "Error inserting into customerContact table: " . $conn->error;
        $stmt->close();
        $conn->close();
        die(); // Exit script if insertion fails
    }

    echo "Registration successful...";
    
    // Closing prepared statement and database connection
    $stmt->close();
    $conn->close();
}
?>
