package billing_app;

import java.io.File;
import java.io.IOException;
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
        try {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select company logo");
            File selectedFile = fileChooser.showOpenDialog(stage);
            tmpLogoPath = Paths.get(selectedFile.getAbsolutePath()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            /* Add some logic for displaying popup error messages. */
        }
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
        currentCompany = load.loadCompanyFromFile(Paths.get(selectedFile.getAbsolutePath()).toString());
        goToView("Overview", "Overview.fxml", (Stage) selectFileButton.getScene().getWindow());
    }


	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}

