 package billing_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import billing_app.items.Address;
import billing_app.items.Bill;
import billing_app.items.Item;
import billing_app.items.OrganizationalId;
import billing_app.logic.Company;

public class LogicTests {

    static Company company;

    @BeforeAll
    public static void initCompany() {
        company = new Company(null);
        company.setOriganizationalId(new OrganizationalId("988623512"));
    }


    @Test
    public void testAddressPostCodeCreation() throws FileNotFoundException, IOException, URISyntaxException {
        Address address = new Address();
        address.setPostalCode("3294");
        assertEquals("STAVERN", address.getCity());
    }

    @Test
    public void testAssignmentOfUUIDOnBill() {
        Bill bill = new Bill(company, null);
        assertNotNull(bill.getBillUUID());
    }

    @Test
    public void testUUIDIsSetOnBill() {
        UUID id = UUID.randomUUID();
        Bill bill2 = new Bill(company, id);
        assertEquals(id, bill2.getBillUUID());
    }

    @Test
    public void testItemIsSetOnBill() {
        Item item = new Item(null, "Fiskepinner", 29.90, 12);
        Bill bill = new Bill(company, null);
        bill.setItems(item, 2);
        assertTrue(bill.getItems().keySet().contains(item));
    }

    @Test
    public void testAddRemoveItemFromBill() {
        Bill bill = new Bill(company, null); 
        Item item = new Item(null, "Fiskeboller", 20, 12);
        bill.addItemToBill(item);
        assertTrue((bill.getItems().keySet().contains(item)));
        bill.removeItemFromBill(item);
        assertFalse(bill.getItems().keySet().contains(item));
    }

    @Test
    public void testAddingTwoCustomersToBill() {
        
    }
}
