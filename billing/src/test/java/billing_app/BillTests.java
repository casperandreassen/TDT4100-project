package billing_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import billing_app.items.Bill;
import billing_app.items.Item;
import billing_app.items.OrganizationalId;
import billing_app.logic.Company;
import billing_app.logic.Customer;

public class BillTests {

    static Company company;
    static Bill bill;

    @BeforeEach
    public void init() {
        company = new Company(null);
        company.setOriganizationalId(new OrganizationalId("988623512"));
        bill = new Bill(company, null);
    }

    @Test
    public void testAssignmentOfUUIDOnBill() {
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
        bill.setItems(item, 2);
        assertTrue(bill.getItems().keySet().contains(item));
    }

    @Test
    public void testAddRemoveItemFromBill() {
        Item item = new Item(null, "Fiskeboller", 20, 12);
        bill.addItemToBill(item);
        assertTrue((bill.getItems().keySet().contains(item)));
        bill.removeItemFromBill(item);
        assertFalse(bill.getItems().keySet().contains(item));
    }

    @Test
    public void testAddingTwoCustomersToBill() {
        Customer customer = new Customer(null);
        Customer customer2 = new Customer(null);
        assertTrue(bill.addCustomerToBill(customer));
        assertFalse(bill.addCustomerToBill(customer2));
    }

    /* Since the logic for all three dates are exaxtly the same its enough to only test one  */

    @Test
    public void testDateParsing() {
        assertThrows(ParseException.class, () -> bill.addDueDate("b04.02.2022"));
    }

    @Test
    public void testDateAdded() throws ParseException {
        GregorianCalendar date = new GregorianCalendar(2022, 03, 22);
        bill.addDueDate("2022-04-22");
        assertEquals(date.toZonedDateTime().toLocalDate(), bill.getDueDate().toZonedDateTime().toLocalDate());
    }

    @Test
    public void testLegalStateThrowsWhenSent() throws ParseException {
        bill.addDateOfDelivery("2022-04-22");
        bill.addDueDate("2022-04-22");
        bill.addDateOfSale("2022-04-22");
        Item item = new Item(null, "Fiskeboller", 20, 12);
        bill.addItemToBill(item);
        bill.addCustomerToBill(new Customer(null));
        company.sendFinishedBill(bill, 1);
        assertThrows(IllegalArgumentException.class, () -> bill.legalState());
    }
    
    @Test
    public void testLegalState() throws ParseException {
        bill.addDateOfDelivery("2022-04-22");
        bill.addDueDate("2022-04-22");
        bill.addDateOfSale("2022-04-22");
        Item item = new Item(null, "Fiskeboller", 20, 12);
        bill.addItemToBill(item);
        bill.addCustomerToBill(new Customer(null));
        assertTrue(bill.legalState());
    }

    @Test
    public void testMinimumLegalStateThrows() {
        assertThrows(IllegalArgumentException.class, () -> bill.minimumLegalState());
    }

    @Test
    public void testTotalCostOfBill() {
        bill.addItemToBill(new Item(null, "Fiskeboller", 20, 12));
        bill.addItemToBill(new Item(null, "Fiskeboller2", 25, 12));
        assertEquals(45.0, bill.getTotalCostOfBill());
    }

    @Test
    public void testTotalTaxOnBill() {
        bill.addItemToBill(new Item(null, "Fiskeboller", 20, 12));
        bill.addItemToBill(new Item(null, "Fiskeboller2", 25, 12));
        assertEquals(5.4, bill.getTotalTaxOnBill());
    }
}
