<?php
// Check if the form is submitted
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Retrieve selected action from the form
    $action = $_POST['action'];

    // Redirect to the appropriate page based on the selected action
    switch ($action) {
        case 'add':
            header("Location: addbranch.html");
            exit;
        case 'modify':
            header("Location: modifybranch.html");
            exit;
        case 'remove':
            header("Location: removebranch.html");
            exit;
        default:
            echo "Invalid action selected.";
            exit;
    }
}
?>
