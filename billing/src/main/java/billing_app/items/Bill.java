package billing_app.items;

import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.UUID;

import billing_app.logic.Company;
import billing_app.logic.Customer;


public class Bill {


    /* Dates are an instance of GregorianCalendar since this is the non depreciated version */
    private UUID billId;
    private int billNumber;
    private GregorianCalendar dateOfSale, dateOfDelivery, dueDate;  
    private HashMap<Item, Integer> itemsOnBill;
    private Company sellingCompany;
    private Customer customer;
    public boolean sent = false;

    /* For creating an empty bill */
    public Bill(Company sellingCompany, UUID billUUID) {
        if (sellingCompany.getOrganizationalId() != null) {
            this.sellingCompany = sellingCompany;
            this.itemsOnBill = new HashMap<Item, Integer>();
            dateOfSale = new GregorianCalendar(0,0,0);
            dateOfDelivery = new GregorianCalendar(0,0,0);
            dueDate = new GregorianCalendar(0,0,0);
            if (billId == null) {
                this.billId = UUID.randomUUID();
            } else {
                this.billId = billUUID;
            }
        }
    }

    public void setItems(Item item, int number) {
        this.itemsOnBill.put(item, number);
    }

    public void setBillId(int billId) {
        if (billId != 0) {
            this.billNumber = billId;
        }
    }

    public int getBillId() {
        return this.billNumber;
    }

    public UUID getBillUUID() {
        return this.billId;
    }

    public void setBillUUID(UUID id) {
        this.billId = id;
    }



    public void addItemToBill(Item item) {
        if (!(this.itemsOnBill.putIfAbsent(item, 1) == null)) {
            this.itemsOnBill.put(item, this.itemsOnBill.get(item) + 1);
        }
    }

    public void removeItemFromBill(Item item) {
        try {
            if (this.itemsOnBill.get(item) == 1) {
                this.itemsOnBill.remove(item);
            } else if (this.itemsOnBill.get(item) != null) {
                this.itemsOnBill.put(item, this.itemsOnBill.get(item) - 1);
            }
        } catch (Exception e) {
            //TODO: handle exception
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

    public void addDateOfSale(GregorianCalendar dateOfSale) throws ParseException {
        if (dateOfSale != null) {
            this.dateOfSale = dateOfSale;   
        } else {
            throw new IllegalArgumentException("Cannot set date of sale in the future");
        }
    }

    public GregorianCalendar getDateOfSale() {
        if (this.dateOfSale == null) {
            return new GregorianCalendar(0, 0, 0);
        }
        return this.dateOfSale;
    }

    public void addDateOfDelivery(GregorianCalendar dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }

    public GregorianCalendar getDateOfDelivery() {
        if (this.dateOfDelivery == null) {
            return new GregorianCalendar(0,0,0);
        }
        return this.dateOfDelivery;
    }

    public void addDueDate(GregorianCalendar dueDate) {
        if (!dueDate.after(new Date())) {
            this.dueDate = dueDate;
        } else {
            throw new IllegalArgumentException("Due date cannot be in the past.");
        }
    }

    public GregorianCalendar getDueDate() {
        if (this.dueDate == null) {
            return new GregorianCalendar(0,0,0);
        }
        return this.dueDate;
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

    public boolean legalState() throws IllegalAccessException {
        if (sent) {
            throw new IllegalAccessException("This bill has already been sent and can therefore not be edited.");
        }
        if (itemsOnBill.size() > 0 && customer != null && sellingCompany != null && dateOfSale != null && dateOfDelivery != null && dueDate != null && !sent) {
            return true;
        } else {
            return false; 
        }
    }

    public boolean minimumLegalState() {
        if (customer != null && itemsOnBill.size() > 0 && sellingCompany != null) {
            return true;
        }
        return false;
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

    public Company getBillCompany() {
        return this.sellingCompany;
    }




}
