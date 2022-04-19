package billing_app.controllers;

import java.io.IOException;
import java.net.URL;

import billing_app.items.Address;
import billing_app.items.Bill;
import billing_app.items.OrganizationalId;
import billing_app.logic.Business;
import billing_app.logic.Company;
import billing_app.logic.Customer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public abstract class GenericController {

    @FXML
    TextField name, address, city, postalCode, country, orgId, startingBillId;

    @FXML
    CheckBox saveCustomer;

    @FXML
    Label legalOrgId;
    
    Company currentCompany;
    Bill activeBill;
    Stage prevStage;

    public void displayMessage(String errorMessage) {
        Stage stage = new Stage();
        StackPane root = new StackPane();
        Label message = new Label(errorMessage);
        Button okButton = new Button("Ok");
        StackPane.setAlignment(message, Pos.CENTER);
        StackPane.setAlignment(okButton, Pos.BOTTOM_CENTER);
        stage.setMinHeight(100);
        stage.setMinWidth(100);
        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                stage.hide();
            }
            
        });
        root.getChildren().addAll(message, okButton);
        stage.setScene(new Scene(root));
        stage.show();
        
    }

    public void setCompany(Company company) {
        this.currentCompany = company;
    }

    public void setActiveBill(Bill bill) {
        this.activeBill = bill; 
    }

    public void setPrevStage(Stage stage) {
        this.prevStage = stage; 
    }


    public void goToView(String title, String view, Stage prevStage) {
        try {
            URL fileUrl = new URL(String.format("file://%1$s/billing/target/classes/billing_app/%2$s", System.getProperty("user.dir"), view));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(fileUrl);
            Stage stage = new Stage();
            Pane root = (Pane) loader.load();
            ((GenericController) loader.getController()).setCompany(currentCompany);
            ((ControllerInterface) loader.getController()).init();
            stage.setScene(new Scene(root));
            prevStage.close();
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            displayMessage(e.toString());
        }
    }

    public void goToView(String title, String view, Stage prevStage, Bill editBill) {
        try {
            URL fileUrl = new URL(String.format("file://%1$s/billing/target/classes/billing_app/%2$s", System.getProperty("user.dir"), view));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(fileUrl);
            Stage stage = new Stage();
            Pane root = (Pane) loader.load();
            if (editBill != null) {
                ((GenericController) loader.getController()).setActiveBill(editBill);
            }
            ((GenericController) loader.getController()).setCompany(currentCompany);
            ((ControllerInterface) loader.getController()).init();
            stage.setScene(new Scene(root));
            prevStage.close();
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            displayMessage(e.toString());
        }
    }

    @FXML
    public void createBusiness(Business tmp) {
        try {
            tmp.setName(name.getText());
            /* Maybe display some error message here aswell. */
            Address tmpAdress = new Address();
            tmpAdress.setAddress(address.getText());
            tmpAdress.setCity(city.getText());
            tmpAdress.setPostalCode(postalCode.getText());
            tmpAdress.setCountry(country.getText());
            tmp.setAddress(tmpAdress);
            try {
                tmp.setOriganizationalId(new OrganizationalId(orgId.getText()));
            } catch (IllegalArgumentException e) {
                /* Display some error message here. */
            }
            if (tmp instanceof Company) {
                Company tmp1 = (Company) tmp;
                tmp1.setCurrentBillId(Integer.valueOf(startingBillId.getText()));
                this.currentCompany = tmp1;
            }
            if (tmp instanceof Customer) {
                this.currentCompany.allCompanyCustomers.add((Customer) tmp);
            }
        } catch (IllegalArgumentException e) {
            displayMessage(e.toString());
        }
    }

    /* This is to get "live" updated validity for the orgid. */
    @FXML
    private void handleOrganizationalIdChange() {
        String tmpOrgId = orgId.getText();
        try {
            OrganizationalId tmp = new OrganizationalId(tmpOrgId);
            legalOrgId.setText("Valid");
        } catch (IllegalArgumentException e) {
            legalOrgId.setText("Invalid");
        }   
    }

    @FXML
    private void handlePostalCodeInput() {
        Address companyAddress = new Address();
        if (postalCode.getText().length() == 4) {
            String cityName = companyAddress.postalCodes.get(postalCode.getText());
            if (city != null) {
                city.setText(cityName);
                country.setText("NORWAY");
            } 
        } else {
            city.setText("");
            country.setText("");
        }
    }
}
