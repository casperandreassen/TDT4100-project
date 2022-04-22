package billing_app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

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
import javafx.stage.FileChooser;
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
            URL fileUrl = getClass().getResource(view);
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
            URL fileUrl = getClass().getResource(view);
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
            try {
                Address tmpAdress = new Address();
                tmpAdress.setAddress(address.getText());
                tmpAdress.setCity(city.getText());
                tmpAdress.setPostalCode(postalCode.getText());
                tmpAdress.setCountry(country.getText());
                tmp.setAddress(tmpAdress);
            } catch (FileNotFoundException e) {
                displayMessage("Could not find postal code file. This should be located in /billing/store/static");
            } catch (IOException e) {
                displayMessage("Error reading file.");
            } catch (URISyntaxException e) {
                displayMessage("Error finding file.");
            }
            try {
                tmp.setOriganizationalId(new OrganizationalId(orgId.getText()));
            } catch (IllegalArgumentException e) {
                displayMessage("Invalid organizational ID");
            }
            if (tmp instanceof Company) {
                Company tmp1 = (Company) tmp;
                try {
                    tmp1.setCurrentBillId(Integer.valueOf(startingBillId.getText()));
                } catch (NumberFormatException e) {
                    displayMessage("Current bill id has to be an integer.");
                }
                this.currentCompany = tmp1;
            }
            if (tmp instanceof Customer) {
                this.currentCompany.allCompanyCustomers.add((Customer) tmp);
            }
        } catch (IllegalArgumentException e) {
            displayMessage(e.toString());
        }
    }

    /* This is kind of a hacky way to do it, should instead ask the class if its legal or not. */
    @FXML
    private void handleOrganizationalIdChange() {
        try {
            new OrganizationalId(orgId.getText());
            legalOrgId.setText("Valid");
        } catch (IllegalArgumentException e) {
            legalOrgId.setText("Invalid");
        }   
    }

    @FXML
    private void handlePostalCodeInput() {
        try {
            if (postalCode.getText().length() == 4) {
                Address companyAddress = new Address();
                String cityName = companyAddress.postalCodes.get(postalCode.getText());
                if (city != null) {
                    city.setText(cityName);
                    country.setText("NORWAY");
                } 
            } else {
                city.setText("");
                country.setText("");
            }  
        } catch (FileNotFoundException e) {
            displayMessage("Could not locate file.");
        } catch (IOException e) {
            displayMessage("Error reading from file.");
        } catch (URISyntaxException e) {

        }
    }
}
