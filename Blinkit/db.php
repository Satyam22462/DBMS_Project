<?Php

$servername="localhost";
$userName="root";
$password="";
$databasename="blinkit";


$con=mysqli_connect($servername,$userName,$password,$databasename);
if(mysqli_connect_errno()){
    echo"Connection failed";
    exit();
}
echo"Connection sucess";
?>