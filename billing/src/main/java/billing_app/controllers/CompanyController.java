package billing_app.controllers;

import java.io.IOException;

import billing_app.Address;
import billing_app.Company;
import billing_app.MainApp;
import billing_app.OrganizationalId;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CompanyController {
    
    @FXML
    private TextField companyName, startingId, orgId, address, postalCode, city, country;

    @FXML 
    private Label validOrgId;

    OrganizationalId companyOrgId;
    Address companyAddress;
    Company company;


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
    private void updateValidOrgId(String valid) {
        validOrgId.setText(valid);
    }

    @FXML
    private void createCompany() {
        String tmpAddress = address.getText();
        String tmpPostalCode = postalCode.getText();
        String tmpCity = city.getText();
        String tmpCountry = country.getText();
        /* int tmpStartingBillId = Integer.parseInt(startingId.getText()); */
        /* String tmpName = companyName.getText(); */
        /* orgId is pulled and validated at entry */
        try {
            companyAddress = new Address(tmpAddress, tmpPostalCode, tmpCity, tmpCountry);
        } finally {
            MainApp.printToConsole("failed address");
        }
        
        /* try {
            company = new Company(tmpName, companyOrgId, tmpStartingBillId, companyAddress);
        } catch (IllegalArgumentException e) {
            MainApp.printToConsole(e.toString());
        }
        MainApp.printToConsole(company.getCompanyName()); */
    }

}

