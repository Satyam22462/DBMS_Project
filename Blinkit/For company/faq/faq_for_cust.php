<?php
// Check if the form is submitted
if(isset($_POST['submit'])) {
    // Retrieve the selected value from the form
    $selectedOption = $_POST['faq'];
    
    // Redirect to the appropriate page based on the selected option
    switch($selectedOption) {
        case 'add FAQ':
            header("Location: addfaqcust.html");
            break;
        case 'change_FAQ':
            header("Location: changefaqcust.html");
            break;
        default:
            echo "Invalid option selected.";
    }
}
?>
