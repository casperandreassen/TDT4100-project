package billing_app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.io.FileUtils;

/*This class aims to provide methods for saving the state of all relevant Java classes, and provide methods for creating the objects again on startup. */

public class SaveState {
    
    Path currentPath;
    Date today = new Date();
    Collection<Company> allCompanies = new ArrayList<Company>();

    public SaveState(Company company) {
        this.currentPath = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
        this.allCompanies.add(company);
    }

    public void saveCurrentState() {
        /* Copy last instace to old directory */

        File lastSave = new File(this.currentPath.toString() + "/store/instance/");
        File locationOldSaves = new File(this.currentPath.toString() + "/store/old/");
        FileUtils.moveDirectory()
        File currentState = new File(this.currentPath.toString() + "/store/instance/" + today + ".txt");
        try {
            currentState.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter writeCurrentState = new FileWriter(currentPath.toString() + "/" + today + ".txt");
            for (Company company : allCompanies) {
                writeCurrentState.write("START," + company.toString() + "\n");
                writeCurrentState.write("String,companyName," + company.getCompanyName() + "\n");
                writeCurrentState.write("String,companyLogoPath," + company.getCompanyLogoPath() + "\n");
                writeCurrentState.write("int,currentBillId," + company.getCurrentBillId() + "\n");
                writeCurrentState.write("START," + company.companyOrganizationalId.toString() + "\n");
                writeCurrentState.write("String,organizationalId," + company.companyOrganizationalId.getOrganizationalId() + "\n");
                writeCurrentState.write("END," + company.companyOrganizationalId.toString() + "\n");
                writeCurrentState.write("START," + company.companyAddress.toString() + "\n");
                writeCurrentState.write("String,address," + company.companyAddress.getAddress() + "\n");
                writeCurrentState.write("String,postalCode," + company.companyAddress.getPostalCode() + "\n");
                writeCurrentState.write("String,city," + company.companyAddress.getCity() + "\n");
                writeCurrentState.write("String,country," + company.companyAddress.getCountry() + "\n");
                writeCurrentState.write("END," + company.companyAddress.toString() + "\n");
            }
            /*Save item data*/

            writeCurrentState.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getPastState() {
        try {
            Path path = FileSystems.getDefault().getPath("/Users/casper/Documents/code/TDT4100-project/billing/store/static_files", "postnummer.txt");
            List<String> postnummer = Files.readAllLines(path);
            for (String postnummere : postnummer) {
                String[] tmp = postnummere.split(",");
                this.postalCodes.put(tmp[0], tmp[1]);
            }
        } catch (IOException e) {
            System.out.println("Failed");
        }
    }


    public static void main(String[] args) {
    }
}
