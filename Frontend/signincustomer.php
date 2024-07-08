<?php
$firstname = $_POST['first_name'];
$secondname = $_POST['second_name'];
$lastname = $_POST['last_name'];
$CustDOB = $_POST['CustDOB'];
$street_number = $_POST['street_number'];
$landmark = $_POST['landmark'];
$city = $_POST['city'];
$pincode = $_POST['pincode'];
$state = $_POST['state'];
$contact = $_POST['contact'];

// Calculate current year
$currentYear = date('Y');

// Calculate the year of birth from CustDOB
$yearOfBirth = date('Y', strtotime($CustDOB));

// Calculate age
$age = $currentYear - $yearOfBirth;

// Establishing connection to MySQL
$conn = new mysqli("localhost", "root", "", "blinkit");
if($conn->connect_error){
    echo "$conn->connect_error";
    die("Connection Failed : ". $conn->connect_error);
} else {
    if($age <= 0) {
        echo "Date of birth is invalid";
        $conn->close();
        die();
    }

    // Check if pincode is exactly 6 characters long
    if(strlen($pincode) !== 6) {
        echo "Pincode should be exactly 6 characters long.";
        $conn->close();
        die();
    }

    // Inserting data into the Customer table
    $stmt = $conn->prepare("INSERT INTO Customer (fname, mname, lname, CustomerDOB, CustomerAge, streetNo, landmark, city, state, pincode, contact) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("ssssissssis", $firstname, $secondname, $lastname, $CustDOB, $age, $street_number, $landmark, $city, $state, $pincode, $contact);
    $execval = $stmt->execute();
    
    if ($execval === FALSE) {
        echo "Error inserting into Customer table: " . $conn->error;
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
