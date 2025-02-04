package tables;

/**
 * It is used as a base for different types of users (Client, Admin)
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
    // Utilisé
    public User() {
    	this.id = -1;
        this.email = "Guest";
        this.password = null;
    }

    // Utilisé
    public User(int id, String email, String password, String firstName, String lastName, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
	}
    
    // Utilisé
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
    
    
}
