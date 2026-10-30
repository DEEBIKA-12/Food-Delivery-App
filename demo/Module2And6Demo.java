import java.time.LocalDateTime;

/** Standalone test of Module 2 + 6. Uses fake restaurant/menu/order classes, so no other module is needed. */
public class Module2And6Demo {

    static class DemoRestaurant implements CartRestaurant {
        public String getId() { return "R1"; }
        public String getCity() { return "Coimbatore"; }
    }

    static class DemoItem implements CartMenuItem {
        private final String id, name; private final double price; private final boolean available;
        DemoItem(String id, String name, double price, boolean available) {
            this.id = id; this.name = name; this.price = price; this.available = available;
        }
        public String getId() { return id; }
        public String getName() { return name; }
        public double getPrice() { return price; }
        public boolean isAvailable() { return available; }
    }

    static class DemoOrder implements RatableOrder {
        private final String id; private final LocalDateTime deliveredAt;
        DemoOrder(String id, LocalDateTime deliveredAt) { this.id = id; this.deliveredAt = deliveredAt; }
        public String getOrderId() { return id; }
        public String getCustomerId() { return "CUST-001"; }
        public String getRestaurantId() { return "R1"; }
        public String getDeliveryPartnerId() { return "DP1"; }
        public boolean isDelivered() { return deliveredAt != null; }
        public LocalDateTime getDeliveredAt() { return deliveredAt; }
    }

    interface Action { void run(); }

    /** Runs an action that SHOULD fail with a business-rule error. */
    static void expectError(String what, Action a) {
        try { a.run(); System.out.println("FAIL (no error): " + what); }
        catch (BusinessRuleException e) { System.out.println("OK   " + what + " -> " + e.getMessage()); }
    }

    public static void main(String[] args) {
        CustomerService customers = new CustomerService();
        CartService carts = new CartService();
        RatingService ratings = new RatingService();

        // ---- Module 2 ----
        Address home = new Address("Home", "12 MG Road", "Coimbatore", "641001");
        Customer asha = customers.register("Asha", "9876543210", "asha@mail.com", home);
        System.out.println("Registered " + asha.getId() + " (" + asha.getRole() + ")");
        expectError("duplicate phone", () -> customers.register("Ravi", "9876543210", "r@mail.com", home));
        expectError("bad phone", () -> customers.register("Ravi", "123", "r@mail.com", home));

        CartRestaurant r1 = new DemoRestaurant();
        CartRestaurant r2 = new CartRestaurant() {
            public String getId() { return "R2"; }
            public String getCity() { return "Chennai"; }
        };
        CartMenuItem biryani = new DemoItem("M1", "Biryani", 200, true);
        CartMenuItem soldOut = new DemoItem("M2", "Dosa", 80, false);

        carts.addItem(asha, r1, biryani, 2);
        System.out.println("Cart total (expect 400.0): " + carts.getCartTotal(asha.getId()));
        expectError("unavailable item", () -> carts.addItem(asha, r1, soldOut, 1));
        expectError("different restaurant", () -> carts.addItem(asha, r2, biryani, 1));
        expectError("more than 10 units", () -> carts.addItem(asha, r1, biryani, 9));
        expectError("address in other city", () -> carts.validateCheckout(asha, r2, home, 0));
        expectError("4th active order", () -> carts.validateCheckout(asha, r1, home, 3));
        carts.validateCheckout(asha, r1, home, 1);
        System.out.println("OK   valid checkout passes");
        System.out.println("Removed at checkout (expect [Biryani]): "
                + carts.removeUnavailableItems(asha.getId(), id -> false));
        carts.addItem(asha, r1, biryani, 1);
        carts.onOrderPlaced(asha.getId());
        System.out.println("Cart empty after order (expect true): " + carts.getCart(asha.getId()).isEmpty());

        // ---- Module 6 ----
        DemoOrder delivered = new DemoOrder("ORD-1", LocalDateTime.now().minusHours(2));
        ratings.submitRating(delivered, "CUST-001", 5, 4, "Great food!");
        System.out.println("Restaurant avg (expect 5.0): " + ratings.getRestaurantAverage("R1"));
        expectError("rate same order twice", () -> ratings.submitRating(delivered, "CUST-001", 3, 3, "x"));
        expectError("after 48 hours", () ->
                ratings.submitRating(new DemoOrder("ORD-2", LocalDateTime.now().minusHours(50)), "CUST-001", 3, 3, "x"));
        expectError("not delivered yet", () -> ratings.submitRating(new DemoOrder("ORD-3", null), "CUST-001", 3, 3, "x"));
        expectError("score 6", () ->
                ratings.submitRating(new DemoOrder("ORD-4", LocalDateTime.now()), "CUST-001", 6, 3, "x"));

        for (int i = 10; i < 20; i++)   // 10 more low ratings -> avg < 2.0 -> flagged
            ratings.submitRating(new DemoOrder("ORD-" + i, LocalDateTime.now()), "CUST-001", 1, 3, "bad");
        System.out.println("Count=" + ratings.getRestaurantRatingCount("R1")
                + " avg=" + String.format("%.2f", ratings.getRestaurantAverage("R1"))
                + " flagged=" + ratings.getFlaggedRestaurantIds());
    }
}
