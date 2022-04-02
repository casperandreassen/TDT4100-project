package billing_app.saving;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import billing_app.Logic.Company;
import javafx.scene.transform.Scale;

/*This class aims to provide methods for saving the state of all relevant Java classes, and provide methods for creating the objects again on startup. */

public class SaveState {
    
    Path currentPath;
    Date today = new Date();
    Collection<Company> allCompanies = new ArrayList<Company>();

    public SaveState(Company company) {
        this.currentPath = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
        this.allCompanies.add(company);
    }

    public SaveState() {
        this.currentPath = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
    }

    public void saveCurrentState() {
        /* Copy last instace to old directory */

        File lastSave = new File(this.currentPath.toString() + "/store/instance/");
        File locationOldSaves = new File(this.currentPath.toString() + "/store/old/");
        File currentState = new File(this.currentPath.toString() + "/store/instance/" + today + ".txt");
        try {
            currentState.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter writeCurrentState = new FileWriter(currentPath.toString() + "/store/instance/save.txt");
            for (Company company : allCompanies) {
                writeCurrentState.write("companyName," + company.getCompanyName() + "\n");
                writeCurrentState.write("companyLogoPath," + company.getCompanyLogoPath() + "\n");
                writeCurrentState.write("currentBillId," + company.getCurrentBillId() + "\n");
                writeCurrentState.write("organizationalId," + company.companyOrganizationalId.getOrganizationalId() + "\n");
                writeCurrentState.write("address," + company.companyAddress.getAddress() + "\n");
                writeCurrentState.write("postalCode," + company.companyAddress.getPostalCode() + "\n");
                writeCurrentState.write("city," + company.companyAddress.getCity() + "\n");
                writeCurrentState.write("country," + company.companyAddress.getCountry() + "\n");
            }
            /*Save item data*/

            writeCurrentState.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getPastState() {
        try {
            Path path = FileSystems.getDefault().getPath(currentPath.toString() + "/billing/store/instance/save.txt");
            List<String> companyData = Files.readAllLines(path);
            String companyName, companyLogoPath, organizationalId, address, postalCode, city, country;
            int currentBillId;

            for (String data : companyData) {
                String data1[] = data.split(",");
                System.out.println(data1[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SaveState ny = new SaveState();
        ny.getPastState();
    }
}
