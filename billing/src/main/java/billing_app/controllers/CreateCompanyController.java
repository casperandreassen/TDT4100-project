package billing_app.controllers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import billing_app.Address;
import billing_app.Company;
import billing_app.MainApp;
import billing_app.OrganizationalId;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CreateCompanyController {
    
    @FXML
    private TextField companyName, startingId, orgId, address, postalCode, city, country;

    @FXML 
    private Label validOrgId;

    @FXML
    private Button selectFileButton;

    OrganizationalId companyOrgId;
    Address companyAddress;
    Path companyLogoPath;
    Company createdCompany;


    @FXML
    private void handleOrganizationalIdChange() {
        String tmpOrgId = orgId.getText();
        try {
            companyOrgId = new OrganizationalId(tmpOrgId);
            updateValidOrgId("Valid");
        } catch (IllegalArgumentException e) {
            updateValidOrgId("Invalid");
        }   
    }


    @FXML
    private void updateValidOrgId(String validity) {
        validOrgId.setText(validity);
    }

    @FXML
    private void handleLogoSelect() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select company logo");
        File selectedFile = fileChooser.showOpenDialog(stage);
        companyLogoPath = Paths.get(selectedFile.getAbsolutePath());
    }

    @FXML
    private void handlePostalCodeInput() {
        if (postalCode.getText().length() == 4) {
            companyAddress = new Address();
            city.setText(companyAddress.postalCodes.get(postalCode.getText()));
            country.setText("NORWAY");
        } else {
            city.setText("");
            country.setText("");
        }
    }

    @FXML
    private void createCompany() {
        createdCompany = new Company();
        createdCompany.setCompanyAddress(companyAddress);
        createdCompany.setCompanyName(companyName.getText());
        createdCompany.setCurrentBillId(Integer.parseInt(startingId.getText()));
        createdCompany.setCompanyLogoPath(companyLogoPath);
        createdCompany.setOriganizationalId(companyOrgId);
        MainApp.printToConsole(createdCompany.toString());
    }

}

