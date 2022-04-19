package billing_app.items;

public class Item {
    private String name; 
    private double price; 
    private double taxOnItem; 
    private String category;


    public Item(String name, double price, double taxOnItem) {
        if (name != null && price > 0 && taxOnItem > 0) {
            this.name = name; 
            this.price = price; 
            this.taxOnItem = taxOnItem; 
        } else {
            throw new IllegalArgumentException();
        }
    }

    public String getName() {
        return name;
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
