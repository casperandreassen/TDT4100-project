package billing_app.saving;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import billing_app.items.Address;
import billing_app.items.Bill;
import billing_app.items.Item;
import billing_app.items.OrganizationalId;
import billing_app.logic.Company;
import billing_app.logic.Customer;
import javafx.fxml.LoadException;

/* Aim for this class is to save and load the company from .txt files. */

public class SaveCompany implements SaveState{

    /* Saves the company state as is. */

    /* Save structure:
    Company name
    UUID of the company
    Organizational ID
    address
    postal code
    city
    country
    company logo path
    current bill id
    number of items in the company (Then we know how many times to read item data)
    UUID of the item
    name 
    price
    tax
    number of customers (Then we know how many times to read customer data)
    UUID of the customer 
    name 
    Organizational ID
    address
    postal code
    city
    country
    number of sent bills (We then know how many sent bills to read.)
    UUID of the bill 
    Bill number 
    date of sale 
    date of delivery
    due date of the bill 
    Total number of items on the bill (we then know how many items to read.)
    UUID of an item (Since the item is already created at this point in the loading process we only need to store the UUID of the item to find it later.)
    Number of the item above 
    UUID of the customer attached to the bill (Same logic as with the items.)
    true/false if the bill is sent or not.
    the 11 steps above repeated for all the unsent bills.
     */


    @Override
    public void saveCompanyState(Company company, File file) throws IOException, IllegalStateException {
        /* The filewriter will auto-close if an Exception is thrown */
        try (FileWriter writeCurrentState = new FileWriter(file);) {
            /* Writes all the data given the file structure above. */
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
            /* Since writing a bill is identical for both types it is written in the method writeBill(). */
            for (Bill bill : company.getCompanySentBills()) {
                writeBill(bill, writeCurrentState);
            }
            writeCurrentState.write(System.lineSeparator());
            writeCurrentState.write(Integer.toString(company.getCompanyUnfinishedBills().size()));
            for (Bill bill : company.getCompanyUnfinishedBills()) {
                writeBill(bill, writeCurrentState);
            }
        } catch (IOException e) {
            throw new IOException(e.toString());
        } catch (NullPointerException e) {
            throw new IllegalStateException("Company or some of its items is in an illegal state and can therefore not be saved.");
        }
    }


    /*  */
    @Override
    public Company loadCompanyFromFile(File file) throws LoadException, FileNotFoundException {
        /* If the file extention is not .txt the file format is illegal. */
        if (!getFileType(file.toString()).equals("txt")) {
            throw new IllegalArgumentException("Invalid file format");
        }

        try {
           /* Reads all lines of the file into a arraylist so that it can be processed later. */
            List<String> lines = new ArrayList<String>();
            try (BufferedReader lineReader = new BufferedReader(new FileReader(file))) {
                String text = null;
                lines = new ArrayList<String>();
 
                while ((text = lineReader.readLine()) != null) {
                lines.add(text);
                }
            }
            /* A lastStop attribute to keep track of where we last read from. Since the different sections can be of different leghts because of varying amount of items etc this is needed.  */
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
            /* Reads all the items */
            for (int i = 0, l = 10; i < Integer.valueOf(lines.get(9)); i++, l += 4) {
                Item item = new Item(UUID.fromString(lines.get(l)), lines.get(l + 1), Double.valueOf(lines.get(l + 2)), Double.valueOf(l + 3));
                newCompany.addItemToCompany(item);
                lastStop = l + 4;
            }
            /* Reads all the customers */
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
            /* Reads all the sent bills. */
            int numberOfSentBills = Integer.valueOf(lines.get(lastStop + 1));
            lastStop++;
            for (int i = 0, l = lastStop + 1; i < numberOfSentBills; i++) {
                Bill newBill = new Bill(newCompany, UUID.fromString(lines.get(l)));
                newBill.setBillId(Integer.valueOf(lines.get(l + 1)));
                newBill.addDateOfSale(lines.get(l + 2));
                newBill.addDateOfDelivery(lines.get(l + 3));
                newBill.addDueDate(lines.get(l + 4));
                for (int v = 0, n = l + 6; v < Integer.valueOf(lines.get(l + 5)); v++, n += 2) {
                    for (Item item : newCompany.getCompanyItems()) {
                        if (item.getItemId().toString().equals(lines.get(n))) {
                            newBill.setItems(item, Integer.valueOf(lines.get(n + 1)));
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
                l = lastStop + 1;
            }
            int numberOfUnfinishedBills = Integer.valueOf(lines.get(lastStop + 1));
            lastStop++;

            /* Reads all the unsent bills. */
            for (int i = 0, l = lastStop + 1; i < numberOfUnfinishedBills; i++, l += 10) {
                Bill newBill = new Bill(newCompany, UUID.fromString(lines.get(l)));
                newBill.setBillId(Integer.valueOf(lines.get(l + 1)));
                newBill.addDateOfSale(lines.get(l + 2));
                newBill.addDateOfDelivery(lines.get(l + 3));
                newBill.addDueDate(lines.get(l + 4));
                for (int v = 0, n = l + 6; v < Integer.valueOf(lines.get(l + 5)); v++, n += 2) {
                    for (Item item : newCompany.getCompanyItems()) {
                        if (item.getItemId().toString().equals(lines.get(n))) {
                            newBill.setItems(item, Integer.valueOf(lines.get(n + 1)));
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
        } catch (IOException | IllegalArgumentException | URISyntaxException | ParseException | NullPointerException e) {
            throw new LoadException();
        }
        
    }

    /* Extracts the file extention of the file. Throws NoSuchElementException if no extention is found. Returns the string representation of the extention after the last "." */
    public static String getFileType(String path) throws NoSuchElementException {
        Optional<String> extention = Optional.ofNullable(path)
            .filter(s -> s.contains("."))
            .map(s -> s.substring((path.lastIndexOf(".") + 1)));
        return extention.get();
    }

    private void writeBill(Bill bill, FileWriter writer) throws IOException {
        writer.write(System.lineSeparator());
        writer.write(bill.getBillUUID().toString());
        writer.write(System.lineSeparator());
        writer.write(Integer.toString(bill.getBillId()));
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
    }
    
}
