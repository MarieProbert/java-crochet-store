package tables;

/**
 * Represents an address associated with a user.
 */
public class Address {

    private int id;
    private String street;
    private String city;
    private String postCode;
    private String country;
    private User user;

    /**
     * Default constructor initializing fields to null.
     */
    public Address() {
        this.street = null;
        this.city = null;
        this.postCode = null;
        this.country = null;
    }

    /**
     * Constructor to initialize an address.
     *
     * @param street   the street address
     * @param city     the city
     * @param postCode the postal code
     * @param country  the country
     * @param user     the associated user
     */
    public Address(String street, String city, String postCode, String country, User user) {
        this.street = street;
        this.city = city;
        this.postCode = postCode;
        this.country = country;
        this.user = user;
    }

    /**
     * Constructor to initialize an address with an ID.
     *
     * @param id       the address ID
     * @param street   the street address
     * @param city     the city
     * @param postCode the postal code
     * @param country  the country
     * @param user     the associated user
     */
    public Address(int id, String street, String city, String postCode, String country, User user) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.postCode = postCode;
        this.country = country;
        this.user = user;
    }

    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getStreet() {
        return street;
    }
 
    public void setStreet(String street) {
        this.street = street;
    }
 
    public String getCity() {
        return city;
    }
 
    public void setCity(String city) {
        this.city = city;
    }
 
    public String getPostCode() {
        return postCode;
    }
 
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
 
    public String getCountry() {
        return country;
    }
 
    public void setCountry(String country) {
        this.country = country;
    }
 
    public User getUser() {
        return user;
    }
 
    public void setUser(User user) {
        this.user = user;
    }
}
