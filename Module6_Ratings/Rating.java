import java.time.LocalDateTime;

/** One rating for a delivered order (restaurant score + delivery partner score). */
public class Rating {
    private final String ratingId;
    private final String orderId;
    private final String customerId;
    private final String restaurantId;
    private final String deliveryPartnerId;
    private final int restaurantScore;
    private final int partnerScore;
    private final String review;
    private final LocalDateTime createdAt;

    public Rating(String ratingId, String orderId, String customerId, String restaurantId,
                  String deliveryPartnerId, int restaurantScore, int partnerScore,
                  String review, LocalDateTime createdAt) {
        validateScore(restaurantScore, "Restaurant");
        validateScore(partnerScore, "Delivery partner");
        this.ratingId = ratingId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.deliveryPartnerId = deliveryPartnerId;
        this.restaurantScore = restaurantScore;
        this.partnerScore = partnerScore;
        this.review = review == null ? "" : review.trim();
        this.createdAt = createdAt;
    }

    private static void validateScore(int score, String who) {
        if (score < 1 || score > 5)
            throw new BusinessRuleException(who + " rating must be between 1 and 5.");
    }

    public String getRatingId()          { return ratingId; }
    public String getOrderId()           { return orderId; }
    public String getCustomerId()        { return customerId; }
    public String getRestaurantId()      { return restaurantId; }
    public String getDeliveryPartnerId() { return deliveryPartnerId; }
    public int getRestaurantScore()      { return restaurantScore; }
    public int getPartnerScore()         { return partnerScore; }
    public String getReview()            { return review; }
    public LocalDateTime getCreatedAt()  { return createdAt; }
}
