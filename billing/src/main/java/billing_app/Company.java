package billing_app;

import java.util.ArrayList;
import java.util.Collection;

public class Company {
    private String companyName;
    OrganizationalId companyOrganizationalId; 
    Address companyAddress;
    /* This should be a Path */
    private String companyLogoPath; 
    private int currentBillId;
    Collection<Item> allCompanyItems; 
    Collection<Customer> allCompanyCustomers; 
    Collection<Bill> companySentBills;
    Collection<Bill> companyUnfinishedBills;
    
    public Company(String companyName, OrganizationalId organizationalId, int startingBillId, Address address) throws IllegalArgumentException {
        if (!(companyName == null) && startingBillId > 0) {
            this.companyName = companyName;
            this.currentBillId = startingBillId;
            this.companyAddress = address;
            this.companyOrganizationalId = organizationalId;
            this.allCompanyItems = new ArrayList<Item>();
            this.allCompanyCustomers = new ArrayList<Customer>();
            this.companySentBills = new ArrayList<Bill>();
            this.companyUnfinishedBills = new ArrayList<Bill>();
        } else {
            throw new IllegalArgumentException("Company needs valid name and starting billID");
        }
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

    /* Possibly add check for if bill already is in database */
    public void addUnfinishedBill(Bill bill) {
        companyUnfinishedBills.add(bill);
    }

    public void sendFinishedBill(Bill bill, int billId) {
        if (bill.legalState()) {
            bill.setBillId(billId);
            this.currentBillId++;
            this.companySentBills.add(bill);
        } else {
            throw new IllegalStateException("Bill is missing legally required fields.");
        }
    }

    public void saveCompanyState() {
        SaveState savestate = new SaveState(this);
        savestate.saveCurrentState();
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public String getCompanyLogoPath() {
        return this.companyLogoPath;
    }

    public int getCurrentBillId() {
        return this.currentBillId;
    }
}
