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
     * Default constructor initializing all fields to null.
     */
    public Address() {
        street = null;
        city = null;
        postCode = null;
        country = null;
    }

    /**
     * Constructor to create an address with the provided values.
     * 
     * @param street The street address.
     * @param city The city of the address.
     * @param postCode The postal code of the address.
     * @param country The country of the address.
     * @param user The user associated with the address.
     */
    public Address(String street, String city, String postCode, String country, User user) {
        this.street = street;
        this.city = city;
        this.postCode = postCode;
        this.country = country;
        this.user = user;
    }

    /**
     * Constructor to create an address with the provided ID and values.
     * 
     * @param id The ID of the address.
     * @param street The street address.
     * @param city The city of the address.
     * @param postCode The postal code of the address.
     * @param country The country of the address.
     * @param user The user associated with the address.
     */
    public Address(int id, String street, String city, String postCode, String country, User user) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.postCode = postCode;
        this.country = country;
        this.user = user;
    }

    /**
     * Returns the client's street address.
     *
     * @return The street address.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the client's street address.
     *
     * @param street The new street address.
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Returns the client's city.
     *
     * @return The city.
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the client's city.
     *
     * @param city The new city.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns the client's postal code.
     *
     * @return The postal code.
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * Sets the client's postal code.
     *
     * @param postCode The new postal code.
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * Returns the client's country.
     *
     * @return The country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the client's country.
     *
     * @param country The new country.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Returns the ID of the address.
     *
     * @return The ID of the address.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the address.
     *
     * @param id The new ID for the address.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the user associated with the address.
     *
     * @return The associated user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with the address.
     *
     * @param user The new user.
     */
    public void setUser(User user) {
        this.user = user;
    }
}
