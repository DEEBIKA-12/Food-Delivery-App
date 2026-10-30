import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** A customer's cart. Holds items from at most one restaurant (enforced by CartService). */
public class Cart {
    private final String customerId;
    private String restaurantId;
    private final Map<String, CartItem> items = new LinkedHashMap<>();
    private LocalDateTime retryDeadline;   // set when a payment fails

    public Cart(String customerId) { this.customerId = customerId; }

    public String getCustomerId()   { return customerId; }
    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public boolean isEmpty() { return items.isEmpty(); }

    public Collection<CartItem> getItems() { return Collections.unmodifiableCollection(items.values()); }
    public CartItem getItem(String menuItemId) { return items.get(menuItemId); }
    public void putItem(CartItem item) { items.put(item.getMenuItemId(), item); }

    public CartItem removeItem(String menuItemId) {
        CartItem removed = items.remove(menuItemId);
        if (items.isEmpty()) restaurantId = null;
        return removed;
    }

    /** Total = sum of (price x quantity). */
    public double getTotal() {
        double total = 0;
        for (CartItem item : items.values()) total += item.getSubtotal();
        return total;
    }

    public void clear() {
        items.clear();
        restaurantId = null;
        retryDeadline = null;
    }

    public LocalDateTime getRetryDeadline() { return retryDeadline; }
    public void setRetryDeadline(LocalDateTime retryDeadline) { this.retryDeadline = retryDeadline; }
}
