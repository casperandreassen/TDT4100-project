package billing_app;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import billing_app.items.Bill;
import billing_app.items.Item;
import billing_app.logic.Customer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/* This controller is for creating new bills */

public class CreateBillController extends GenericController implements ControllerInterface {

    @FXML
    private StackPane sellingCompanyInfoPane;

    @FXML
    private ChoiceBox<Item> selectItem;

    @FXML
    private ChoiceBox<Customer> customerSelect;
    
    @FXML 
    private TextField name, address, city, postalCode, country, orgId, itemName, itemPrice, itemTax; 

    @FXML
    private CheckBox saveCustomer, saveItem;

    @FXML
    private VBox itemsOnBill;
    
    @FXML
    private TilePane customerChoicePane, companyItemsContainer;

    @FXML
    private Button addItemButton, addItemBill, saveBillButton, completeBillButton, addExistingCustomerButton, addNewCustomerButton;

    @FXML
    private Label currentCompanyInfo, customerOnBill, totalWithoutTax, billTotal, totalTax;

    @FXML
    private DatePicker dateOfSale, deliveryDate, dueDate;

    private Bill newBill;

    List<Item> itemsOnDisplay = new ArrayList<Item>();

    private Customer tmpCustomer;
    private Item tmpItem;

    /* This method displays all customers assiciated to the current company */
    @FXML
    private void displayCustomersInChoiceBox() {
        customerChoicePane.getChildren().remove(customerSelect);
        ObservableList<Customer> customers = FXCollections.observableArrayList(currentCompany.allCompanyCustomers);
        customerSelect = new ChoiceBox<Customer>(customers);

        customerSelect.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @SuppressWarnings("rawtypes")
            public void changed(ObservableValue ov, Number value, Number newValue) {
                tmpCustomer = customers.get((int) newValue);
            }
        });
        customerChoicePane.getChildren().add(customerSelect);
    }

    /* This method is for creating a new customer and adding it to both the bill and the current company */
    @FXML
    private void addNewCustomer() {
        Customer tmp = new Customer(UUID.randomUUID());
        if (createBusiness(tmp)) {
            newBill.addCustomerToBill(tmp);
            customerOnBill.setText(newBill.getBillCustomer().toString());
            displayCustomersInChoiceBox();
        }
    }

    /* This method is for adding an already existing customer to the bill. */
    @FXML
    private void addCustomer() {
        if (tmpCustomer != null) {
            newBill.addCustomerToBill(this.tmpCustomer);
            customerOnBill.setText(newBill.getBillCustomer().toString());
        } else {
            displayMessage("Missing customer info");
        }
    }
    /* Remvoes the active user from the bill. */
    @FXML
    private void removeCustomer() {
        try {
            newBill.removeCustomer();
            customerOnBill.setText("NO CUSTOMER");
        } catch (IllegalArgumentException e) {
            displayMessage("Error removing customer from bill");
        }
    }

    /* Reders all the items on the bill to the items section. */
    @FXML
    private void displayBillItems() {
        itemsOnBill.getChildren().clear();
        for (Item item : newBill.getItems().keySet()) {
            StackPane pane = new StackPane();
            pane.setPrefSize(552, 40);
            pane.setPadding(new Insets(5, 5, 5, 5));
            Integer numberOfItemInCart = newBill.getItems().get(item);
            Label itemName = new Label("Item: " + item.getName());
            Label numberOfItem = new Label(Integer.toString(numberOfItemInCart));
            Label priceOfItem = new Label("Sum: " + String.format("%.2f", item.getPrice() * numberOfItemInCart));
            Label taxOfItems = new Label("Tax:" + String.format("%.2f", (item.getPrice() * numberOfItemInCart) * (item.getTaxOnItem() / 10)));
            Button plus = new Button("+");
            plus.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    newBill.addItemToBill(item);
                    displayBillItems();
                }
            });
            Button minus = new Button("-");
            minus.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    newBill.removeItemFromBill(item);
                    displayBillItems();
                }
            });
            StackPane.setAlignment(itemName, Pos.TOP_LEFT);
            StackPane.setAlignment(numberOfItem, Pos.BOTTOM_LEFT);
            StackPane.setAlignment(priceOfItem, Pos.BOTTOM_RIGHT);
            StackPane.setAlignment(taxOfItems, Pos.TOP_RIGHT);
            StackPane buttons = new StackPane();
            buttons.setMaxWidth(50);
            StackPane.setAlignment(plus, Pos.CENTER_LEFT);
            StackPane.setAlignment(minus, Pos.CENTER_RIGHT);
            buttons.getChildren().addAll(plus, minus);
            StackPane.setAlignment(buttons, Pos.CENTER);
            pane.getChildren().addAll(itemName, numberOfItem, priceOfItem, buttons);
            itemsOnBill.getChildren().add(pane);
        }
    }

    /* Updates totals for the bill */
    @FXML
    private void updateTotals() {
        Double totalBillCost = newBill.getTotalCostOfBill();
        Double totalBillTax = newBill.getTotalTaxOnBill();

        totalWithoutTax.setText(String.format("%.2f", totalBillCost - totalBillTax));
        totalTax.setText(String.format("%.2f", totalBillTax));
        billTotal.setText(String.format("%.2f", totalBillCost));
    }

    /* Displays all items avalible in the company */
    @FXML
    private void displayAllCompanyItems() {
        companyItemsContainer.getChildren().remove(selectItem);
        ObservableList<Item> items = FXCollections.observableArrayList(currentCompany.getCompanyItems());
        selectItem = new ChoiceBox<Item>(items);
        selectItem.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @SuppressWarnings("rawtypes")
            public void changed(ObservableValue ov, Number value, Number newValue) {
                /* Sets the tmpItem as the item selected. */
                tmpItem = items.get((int) newValue);
            }
        });
        companyItemsContainer.getChildren().add(selectItem);
    }

    /* Adds the selected item in the choicebox */
    @FXML
    private void addItemToBill() {
        if (tmpItem != null) {
            newBill.addItemToBill(tmpItem);
            displayBillItems();
            updateTotals();
        }
    }

    /* Adds a newly created item to the bill */
    @FXML 
    private void addNewItemToBill() {
        try {
            Item newItem = new Item(null, itemName.getText(), Double.valueOf(itemPrice.getText()), Double.valueOf(itemTax.getText()));
            currentCompany.addItemToCompany(newItem);
            tmpItem = newItem;
            addItemToBill();
            displayBillItems();
            updateTotals();
            displayAllCompanyItems();
        } catch (IllegalArgumentException e) {
            displayMessage("Invalid item fields");
        }
    }

    /* The following three methods add dates to the bill. */
    @FXML
    private void addDateOfSale() {
        try {
            newBill.addDateOfSale(dateOfSale.getValue().toString());
        } catch (ParseException e) {
            displayMessage("Error reading date of sale");
        }
    }

    @FXML
    private void addDateOfDelivery() {
        try {
            newBill.addDateOfDelivery(deliveryDate.getValue().toString());
        } catch (ParseException e) {
            displayMessage("Error reading date of delivery");
        }
    }

    @FXML
    private void addDueDate() {
        try {
            newBill.addDueDate(dueDate.getValue().toString());
        } catch (ParseException e) {
            displayMessage("Error reading due date");
        }
    }

    /* Completes and sends a bill and goes back to the overview screen. */
    @FXML
    private void completeBill() {
        try {
            currentCompany.sendFinishedBill(newBill, currentCompany.getCurrentBillId());
            goToView("Overview", "Overview.fxml", (Stage) name.getScene().getWindow());
        } catch (IllegalArgumentException e) {
            displayMessage("You need to fill out all fields in order to complete a bill.");
        }
    }

    /* Saves the already added fields to the bill and saves it as a unfinished bill and goes back to the overview screen. */
    @FXML
    private void saveUnfinishedBill() {
        try {
            if (newBill.minimumLegalState()) {
                currentCompany.addUnfinishedBill(newBill);
                goToView("Overview", "Overview.fxml", (Stage) name.getScene().getWindow());
            }   
        } catch (IllegalArgumentException e) {
            displayMessage("You need to minimum have a customer and an item to save a bill. ");
        }
    }

    /* Exits create bill and does not save what has been done on the bill. */
    @FXML
    private void exitCreateBill() {
        goToView("Overview", "Overview.fxml", (Stage) name.getScene().getWindow());
    }

    /* Init allows a already edited bill to viewed. If no bill is specified a new one gets created. */
    public void init() {
        if (activeBill != null) {
            this.newBill = activeBill;
            displayBillItems();
            try {
                customerOnBill.setText(newBill.getBillCustomer().toString());
            } catch (NullPointerException e) {
                customerOnBill.setText("NO CUSTOMER");
            }

        } else {
            newBill = new Bill(currentCompany, null);
        }
        displayCustomersInChoiceBox();
        displayAllCompanyItems();
        currentCompanyInfo.setText(currentCompany.toString());
    }
}
