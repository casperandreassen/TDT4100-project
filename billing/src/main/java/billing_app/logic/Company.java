package billing_app.logic;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

import billing_app.items.Bill;
import billing_app.items.Item;
import billing_app.saving.SaveState;

public class Company extends Business{

    private Path companyLogoPath; 
    private int currentBillId;
    public Collection<Item> allCompanyItems; 
    public Collection<Customer> allCompanyCustomers; 
    public Collection<Bill> companySentBills;
    public Collection<Bill> companyUnfinishedBills;
    
    public Company() {
        this.allCompanyItems = new ArrayList<Item>();
        this.allCompanyCustomers = new ArrayList<Customer>();
        this.companySentBills = new ArrayList<Bill>();
        this.companyUnfinishedBills = new ArrayList<Bill>();
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

    public Path getCompanyLogoPath() {
        return this.companyLogoPath;
    }

    public void setCompanyLogoPath(Path newPath) {
        this.companyLogoPath = newPath;
    }

    public int getCurrentBillId() {
        return this.currentBillId;
    }

    public void setCurrentBillId(int startingBillId) {
        this.currentBillId = startingBillId;
    }



}
