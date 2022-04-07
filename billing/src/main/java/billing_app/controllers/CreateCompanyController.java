package billing_app.controllers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import billing_app.MainApp;
import billing_app.items.Address;
import billing_app.items.OrganizationalId;
import billing_app.logic.Company;
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
    private Label validOrgId, logoPath;

    @FXML
    private Button selectFileButton;

    OrganizationalId companyOrgId;
    Address companyAddress;
    Path companyLogoPath;
    Company createdCompany = new Company();

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
        if (createdCompany.isValidLogo(companyLogoPath)) {
            logoPath.setText(companyLogoPath.toString().split("\\/(.*)")[0]);
        } else {
            logoPath.setText("Invalid file");
        }
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
        createdCompany.setAddress(companyAddress);
        createdCompany.setName(companyName.getText());
        createdCompany.setCurrentBillId(Integer.parseInt(startingId.getText()));
        createdCompany.setCompanyLogoPath(companyLogoPath);
        createdCompany.setOriganizationalId(companyOrgId);
        MainApp.printToConsole(createdCompany.toString());
    }
}

