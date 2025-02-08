package tables;

/**
 * Represents a base class for different types of users (Client, Admin).
 */
public class User {
    /** Unique identifier of the user. */
    private int id;
    /** Email address of the user. */
    private String email;
    /** Password of the user. */
    private String password;
    
    private String firstName;
    private String lastName;
    private String role;
    private int active;
    private Address address;

    /**
     * Constructor with ID, email, and password.
     *
     * @param id       The user's unique identifier.
     * @param email    The user's email address.
     * @param password The user's password.
     */
    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor with email and password.
     *
     * @param email    The user's email address.
     * @param password The user's password.
     */
    public User(String email, String password) {
        this.id = -1;
        this.email = email;
        this.password = password;
    }

    /**
     * Default constructor for creating a guest user.
     * Email is set to "Guest" and password is null.
     */
    public User() {
        this.id = -1;
        this.email = "Guest";
        this.password = null;
    }

    /**
     * Constructor with all user details.
     *
     * @param id       The user's unique identifier.
     * @param email    The user's email address.
     * @param password The user's password.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param role     The user's role (e.g., Admin, Client).
     * @param active   The user's status (active/inactive).
     */
    public User(int id, String email, String password, String firstName, String lastName, String role, int active) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.active = active;
    }

    /**
     * Constructor with user details (excluding ID).
     *
     * @param email    The user's email address.
     * @param password The user's password.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param role     The user's role.
     */
    public User(String email, String password, String firstName, String lastName, String role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    /**
     * Returns the user's unique identifier.
     *
     * @return The user's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the user's unique identifier.
     *
     * @param id The new ID for the user.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the user's email address.
     *
     * @return The user's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email The new email address for the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the user's password.
     *
     * @return The user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password The new password for the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the user's first name.
     *
     * @return The user's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstName The new first name for the user.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the user's last name.
     *
     * @return The user's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName The new last name for the user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the user's role (e.g., Admin, Client).
     *
     * @return The user's role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the user's role.
     *
     * @param role The new role for the user.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns the user's address.
     *
     * @return The user's address.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Sets the user's address.
     *
     * @param address The new address for the user.
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Returns the user's active status.
     *
     * @return The active status of the user (1 for active, 0 for inactive).
     */
    public int getActive() {
        return active;
    }

    /**
     * Sets the user's active status.
     *
     * @param active The new active status for the user.
     */
    public void setActive(int active) {
        this.active = active;
    }
}
