package billing_app.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import billing_app.MainApp;
import billing_app.items.Bill;
import billing_app.items.Item;

public class Company extends Business{

    private String companyLogoPath; 
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


    public FileInputStream getCompanyLogoFileStream() throws FileNotFoundException {
        return new FileInputStream(this.companyLogoPath);
    }

    public void setCompanyLogoPath(String pathToFile) {
        this.companyLogoPath = pathToFile;
    }

    public int getCurrentBillId() {
        return this.currentBillId;
    }

    public void setCurrentBillId(int startingBillId) {
        this.currentBillId = startingBillId;
    }


    public static void main(String[] args) {
        
    }


}
