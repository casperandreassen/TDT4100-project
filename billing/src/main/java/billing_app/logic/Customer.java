package billing_app.logic;

import billing_app.items.Address;
import billing_app.items.OrganizationalId;

public class Customer extends Business{
    
    public Customer(String name, Address address, OrganizationalId orgId) {
        if (name != null && address != null) {
            this.name = name;
            this.address = address;
            this.CustomerOrganizationalId = orgId;
        }
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public OrganizationalId getCustomerOrganizationalId() {
        return CustomerOrganizationalId;
    }
}
