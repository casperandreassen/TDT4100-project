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
}
