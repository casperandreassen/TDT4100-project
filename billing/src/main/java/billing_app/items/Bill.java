package billing_app.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import billing_app.logic.Company;
import billing_app.logic.Customer;

import billing_app.items.Item;

public class Bill {


    /* Dates are an instance of GregorianCalendar since this is the non depreciated version */

    private int billId; 
    private GregorianCalendar dateOfSale;  
    private HashMap<Item, Integer> itemsOnBill;
    private Company sellingCompany;
    private Customer customer;
    private GregorianCalendar dateOfDelivery; 
    private GregorianCalendar dueDate = new GregorianCalendar(2022, 05, 21);

    /* For creating an empty bill */
    public Bill(Company sellingCompany) {
        if (sellingCompany.getOrganizationalId() != null && this.billId == 0) {
            this.sellingCompany = sellingCompany;
            this.itemsOnBill = new HashMap<Item, Integer>();
        }
    }

    public void addItemToBill(Item item) {
        if (!(this.itemsOnBill.putIfAbsent(item, 1) == null)) {
            this.itemsOnBill.put(item, this.itemsOnBill.get(item) + 1);
        }
    }

    public void removeItemFromBill(Item item) {
        if (this.itemsOnBill.get(item) == 1) {
            this.itemsOnBill.remove(item);
        } else if (this.itemsOnBill.get(item) != null) {
            this.itemsOnBill.put(item, this.itemsOnBill.get(item) - 1);
        }
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
        if (dateOfSale.before(new Date())) {
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

    public HashMap<Item, Integer> getItems() {
        return this.itemsOnBill;
    }

    public Customer getBillCustomer() {
        return this.customer;
    }

    public void removeCustomer() throws IllegalArgumentException {
        if (this.customer != null) {
            this.customer = null;
        } else {
            throw new IllegalArgumentException("You need to add a customer before you can add it to the bill.");
        }
    }

    public boolean legalState() {
        if (billId > 0 && itemsOnBill.size() > 0 && customer != null && sellingCompany != null && dateOfSale != null && dateOfDelivery != null && dueDate != null) {
            return true;
        } else {
            return false; 
        }
    }

    public double getTotalCostOfBill() {
        double totalCost = 0;
        for (Item item : itemsOnBill.keySet()) {
            totalCost += item.getPrice() * itemsOnBill.get(item);
        }
        return totalCost;
    }

    public double getTotalTaxOnBill() {
        double totalTax = 0;
        for (Item item : itemsOnBill.keySet()) {
            totalTax += (item.getPrice() * itemsOnBill.get(item)) * (item.getTaxOnItem() / 100);
        }
        return totalTax;

    }



}
