import java.time.LocalDateTime;

/**
 * The only thing Module 6 needs to know about an order.
 * Module 3's Order class should declare: "implements RatableOrder".
 */
public interface RatableOrder {
    String getOrderId();
    String getCustomerId();
    String getRestaurantId();
    String getDeliveryPartnerId();
    boolean isDelivered();
    LocalDateTime getDeliveredAt();
}
