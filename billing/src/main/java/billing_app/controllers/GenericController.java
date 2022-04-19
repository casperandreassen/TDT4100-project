package billing_app.controllers;

import java.io.IOException;
import java.net.URL;

import billing_app.MainApp;
import billing_app.items.Address;
import billing_app.items.OrganizationalId;
import billing_app.logic.Business;
import billing_app.logic.Company;
import billing_app.logic.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class GenericController {

    @FXML
    TextField name, address, city, postalCode, country, orgId, startingBillId;

    @FXML
    Label legalOrgId;
    
    Company currentCompany;
    Stage prevStage;

    public void setCompany(Company company) {
        this.currentCompany = company;
    }

    public void setPrevStage(Stage stage) {
        this.prevStage = stage; 
    }


    public void goToView(String title, String view, Stage prevStage ) {
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
            e.printStackTrace();
        }
    }

    @FXML
    public void createBusiness(Business tmp) {
        try {
            tmp.setName(name.getText());
            Address tmpAdress = new Address();
            tmpAdress.setAddress(address.getText());
            tmpAdress.setCity(city.getText());
            tmpAdress.setPostalCode(postalCode.getText());
            tmpAdress.setCountry(country.getText());
            tmp.setAddress(tmpAdress);
            if (tmp instanceof Company) {
                Company tmp1 = (Company) tmp;
                tmp1.setCurrentBillId(Integer.valueOf(startingBillId.getText()));
                this.currentCompany = tmp1;
            }
            if (tmp instanceof Customer) {
                this.currentCompany.allCompanyCustomers.add((Customer) tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
}
