package billing_app.items;

import java.util.stream.IntStream;

public class OrganizationalId {

    private String organizationalId;

    /* Creates the object and checks if the organizational id is valid. */
    public OrganizationalId(String organizationalId) {
        if (isValid(organizationalId)) {
            this.organizationalId = organizationalId;
        } else {
            throw new IllegalArgumentException("Invalid organizationalId");
        }
    }

    public String getOrganizationalId() {
        return this.organizationalId;
    }


    /*Validate "organisasjonsnummer" using standard defined by Brønnøysundregisterene https://www.brreg.no/om-oss/oppgavene-vare/alle-registrene-vare/om-enhetsregisteret/organisasjonsnummeret/ */
    private boolean isValid(String organizationalId) {
        if (organizationalId.length() != 9) {
            return false; 
        }
        int[] weights = {3, 2, 7, 6, 5, 4, 3, 2};
        for (int i = 0; i < 8; i++) {
            weights[i] = Integer.parseInt(Character.toString(organizationalId.charAt(i))) * weights[i];
        }
        int sum = IntStream.of(weights).sum();
        
        /*Mod 11*/
        int mod = sum % 11; 
        int controlDigit;
        if (mod == 0) {
            controlDigit = 0; 
        } else if (mod == 1) {
            return false; 
        } else {
            controlDigit = 11 - mod;
        }
        if (Integer.toString(controlDigit).equals(Character.toString(organizationalId.charAt(8)))) {
            return true;
        } else {
            return false;
        }
    } 

    @Override
    public String toString() {
        return organizationalId;
    }
}
