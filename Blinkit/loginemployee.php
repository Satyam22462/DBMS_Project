<?php
$contact_1 = $_POST['contact_1'];

// Establishing connection to MySQL
$conn = new mysqli("localhost", "root", "", "blinkit");
if ($conn->connect_error) {
    echo "$conn->connect_error";
    die("Connection Failed : " . $conn->connect_error);
} else {
    // Check if contact exists in the Employee table
    $stmt = $conn->prepare("SELECT * FROM Employee WHERE contact = ?");
    $stmt->bind_param("i", $contact_1);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        // Contact exists, perform login actions
        echo "Login successful...";
        // Additional login logic can be added here
        echo "<meta http-equiv='refresh' content='3;url=For company/homeforemp.html'>";
        $stmt->close();
        $conn->close();
    } else {
        // Contact does not exist, display error message
        echo "Invalid contact number. Please try again.";
        $stmt->close();
        $conn->close();
    }

    // Closing prepared statement and database connection

    // Redirecting to another page after 3 seconds
   
}
?>
