package billing_app.items;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import billing_app.MainApp;

/*  */

public class Address {

    String address; 
    String postalCode; 
    String city; 
    String country;
    public Map<String, String> postalCodes = new HashMap<String, String>();

    /* The constructor just creates the hashmap so that it can be checked for a mach. */

    public Address() throws FileNotFoundException, IOException, URISyntaxException {
        createHashMap();
    }

    /* This creates a hashmap so that it can be searched for the actual post code. This provides the functionalty for auto-completion of city and country with Norwgian post codes. It takes around 4-5 ms to create the hashmap so no significant performance decrease. */
    private void createHashMap() throws FileNotFoundException, IOException, URISyntaxException {
        URL pathToFile = MainApp.class.getResource("postnummer.txt");
        BufferedReader reader = new BufferedReader(new FileReader(Paths.get(pathToFile.toURI()).toFile()));
        postalCodes = reader.lines()
            .map(s -> s.split("\\t", 2))
            .collect(Collectors.toMap(s -> s[0], s -> s[1]));
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

    @Override
    public String toString() {
        return String.format("%1$s, %2$s, %3$s, %4$s.", this.address, this.postalCode, this.city, this.country);
    }
}
