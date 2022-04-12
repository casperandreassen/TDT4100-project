package billing_app.controllers;

import java.io.IOException;
import java.net.URL;

import billing_app.MainApp;
import billing_app.logic.Company;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class GenericController {
    
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

            MainApp.printToConsole(new URL(String.format("file://%1$s/billing/src/main/resources/billing_app/%2$s", System.getProperty("user.dir"), view)).toString());
            FXMLLoader loader = new FXMLLoader(new URL(String.format("file://%1$s/billing/src/main/resources/billing_app/%2$s", System.getProperty("user.dir"), view)));
            Parent root = loader.load();

            ((GenericController) loader.getController()).setCompany(currentCompany);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            MainApp.printToConsole(e.toString());
        }
    }
}
