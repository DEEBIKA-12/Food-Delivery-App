import java.util.HashMap;
import java.util.Map;

/** Module 2: customer registration and lookup (in-memory). */
public class CustomerService {
    private final Map<String, Customer> customersById = new HashMap<>();
    private final Map<String, String> idByPhone = new HashMap<>();   // enforces unique phone
    private int sequence = 0;

    public Customer register(String name, String phone, String email, Address firstAddress) {
        if (phone != null && idByPhone.containsKey(phone))
            throw new BusinessRuleException("Phone number is already registered.");
        String id = String.format("CUST-%03d", ++sequence);
        Customer customer = new Customer(id, name, phone, email, firstAddress);  // validates fields
        customersById.put(id, customer);
        idByPhone.put(phone, id);
        return customer;
    }

    public Customer findById(String id) {
        Customer c = customersById.get(id);
        if (c == null) throw new BusinessRuleException("Customer not found: " + id);
        return c;
    }

    public Customer findByPhone(String phone) {
        String id = idByPhone.get(phone);
        if (id == null) throw new BusinessRuleException("No customer with phone: " + phone);
        return customersById.get(id);
    }

    public void addAddress(String customerId, Address address) { findById(customerId).addAddress(address); }
}
