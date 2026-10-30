import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A customer must always have at least one delivery address. */
public class Customer extends User {
    private final List<Address> addresses = new ArrayList<>();

    public Customer(String id, String name, String phone, String email, Address firstAddress) {
        super(id, name, phone, email);
        if (firstAddress == null)
            throw new BusinessRuleException("At least one delivery address is required.");
        addresses.add(firstAddress);
    }

    public void addAddress(Address address) {
        if (address == null) throw new BusinessRuleException("Address cannot be null.");
        addresses.add(address);
    }

    public List<Address> getAddresses() { return Collections.unmodifiableList(addresses); }

    @Override
    public String getRole() { return "CUSTOMER"; }
}
