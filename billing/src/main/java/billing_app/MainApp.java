package billing_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Create company");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateCompany.fxml"));
        
        Pane pane = (Pane)loader.load();
        ((GenericController) loader.getController()).setCompany(null);
        ((ControllerInterface) loader.getController()).init();

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
