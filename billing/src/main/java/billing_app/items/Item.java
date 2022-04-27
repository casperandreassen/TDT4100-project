package billing_app.items;

import java.util.UUID;

public class Item {
    private String name; 
    private double price; 
    private double taxOnItem; 
    private UUID itemID;


    /* Constructor for an item. If no UUID is specified (i.e its a new item) a new UUID will be generated, or else the specified UUID will be set. UUID is used to identify the item on save/load. */
    public Item(UUID id, String name, double price, double taxOnItem) {
        if (name != null && price > 0 && taxOnItem > 0) {
            this.name = name; 
            this.price = price; 
            this.taxOnItem = taxOnItem;
            if (id != null) {
                itemID = id;
            } else {
                itemID = UUID.randomUUID();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public String getName() {
        return name;
    }

    public UUID getItemId() {
        return this.itemID;
    }

    public double getPrice() {
        return price;
    }


    public double getTaxOnItem() {
        return taxOnItem;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
