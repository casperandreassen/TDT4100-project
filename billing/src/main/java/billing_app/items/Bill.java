package billing_app.items;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    /* The constructor creates a "shell" for a bill. Containing just the bare minimum for a bill, a selling company and an UUID for the bill used in saving/loading the bill. If no UUID is specified in the constructor that implies that it is a new bill and a UUID is generated for it. */
    public Bill(Company sellingCompany, UUID billUUID) {
        if (sellingCompany.getOrganizationalId() != null) {
            this.sellingCompany = sellingCompany;
            this.itemsOnBill = new HashMap<Item, Integer>();
            dateOfSale = new GregorianCalendar(0,0,0);
            dateOfDelivery = new GregorianCalendar(0,0,0);
            dueDate = new GregorianCalendar(0,0,0);
            if (billUUID == null) {
                this.billId = UUID.randomUUID();
            } else {
                this.billId = billUUID;
            }
        }
    }

    /* setItems is used for setting bill items when bill is recreated from savefile. */
    public void setItems(Item item, int number) {
        this.itemsOnBill.put(item, number);
    }

    /* So that we can save all the items that are on the bill. */
    public HashMap<Item, Integer> getItems() {
        return this.itemsOnBill;
    }

    /* Bill id is the outwardfacing id for the bill and is not used internally in any meaningful way. */
    public void setBillId(int billId) {
        if (billId != 0) {
            this.billNumber = billId;
        }
    }

    public int getBillId() {
        return this.billNumber;
    }

    /* BillUUID is used to identify the bill internally. */
    public UUID getBillUUID() {
        return this.billId;
    }

    public void setBillUUID(UUID id) {
        this.billId = id;
    }


    /* Add and remove item is used to increase or decrease the number of one item on the bill */
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

    /* Add and remove customer from bill adds and removes customer from bill if it is necessary */
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

    /* Adds and gets dates on the bills. Throws a parse exception if there is an error parsing the inputted date into a GregorianCalendar object. For example if the user manually inputted the date incorrectly. */
    public void addDateOfSale(String dateOfSale) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = df.parse(dateOfSale);
        this.dateOfSale.setTime(date);
    }

    /* If date is not set we create a base date, this is so that is gets saved properly. Same logic for the two other dates. */
    public GregorianCalendar getDateOfSale() {
        if (this.dateOfSale == null) {
            return new GregorianCalendar(0, 0, 0);
        }
        return this.dateOfSale;
    }

    public void addDateOfDelivery(String dateOfDelivery) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = df.parse(dateOfDelivery);
        this.dateOfDelivery.setTime(date);
    }

    public GregorianCalendar getDateOfDelivery() {
        if (this.dateOfDelivery == null) {
            return new GregorianCalendar(0,0,0);
        }
        return this.dateOfDelivery;
    }

    public void addDueDate(String dueDate) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = df.parse(dueDate);
        this.dueDate.setTime(date);
       
    }

    public GregorianCalendar getDueDate() {
        if (this.dueDate == null) {
            return new GregorianCalendar(0,0,0);
        }
        return this.dueDate;
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

    /* Checks if the bill is in a legal state and can be marked as a sent bill. If the bill already has been sent, it cannot be changed. */
    public boolean legalState() throws IllegalArgumentException {
        if (sent) {
            throw new IllegalArgumentException("This bill has already been sent and can therefore not be edited.");
        }
        GregorianCalendar baseDate = new GregorianCalendar(0,0,0);
        if (itemsOnBill.size() > 0 && customer != null && sellingCompany != null && !this.dateOfSale.equals(baseDate) && !this.dateOfDelivery.equals(baseDate) && !this.dueDate.equals(baseDate) && !sent) {
            return true;
        } else {
            return false;
        }
    }

    /* Checks for the minimum legal state that can be saved for later use. */
    public boolean minimumLegalState() throws IllegalArgumentException {
        if (customer != null && itemsOnBill.size() > 0 && sellingCompany != null) {
            return true;
        } else {
            throw new IllegalArgumentException();
        }
    }


    /* Calculates the total cost of the entire bill. */
    public double getTotalCostOfBill() {
        double totalCost = 0;
        for (Item item : itemsOnBill.keySet()) {
            totalCost += item.getPrice() * itemsOnBill.get(item);
        }
        return totalCost;
    }

    /* Calculates the total tax on the bill. */
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
