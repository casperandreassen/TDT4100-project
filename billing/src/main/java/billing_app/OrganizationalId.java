package billing_app;

public class OrganizationalId {

    private String organizationalId;

    public OrganizationalId(String organizationalId) {
        if (isValid(organizationalId)) {
            this.organizationalId = organizationalId;
        }
    }
/*Validate "organisasjonsnummer" using standard defines by Brønnøysundregisterene https://www.brreg.no/om-oss/oppgavene-vare/alle-registrene-vare/om-enhetsregisteret/organisasjonsnummeret/ */
    private boolean isValid(String organizationalId) {
        if (organizationalId.length() != 9) {
            return false; 
        }
        int[] weights = {3, 2, 7, 6, 5, 4, 3, 2};
        int[] numbers = new int[8];
        for (int i = 0; i < 8; i++) {
            numbers[i] = organizationalId.charAt(i) * weights[i];
        }
        System.out.println(numbers);
        return true;
    }

    public static void main(String[] args) {
        OrganizationalId ny = new OrganizationalId("123456785");
    }
    
}
