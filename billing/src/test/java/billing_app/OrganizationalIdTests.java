package billing_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import billing_app.items.OrganizationalId;

public class OrganizationalIdTests {


    @Test
    public void testMultipleOrganizationalIds() {
        String[] validOrgIds = {"988623512", "877308863", "784965082", "813265125", "349024772"};
        for (String orgId : validOrgIds) {
            assertEquals(orgId, new OrganizationalId(orgId).getOrganizationalId());
        }
    }

    @Test
    public void testMultipleInvalidOrganizationalIds() {
        String[] invalidOrgIds = {"988625651982", "877308963", "784967082", "81125", "349024773"};
        for (String orgId : invalidOrgIds) {
            assertThrows(IllegalArgumentException.class, () -> new OrganizationalId(orgId));
        }
    }
    
}
