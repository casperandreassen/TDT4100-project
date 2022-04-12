package billing_app;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import billing_app.controllers.CreateCompanyController;
import billing_app.controllers.OverviewController;
import billing_app.controllers.CreateCompanyController;
import billing_app.items.Bill;
import billing_app.items.Item;
import billing_app.items.OrganizationalId;
import billing_app.logic.Company;
import billing_app.logic.Customer;

public class MainApp extends Application {

    /* public static List<Company> companies = new ArrayList<Company>(); */
    public static Company companies = new Company();

    @FXML
    public static double viewPortMaxHeight;
    public static double viewPortMaxWidth;

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static void printToConsole(String s) {
        System.out.println(s);
    }

    /* public static void addCompanyToApp(Company company) {
        if (company != null) {
            companies.add(company);
        }
    } */

    /* This needs to be made properly */
    public static Company getCompany() {
        return companies;
    }

    public static void addCompanyToApplication() { 
        companies.setName("Statoil");
        companies.setCompanyLogoPath("/Users/casper/code/TDT4100-project/billing/src/main/resources/img/default_company_avatar.jpg");
        OrganizationalId testOrgId = new OrganizationalId("991825827");
        companies.setOriganizationalId(testOrgId);
        Customer kunde = new Customer("1892374");
        kunde.setName("Apple");
        Item testItem = new Item("Kjøttboller", 29.90, 12.0, "Canned foods");
        Item testItem2 = new Item("iPhone 13 Pro Max", 13900.0, 25.0, "Mobile Phones");
        Bill bill = new Bill(companies);
        bill.addItemToBill(testItem);
        bill.addItemToBill(testItem2);
        bill.addCustomerToBill(kunde);
        Bill bill2 = new Bill(companies);
        bill2.addItemToBill(testItem);
        bill2.addItemToBill(testItem2);
        bill2.addCustomerToBill(kunde);
        companies.addUnfinishedBill(bill);
        companies.addUnfinishedBill(bill2);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        addCompanyToApplication();
        primaryStage.setTitle("Billing app");
        System.out.println(getClass().getResource("CreateCompany.fxml").getPath());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Overview.fxml"));
        
        Pane pane = (Pane)loader.load();

        OverviewController controller = (OverviewController) loader.getController();
        controller.setPrevStage(primaryStage);
        controller.setCompany(companies);
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
