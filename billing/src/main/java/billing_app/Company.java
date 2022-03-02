package billing_app;

import java.util.Collection;

import javax.management.ObjectInstance;
import javax.management.relation.InvalidRelationTypeException;

public class Company {
    private String companyName;
    private OrganizationalId organizationalId; 
    private String companyLogoPath; 
    private int lastUsedBillId; 
    private Collection<Item> allCompanyItems; 
    private Collection<Customer> allCompanyCustomers; 
    private Collection<Bill> companySentBills;
    private Collection<Bill> companyUnfinishedBills;
    
    public Company(String companyName, String organizationalId, int startingBillId) throws IllegalArgumentException {
        if (!(companyName == null) && startingBillId > 0 && validOrganizationalId()) {
            this.companyName = companyName;
            this.lastUsedBillId = startingBillId;
        } else {
            throw new IllegalArgumentException("Company needs valid name and starting billID");
        }
    }

    private boolean validOrganizationalId() {

    }

    public void addCustomerToCompany(Customer customer) throws IllegalArgumentException {
        if(!this.allCompanyCustomers.contains(customer)) {
            this.allCompanyCustomers.add(customer);
        } else {
            throw new IllegalArgumentException("Customer already customer to company");
        }
    }

    public void addItemToCompany(Item item) {
        if (!this.allCompanyItems.contains(item)) {
            this.allCompanyItems.add(item);
        } else {
            throw new IllegalArgumentException("Duplicate item");
        }
    }
    /* Possibly add check for if bill already is in database*/
    public void addBillToCompany(Bill bill) {
        if (bill.finished) {
            this.companySentBills.add(bill);
        } else {
            this.companyUnfinishedBills.add(bill);
        }
    }


}
