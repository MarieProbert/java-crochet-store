package tables;

/**
 * Represents a base class for different types of users (Client, Admin).
 */
public class User {

    private int id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private int active;
    private Address address;

    /**
     * Default constructor for creating a guest user.
     * The email is set to "Guest" and password is null.
     */
    public User() {
        this.id = -1;
        this.email = "Guest";
        this.password = null;
    }

    /**
     * Constructor with all user details.
     *
     * @param id        the unique identifier of the user
     * @param email     the email address of the user
     * @param password  the password of the user
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param role      the role of the user (e.g., Admin, Client)
     * @param active    the active status of the user (1 for active, 0 for inactive)
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
     * @param email     the email address of the user
     * @param password  the password of the user
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param role      the role of the user
     */
    public User(String email, String password, String firstName, String lastName, String role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
 
    public String getFirstName() {
        return firstName;
    }
 
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
 
    public String getLastName() {
        return lastName;
    }
 
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
 
    public String getRole() {
        return role;
    }
 
    public void setRole(String role) {
        this.role = role;
    }
 
    public int getActive() {
        return active;
    }
 
    public void setActive(int active) {
        this.active = active;
    }
 
    public Address getAddress() {
        return address;
    }
 
    public void setAddress(Address address) {
        this.address = address;
    }
}
