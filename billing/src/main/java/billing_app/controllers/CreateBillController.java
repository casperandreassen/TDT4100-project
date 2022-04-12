package billing_app.controllers;

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
    Button addItemButton, addItemBill, saveBillButton, completeBillButton;

    Bill newBill;

    @FXML
    public void displayCustomersInChoiceBox() {
        ObservableList<Customer> customers = FXCollections.observableArrayList(currentCompany.allCompanyCustomers);
        ChoiceBox<Customer> customerSelect = new ChoiceBox<Customer>(customers);

        customerSelect.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number newValue) {
                newBill.addCustomerToBill((Customer) ov);
            }
        });
        customerChoicePane.getChildren().add(customerSelect);
        MainApp.printToConsole(customers.toString());
    }

    @FXML
    public void init() {
        displayCustomersInChoiceBox();
        newBill = new Bill(currentCompany);
    }
}
