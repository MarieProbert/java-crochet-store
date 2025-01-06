package tables;

/**
 * Abstract class representing a generic user.
 * It is used as a base for different types of users (Client, Admin)
 */
public abstract class User {
    /** Unique identifier of the user. */
    private int id;

    /** Email address of the user. */
    private String email;

    /** Password of the user. */
    private String password;

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
        this.email = email;
        this.password = password;
    }

    /**
     * Default constructor for creating a guest user.
     * Email is set to "Guest" and password is null.
     */
    public User() {
        this.email = "Guest";
        this.password = null;
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
}
