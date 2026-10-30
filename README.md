# Module 2 (Customer & Cart) + Module 6 (Ratings & Reviews)

Plain Java 17, no package lines (matches the team's style), no database.

Folders: Module2_Customer_Cart, Module6_Ratings, demo.

Compile + run (PowerShell, from this folder):
    javac -d out (Get-ChildItem -Recurse -Filter *.java).FullName
    java -cp out Module2And6Demo

Integration (teammates):
- Module 1: `Restaurant implements CartRestaurant`, `MenuItem implements CartMenuItem`
- Module 3: `Order implements RatableOrder`; call cartService.validateCheckout(...) before placing and cartService.onOrderPlaced(...) after
- Module 5: call cartService.onPaymentFailed(customerId) when payment fails
