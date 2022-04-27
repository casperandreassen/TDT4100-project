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
    private VBox main_vbox;

    @FXML
    private Label totalWithoutTax, totalTax, totalRevenue, pageLabel;

    @FXML
    private Button knapp;

    @FXML
    private Pane root_pane;

    @FXML
    private StackPane companyInfoPane;

    @FXML
    private Pane company_avatar_pane;

    /* For keeping track of what bills are on display and on what page. */
    private List<ArrayList<Bill>> pages = new ArrayList<ArrayList<Bill>>();
    int activePage;


    @FXML
    private void goToCreateBill() {
        goToView("Create new bill", "CreateBill.fxml", (Stage) root_pane.getScene().getWindow());
    }

    /* Tells displayPage to render the next page. */
    @FXML
    private void nextPage() {
        if (activePage < pages.size()) {
            displayPage(pages.get(activePage + 1));
            activePage++;
            pageLabel.setText(String.format("%1$s/%2$s", activePage + 1, pages.size()));
        }
    }

    /* Tells displayPage to render the previous page. */
    @FXML
    private void previousPage() {
        if (activePage > 0) {
            displayPage(pages.get(activePage - 1));
            activePage--;
            pageLabel.setText(String.format("%1$s/%2$s", activePage + 1, pages.size()));
        }
    }

    /* Calculates how many pages are needed and what bills should be on each page. */
    @FXML
    private void displayBillsInVbox(List<Bill> bills) {
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


    /* Renderes a collection of bills in the bills section. */
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
                displayMessage(e.toString());
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
            /* If the bill is not completed we add a edit button to allow the user to continue work. */
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

    /* Lets the user switch between sent and uncompleted bills. */
    @FXML
    public void showUncompletedBills() {
        displayBillsInVbox(currentCompany.getCompanyUnfinishedBills());
    }

    @FXML
    public void showCompletedBills() {
        displayBillsInVbox(currentCompany.getCompanySentBills());
    }

    /* Uses the saveCompany class to save the current company state and uses selectSaceLocationHandler to get the path to save to. */
    @FXML
    public void saveCompanyState() {
        SaveCompany save = new SaveCompany();
        try {
            save.saveCompanyState(currentCompany, selectSaveLocationHandler());   
        } catch (IOException e) {
            displayMessage("Could not save to file");
        }
    }

    /* Opens a filechooser so that the user can select where the savefile should be located. */
    private File selectSaveLocationHandler() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select where to save");
        File selectedLocation = fileChooser.showSaveDialog(stage);
        return new File(Paths.get(selectedLocation.getAbsolutePath()).toString() + ".txt");
    }

    /* Initiazies the overview screen with bills, image, company info and totals. */
    public void init() {
        displayBillsInVbox(currentCompany.companySentBills);
        try {
            ImageView company_avatar = new ImageView(new Image(currentCompany.getCompanyLogoFileStream()));
            company_avatar.setFitHeight(100); 
            company_avatar.setFitWidth(100);
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
