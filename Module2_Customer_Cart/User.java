/** Base class shared by Customer, RestaurantOwner, DeliveryPartner and Admin. */
public abstract class User {
    private final String id;
    private String name;
    private final String phone;
    private String email;

    protected User(String id, String name, String phone, String email) {
        if (name == null || name.isBlank())
            throw new BusinessRuleException("Name is required.");
        if (phone == null || !phone.matches("[6-9]\\d{9}"))
            throw new BusinessRuleException("Phone must be a valid 10-digit number.");
        if (email == null || !email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$"))
            throw new BusinessRuleException("Email is not valid.");
        this.id = id;
        this.name = name.trim();
        this.phone = phone;
        this.email = email.trim();
    }

    public String getId()    { return id; }
    public String getName()  { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    /** Each subclass reports its own role (polymorphism). */
    public abstract String getRole();
}
