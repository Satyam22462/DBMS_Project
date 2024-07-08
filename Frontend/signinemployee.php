<?php
$firstname = $_POST['first_name'];
$secondname = $_POST['second_name'];
$lastname = $_POST['last_name'];
$adhaar_number = $_POST['adhaar_number'];
$email = $_POST['email'];
$joindate = $_POST['joindate'];
$leavedate = $_POST['leavedate'];
$contact = $_POST['contact'];
$post = $_POST['post'];
$workingstatus = $_POST['workingstatus'];

$salary = 15000; // Default salary

// Determining salary based on post
if ($post == "worker") {
    $salary = 15000;
} elseif ($post == "dman") {
    $salary = 20000;
} elseif ($post == "manager") {
    $salary = 70000;
} elseif ($post == "packagingmen") {
    $salary = 30000;
} elseif ($post == "heademployee") {
    $salary = 50000;
}

// Establishing connection to MySQL
$conn = new mysqli("localhost", "root", "", "blinkit");
if ($conn->connect_error) {
    echo "$conn->connect_error";
    die("Connection Failed : " . $conn->connect_error);
} else {
    // Check if Aadhar number is exactly 12 characters long
    if (strlen($adhaar_number) !== 12) {
        echo "Aadhar number should be exactly 12 characters long.";
        $conn->close();
        die();
    }

    // Inserting data into the Employee table
    $stmt = $conn->prepare("INSERT INTO Employee (fname, mname, lname, EmployeeAadhar, EmployeeEmail, EmployeeJoiningDate, EmployeeLeavingDate, EmployeeSalary, contact) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("sssssssis", $firstname, $secondname, $lastname, $adhaar_number, $email, $joindate, $leavedate, $salary, $contact);
    $execval = $stmt->execute();

    if ($execval === FALSE) {
        echo "Error inserting into Employee table: " . $conn->error;
        $stmt->close();
        $conn->close();
        die(); // Exit script if insertion fails
    }

    // Retrieve the EmployeeID of the inserted employee
    $employeeID = $stmt->insert_id;

    // If the post is "manager", insert data into the Manager table
    if ($post == "manager") {
        $managerEmail = $email; // Assuming manager email is the same as employee email
        $managerContact = $contact; // Assuming manager contact is the same as employee contact

        // Inserting data into the Manager table
        $managerStmt = $conn->prepare("INSERT INTO Manager (ManagerEmail, ManagerContact, EmployeeID) VALUES (?, ?, ?)");
        $managerStmt->bind_param("ssi", $managerEmail, $managerContact, $employeeID);
        $managerExecVal = $managerStmt->execute();

        if ($managerExecVal === FALSE) {
            echo "Error inserting into Manager table: " . $conn->error;
            $managerStmt->close();
            $stmt->close();
            $conn->close();
            die(); // Exit script if insertion fails
        }
    }

    echo "Registration successful...";

    // Closing prepared statements and database connection
    if (isset($managerStmt)) {
        $managerStmt->close();
    }
    $stmt->close();
    $conn->close();
}
?>
