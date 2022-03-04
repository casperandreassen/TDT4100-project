package billing_app;

import java.util.Collection;
import java.util.Date;

public class Bill {

    private int billId; 
    private Date dateOfSale;  
    private Collection<Item> itemsOnBill; 
    private Company sellingCompany;
    private Customer customer;
    private Date timeOfDelivery; 
    private Date dueDate;

    /* For creating an empty bill */
    public Bill(Company sellingCompany) {
        if (sellingCompany.companyOrganizationalId != null && this.billId == 0) {
            this.sellingCompany = sellingCompany;
        }
    }

    public void addItemToBill(Item item) {
        this.itemsOnBill.add(item);
    }

    public void addCustomerToBill(Customer customer) {
        this.customer = customer;
    }

    public void addDateOfSale(Date dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public void addTimeOfDelivery(Date timeOfDelivery) {
        this.timeOfDelivery = timeOfDelivery;
    }

    public void addDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public boolean legalState() {
        if (billId > 0 && itemsOnBill.size() > 0 && customer != null && sellingCompany != null && dateOfSale != null && timeOfDelivery != null && dueDate != null) {
            return true;
        } else {
            return false; 
        }
    }



}
