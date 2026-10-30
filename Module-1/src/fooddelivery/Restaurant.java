package fooddelivery;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Restaurant {
    private final int id;
    private final String name;
    private final CuisineType cuisineType;
    private final String city;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private final double minimumOrderValue;
    private boolean open;
    private boolean activated;
    private final List<MenuItem> menuItems = new ArrayList<>();
    private int activeOrders;

    public Restaurant(int id, String name, CuisineType cuisineType, String city,
                      LocalTime openingTime, LocalTime closingTime,
                      double minimumOrderValue) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Restaurant name is required.");
        }
        if (cuisineType == null || city == null || city.isBlank()) {
            throw new IllegalArgumentException("Cuisine type and city are required.");
        }
        if (openingTime == null || closingTime == null
                || !openingTime.isBefore(closingTime)) {
            throw new IllegalArgumentException("Closing time must be after opening time.");
        }
        if (minimumOrderValue < 0) {
            throw new IllegalArgumentException("Minimum order value cannot be negative.");
        }

        this.id = id;
        this.name = name;
        this.cuisineType = cuisineType;
        this.city = city;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.minimumOrderValue = minimumOrderValue;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public CuisineType getCuisineType() { return cuisineType; }
    public String getCity() { return city; }
    public LocalTime getOpeningTime() { return openingTime; }
    public LocalTime getClosingTime() { return closingTime; }
    public double getMinimumOrderValue() { return minimumOrderValue; }
    public boolean isOpen() { return open; }
    public boolean isActivated() { return activated; }
    public int getActiveOrders() { return activeOrders; }
    public List<MenuItem> getMenuItems() {
        return Collections.unmodifiableList(menuItems);
    }

    void addMenuItem(MenuItem item) {
        menuItems.add(item);
    }

    MenuItem findMenuItem(int itemId) {
        for (MenuItem item : menuItems) {
            if (item.getId() == itemId) return item;
        }
        return null;
    }

    void updateTimings(LocalTime openingTime, LocalTime closingTime) {
        if (openingTime == null || closingTime == null
                || !openingTime.isBefore(closingTime)) {
            throw new IllegalArgumentException("Closing time must be after opening time.");
        }
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    void setOpen(boolean open) { this.open = open; }
    void activate() { this.activated = true; }
    void setActiveOrders(int activeOrders) {
        if (activeOrders < 0) throw new IllegalArgumentException("Invalid active order count.");
        this.activeOrders = activeOrders;
    }

    public boolean canAcceptOrders(LocalTime currentTime) {
        return open && !currentTime.isBefore(openingTime)
                && currentTime.isBefore(closingTime);
    }

    @Override
    public String toString() {
        return "Restaurant ID: " + id + " | " + name + " | " + cuisineType
                + " | " + city + " | " + openingTime + "-" + closingTime
                + " | Minimum order: Rs. " + minimumOrderValue
                + " | " + (open ? "Owner marked Open" : "Owner marked Closed")
                + " | " + (activated ? "Activated" : "Not activated");
    }
}
