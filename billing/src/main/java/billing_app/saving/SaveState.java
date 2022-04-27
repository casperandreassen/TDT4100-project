package billing_app.saving;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

import billing_app.logic.Company;

/* Interface for saving the company. Depends on filechoosers to know where to save and where to load from. */

public interface SaveState {

    public void saveCompanyState(Company company, File file) throws IOException;
    public Company loadCompanyFromFile(File file) throws FileNotFoundException, IOException, URISyntaxException, ParseException;
    
}
