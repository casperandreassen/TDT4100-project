package billing_app.logic;

import java.util.UUID;

import billing_app.items.Address;
import billing_app.items.OrganizationalId;

/* Aim for this abstract class is to consolidate similar methods in both Customer and Company. */

public abstract class Business {
    
    String name;
    OrganizationalId orgId; 
    Address address;
    UUID businessId;

    /* UUID is used for identifying the object on save/load. */
    public void setId(UUID id) {
        this.businessId = id;
    }

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

    public UUID getBusinessId() {
        return this.businessId;
    }

    @Override
    public String toString() {
        return String.format("%1$s, %2$s.", this.name, this.address);
    }




}
