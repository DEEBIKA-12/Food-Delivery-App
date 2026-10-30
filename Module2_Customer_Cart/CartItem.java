/** One line in a cart. Price is a snapshot taken when the item was added. */
public class CartItem {
    private final String menuItemId;
    private final String name;
    private final double unitPrice;
    private int quantity;

    public CartItem(String menuItemId, String name, double unitPrice, int quantity) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.unitPrice = unitPrice;
        setQuantity(quantity);
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) throw new BusinessRuleException("Quantity must be at least 1.");
        this.quantity = quantity;
    }

    public String getMenuItemId() { return menuItemId; }
    public String getName()       { return name; }
    public double getUnitPrice()  { return unitPrice; }
    public int getQuantity()      { return quantity; }
    public double getSubtotal()   { return unitPrice * quantity; }
}
