package billing_app.items;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import billing_app.MainApp;

public class Address {

    String address; 
    String postalCode; 
    String city; 
    String country;
    public Map<String, String> postalCodes = new HashMap<String, String>();

    public Address() {
        createHashMap();
    }

    /* Takes around 4-5 ms to create the hashmap */
    private void createHashMap() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FileSystems.getDefault().getPath(System.getProperty("user.dir")) + "/billing/store/static/postnummer.txt"));

            postalCodes = reader.lines()
                .map(s -> s.split("\\t", 2))
                .collect(Collectors.toMap(s -> s[0], s -> s[1]));
            
            reader.close();
        } catch (FileNotFoundException e) {
            
        } catch (IOException e) {
            MainApp.printToConsole("IO Error");
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        if (postalCodes.get(postalCode) != null) {
            this.postalCode = postalCode;
            this.city = postalCodes.get(postalCode);
            this.country = "Norway";
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static void main(String[] args) {
        Address customerAddress = new Address();
        long start = System.currentTimeMillis();
        customerAddress.createHashMap();
        System.out.println(customerAddress.postalCodes.get("2013"));
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        System.out.println("Total time: " + Long.toString(elapsedTime));
    }
}