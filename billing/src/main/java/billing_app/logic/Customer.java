package billing_app.logic;

import java.util.UUID;

public class Customer extends Business{
    
    /* if no UUID is specified a UUID wil be generated. */
    public Customer(UUID customerId) {
        if (customerId == null) {
            setId(UUID.randomUUID());
        } else {
            businessId = customerId;
        }
    }
}
