<?php
$servername = "localhost" ;
$username = "root";
$password = "";
$database = "blinkit";

// Create connection
$conn = new mysqli($servername, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}

// Prepare and bind the parameters
$stmt = $conn->prepare("INSERT INTO EmployeeFAQ (question, answer) VALUES (?, ?)");
$stmt->bind_param("ss", $question, $answer);

// Set parameters and execute
$question = $_POST['question'];
$answer = $_POST['answer'];
$stmt->execute();

echo "New FAQ added successfully";

$stmt->close();
$conn->close();
?>
