package billing_app.controllers;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import billing_app.MainApp;
import billing_app.items.Bill;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OverviewController extends GenericController implements ControllerInterface {

    @FXML
    VBox main_vbox;

    @FXML
    Button knapp;

    @FXML
    Pane root_pane;

    @FXML
    StackPane companyInfoPane;

    @FXML
    Pane company_avatar_pane;

    List<Bill> billsOnDisplay = new ArrayList<Bill>();



    /* Get this properly working sometime */
    public void adjustMainPaneSize() {
        root_pane.prefHeight(MainApp.viewPortMaxHeight);
        root_pane.prefWidth(MainApp.viewPortMaxWidth);
    }

    public void goToCreateBill() {
        goToView("Create new bill", "CreateBill.fxml", (Stage) root_pane.getScene().getWindow());
    }


    /* Add all info to bills and make it clickable */
    public void displayBillsInVbox(Collection<Bill> bills) {
        main_vbox.getChildren().clear();
        for (Bill bill : bills) {
            StackPane pane = new StackPane();
            pane.setPrefSize(814, 100);
            pane.setPadding(new Insets(5, 5, 5, 5));

            Label customer = new Label("Customer: " + bill.getBillCustomer().getName());
            Label totalMva = new Label("Tax: " + Double.toString(bill.getTotalTaxOnBill()));
            Label totalCost = new Label("Total: " + Double.toString(bill.getTotalCostOfBill()));
            String date = "N/A";
            try {
                date = bill.getDueDate().
            toZonedDateTime().format(DateTimeFormatter.ofPattern("d MMM uuuu"));
            } catch (NullPointerException e) {
            }

            Label dueDate = new Label("Due date: " + date);
            Button editButton = new Button("Edit");
            editButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    goToView("Edit", "CreateBill.fxml", (Stage) main_vbox.getScene().getWindow(), bill);
                }
                
            });

            StackPane.setAlignment(customer, Pos.TOP_LEFT);
            StackPane.setAlignment(totalMva, Pos.CENTER_RIGHT);
            StackPane.setAlignment(totalCost, Pos.BOTTOM_RIGHT);
            StackPane.setAlignment(dueDate, Pos.BOTTOM_LEFT);
            StackPane.setAlignment(editButton, Pos.CENTER);

            pane.getChildren().addAll(customer, totalMva, totalCost, dueDate, editButton);
            main_vbox.getChildren().add(pane);
            }
    }

    @FXML
    public void showUncompletedBills() {
        displayBillsInVbox(currentCompany.companyUnfinishedBills);
    }

    @FXML
    public void showCompletedBills() {
        displayBillsInVbox(currentCompany.companySentBills);
    }


    @FXML
    public void init() {
        displayBillsInVbox(currentCompany.companySentBills);
        try {
            ImageView company_avatar = new ImageView(new Image(currentCompany.getCompanyLogoFileStream()));
            company_avatar.setFitHeight(50);
            company_avatar.setFitWidth(60);
            company_avatar_pane.getChildren().add(company_avatar);
            
        } catch (FileNotFoundException e) {
            MainApp.printToConsole(e.toString());
        }
        /* Should also catch IOException here */

        Label companyName = new Label(currentCompany.getName());
        Label companyAddress = new Label(currentCompany.getAddress().toString());
        StackPane.setAlignment(companyName, Pos.TOP_LEFT);
        StackPane.setAlignment(companyAddress, Pos.BOTTOM_LEFT);
        companyInfoPane.getChildren().addAll(companyName, companyAddress);

        companyName.setText(currentCompany.getName());
        companyAddress.setText(currentCompany.getAddress().toString());
    }


}
