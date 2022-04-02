package billing_app.logic;

import billing_app.exceptions.MissingOrganizationalIdException;
import billing_app.items.Address;
import billing_app.items.OrganizationalId;

public abstract class Business {
    
    String name;
    OrganizationalId orgId; 
    Address address; 


    public void setOriganizationalId(String orgId) {
        this.orgId = new OrganizationalId(orgId);
    }

    public OrganizationalId getOrganizationalId() {
        if (this.orgId != null) {
            return this.orgId;
        }
        throw new MissingOrganizationalIdException();
    }

    public void setAddress() {
        
    }

    public Address getAddress() {

    }

    public void setName() {

    }

    public String getName() {

    }



}
