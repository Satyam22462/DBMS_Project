<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $action = $_POST["action"];

    switch ($action) {
        case "add_product_category":
            header("Location: addproductcategory.html");
            break;
        case "modify_product":
            header("Location: modifyproductcategory.html");
            break;
        case "remove_product_category":
            header("Location: removeproductcategory.html");
            break;
        default:
            echo "Invalid action.";
    }
}
?>
