package billing_app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

public class Bill {


    /* Dates should be an instance of GregorianCalendar https://docs.oracle.com/javase/7/docs/api/java/util/GregorianCalendar.html */

    private int billId; 
    private GregorianCalendar dateOfSale;  
    private Collection<Item> itemsOnBill;
    private Company sellingCompany;
    private Customer customer;
    private GregorianCalendar dateOfDelivery; 
    private GregorianCalendar dueDate;

    /* For creating an empty bill */
    public Bill(Company sellingCompany) {
        if (sellingCompany.getOrganizationalId() != null && this.billId == 0) {
            this.sellingCompany = sellingCompany;
            this.itemsOnBill = new ArrayList<Item>();
        }
    }

    public void addItemToBill(Item item) {
        this.itemsOnBill.add(item);
    }

    /* Returns True if the item was removed from the bill. */
    public boolean removeItemFromBill(Item item) {
        return this.itemsOnBill.remove(item);
    }

    public boolean addCustomerToBill(Customer customer) {
        if (this.customer == null) {
            this.customer = customer;
            return true;
        }
        return false; 
    }

    public boolean removeCustomerFromBill() {
        if (this.customer != null) {
            this.customer = null;
            return true; 
        }
        return false; 
    }

    public void addDateOfSale(GregorianCalendar dateOfSale) {
        if (!dateOfSale.after(new Date())) {
            this.dateOfSale = dateOfSale;   
        } else {
            throw new IllegalArgumentException("Cannot set date of sale in the future");
        }
    }

    public GregorianCalendar getDateOfSale() {
        return this.dateOfSale;
    }

    public void addDateOfDelivery(GregorianCalendar dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }

    public GregorianCalendar getDateOfDelivery() {
        return this.dateOfDelivery;
    }

    public void addDueDate(GregorianCalendar dueDate) {
        if (!dueDate.before(new Date())) {
            this.dueDate = dueDate;
        } else {
            throw new IllegalArgumentException("Due date cannot be in the past.");
        }
    }

    public GregorianCalendar getDueDate() {
        return this.dueDate;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public Collection<Item> getItems() {
        return this.itemsOnBill;
    }

    public Customer getBillCustomer() {
        return this.customer;
    }

    public boolean legalState() {
        if (billId > 0 && itemsOnBill.size() > 0 && customer != null && sellingCompany != null && dateOfSale != null && timeOfDelivery != null && dueDate != null) {
            return true;
        } else {
            return false; 
        }
    }



}
