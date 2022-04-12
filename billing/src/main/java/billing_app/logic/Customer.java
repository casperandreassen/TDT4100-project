package billing_app.logic;

public class Customer extends Business{
    
    String customerId;

    public Customer(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return String.format("%s", this.name);
    }
}
