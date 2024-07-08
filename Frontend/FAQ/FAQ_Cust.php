<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Blinkit - Customer FAQs</title>
</head>
<body>
    <h1>Customer FAQs</h1>
    <?php
    // Connect to your MySQL database
    $servername = "localhost"; // Change this if your MySQL server is on a different host
    $username = "root";
    $password = "";
    $database = "blinkit";

    $conn = new mysqli($servername, $username, $password, $database);
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    // Fetch and display FAQs for customers
    $customerFAQQuery = "SELECT question, answer FROM CustomerFAQ";
    $customerFAQResult = $conn->query($customerFAQQuery);

    if ($customerFAQResult->num_rows > 0) {
        while($row = $customerFAQResult->fetch_assoc()) {
            echo "<p><strong>Question:</strong> " . $row["question"] . "</p>";
            echo "<p><strong>Answer:</strong> " . $row["answer"] . "</p>";
        }
    } else {
        echo "No FAQs found for customers.";
    }

    $conn->close();
    ?>
</body>
</html>
