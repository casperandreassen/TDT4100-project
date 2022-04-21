package billing_app.saving;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import billing_app.MainApp;
import billing_app.items.Address;
import billing_app.items.Bill;
import billing_app.items.Item;
import billing_app.items.OrganizationalId;
import billing_app.logic.Company;
import billing_app.logic.Customer;

public class SaveCompany implements SaveState{

    @Override
    public void saveCompanyState(Company company) {
        try {
            FileWriter writeCurrentState = new FileWriter(new File(System.getProperty("user.home") + "/Documents/save.txt"));
            writeCurrentState.write(company.getName());
            writeCurrentState.write(System.lineSeparator());
            writeCurrentState.write(company.getBusinessId().toString());
            writeCurrentState.write(System.lineSeparator());
            writeCurrentState.write(company.getOrganizationalId().toString());
            writeCurrentState.write(System.lineSeparator());
            writeCurrentState.write(company.getAddress().getAddress());
            writeCurrentState.write(System.lineSeparator());
            writeCurrentState.write(company.getAddress().getPostalCode());
            writeCurrentState.write(System.lineSeparator());
            writeCurrentState.write(company.getAddress().getCity());
            writeCurrentState.write(System.lineSeparator());
            writeCurrentState.write(company.getAddress().getCountry());
            writeCurrentState.write(System.lineSeparator());
            writeCurrentState.write(company.getCompanyLogoPath());
            writeCurrentState.write(System.lineSeparator());
            writeCurrentState.write(Integer.toString(company.getCurrentBillId()));
            writeCurrentState.write(System.lineSeparator());
            writeCurrentState.write(Integer.toString(company.getCompanyItems().size()));
            for (Item item : company.getCompanyItems()) {
                writeCurrentState.write(System.lineSeparator());
                writeCurrentState.write(item.getItemId().toString());
                writeCurrentState.write(System.lineSeparator());
                writeCurrentState.write(item.getName());
                writeCurrentState.write(System.lineSeparator());
                writeCurrentState.write(Double.toString(item.getPrice()));
                writeCurrentState.write(System.lineSeparator());
                writeCurrentState.write(Double.toString(item.getTaxOnItem()));
            }
            writeCurrentState.write(System.lineSeparator());
            writeCurrentState.write(Integer.toString(company.getCompanyCustomers().size()));
            for (Customer customer : company.getCompanyCustomers()) {
                writeCurrentState.write(System.lineSeparator());
                writeCurrentState.write(customer.getBusinessId().toString());
                writeCurrentState.write(System.lineSeparator());
                writeCurrentState.write(customer.getName());
                writeCurrentState.write(System.lineSeparator());
                writeCurrentState.write(customer.getOrganizationalId().toString());
                writeCurrentState.write(System.lineSeparator());
                writeCurrentState.write(customer.getAddress().getAddress());
                writeCurrentState.write(System.lineSeparator());
                writeCurrentState.write(customer.getAddress().getPostalCode());
                writeCurrentState.write(System.lineSeparator());
                writeCurrentState.write(customer.getAddress().getCity());
                writeCurrentState.write(System.lineSeparator());
                writeCurrentState.write(customer.getAddress().getCountry());
            }
            writeCurrentState.write(System.lineSeparator());
            writeCurrentState.write(Integer.toString(company.getCompanySentBills().size()));
            for (Bill bill : company.getCompanySentBills()) {
                writeBill(bill, writeCurrentState);
            }
            writeCurrentState.write(System.lineSeparator());
            writeCurrentState.write(Integer.toString(company.getCompanyUnfinishedBills().size()));
            for (Bill bill : company.getCompanyUnfinishedBills()) {
                writeBill(bill, writeCurrentState);
            }
            writeCurrentState.close();
        } catch (IOException e) {
        }
        
        
    }

    @Override
    public Company loadCompanyFromFile(String filePath) {
        List<String> lines = new ArrayList<String>();
        try {
            BufferedReader lineReader = new BufferedReader(new FileReader(filePath));
            String text = null;
            lines = new ArrayList<String>();
 
            while ((text = lineReader.readLine()) != null) {
                lines.add(text);
            }
            lineReader.close();

        } catch (IOException e) {
        }
        int lastStop = 0;
        Company newCompany = new Company(UUID.fromString(lines.get(1)));
        newCompany.setName(lines.get(0));
        newCompany.setOriganizationalId(new OrganizationalId(lines.get(2)));
        Address companyAddress = new Address();
        companyAddress.setAddress(lines.get(3));
        companyAddress.setPostalCode(lines.get(4));
        companyAddress.setCity(lines.get(5));
        companyAddress.setCountry(lines.get(6));
        newCompany.setAddress(companyAddress);
        newCompany.setCompanyLogoPath(lines.get(7));
        newCompany.setCurrentBillId(Integer.valueOf(lines.get(8)));
        for (int i = 0, l = 10; i < Integer.valueOf(lines.get(9)); i++, l += 4) {
            Item item = new Item(UUID.fromString(lines.get(l)), lines.get(l + 1), Double.valueOf(lines.get(l + 2)), Double.valueOf(l + 3));
            newCompany.addItemToCompany(item);
            lastStop = l + 4;
        }
        int numberOfCustomers = Integer.valueOf(lines.get(lastStop));
        for (int i = 0, l = lastStop + 1; i < numberOfCustomers; i++, l += 7) {
            Customer newCustomer = new Customer(UUID.fromString(lines.get(l)));
            newCustomer.setName(lines.get(l + 1));
            newCustomer.setOriganizationalId(new OrganizationalId(lines.get(l + 2)));
            Address customerAddress = new Address();
            customerAddress.setAddress(lines.get(l + 3));
            customerAddress.setPostalCode(lines.get(l + 4));
            customerAddress.setCity(lines.get(l + 5));
            customerAddress.setCountry(lines.get(l + 6));
            newCustomer.setAddress(customerAddress);
            newCompany.addCustomerToCompany(newCustomer);
            lastStop = l + 6;
        }
        int numberOfSentBills = Integer.valueOf(lines.get(lastStop + 1));
        lastStop++;
        for (int i = 0, l = lastStop + 1; i < numberOfSentBills; i++, l += 9) {
            Bill newBill = new Bill(newCompany, UUID.fromString(lines.get(l)));
            String[] dateOfSaleString = lines.get(l + 1).split("[-.,:\n]");
            String[] dateOfDeliveryString = lines.get(l + 2).split("[-.,:\n]");
            String[] dueDateString = lines.get(l + 3).split("[-.,:\n]");
            try {
                newBill.addDateOfSale(new GregorianCalendar(Integer.valueOf(dateOfSaleString[0]), Integer.valueOf(dateOfSaleString[1]), Integer.valueOf(dateOfSaleString[2])));
                newBill.addDateOfDelivery(new GregorianCalendar(Integer.valueOf(dateOfDeliveryString[0]), Integer.valueOf(dateOfDeliveryString[1]), Integer.valueOf(dateOfDeliveryString[2])));
                newBill.addDueDate(new GregorianCalendar(Integer.valueOf(dueDateString[0]), Integer.valueOf(dueDateString[1]), Integer.valueOf(dueDateString[2])));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for (int v = 0, n = l + 5; v < Integer.valueOf(lines.get(l + 4)); v++, n += 2) {
                for (Item item : newCompany.getCompanyItems()) {
                    if (item.getItemId().toString().equals(lines.get(n))) {
                        newBill.setItems(item, Integer.valueOf(lines.get(n + 1)));
                        continue;
                    }
                }
                lastStop = n + 1;
            }
            for (Customer customer : newCompany.getCompanyCustomers()) {
                if (customer.getBusinessId().toString().equals(lines.get(lastStop + 1))) {
                    newBill.addCustomerToBill(customer);
                    continue;
                }
            }
            newBill.sent = lines.get(lastStop + 2).equals("true") ? true : false; 
            newCompany.addSentBill(newBill);
            lastStop += 2;
        }
        int numberOfUnfinishedBills = Integer.valueOf(lines.get(lastStop + 1));
        lastStop++;
        for (int i = 0, l = lastStop + 1; i < numberOfUnfinishedBills; i++, l += 9) {
            Bill newBill = new Bill(newCompany, UUID.fromString(lines.get(l)));
            String[] dateOfSaleString = lines.get(l + 1).split("[-.,:\n]");
            String[] dateOfDeliveryString = lines.get(l + 2).split("[-.,:\n]");
            String[] dueDateString = lines.get(l + 3).split("[-.,:\n]");
            try {
                newBill.addDateOfSale(new GregorianCalendar(Integer.valueOf(dateOfSaleString[0]), Integer.valueOf(dateOfSaleString[1]), Integer.valueOf(dateOfSaleString[2])));
                newBill.addDateOfDelivery(new GregorianCalendar(Integer.valueOf(dateOfDeliveryString[0]), Integer.valueOf(dateOfDeliveryString[1]), Integer.valueOf(dateOfDeliveryString[2])));
                newBill.addDueDate(new GregorianCalendar(Integer.valueOf(dueDateString[0]), Integer.valueOf(dueDateString[1]), Integer.valueOf(dueDateString[2])));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for (int v = 0, n = l + 5; v < Integer.valueOf(lines.get(l + 4)); v++, n += 2) {
                for (Item item : newCompany.getCompanyItems()) {
                    if (item.getItemId().toString().equals(lines.get(n))) {
                        newBill.setItems(item, Integer.valueOf(lines.get(n + 1)));
                        break;
                    }
                }
                lastStop = n + 1;
            }
            for (Customer customer : newCompany.getCompanyCustomers()) {
                if (customer.getBusinessId().toString().equals(lines.get(lastStop + 1))) {
                    newBill.addCustomerToBill(customer);
                    break;
                }
            }
            newBill.sent = lines.get(lastStop + 2) == "true" ? true : false;
            newCompany.addUnfinishedBill(newBill);
        }

        return newCompany;
    }

    private void writeBill(Bill bill, FileWriter writer) {
        try {
            writer.write(System.lineSeparator());
            writer.write(bill.getBillUUID().toString());
            writer.write(System.lineSeparator());
            writer.write(bill.getDateOfDelivery().
            toZonedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            writer.write(System.lineSeparator());
            writer.write(bill.getDueDate().
            toZonedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            writer.write(System.lineSeparator());
            writer.write(bill.getDateOfSale().
            toZonedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            writer.write(System.lineSeparator());
            writer.write(Integer.toString(bill.getItems().keySet().size()));
            for (Item item : bill.getItems().keySet()) {
                writer.write(System.lineSeparator());
                writer.write(item.getItemId().toString());
                writer.write(System.lineSeparator());
                writer.write(Integer.toString(bill.getItems().get(item)));
            }
            writer.write(System.lineSeparator());
            writer.write(bill.getBillCustomer().getBusinessId().toString());
            writer.write(System.lineSeparator());
            writer.write(bill.sent ? "true" : "false");


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
