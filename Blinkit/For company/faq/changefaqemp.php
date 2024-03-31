<?php
if(isset($_POST['submit'])) {
    $oldQuestion = $_POST['oldQuestion'];
    $newQuestion = $_POST['newQuestion'];
    $newAnswer = $_POST['newAnswer'];

    // Establish connection to MySQL
    $conn = new mysqli("localhost", "root", "", "blinkit");

    // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    // Check if the old question exists in the database
    $stmt = $conn->prepare("SELECT * FROM EmployeeFAQ WHERE question = ?");
    $stmt->bind_param("s", $oldQuestion);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        // Old question exists, update it with the new question and answer
        $stmt = $conn->prepare("UPDATE EmployeeFAQ SET question = ?, answer = ? WHERE question = ?");
        $stmt->bind_param("sss", $newQuestion, $newAnswer, $oldQuestion);
        $stmt->execute();
        
        echo "FAQ updated successfully.";
    } else {
        // Old question does not exist
        echo "Question does not exist. Please try again.";
    }

    // Closing prepared statement and database connection
    $stmt->close();
    $conn->close();
}
?>
