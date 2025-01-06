package tables;

/**
 * Class representing an administrator in the system.
 * Extends the `User` class without additional fields or methods.
 */
public class Admin extends User {

    /**
     * Constructor with ID, email, and password.
     *
     * @param id       The administrator's unique identifier.
     * @param email    The administrator's email address.
     * @param password The administrator's password.
     */
    public Admin(int id, String email, String password) {
        super(id, email, password);
    }

    /**
     * Default constructor for a guest administrator.
     */
    public Admin() {
        super();
    }
}
