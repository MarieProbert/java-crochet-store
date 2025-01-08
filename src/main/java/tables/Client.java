package tables;

/**
 * Class representing a client in the system.
 * Extends the 'User' class and adds additional personal and address information.
 */
public class Client extends User {

    /** First name of the client. */
    private String firstName;

    /** Last name of the client. */
    private String lastName;

    /** Street address of the client. */
    private String street;

    /** City of the client. */
    private String city;

    /** Postal code of the client. */
    private int postCode;

    /** Country of the client. */
    private String country;

    /**
     * Constructor with all fields.
     *
     * @param id        The client's unique identifier.
     * @param email     The client's email address.
     * @param password  The client's password.
     * @param firstName The client's first name.
     * @param lastName  The client's last name.
     * @param street    The client's street address.
     * @param city      The client's city.
     * @param postCode  The client's postal code.
     * @param country   The client's country.
     */
    public Client(int id, String email, String password, String firstName, String lastName, String street, String city, int postCode, String country) {
        super(id, email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
        this.postCode = postCode;
        this.country = country;
    }

    /**
     * Constructor without ID. It is only useful for inserting a client in the database
     *
     * @param email     The client's email address.
     * @param password  The client's password.
     * @param firstName The client's first name.
     * @param lastName  The client's last name.
     * @param street    The client's street address.
     * @param city      The client's city.
     * @param postCode  The client's postal code.
     * @param country   The client's country.
     */
    public Client(String email, String password, String firstName, String lastName, String street, String city, int postCode, String country) {
        super(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
        this.postCode = postCode;
        this.country = country;
    }

    /**
     * Constructor with email and password only.
     *
     * @param email    The client's email address.
     * @param password The client's password.
     */
    public Client(String email, String password) {
        super(email, password);
    }

    /**
     * Default constructor for a guest client.
     */
    public Client() {
        super();
    }

    /**
     * Returns the client's first name.
     *
     * @return The client's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the client's first name.
     *
     * @param firstName The new first name for the client.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the client's last name.
     *
     * @return The client's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the client's last name.
     *
     * @param lastName The new last name for the client.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the client's street address.
     *
     * @return The client's street address.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the client's street address.
     *
     * @param street The new street address for the client.
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Returns the client's city.
     *
     * @return The client's city.
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the client's city.
     *
     * @param city The new city for the client.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns the client's postal code.
     *
     * @return The client's postal code.
     */
    public int getPostCode() {
        return postCode;
    }

    /**
     * Sets the client's postal code.
     *
     * @param postCode The new postal code for the client.
     */
    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    /**
     * Returns the client's country.
     *
     * @return The client's country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the client's country.
     *
     * @param country The new country for the client.
     */
    public void setCountry(String country) {
        this.country = country;
    }
}

