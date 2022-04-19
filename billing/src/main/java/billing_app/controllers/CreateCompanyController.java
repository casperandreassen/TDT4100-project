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
    private TextField startingId;

    @FXML 
    private Label validOrgId, logoPath;

    @FXML
    private Button selectFileButton, createCompany;

    String tmpLogoPath;

    Stage prevStage;


    @FXML
    private void handleLogoSelect() {
        try {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select company logo");
            File selectedFile = fileChooser.showOpenDialog(stage);
            tmpLogoPath = Paths.get(selectedFile.getAbsolutePath()).toString();
            logoPath.setText(Paths.get(selectedFile.getAbsolutePath()).toString());
        } catch (Exception e) {
            e.printStackTrace();
            /* Add some logic for displaying popup error messages. */
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

    @FXML
    private void createCompany() {
        createBusiness(new Company());
        currentCompany.setCompanyLogoPath(tmpLogoPath);
        goToView("Overview", "Overview.fxml", (Stage) selectFileButton.getScene().getWindow());
    }

    public void setPrevStage(Stage stage) {
        this.prevStage = stage;
    }


	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}

