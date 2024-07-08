<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $contact = $_POST['contact'];

    // Establishing connection to MySQL
    $conn = new mysqli("localhost", "root", "", "blinkit");
    if ($conn->connect_error) {
        echo "$conn->connect_error";
        die("Connection Failed : " . $conn->connect_error);
    } else {
        // Check if the contact exists in the customer table
        $stmt = $conn->prepare("SELECT CustomerID, fname FROM Customer WHERE contact = ?");
        $stmt->bind_param("i", $contact);
        $stmt->execute();
        $stmt->store_result();
        $count = $stmt->num_rows;

        if ($count > 0) {
            // Contact exists, allow login
            session_start();
            $stmt->bind_result($customerID, $fname);
            $stmt->fetch();
            $_SESSION['customerID'] = $customerID;
            $_SESSION['fname'] = $fname;
            echo "Login successful!";
            // You can redirect the user to another page here if needed
            echo "<meta http-equiv='refresh' content='3;url=homepage.html'>";
            $stmt->close();
            $conn->close();
        } else {
            // Contact does not exist, display error
            echo "Error: Contact not found. Please check your contact number.";
            $stmt->close();
            $conn->close();
        }

        // Closing prepared statement and database connection
    }
}
?>
