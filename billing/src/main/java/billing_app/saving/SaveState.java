package billing_app.saving;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import billing_app.logic.Company;

public interface SaveState {

    public void saveCompanyState(Company company, String filePath) throws IOException;
    public Company loadCompanyFromFile(String filePath) throws FileNotFoundException, IOException, URISyntaxException;
    
}
