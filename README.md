# My CartMate
* *Guide : Dr. Vikram Goyal* 
* Replica of Blinkit a famous Grocery delivery app in CLI.
* **Tech Stack:** Java, SQL

* **Required Tools:** An IDE (IntelliJ IDEA would be best), Xampp, a JDBC which should be Platform Independent (mysql-connector-j-8.3.0 Download link(zip): https://downloads.mysql.com/archives/c-j/)

## How to Run :
  * Step 0: Clone this repo.
  * Step 1: Download the JDBC connector first then unzip it.
  * Step 2: Open the CLI named folder in IntelliJ IDEA.
  * Step 3: Go to files > Project Structures > Modules > click on "+" > Select Jar and Directories and select the jar file of msql connector > apply > OK.
  * Step 4: Now open the Xampp > Start the Apache and MySQL > click on admin of MySQL, you'll we redirected to php local host site.
  * Step 5: Copy all things form the Embedded SQL with Triggers.txt file.
  * Step 6: Now on the localhost website Create a Database with name 'blink' (else go to DatabaseConnection.java file and you'll find this line "private static final String URL = "jdbc:mysql://localhost:3306/blink";" just change the last word 'blink' to whatever name you want to have your database. The name of your database and the last word of this string should be same) > Go to that Database > SQL section > paste all things from step 5 > click on GO and ensure "enable foreign key check" checkbox should be enabled.
   ###### Note : You have successfully created a database. And there are already some data inserted in the database.
  * Step 7: Run the main.java file in IntelliJ IDEA and here it's everything done.
   ###### Note : It's v1.0 so some functionalities are not implemented we're working on that.


#### Error:
  * If you counter an error like mysql in xampp is not starting to fix this just close the process going on port(3306) or go to the task manager > processes > search mysql and end that task.



#### Some Insights :
##### Intiial Menu :
* on running you'll be presented with the following menu:
  1. Enter as Seller : Choosing 1 will show you the Seller/Admin Menu.
  2. Enter as Customer : Choosing 2 will show you the Customer Menu.
  3. Exit : Choosing 3 will close the program.
         
##### Customer Menu : 
* This menu will show you the following options: 
  1. Login : Choosing 1 will ask you your gmail and password.
  2. Sign Up : Choosing 2 will ask you your details to get signed up.
  3. Back : Choosing 3 will lead you to the Main Menu.

##### Admin/Seller Menu:
* This menu will show you the following options:
  1. Apply for Seller (Under Development!! plz wait for v2.0).
  2. Login as Seller : Choosing 2 will ask you the seller email and password (seller email = 'manager1@example.com', password = 'manager1001').
  3. Exit : Choosing 3 will lead you to Main Menu.
    
##### Seller Login Menu: 
* This menu will show you the basically the management of the products, ware houses, customer data etc.
  1. Add Product Category : By Choosing 1 you'll be asked to enter the name of the category you want to create and the description of that category.
  2. Add Product : By Choosing 1 firstly all the Product Categories Name with ID showned then you'll be asked to enter the name of the Product, Product Price, and then Product ID in which you want to add that product, Product Description, Brand Name, Stock Quantity after entering stock quantity all the warehouses that are present will be listed and you'll be asked to choose that you want to add that stock to specific warehouse or all the warehouses.
  3. Remove product Category : By Choosing 3 you will be asked to enter the product category id you want to remove for the database.
  4. Remove Product : Explore it by yourself
  5. Update Product Details : (Under Development!!)
  6. Update Product Category Details : (Under Development!!)
  7. Inventory Analysis: By Choosing option 7 you will get the product id and warehouse id which has max stock qty.
  8. Sneek In Inventory : By Choosing option 8 you will get the overall analysis of the inventory.
  9. Add more stocks : Explore it by Yourself
  10. Logout : Choosing option 10 will loged you out and send you to the main menu.

  ##### Customer Login Menu:
  In this menu you can buy any product, add products to cart, you can view last 5 orders made by you, etc.



Contributors:
* Prince Kumar (prince22378@iiitd.ac.in)
* Satyam (satyam22462@iiitd.ac.in)
* Siddhant Singh (siddhant22497@iiitd.ac.in)
* Prajil Bhagat (prajil22359@iiitd.ac.in)

* for any query related to my cartmate just mail us.
