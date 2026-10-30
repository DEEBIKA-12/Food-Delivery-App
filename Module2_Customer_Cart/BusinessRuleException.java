/** Thrown whenever a business rule from the problem statement is broken. */
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
