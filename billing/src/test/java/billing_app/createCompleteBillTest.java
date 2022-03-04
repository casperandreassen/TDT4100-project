package billing_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class createCompleteBillTest {
    

    @Test
    public void createCompleteCompany() {
        Address companyAddress = new Address("Moholt Almenning 11", "7050", "Trondheim", "Norway");
        Company company1 = new Company("Caspers-firma", "123456785", 1, companyAddress);
        Address customerAddress = new Address("Rekkeviksgate 68C", "3260", "Larvik", "Norway");
        Customer customer1 = new Customer("Dag Ronny Andreassen", customerAddress, "548052505");
        company1.addCustomerToCompany(customer1);
        assertEquals(true, company1.allCompanyCustomers.contains(customer1));
        assertThrows(IllegalArgumentException.class, () -> {
            company1.addCustomerToCompany(customer1);
        });
        company1.saveCompanyState();
    }
}
