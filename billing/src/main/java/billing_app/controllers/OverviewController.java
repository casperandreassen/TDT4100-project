package billing_app.controllers;

import billing_app.Bill;
import billing_app.Company;
import billing_app.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

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
            // billOverviewScrollPane.getChildren().add(new Pane());
            billOverviewScrollPane.setContent(new Label(bill.toString()));
        }
    }

    public static void main(String[] args) {
        OverviewController test = new OverviewController();
        test.currentCompany = new Company();
        test.currentCompany.setCompanyName("caspers firma");
    }
}
