package billing_app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
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
        if (address != null && postalCode != null && city != null && country != null) {
            this.address = address;
            this.postalCode = postalCode;
            this.city = city; 
            this.country = country;
        }
    }

    public void createHashMap() {
        try {
            FileReader fr = new FileReader(new File(FileSystems.getDefault().getPath(System.getProperty("user.dir")) + "/store/static/", "postnummer.txt"));
            BufferedReader reader = new BufferedReader(fr);
            System.out.println(reader.read());
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
        customerAddress.createHashMap();
    }
}
