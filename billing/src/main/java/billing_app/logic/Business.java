package billing_app.logic;

import billing_app.items.Address;
import billing_app.items.Bill;
import billing_app.items.OrganizationalId;

public abstract class Business {
    
    String name;
    OrganizationalId orgId; 
    Address address;


    public void setOriganizationalId(OrganizationalId orgId) {
        this.orgId = orgId;
    }

    public OrganizationalId getOrganizationalId() {
        if (this.orgId != null) {
            return this.orgId;
        }
        throw new IllegalStateException();
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }


    @Override
    public String toString() {
        return String.format("%1$s, %2$s.", this.name, this.address);
    }




}
