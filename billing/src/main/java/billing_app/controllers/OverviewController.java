package billing_app.controllers;

import billing_app.MainApp;
import billing_app.items.Bill;
import billing_app.logic.Company;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

public class OverviewController {
    @FXML
    TabPane companiesTabPane; 

    @FXML 
    ScrollPane billOverviewScrollPane;

    Company currentCompany;

    public void displayCompaniesInTabPane() {
        for (Company company : MainApp.companies) {
            companiesTabPane.getTabs().add(new Tab("hei"));
        }
    }

    public void displayCompanySentBillsInScrollPane() {
        for (Bill bill : currentCompany.companySentBills) {
            Pane billPaneTemplate = new Pane();
            billPaneTemplate.setPrefSize(290, 60);
            /* billOverviewScrollPane.setContent(new ); */
        }
    }

    public static void main(String[] args) {
        OverviewController test = new OverviewController();
        test.currentCompany = new Company();
        test.currentCompany.setCompanyName("caspers firma");
    }
}
