package billing_app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import billing_app.logic.Company;
import billing_app.saving.SaveCompany;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select company logo");
        File selectedFile = fileChooser.showOpenDialog(stage);
        tmpLogoPath = Paths.get(selectedFile.getAbsolutePath()).toString();
    }

    @FXML
    private void createCompany() {
        createBusiness(new Company(null));
        currentCompany.setCompanyLogoPath(tmpLogoPath);
        goToView("Overview", "Overview.fxml", (Stage) selectFileButton.getScene().getWindow());
    }

    public void setPrevStage(Stage stage) {
        this.prevStage = stage;
    }

    @FXML
    public void loadCompany() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select savefile");
        File selectedFile = fileChooser.showOpenDialog(stage);
        SaveCompany load = new SaveCompany();
        try {
            currentCompany = load.loadCompanyFromFile(Paths.get(selectedFile.getAbsolutePath()).toString());
        } catch (FileNotFoundException e) {
            displayMessage("Could not locate savefile");
        } catch (IOException e) {
            displayMessage("Error reading from savefile");
        } catch (URISyntaxException e) {
            displayMessage("Could not locate postnummer text file");
        }
        goToView("Overview", "Overview.fxml", (Stage) selectFileButton.getScene().getWindow());
    }


	@Override
	public void init() {
	}
}

