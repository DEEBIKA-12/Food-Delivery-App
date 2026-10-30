import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/** Module 2: all cart rules live here. */
public class CartService {
    public static final int MAX_QUANTITY_PER_ITEM = 10;
    public static final int MAX_ACTIVE_ORDERS = 3;
    public static final int PAYMENT_RETRY_MINUTES = 15;

    private final Map<String, Cart> carts = new HashMap<>();   // key = customerId
    private final Clock clock;

    public CartService() { this(Clock.systemDefaultZone()); }
    public CartService(Clock clock) { this.clock = clock; }

    public Cart getCart(String customerId) { return carts.computeIfAbsent(customerId, Cart::new); }

    public void addItem(Customer customer, CartRestaurant restaurant, CartMenuItem menuItem, int quantity) {
        if (quantity <= 0)
            throw new BusinessRuleException("Quantity must be at least 1.");
        if (!menuItem.isAvailable())
            throw new BusinessRuleException("'" + menuItem.getName() + "' is currently unavailable.");

        Cart cart = getCart(customer.getId());

        // Rule: one restaurant per cart
        if (!cart.isEmpty() && !cart.getRestaurantId().equals(restaurant.getId()))
            throw new BusinessRuleException(
                "Your cart has items from another restaurant. Clear it before adding from this one.");

        // Rule: max 10 units of the same item (existing + new)
        CartItem existing = cart.getItem(menuItem.getId());
        int newQuantity = (existing == null ? 0 : existing.getQuantity()) + quantity;
        if (newQuantity > MAX_QUANTITY_PER_ITEM)
            throw new BusinessRuleException(
                "You can add at most " + MAX_QUANTITY_PER_ITEM + " units of the same item.");

        if (existing == null) {
            cart.setRestaurantId(restaurant.getId());
            cart.putItem(new CartItem(menuItem.getId(), menuItem.getName(), menuItem.getPrice(), newQuantity));
        } else {
            existing.setQuantity(newQuantity);
        }
    }

    public void updateQuantity(String customerId, String menuItemId, int newQuantity) {
        Cart cart = getCart(customerId);
        CartItem item = cart.getItem(menuItemId);
        if (item == null) throw new BusinessRuleException("Item is not in the cart.");
        if (newQuantity <= 0) { cart.removeItem(menuItemId); return; }
        if (newQuantity > MAX_QUANTITY_PER_ITEM)
            throw new BusinessRuleException(
                "You can add at most " + MAX_QUANTITY_PER_ITEM + " units of the same item.");
        item.setQuantity(newQuantity);
    }

    public void removeItem(String customerId, String menuItemId) {
        if (getCart(customerId).removeItem(menuItemId) == null)
            throw new BusinessRuleException("Item is not in the cart.");
    }

    public void clearCart(String customerId) { getCart(customerId).clear(); }

    public double getCartTotal(String customerId) { return getCart(customerId).getTotal(); }

    /**
     * At checkout: drops items that became unavailable after being added.
     * @param isAvailable asks Module 1 "is this menu item id still available?"
     * @return names of removed items, so the UI can notify the customer
     */
    public List<String> removeUnavailableItems(String customerId, Predicate<String> isAvailable) {
        Cart cart = getCart(customerId);
        List<String> removedNames = new ArrayList<>();
        for (CartItem item : new ArrayList<>(cart.getItems())) {
            if (!isAvailable.test(item.getMenuItemId())) {
                cart.removeItem(item.getMenuItemId());
                removedNames.add(item.getName());
            }
        }
        return removedNames;
    }

    /**
     * Cart-side checks before Module 3 places an order.
     * @param activeOrderCount customer's orders not yet Delivered/Cancelled (from Module 3)
     */
    public void validateCheckout(Customer customer, CartRestaurant restaurant,
                                 Address deliveryAddress, int activeOrderCount) {
        Cart cart = getCart(customer.getId());
        if (cart.isEmpty())
            throw new BusinessRuleException("Your cart is empty.");
        if (!cart.getRestaurantId().equals(restaurant.getId()))
            throw new BusinessRuleException("Cart does not belong to this restaurant.");
        if (deliveryAddress == null || !customer.getAddresses().contains(deliveryAddress))
            throw new BusinessRuleException("Choose one of your saved delivery addresses.");
        if (!deliveryAddress.isInCity(restaurant.getCity()))
            throw new BusinessRuleException("Delivery address must be in the same city as the restaurant.");
        if (activeOrderCount >= MAX_ACTIVE_ORDERS)
            throw new BusinessRuleException("You can have at most " + MAX_ACTIVE_ORDERS + " active orders.");
    }

    /** Module 3 calls this after an order is placed: the cart is cleared. */
    public void onOrderPlaced(String customerId) { getCart(customerId).clear(); }

    /** Module 5 calls this when payment fails: the customer gets 15 minutes to retry. */
    public void onPaymentFailed(String customerId) {
        getCart(customerId).setRetryDeadline(LocalDateTime.now(clock).plusMinutes(PAYMENT_RETRY_MINUTES));
    }

    /** Releases carts whose retry window has passed. Returns how many were released. */
    public int releaseExpiredCarts() {
        LocalDateTime now = LocalDateTime.now(clock);
        int released = 0;
        for (Cart cart : carts.values()) {
            if (cart.getRetryDeadline() != null && now.isAfter(cart.getRetryDeadline())) {
                cart.clear();
                released++;
            }
        }
        return released;
    }
}
