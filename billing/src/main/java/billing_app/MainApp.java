package billing_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class MainApp extends Application {

    public static Collection<Company> companies = new ArrayList<Company>();

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static void printToConsole(String s) {
        System.out.println(s);
    }

    public static void addCompanyToApplication(Company company) {
        companies.add(company);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Example App");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("Overview.fxml"))));
        primaryStage.show();
    }

}
