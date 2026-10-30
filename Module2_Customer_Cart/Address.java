/** Immutable delivery address. */
public class Address {
    private final String label;
    private final String street;
    private final String city;
    private final String pincode;

    public Address(String label, String street, String city, String pincode) {
        if (isBlank(street) || isBlank(city) || isBlank(pincode))
            throw new BusinessRuleException("Street, city and pincode are required for an address.");
        this.label = isBlank(label) ? "Address" : label.trim();
        this.street = street.trim();
        this.city = city.trim();
        this.pincode = pincode.trim();
    }

    private static boolean isBlank(String s) { return s == null || s.isBlank(); }

    public String getLabel()   { return label; }
    public String getStreet()  { return street; }
    public String getCity()    { return city; }
    public String getPincode() { return pincode; }

    /** Case-insensitive city comparison used for serviceability checks. */
    public boolean isInCity(String otherCity) {
        return otherCity != null && city.equalsIgnoreCase(otherCity.trim());
    }

    @Override
    public String toString() { return label + ": " + street + ", " + city + " - " + pincode; }
}
