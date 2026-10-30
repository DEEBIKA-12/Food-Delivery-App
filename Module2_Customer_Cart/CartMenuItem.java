/**
 * What the cart needs to know about a menu item.
 * Module 1's MenuItem class should declare: "implements CartMenuItem".
 */
public interface CartMenuItem {
    String getId();
    String getName();
    double getPrice();
    boolean isAvailable();
}
