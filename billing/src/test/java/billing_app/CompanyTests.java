package billing_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import billing_app.items.Bill;
import billing_app.items.Item;
import billing_app.items.OrganizationalId;
import billing_app.logic.Company;
import billing_app.logic.Customer;

public class CompanyTests {

    Company company;

    @BeforeEach
    public void init() {
        company = new Company(null);
        company.setOriganizationalId(new OrganizationalId("784965082"));
    }

    @Test
    public void testAssignmentOfUUID() {
        assertNotNull(company.getBusinessId());
        assertEquals(UUID.class, company.getBusinessId().getClass());
    }

    @Test
    public void addDuplicateCustomer() {
        Customer customer = new Customer(null);
        company.addCustomerToCompany(customer);
        assertThrows(IllegalArgumentException.class, () -> company.addCustomerToCompany(customer));
    }

    @Test 
    public void addDuplicateItem() {
        Item item = new Item(null, "Fiskeboller", 20, 12);
        company.addItemToCompany(item);
        assertThrows(IllegalArgumentException.class, () -> company.addItemToCompany(item));
    }

    @Test
    public void finishedBillIsMoved() throws ParseException {
        Bill bill = new Bill(company, null);
        bill.addCustomerToBill(new Customer(null));
        bill.addItemToBill(new Item(null, "Fiskeboller", 20, 12));
        bill.addDateOfDelivery("2022-04-22");
        bill.addDueDate("2022-04-22");
        bill.addDateOfSale("2022-04-22");
        company.addUnfinishedBill(bill);
        assertTrue(company.getCompanyUnfinishedBills().contains(bill));
        company.sendFinishedBill(bill, 2);
        assertFalse(company.getCompanyUnfinishedBills().contains(bill));
        assertTrue(company.getCompanySentBills().contains(bill));
    }


    @Test
    public void testLogoFileStream() {
        company.setCompanyLogoPath("random/invalid/path");
        assertThrows(FileNotFoundException.class, () -> company.getCompanyLogoFileStream());
    }

    @Test
    public void testCalculateTotals() throws ParseException {
        Bill bill = new Bill(company, null);
        Bill bill2 = new Bill(company, null);
        Bill bill3 = new Bill(company, null);
        bill.addDateOfDelivery("2022-04-22");
        bill.addDueDate("2022-04-22");
        bill.addDateOfSale("2022-04-22");
        bill2.addDateOfDelivery("2022-04-22");
        bill2.addDueDate("2022-04-22");
        bill2.addDateOfSale("2022-04-22");
        bill3.addDateOfDelivery("2022-04-22");
        bill3.addDueDate("2022-04-22");
        bill3.addDateOfSale("2022-04-22");
        bill.addCustomerToBill(new Customer(null));
        bill2.addCustomerToBill(new Customer(null));
        bill3.addCustomerToBill(new Customer(null));
        Item item = new Item(null, "Fiskeboller", 20, 12);
        bill.addItemToBill(item);
        bill2.addItemToBill(item);
        bill3.addItemToBill(item);
        company.sendFinishedBill(bill, 1);
        company.sendFinishedBill(bill2, 2);
        company.sendFinishedBill(bill3, 3);
        assertEquals(60, company.calculateTotalRevenue());
        assertEquals("7.20", String.format("%.2f", company.calculateTotalTax()));
    }


}
