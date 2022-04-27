package billing_app;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import billing_app.items.Item;

public class ItemTests {


    @Test
    public void testUUIDAssignment() {
        Item item = new Item(null, "Fiskeboller", 20, 12);
        assertNotNull(item.getItemId());
    }


    @Test
    public void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new Item(null, "Fiskekaker", -20, 1));
    }

    
}
