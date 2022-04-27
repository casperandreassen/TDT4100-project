package billing_app;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import billing_app.logic.Company;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/* This controller creates new companies and loads from savefile. */
public class CreateCompanyController extends GenericController implements ControllerInterface {
    
    @FXML
    private TextField startingId;

    @FXML 
    private Label validOrgId, logoPath;

    @FXML
    private Button selectFileButton, createCompany;

    private String tmpLogoPath;

    Stage prevStage;

    /* Opens a filechooser window and lets the user choose the picture it wants to use as a logo. */
    @FXML
    private void handleLogoSelect() {
        try {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select company logo");
            File selectedFile = fileChooser.showOpenDialog(stage);
            tmpLogoPath = Paths.get(selectedFile.getAbsolutePath()).toString();
        } catch (RuntimeException e) {
        }
    }

    /* Create company uses the createBusiness method defined in the generic controller. Uses comppanys getLogoPath method to set the logo of the company. It also goes to the overview screen once it is complete.*/
    @FXML
    private void createCompany() {
        try {
            createBusiness(new Company(null));
        } catch (RuntimeException e) {
            displayMessage("Missing fields");
        }
        try {
            currentCompany.setCompanyLogoPath(currentCompany.getLogoPath(tmpLogoPath));
            goToView("Overview", "Overview.fxml", (Stage) selectFileButton.getScene().getWindow());
        } catch (URISyntaxException e) {
            displayMessage("Error finding default avatar");
        }
    }

    /* Unused init method from interface. */
	public void init() {
	}
}

