package billing_app;

import java.util.Collection;

public class Customer {
    private String name; 
    private String address; 
    private OrganizationalId organizationalId;
    private Collection<Bill> customerBills;  
}
