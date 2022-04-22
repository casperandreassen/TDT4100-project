package billing_app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import billing_app.items.Bill;

import billing_app.saving.SaveCompany;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class OverviewController extends GenericController implements ControllerInterface {

    @FXML
    VBox main_vbox;

    @FXML
    Label totalWithoutTax, totalTax, totalRevenue, pageLabel;

    @FXML
    Button knapp;

    @FXML
    Pane root_pane;

    @FXML
    StackPane companyInfoPane;

    @FXML
    Pane company_avatar_pane;

    List<Bill> billsOnDisplay = new ArrayList<Bill>();
    List<ArrayList<Bill>> pages = new ArrayList<ArrayList<Bill>>();
    int activePage;



    public void goToCreateBill() {
        goToView("Create new bill", "CreateBill.fxml", (Stage) root_pane.getScene().getWindow());
    }


    @FXML
    public void nextPage() {
        if (activePage < pages.size()) {
            displayPage(pages.get(activePage + 1));
            activePage++;
            pageLabel.setText(String.format("%1$s/%2$s", activePage + 1, pages.size()));
        }
    }

    @FXML
    public void previousPage() {
        if (activePage > 0) {
            displayPage(pages.get(activePage - 1));
            activePage--;
            pageLabel.setText(String.format("%1$s/%2$s", activePage + 1, pages.size()));
        }
    }

    /* Add all info to bills and make it clickable */
    public void displayBillsInVbox(List<Bill> bills) {
        pages.clear();
        int maxPageNumber;
        if (bills.size() > 7) {
            maxPageNumber = 7;
            int numberOfPagesNeeded = (int) Math.ceil(bills.size() / 7.0);
            for (int i = 0; i < numberOfPagesNeeded; i++) {
                pages.add(new ArrayList<Bill>());
                for (int l = i * maxPageNumber; l < maxPageNumber * (i+1); l++) {
                    if (l < bills.size()) {
                        pages.get(i).add(bills.get(l));
                    } else {
                        break;
                    }
                }
            }
            displayPage(pages.get(0));
            activePage = 0;
            pageLabel.setText(String.format("1/%s", numberOfPagesNeeded));
        } else {
            pageLabel.setText(String.format("1/%s", 1));
            displayPage(bills);
        }
    }

    private void displayPage(List<Bill> bills) {
        main_vbox.getChildren().clear();
        for (Bill bill : bills) {
            StackPane pane = new StackPane();
            pane.setPrefSize(814, 100);
            pane.setPadding(new Insets(5, 5, 5, 5));
            pane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
            Label customer;
            Label totalMva;
            Label totalCost;
            Label billNumber;
            try {
                customer = new Label("Customer: " + bill.getBillCustomer().getName());
                totalMva = new Label("Tax: " + String.format("%.2f", bill.getTotalTaxOnBill()));
                totalCost = new Label("Total: " + String.format("%.2f", bill.getTotalCostOfBill()));
                billNumber = new Label("Bill number: " + String.format("%s", bill.getBillId()));
            } catch (NullPointerException e) {
                customer = new Label("Customer: N/A");
                totalMva = new Label("Tax: 0.0");
                totalCost = new Label("Total: 0.0");
                billNumber = new Label("Bill number: N/A");
            }
            String date = "N/A";
            try {
                date = bill.getDueDate().
            toZonedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (NullPointerException e) {
            }

            Label dueDate = new Label("Due date: " + date);
            if (!bill.sent) {
                Button typeButton = new Button("Edit");
                typeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    goToView("Edit", "CreateBill.fxml", (Stage) main_vbox.getScene().getWindow(), bill);
            }
            });
                StackPane.setAlignment(typeButton, Pos.CENTER_LEFT);
                pane.getChildren().add(typeButton);

            }
            StackPane.setAlignment(customer, Pos.TOP_LEFT);
            StackPane.setAlignment(totalMva, Pos.CENTER_RIGHT);
            StackPane.setAlignment(totalCost, Pos.BOTTOM_RIGHT);
            StackPane.setAlignment(dueDate, Pos.BOTTOM_LEFT);
            StackPane.setAlignment(billNumber, Pos.TOP_RIGHT);

            pane.getChildren().addAll(customer, totalMva, totalCost, dueDate, billNumber);
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
    public void saveCompanyState() {
        SaveCompany save = new SaveCompany();
        try {
            save.saveCompanyState(currentCompany, selectSaveLocationHandler());   
        } catch (IOException e) {
            displayMessage("Could not save to file");
        }
    }

    private String selectSaveLocationHandler() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select where to save");
        File selectedLocation = fileChooser.showSaveDialog(stage);
        return Paths.get(selectedLocation.getAbsolutePath()).toString() + ".txt";
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
            displayMessage("Could not locate image.");
        } catch (NullPointerException e) {
            displayMessage("Invalid file");
        }

        Label companyName = new Label(currentCompany.getName());
        Label companyAddress = new Label(currentCompany.getAddress().toString());
        totalRevenue.setText(String.format("%.2f", currentCompany.calculateTotalRevenue()));
        totalTax.setText(String.format("%.2f", currentCompany.calculateTotalTax()));
        totalWithoutTax.setText(String.format("%.2f", currentCompany.calculateTotalRevenueWithoutTax()));


        StackPane.setAlignment(companyName, Pos.TOP_LEFT);
        StackPane.setAlignment(companyAddress, Pos.BOTTOM_LEFT);
        companyInfoPane.getChildren().addAll(companyName, companyAddress);

        companyName.setText(currentCompany.getName());
        companyAddress.setText(currentCompany.getAddress().toString());
    }


}
