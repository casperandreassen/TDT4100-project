package billing_app;

public class Item {
    private String name; 
    private double price; 
    private int taxOnItem; 
    private String category;


    public Item(String name, double price, int taxOnItem, String category) {
        if (name != null && price > 0 && taxOnItem > 0 && category != null) {
            this.name = name; 
            this.price = price; 
            this.taxOnItem = taxOnItem; 
            this.category = category;
        }
    }

    public String getName() {
        return name;
    }


    public double getPrice() {
        return price;
    }


    public int getTaxOnItem() {
        return taxOnItem;
    }

    public String getCategory() {
        return category;
    }
}
