package billing_app;

public class Customer {

    private String name; 
    Address address;
    private OrganizationalId CustomerOrganizationalId;
    
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
