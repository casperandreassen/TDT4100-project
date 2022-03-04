package billing_app;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class OrganizationalIdTest {
    
    /* Testing various known invalid numbers */
    @Test
    public void testInvalid() {
        assertThrows(IllegalArgumentException.class , () -> {
            OrganizationalId org = new OrganizationalId("974760676");
        });
        assertThrows(IllegalArgumentException.class , () -> {
            OrganizationalId org = new OrganizationalId("123456786");
        });
        assertThrows(IllegalArgumentException.class , () -> {
            OrganizationalId org = new OrganizationalId("924337658");
        });
        assertThrows(IllegalArgumentException.class , () -> {
            OrganizationalId org = new OrganizationalId("201365859");
        });
    }

    /* Testing various known valid numbers */

    @Test
    public void testValid() {
        OrganizationalId org = new OrganizationalId("220239306");
        OrganizationalId org1 = new OrganizationalId("123456785");
        OrganizationalId org2 = new OrganizationalId("914337658");
        OrganizationalId org3 = new OrganizationalId("201365759");
    }

}
