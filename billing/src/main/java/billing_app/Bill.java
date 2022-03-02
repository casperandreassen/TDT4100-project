package billing_app;

import java.util.Collection;
import java.util.Date;

public class Bill {

    private int billId; 
    private Date dateOfSale; 
    private String buyersName; 
    private String buyersAddress; 
    private String buyersOrganizationalId; 
    private Collection<Item> itemsOnBill; 
    private Date timeOfDelivery; 
    private Date dueDate;
    public boolean finished;



}
