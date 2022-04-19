package billing_app.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import billing_app.items.Bill;
import billing_app.items.Item;
import billing_app.logic.Customer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class CreateBillController extends GenericController implements ControllerInterface {

    @FXML
    StackPane sellingCompanyInfoPane;

    @FXML
    ChoiceBox<Item> selectItem;

    @FXML
    ChoiceBox<Customer> customerSelect;
    
    @FXML 
    TextField name, address, city, postalCode, country, orgId, itemName, itemPrice, itemTax; 

    @FXML
    CheckBox saveCustomer, saveItem;

    @FXML
    VBox itemsOnBill;
    
    @FXML
    TilePane customerChoicePane, companyItemsContainer;

    @FXML
    Button addItemButton, addItemBill, saveBillButton, completeBillButton, addExistingCustomerButton, addNewCustomerButton;

    @FXML
    Label currentCompanyInfo, customerOnBill, totalWithoutTax, billDiscount, billTotal, totalTax;

    @FXML
    DatePicker dateOfSale, deliveryDate, dueDate;

    Bill newBill;

    List<Item> itemsOnDisplay = new ArrayList<Item>();

    Customer tmpCustomer;
    Item tmpItem;

    @FXML
    public void displayCustomersInChoiceBox() {
        customerChoicePane.getChildren().remove(customerSelect);
        ObservableList<Customer> customers = FXCollections.observableArrayList(currentCompany.allCompanyCustomers);
        customerSelect = new ChoiceBox<Customer>(customers);

        customerSelect.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number newValue) {
                tmpCustomer = customers.get((int) newValue);
            }
        });
        customerChoicePane.getChildren().add(customerSelect);
    }

    /* UUIDS are created automatically for each customer. */

    @FXML
    public void addNewCustomer() {
        Customer tmp = new Customer(UUID.randomUUID());
        createBusiness(tmp);
        newBill.addCustomerToBill(tmp);
        customerOnBill.setText(newBill.getBillCustomer().toString());
        displayCustomersInChoiceBox();
    }


    @FXML
    public void addCustomer() {
        if (tmpCustomer != null) {
            newBill.addCustomerToBill(this.tmpCustomer);
            customerOnBill.setText(newBill.getBillCustomer().toString());
        } else {
            displayMessage("Missing customer info");
        }
    }

    @FXML
    public void removeCustomer() {
        try {
            newBill.removeCustomer();
        } catch (IllegalArgumentException e) {
            displayMessage("Error removing customer from bill");
        }
    }

    @FXML
    public void displayBillItems() {
        itemsOnBill.getChildren().clear();
        for (Item item : newBill.getItems().keySet()) {
            StackPane pane = new StackPane();
            pane.setPrefSize(552, 40);
            pane.setPadding(new Insets(5, 5, 5, 5));
            Integer numberOfItemInCart = newBill.getItems().get(item);
            Label itemName = new Label("Item: " + item.getName());
            Label numberOfItem = new Label(Integer.toString(numberOfItemInCart));
            Label priceOfItem = new Label("Sum: " + Double.toString(item.getPrice() * numberOfItemInCart));
            Label taxOfItems = new Label("Tax:" + Double.toString((item.getPrice() * numberOfItemInCart) * (item.getTaxOnItem() / 10)));
            /* Button plus = new Button("+");
            plus.setOnAction(event -> changeNumberOfItem(event));
            Button minus = new Button("-");
            plus.setOnAction(event -> changeNumberOfItem(event)); */
            StackPane.setAlignment(itemName, Pos.TOP_LEFT);
            StackPane.setAlignment(numberOfItem, Pos.BOTTOM_LEFT);
            StackPane.setAlignment(priceOfItem, Pos.BOTTOM_RIGHT);
            StackPane.setAlignment(taxOfItems, Pos.TOP_RIGHT);
            /* StackPane buttons = new StackPane();
            buttons.getChildren().addAll(plus, minus); */
            /* StackPane.setAlignment(buttons, Pos.CENTER); */
            pane.getChildren().addAll(itemName, numberOfItem, priceOfItem);
            itemsOnBill.getChildren().add(pane);
        }
        Double totalBillCost = newBill.getTotalCostOfBill();
        Double totalBillTax = newBill.getTotalTaxOnBill();

        totalWithoutTax.setText(Double.toString(totalBillCost - totalBillTax));
        totalTax.setText(Double.toString(totalBillTax));
        billTotal.setText(Double.toString(totalBillCost));
    }

    @FXML
    public void displayAllCompanyItems() {
        companyItemsContainer.getChildren().remove(selectItem);
        ObservableList<Item> items = FXCollections.observableArrayList(currentCompany.getCompanyItems());
        selectItem = new ChoiceBox<Item>(items);
        selectItem.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number newValue) {
                tmpItem = items.get((int) newValue);
            }
        });
        companyItemsContainer.getChildren().add(selectItem);
    }

    @FXML
    public void addItemToBill() {
        if (tmpItem != null) {
            newBill.addItemToBill(tmpItem);
            displayBillItems();
        }
    }

    @FXML 
    public void addNewItemToBill() {
        try {
            Item newItem = new Item(itemName.getText(), Double.valueOf(itemPrice.getText()), Double.valueOf(itemTax.getText()));
            currentCompany.addItemToCompany(newItem);
            tmpItem = newItem;
            addItemToBill();
        } catch (IllegalArgumentException e) {
            displayMessage("Invalid item fields");
        }
    }

    @FXML
    public void completeBill() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar dateOs = new GregorianCalendar();
        GregorianCalendar dateOd = new GregorianCalendar();
        GregorianCalendar dueD = new GregorianCalendar();

        try {
            Date date = df.parse(dateOfSale.getValue().toString());
            dateOs.setTime(date);
            newBill.addDateOfSale(dateOs);

            df.parse(deliveryDate.getValue().toString());
            dateOd.setTime(date);
            newBill.addDateOfDelivery(dateOd);

            df.parse(dueDate.getValue().toString());
            dueD.setTime(date);
            newBill.addDueDate(dueD);
        } catch (ParseException e) {
            displayMessage("Date format conversion error");
        } catch (NullPointerException e) {
            displayMessage("Missing fields");
        }

        try {
            currentCompany.sendFinishedBill(newBill, currentCompany.getCurrentBillId());
            currentCompany.setCurrentBillId(currentCompany.getCurrentBillId() + 1);
            goToView("Overview", "Overview.fxml", (Stage) name.getScene().getWindow());
        } catch (IllegalAccessException e) {
            displayMessage(e.toString());
        }
    }
    @FXML
    public void saveUnfinishedBill() {
        currentCompany.addUnfinishedBill(newBill);
        goToView("Overview", "Overview.fxml", (Stage) name.getScene().getWindow());
    }

    @FXML
    public void init() {
        if (activeBill != null) {
            this.newBill = activeBill;
            displayBillItems();
            customerOnBill.setText(newBill.getBillCustomer().toString());
        } else {
            newBill = new Bill(currentCompany);
        }
        displayCustomersInChoiceBox();
        displayAllCompanyItems();
        currentCompanyInfo.setText(currentCompany.toString());
    }
}
