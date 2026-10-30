import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Module 6: ratings and reviews. */
public class RatingService {
    public static final int RATING_WINDOW_HOURS = 48;
    public static final int FLAG_MIN_RATINGS = 10;
    public static final double FLAG_BELOW_AVERAGE = 2.0;

    /** Running totals so an average is recalculated cheaply after every rating. */
    private static class Stats {
        private int sum;
        private int count;
        void add(int score) { sum += score; count++; }
        double average() { return count == 0 ? 0.0 : (double) sum / count; }
    }

    private final Map<String, Rating> ratingByOrderId = new HashMap<>();   // enforces "once per order"
    private final Map<String, Stats> restaurantStats = new HashMap<>();
    private final Map<String, Stats> partnerStats = new HashMap<>();
    private final Map<String, List<Rating>> ratingsByRestaurant = new HashMap<>();
    private final Set<String> flaggedRestaurants = new LinkedHashSet<>();
    private final Clock clock;
    private int sequence = 0;

    public RatingService() { this(Clock.systemDefaultZone()); }
    public RatingService(Clock clock) { this.clock = clock; }

    public Rating submitRating(RatableOrder order, String customerId,
                               int restaurantScore, int partnerScore, String review) {
        if (!order.getCustomerId().equals(customerId))
            throw new BusinessRuleException("You can only rate your own orders.");
        if (!order.isDelivered())
            throw new BusinessRuleException("You can rate an order only after it is Delivered.");
        if (ratingByOrderId.containsKey(order.getOrderId()))
            throw new BusinessRuleException("This order has already been rated.");

        LocalDateTime now = LocalDateTime.now(clock);
        Duration sinceDelivery = Duration.between(order.getDeliveredAt(), now);
        if (sinceDelivery.compareTo(Duration.ofHours(RATING_WINDOW_HOURS)) > 0)
            throw new BusinessRuleException("The " + RATING_WINDOW_HOURS + "-hour rating window has closed.");

        // Rating's constructor checks 1..5; nothing is saved if it throws.
        Rating rating = new Rating(String.format("RAT-%03d", ++sequence), order.getOrderId(), customerId,
                order.getRestaurantId(), order.getDeliveryPartnerId(),
                restaurantScore, partnerScore, review, now);

        ratingByOrderId.put(order.getOrderId(), rating);
        ratingsByRestaurant.computeIfAbsent(order.getRestaurantId(), k -> new ArrayList<>()).add(rating);

        Stats rs = restaurantStats.computeIfAbsent(order.getRestaurantId(), k -> new Stats());
        rs.add(restaurantScore);   // average recalculated after every new rating
        partnerStats.computeIfAbsent(order.getDeliveryPartnerId(), k -> new Stats()).add(partnerScore);

        if (rs.count >= FLAG_MIN_RATINGS && rs.average() < FLAG_BELOW_AVERAGE)
            flaggedRestaurants.add(order.getRestaurantId());

        return rating;
    }

    public double getRestaurantAverage(String restaurantId) {
        Stats s = restaurantStats.get(restaurantId);
        return s == null ? 0.0 : s.average();
    }

    public int getRestaurantRatingCount(String restaurantId) {
        Stats s = restaurantStats.get(restaurantId);
        return s == null ? 0 : s.count;
    }

    public double getPartnerAverage(String partnerId) {
        Stats s = partnerStats.get(partnerId);
        return s == null ? 0.0 : s.average();
    }

    public List<Rating> getRatingsForRestaurant(String restaurantId) {
        return Collections.unmodifiableList(ratingsByRestaurant.getOrDefault(restaurantId, List.of()));
    }

    public boolean isOrderRated(String orderId) { return ratingByOrderId.containsKey(orderId); }

    /** For the Admin menu. */
    public Set<String> getFlaggedRestaurantIds() { return Collections.unmodifiableSet(new HashSet<>(flaggedRestaurants)); }

    public void clearFlag(String restaurantId) { flaggedRestaurants.remove(restaurantId); }
}
