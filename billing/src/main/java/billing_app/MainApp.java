package billing_app;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

import billing_app.items.Bill;
import billing_app.items.Item;
import billing_app.items.OrganizationalId;
import billing_app.logic.Company;
import billing_app.logic.Customer;

public class MainApp extends Application {


    @FXML

    public static Collection<Company> companies = new ArrayList<Company>();
    public static double viewPortMaxHeight;
    public static double viewPortMaxWidth;

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static void printToConsole(String s) {
        System.out.println(s);
    }

    public static Company addCompanyToApplication() {
        Company testCompany = new Company(); 
        testCompany.setCompanyLogoPath(new )
        testCompany.setName("Statoil");
        OrganizationalId testOrgId = new OrganizationalId("991825827");
        testCompany.setOriganizationalId(testOrgId);
        Customer kunde = new Customer("1892374");
        kunde.setName("Apple");
        Item testItem = new Item("Kjøttboller", 29.90, 12.0, "Canned foods");
        Item testItem2 = new Item("iPhone 13 Pro Max", 13900.0, 25.0, "Mobile Phones");
        Bill bill = new Bill(testCompany);
        bill.addItemToBill(testItem);
        bill.addItemToBill(testItem2);
        bill.addCustomerToBill(kunde);
        Bill bill2 = new Bill(testCompany);
        bill2.addItemToBill(testItem);
        bill2.addItemToBill(testItem2);
        bill2.addCustomerToBill(kunde);
        testCompany.addUnfinishedBill(bill);
        testCompany.addUnfinishedBill(bill2);
        return testCompany;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Example App");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("Overview.fxml"))));
        primaryStage.setMaximized(true);
        viewPortMaxHeight = primaryStage.getMaxHeight();
        viewPortMaxWidth = primaryStage.getMaxWidth();
        
        primaryStage.show();
    }

}
