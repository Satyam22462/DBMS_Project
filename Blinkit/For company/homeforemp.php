<?php
// Check if the form is submitted
if(isset($_POST['submit'])) {
    // Retrieve the selected value from the form
    $selectedOption = $_POST['work'];
    
    // Redirect to the appropriate page based on the selected option
    switch($selectedOption) {
        case 'FAQ_Cust':
            header("Location: faq/faq_for_cust.html");
            break;
        case 'FAQ_Emp':
            header("Location: faq/faq_for_emp.html");
            break;
        case 'stores':
            header("Location: stores.php"); // Update with the appropriate file name
            break;
        case 'product':
            header("Location: product.php"); // Update with the appropriate file name
            break;
        case 'productcatergory':
            header("Location: product_category.php"); // Update with the appropriate file name
            break;
        case 'warehouse':
            header("Location: warehouse.php"); // Update with the appropriate file name
            break;
        default:
            echo "Invalid option selected.";
    }
}
?>
