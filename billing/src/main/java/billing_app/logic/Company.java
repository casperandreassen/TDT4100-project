package billing_app.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import billing_app.MainApp;
import billing_app.items.Bill;
import billing_app.items.Item;
import billing_app.saving.SaveCompany;

public class Company extends Business{

    private String companyLogoPath; 
    private int currentBillId;
    public Collection<Item> allCompanyItems; 
    public Collection<Customer> allCompanyCustomers; 
    public List<Bill> companySentBills;
    public List<Bill> companyUnfinishedBills;
    
    /* Sets UUID if none is specified, else one is generated. Also instanciates the lists used in this class. */
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
    
    /* Adds a customer to the company, if the customer already is in the company it throws a illegalargumentexception. */
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

    /* Adds a item to the company, if the item already exists an illegalargumentexception is thrown. */
    public void addItemToCompany(Item item) throws IllegalArgumentException {
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

    /* Adds a finished bill and checks if the bill is in a legal state. Passes the illegalargumentexception if not. */
    public void sendFinishedBill(Bill bill, int billId) throws IllegalArgumentException {
        if (bill.legalState()) {
            bill.setBillId(billId);
            this.currentBillId++;
            this.companySentBills.add(bill);
            this.companyUnfinishedBills.remove(bill);
            bill.sent = true;
        }
    }

    public List<Bill> getCompanySentBills() {
        return this.companySentBills;
    }

    public List<Bill> getCompanyUnfinishedBills() {
        return this.companyUnfinishedBills;
    }

    /* Returns a FileInputStream for the logo of the company so that it can be used in the UI. */
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

    /* Calculates the company total revenue. Delegates part of the calculation to the bill object's. */
    public double calculateTotalRevenue() {
        return companySentBills.stream().map(bill -> bill.getTotalCostOfBill()).reduce(0.0, (a, b) -> a + b);
    }

    /* Calculates the company total tax. Delegates part of the calculation to the bill objects. */
    public double calculateTotalTax() {
        return companySentBills.stream().map(bill -> bill.getTotalTaxOnBill()).reduce(0.0, (a, b) -> a + b);
    }

    /* Calculates the company total revenue without tax and delegates part of the calculation to the bill objects. */
    public double calculateTotalRevenueWithoutTax() {
        return companySentBills.stream().map(bill -> bill.getTotalCostOfBill() - bill.getTotalTaxOnBill()).reduce(0.0, (a, b) -> a + b); 
    }


    /* Validates that the logo is legal, and if no logo is specified it sets the default avatar as the logo. */
    public String getLogoPath(String logoPath) throws URISyntaxException {
        if (logoPath != null) {
            String[] acceptedFiles = {"jpg", "png", "jpeg"};
            String fileFormat = SaveCompany.getFileType(logoPath);
            if (Arrays.asList(acceptedFiles).contains(fileFormat)) {
                return logoPath;
            } 
        } 
        return MainApp.class.getResource("default_company_avatar.jpg").getPath();
        }
}
