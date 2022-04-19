package billing_app.controllers;

import java.util.UUID;

import billing_app.MainApp;
import billing_app.items.Bill;
import billing_app.items.Item;
import billing_app.logic.Company;
import billing_app.logic.Customer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

public class CreateBillController extends GenericController implements ControllerInterface {

    @FXML
    StackPane sellingCompanyInfoPane;

    @FXML
    ChoiceBox<Item> selectItem;
    
    @FXML 
    TextField customerName, customerOrgId, customerAddress, customerPostalCode, customerCity, customerCountry, itemName, itemPrice, itemTax; 

    @FXML
    CheckBox saveCustomer, saveItem;

    @FXML
    Pane billItemsPane; 
    
    @FXML
    TilePane customerChoicePane;

    @FXML
    Button addItemButton, addItemBill, saveBillButton, completeBillButton, addExistingCustomerButton, addNewCustomerButton;

    @FXML
    Label currentCompanyInfo;

    Bill newBill;

    Customer tmpCustomer;

    @FXML
    public void displayCustomersInChoiceBox() {
        ObservableList<Customer> customers = FXCollections.observableArrayList(currentCompany.allCompanyCustomers);
        ChoiceBox<Customer> customerSelect = new ChoiceBox<Customer>(customers);

        customerSelect.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number newValue) {
                tmpCustomer = customers.get((int) newValue);
            }
        });
        customerChoicePane.getChildren().add(customerSelect);
        MainApp.printToConsole(customers.toString());
    }

    @FXML
    public void addNewCustomer() {
        createBusiness(new Customer(UUID.randomUUID()));
        
    }


    @FXML
    public void addCustomer() {
        if (tmpCustomer != null) {
            newBill.addCustomerToBill(this.tmpCustomer);
        } else {
            throw new IllegalArgumentException("You need to add a customer before you can add it to the bill.");
        }
    }

    @FXML
    public void removeCustomer() {
        try {
            newBill.removeCustomer();
        } catch (IllegalArgumentException e) {
            /* Maybe add a popup system for these kinds of messages. */
        }
    }

    @FXML
    public void init() {
        displayCustomersInChoiceBox();
        newBill = new Bill(currentCompany);
        currentCompanyInfo.setText(currentCompany.toString());
    }
}
