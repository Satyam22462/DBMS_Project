<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Handle the form submission
    if (isset($_POST["submit"])) {
        // Handle form data
        $selectedOption = $_POST["product"];
        
        // Now you can redirect to different HTML files based on the selected option
        switch ($selectedOption) {
            case "addproduct":
                header("Location: addproduct.html"); // Redirect to add_product.html
                break;
            case "modifyproduct":
                header("Location: modifyproduct.html"); // Redirect to modify_product.html
                break;
            case "removeproduct":
                header("Location: removeproduct.html"); // Redirect to remove_product.html
                break;
            default:
                // Redirect to some default page if needed
                break;
        }
    }
}
?>
