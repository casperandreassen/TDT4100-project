package billing_app;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class Address {

    String address; 
    String postalCode; 
    String city; 
    String country;
    HashMap<String, String> postalCodes = new HashMap<String, String>();
    
    /* Plan is to add full address validation in this class, see https://www.bring.no/tjenester/adressetjenester/postnummer for implementation. */

    public Address(String address, String postalCode, String city, String country) {
        createHashMap();
        if (address != null && postalCode != null && city != null && country != null) {
            this.address = address;
            this.postalCode = postalCode;
            this.city = city; 
            this.country = country;
        }
    }

    public void createHashMap() {
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

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public static void main(String[] args) {
        Address customerAddress = new Address("Rekkeviksgate 68C", "3260", "Larvik", "Norway");
    }
}
