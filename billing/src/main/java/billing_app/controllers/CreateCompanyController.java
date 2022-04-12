package billing_app.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import billing_app.MainApp;
import billing_app.items.Address;
import billing_app.items.OrganizationalId;
import billing_app.logic.Company;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CreateCompanyController extends GenericController implements ControllerInterface {
    
    @FXML
    private TextField companyName, startingId, orgId, address, postalCode, city, country;


    @FXML 
    private Label validOrgId, logoPath;

    @FXML
    private Button selectFileButton, createCompany;

    OrganizationalId companyOrgId;
    Address companyAddress;
    String companyLogoPath;
    Company createdCompany = new Company();

    Stage prevStage;

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
        companyLogoPath = Paths.get(selectedFile.getAbsolutePath()).toString();
        if (createdCompany.isValidLogo(companyLogoPath)) {
            logoPath.setText(companyLogoPath.toString().split("\\/(.*)")[0]);
        } else {
            logoPath.setText("Invalid file");
        }
    }

    @FXML
    private void handlePostalCodeInput() {
        companyAddress = new Address();
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

    @FXML
    private void createCompany() {
        companyAddress.setAddress(address.getText());
        companyAddress.setCity(city.getText());
        companyAddress.setPostalCode(postalCode.getText());
        companyAddress.setCountry(country.getText());

        createdCompany.setAddress(companyAddress);
        createdCompany.setName(companyName.getText());
        createdCompany.setCurrentBillId(Integer.parseInt(startingId.getText()));
        createdCompany.setCompanyLogoPath(companyLogoPath);
        createdCompany.setOriganizationalId(companyOrgId);
        goToView("Overview", "Overview.fxml", (Stage) selectFileButton.getScene().getWindow());
    }

    public void setPrevStage(Stage stage) {
        this.prevStage = stage;
    }

    public void goToOverview() {
        try {
            Stage stage = new Stage();
            stage.setTitle("Overview");
            Pane myPane = null;

            /* Add this as a relative path for the project */
            myPane = FXMLLoader.load(new URL("file:///Users/casper/code/TDT4100-project/billing/src/main/resources/billing_app/Overview.fxml"));
            Scene scene = new Scene(myPane);
            stage.setScene(scene);
            prevStage.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException w) {
            w.printStackTrace();
        }
    }

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}

