<?php
// Check if the form is submitted
if(isset($_POST['submit'])) {
    // Retrieve the selected value from the form
    $selectedOption = $_POST['work'];
    
    // Redirect to the appropriate page based on the selected option
    switch($selectedOption) {
        case 'add':
            header("Location: addwarehouse.html");
            break;
        case 'modify':
            header("Location: modifywarehouse.html");
            break;
        case 'remove':
            header("Location: removewarehouse.html");
            break;
        default:
            echo "Invalid option selected.";
            break;
    }
}
?>
