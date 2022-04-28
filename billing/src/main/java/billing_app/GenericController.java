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
import billing_app.saving.SaveCompany;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
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

/* GenericController is an abstact controller class that contains methods and attributes that are shared between the controllers. */

public abstract class GenericController {

    @FXML
    private TextField name, address, city, postalCode, country, orgId, startingBillId;

    @FXML
    private CheckBox saveCustomer;

    @FXML
    private Label legalOrgId;

    @FXML
    private Button selectFileButton;
    
    public Company currentCompany;
    public Bill activeBill;
    public Stage prevStage;

    /* displayMessage is for displaying error or other relevant messages to the user */
    
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
        stage.isFocused();
    }

    public void setCompany(Company company) {
        this.currentCompany = company;
    }

    public void setActiveBill(Bill bill) {
        this.activeBill = bill; 
    }

    /* Method for setting the previous stage for closing when switching between windows. */
    public void setPrevStage(Stage stage) {
        this.prevStage = stage; 
    }


    /* goToView allows controllers that implement ControllerInterface and extends GenericController to switch between windows and keep track of the current company while switching between windows. It also calls the method init that is defined in the interface. */
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

    /* This goToView is just for allowing editing of a bill. */
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

    /* createBusiness takes all the data that is shared between a customer and a company and makes either a company or customer based on what type is passed in. */
    @FXML
    public boolean createBusiness(Business tmp) {
        boolean legal = true;
        try {
            tmp.setName(name.getText());
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
                legal = false;
                displayMessage("Invalid organizational ID");
            }
            if (tmp instanceof Company) {
                Company tmp1 = (Company) tmp;
                try {
                    tmp1.setCurrentBillId(Integer.valueOf(startingBillId.getText()));
                } catch (NumberFormatException e) {
                    displayMessage("Current bill id has to be an integer.");
                    legal = false;
                }
                if (legal) this.currentCompany = tmp1;
            }
            if (tmp instanceof Customer) {
                if (legal) this.currentCompany.allCompanyCustomers.add((Customer) tmp);
            }
        } catch (IllegalArgumentException e) {
            displayMessage(e.toString());
        }
        return legal;
    }

    /* Mothod for displaying a visual aid if the Organizational id is valid or not when the user types it in. */
    @FXML
    private void handleOrganizationalIdChange() {
        try {
            new OrganizationalId(orgId.getText());
            legalOrgId.setText("Valid");
        } catch (IllegalArgumentException e) {
            legalOrgId.setText("Invalid");
        }   
    }

    /* Method for giving "autocomplete" on city and country based on the postal code entered. */
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

    /* Uses the saveCompany method loadCompanyFromFile to load a exsisting company and goes to the overview screen. It lets the user select the savefile it wants to use. Displays appropriate messages based on the exception thrown.*/
    @FXML
    private void loadCompany() {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select savefile");
            File selectedFile = fileChooser.showOpenDialog(stage);
            SaveCompany load = new SaveCompany();
            try {
                currentCompany = load.loadCompanyFromFile(Paths.get(selectedFile.getAbsolutePath()).toFile());
                goToView("Overview", "Overview.fxml", (Stage) selectFileButton.getScene().getWindow());
            } catch (FileNotFoundException e) {
                displayMessage("Could not locate savefile");
            } catch (LoadException e) {
                displayMessage("Error reading from savefile");
            } catch (IllegalArgumentException e) {
                displayMessage("Invalid file format.");
            }
    }
}
