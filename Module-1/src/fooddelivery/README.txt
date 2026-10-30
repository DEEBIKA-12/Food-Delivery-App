FOOD DELIVERY APP - MODULE 1
Restaurant and Menu Management
Java 21 | Console Application | No Database

FILES
-----
Main.java              - Console menu and user interaction
Restaurant.java        - Restaurant data and rules
MenuItem.java          - Menu item data and validation
RestaurantService.java - Business logic and permission checks
Role.java              - Admin, Restaurant Owner, Customer
CuisineType.java       - Allowed cuisine types
FoodCategory.java      - Veg and Non-Veg

HOW TO RUN (Windows)
--------------------
1. Extract this ZIP file.
2. Open the extracted folder in File Explorer.
3. Click the address bar, type cmd, and press Enter.
4. Run these commands:

   javac -d out src\fooddelivery\*.java
   java -cp out fooddelivery.Main

REQUIREMENTS
------------
Install JDK 21 or later. Check using:
   java -version
   javac -version

IMPORTANT
---------
- Data is stored in ArrayList while the program is running.
- Data is not saved after the program exits.
- Choose Admin or Restaurant Owner to add/edit restaurants and menus.
- Choose Customer to test that restricted actions are rejected.
- The active order count option is included as a small testing helper
  so you can demonstrate the restaurant deletion restriction.
- Opening hours use 24-hour time (HH:mm).
- A restaurant can accept orders only if its owner-marked status is Open
  and the current time is within opening and closing time. The
  canAcceptOrders() method is ready for later order-placement modules.
