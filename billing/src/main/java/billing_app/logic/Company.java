package billing_app.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import billing_app.items.Bill;
import billing_app.items.Item;

public class Company extends Business{

    private String companyLogoPath; 
    private int currentBillId;
    public Collection<Item> allCompanyItems; 
    public Collection<Customer> allCompanyCustomers; 
    public List<Bill> companySentBills;
    public List<Bill> companyUnfinishedBills;
    
    public Company(UUID id) {
        if (id == null) {
            setId(UUID.randomUUID());
        } else {
            setId(id);
        }
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

    public Collection<Customer> getCompanyCustomers() {
        return this.allCompanyCustomers;
    }

    public void addItemToCompany(Item item) {
        if (!this.allCompanyItems.contains(item)) {
            this.allCompanyItems.add(item);
        } else {
            throw new IllegalArgumentException("Duplicate item");
        }
    }

    public void addUnfinishedBill(Bill bill) {
        if (!companyUnfinishedBills.contains(bill)) {
            companyUnfinishedBills.add(bill);
        } 
    }

    public void addSentBill(Bill bill) {
        if (!companySentBills.contains(bill)) {
            companySentBills.add(bill);
        }
    }

    public void sendFinishedBill(Bill bill, int billId) throws IllegalAccessException {
        if (bill.legalState()) {
            bill.setBillId(billId);
            this.currentBillId++;
            this.companySentBills.add(bill);
            this.companyUnfinishedBills.remove(bill);
            bill.sent = true;
        } else {
            throw new IllegalStateException("Bill is missing legally required fields.");
        }
    }

    public Collection<Bill> getCompanySentBills() {
        return this.companySentBills;
    }

    public Collection<Bill> getCompanyUnfinishedBills() {
        return this.companyUnfinishedBills;
    }


    public FileInputStream getCompanyLogoFileStream() throws FileNotFoundException {
        return new FileInputStream(this.companyLogoPath);
    }

    public void setCompanyLogoPath(String pathToFile) {
        this.companyLogoPath = pathToFile;
    }

    public String getCompanyLogoPath() {
        return this.companyLogoPath;
    }

    public int getCurrentBillId() {
        return this.currentBillId;
    }

    public void setCurrentBillId(int startingBillId) {
        this.currentBillId = startingBillId;
    }

    public Collection<Item> getCompanyItems() {
        return this.allCompanyItems;
    }

    public double calculateTotalRevenue() {
        double total = 0;
        for (Bill bill : companySentBills) {
            total += bill.getTotalCostOfBill();
        }
        return total;
    }

    public double calculateTotalTax() {
        double totalTax = 0; 
        for (Bill bill : companySentBills) {
            totalTax += bill.getTotalTaxOnBill();
        }
        return totalTax;
    }

    public double calculateTotalRevenueWithoutTax() {
        double total = 0; 
        for (Bill bill : companySentBills) {
            total += bill.getTotalCostOfBill() - bill.getTotalTaxOnBill();
        }
        return total;
    }


    public static void main(String[] args) {
        
    }


}
