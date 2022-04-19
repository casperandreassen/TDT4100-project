package billing_app.logic;

import java.util.UUID;

public class Customer extends Business{
    
    UUID customerId;

    public Customer(UUID customerId) {
        this.customerId = customerId;
    }


}
