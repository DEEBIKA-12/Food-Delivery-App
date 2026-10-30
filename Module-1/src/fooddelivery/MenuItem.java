package fooddelivery;

public class MenuItem {
    private final int id;
    private final String name;
    private double price;
    private final FoodCategory category;
    private boolean available;

    public MenuItem(int id, String name, double price,
                    FoodCategory category, boolean available) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Menu item name is required.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category is required.");
        }

        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = available;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public FoodCategory getCategory() { return category; }
    public boolean isAvailable() { return available; }

    public void updatePrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        this.price = price;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Item ID: " + id + " | " + name + " | Rs. " + price
                + " | " + category + " | "
                + (available ? "Available" : "Unavailable");
    }
}
