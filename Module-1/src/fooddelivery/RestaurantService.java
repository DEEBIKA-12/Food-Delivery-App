package fooddelivery;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RestaurantService {
    private final List<Restaurant> restaurants = new ArrayList<>();
    private int nextRestaurantId = 1;
    private int nextMenuItemId = 1;

    private void checkPermission(Role role) {
        if (role != Role.ADMIN && role != Role.RESTAURANT_OWNER) {
            throw new SecurityException("Only Admin or Restaurant Owner can perform this action.");
        }
    }

    public Restaurant addRestaurant(Role role, String name, CuisineType cuisine,
                                    String city, LocalTime opening, LocalTime closing,
                                    double minimumOrder) {
        checkPermission(role);
        Restaurant restaurant = new Restaurant(nextRestaurantId++, name, cuisine,
                city, opening, closing, minimumOrder);
        restaurants.add(restaurant);
        return restaurant;
    }

    public MenuItem addMenuItem(Role role, int restaurantId, String name,
                                double price, FoodCategory category, boolean available) {
        checkPermission(role);
        Restaurant restaurant = findRestaurant(restaurantId);
        if (restaurant == null) throw new IllegalArgumentException("Restaurant not found.");

        MenuItem item = new MenuItem(nextMenuItemId++, name, price, category, available);
        restaurant.addMenuItem(item);
        return item;
    }

    public void activateRestaurant(Role role, int restaurantId) {
        checkPermission(role);
        Restaurant restaurant = requireRestaurant(restaurantId);
        restaurant.activate();
    }

    public void setRestaurantOpen(Role role, int restaurantId, boolean open) {
        checkPermission(role);
        Restaurant restaurant = requireRestaurant(restaurantId);
        if (!restaurant.isActivated()) {
            throw new IllegalStateException("Activate the restaurant before changing its status.");
        }
        restaurant.setOpen(open);
    }

    public void updateMenuPrice(Role role, int restaurantId, int itemId, double price) {
        checkPermission(role);
        MenuItem item = requireMenuItem(requireRestaurant(restaurantId), itemId);
        item.updatePrice(price);
    }

    public void updateMenuAvailability(Role role, int restaurantId,
                                        int itemId, boolean available) {
        checkPermission(role);
        MenuItem item = requireMenuItem(requireRestaurant(restaurantId), itemId);
        item.setAvailable(available);
    }

    public void updateTimings(Role role, int restaurantId,
                              LocalTime opening, LocalTime closing) {
        checkPermission(role);
        requireRestaurant(restaurantId).updateTimings(opening, closing);
    }

    public void deleteRestaurant(Role role, int restaurantId) {
        checkPermission(role);
        Restaurant restaurant = requireRestaurant(restaurantId);
        if (restaurant.getActiveOrders() > 0) {
            throw new IllegalStateException("Cannot delete a restaurant with active orders.");
        }
        restaurants.remove(restaurant);
    }

    public void setActiveOrderCount(Role role, int restaurantId, int count) {
        checkPermission(role);
        requireRestaurant(restaurantId).setActiveOrders(count);
    }

    public Restaurant findRestaurant(int id) {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getId() == id) return restaurant;
        }
        return null;
    }

    public List<Restaurant> getAllRestaurants() {
        return new ArrayList<>(restaurants);
    }

    private Restaurant requireRestaurant(int id) {
        Restaurant restaurant = findRestaurant(id);
        if (restaurant == null) throw new IllegalArgumentException("Restaurant not found.");
        return restaurant;
    }

    private MenuItem requireMenuItem(Restaurant restaurant, int itemId) {
        MenuItem item = restaurant.findMenuItem(itemId);
        if (item == null) throw new IllegalArgumentException("Menu item not found.");
        return item;
    }
}
