 package billing_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.GregorianCalendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import billing_app.items.Address;
import billing_app.items.Bill;
import billing_app.items.Item;
import billing_app.items.OrganizationalId;
import billing_app.logic.Company;
import billing_app.logic.Customer;

public class LogicTests {

    Company testCompany;
    Address testAddress, testAddress2; 
    Customer testCustomer, testCustomer2; 
    Item testItem, testItem2;
    OrganizationalId testOrgId, testOrgId2;
    GregorianCalendar date1, date2, date3, date4;

    @BeforeEach
    public void initOrgIds() {
        testOrgId = new OrganizationalId("991825827");
        testOrgId2 = new OrganizationalId("235833115");
    }
    
    @BeforeEach
    public void initCompany() {
        testCompany = new Company(); 
        testCompany.setCompanyName("Statoil");
        testCompany.setOriganizationalId(testOrgId);
        testItem = new Item("Kjøttboller", 29.90, 12.0, "Canned foods");
        testItem2 = new Item("iPhone 13 Pro Max", 13900.0, 25.0, "Mobile Phones");
    }

    @BeforeEach
    public void initAddresses() {
        testAddress = new Address();
        testAddress.setAddress("Moholt Almenning 11");
        testAddress.setPostalCode("7050");

        testAddress2 = new Address();
        testAddress2.setAddress("Rekkeviksgate 68C");
        testAddress2.setPostalCode("3260");
    }

    @BeforeEach
    public void initCustomers() {
        testCustomer = new Customer("Oskar Nesheim", testAddress, testOrgId);
    }

    @BeforeEach
    public void ititDates() {
        date1 = new GregorianCalendar(2022, 3, 24);
        date2 = new GregorianCalendar(2022, 3, 15);
        date3 = new GregorianCalendar(2022, 4, 24);
        date4 = new GregorianCalendar(2022, 10, 7);
    }

    @Test
    public void testItem() {
        assertEquals("Kjøttboller", testItem.getName());
        assertEquals(29.90, testItem.getPrice());
        assertEquals(12.0, testItem.getTaxOnItem());
        assertEquals("Canned foods", testItem.getCategory());
    }

    @Test
    public void testBill() {
        Bill testBill = new Bill(testCompany);

        /* Test that items get added to the bill */
        testBill.addItemToBill(testItem);
        testBill.addItemToBill(testItem2);
        assertTrue(testBill.getItems().contains(testItem));
        assertTrue(testBill.getItems().contains(testItem2));


        /* Test that an item can be removed from the bill */
        assertTrue(testBill.removeItemFromBill(testItem));
        assertFalse(testBill.getItems().contains(testItem));

        /* Test adding/removing customer to bill */
        assertTrue(testBill.addCustomerToBill(testCustomer));
        assertEquals(testCustomer, testBill.getBillCustomer());
        assertTrue(testBill.removeCustomerFromBill());
        assertEquals(null, testBill.getBillCustomer());

        /* Test adding dates to bill */
        testBill.addDateOfSale(date2);
        testBill.addDateOfDelivery(date1);
        testBill.addDueDate(date3);

        assertEquals(date2, testBill.getDateOfSale());
        assertEquals(date1, testBill.getDateOfDelivery());
        assertEquals(date3, testBill.getDueDate());

        /* Test adding illegal dates to bill */

        assertThrows(IllegalArgumentException.class, () -> {
            testBill.addDateOfSale(date4);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            testBill.addDueDate(date2);
        });
    }
}
