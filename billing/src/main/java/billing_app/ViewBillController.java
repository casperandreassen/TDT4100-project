package billing_app;

import billing_app.items.Bill;
import billing_app.items.Item;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewBillController extends GenericController implements ControllerInterface {

    @FXML
    VBox itemsOnBill;


    @FXML
    Label currentCompanyInfo, customerOnBill, billNumber, totalWithoutTax, totalTax, billTotal, dateOfSale, dateOfDelivery, dueDate;

    Bill currentBill;

    /* Reders all the items on the bill to the items section. */
    @FXML
    private void displayBillItems() {
        itemsOnBill.getChildren().clear();
        for (Item item : currentBill.getItems().keySet()) {
            StackPane pane = new StackPane();
            pane.setPrefSize(552, 40);
            Integer numberOfItemInCart = currentBill.getItems().get(item);
            Label itemName = new Label("Item: " + item.getName());
            Label numberOfItem = new Label(Integer.toString(numberOfItemInCart));
            Label priceOfItem = new Label("Sum: " + String.format("%.2f", item.getPrice() * numberOfItemInCart));
            Label taxOfItems = new Label("Tax:" + String.format("%.2f", (item.getPrice() * numberOfItemInCart) * (item.getTaxOnItem() / 10)));
            StackPane.setAlignment(itemName, Pos.TOP_LEFT);
            StackPane.setAlignment(numberOfItem, Pos.BOTTOM_LEFT);
            StackPane.setAlignment(priceOfItem, Pos.BOTTOM_RIGHT);
            StackPane.setAlignment(taxOfItems, Pos.TOP_RIGHT);
            StackPane buttons = new StackPane();
            StackPane.setAlignment(buttons, Pos.CENTER);
            pane.getChildren().addAll(itemName, numberOfItem, priceOfItem);
            itemsOnBill.getChildren().add(pane);
        }
    }

    /* Updates totals for the bill */
    @FXML
    private void updateTotals() {
        Double totalBillCost = currentBill.getTotalCostOfBill();
        Double totalBillTax = currentBill.getTotalTaxOnBill();

        totalWithoutTax.setText(String.format("%.2f", totalBillCost - totalBillTax));
        totalTax.setText(String.format("%.2f", totalBillTax));
        billTotal.setText(String.format("%.2f", totalBillCost));
    }

    /* Exits create bill and does not save what has been done on the bill. */
    @FXML
    private void exitCreateBill() {
        goToView("Overview", "Overview.fxml", (Stage) itemsOnBill.getScene().getWindow());
    }

    public void init() {
        this.currentBill = activeBill;
        updateTotals();
        displayBillItems();
        currentCompanyInfo.setText(currentCompany.toString());
        customerOnBill.setText(currentBill.getBillCustomer().toString());
        billNumber.setText(Integer.toString(currentBill.getBillId()));
        dateOfSale.setText(currentBill.getDateOfSale().toZonedDateTime().toLocalDate().toString());
        dateOfDelivery.setText(currentBill.getDateOfDelivery().toZonedDateTime().toLocalDate().toString());
        dueDate.setText(currentBill.getDueDate().toZonedDateTime().toLocalDate().toString());
    }
    
}
