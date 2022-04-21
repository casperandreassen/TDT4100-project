package billing_app.items;

import java.util.UUID;

public class Item {
    private String name; 
    private double price; 
    private double taxOnItem; 
    private String category;
    private UUID itemID;


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

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
