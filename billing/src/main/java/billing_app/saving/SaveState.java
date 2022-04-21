package billing_app.saving;

import billing_app.logic.Company;

public interface SaveState {

    public void saveCompanyState(Company company);
    public Company loadCompanyFromFile(String filePath);
    
}
