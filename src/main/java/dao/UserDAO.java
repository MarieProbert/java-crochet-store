package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tables.Address;
import tables.User;
import util.DatabaseConnection;
import util.HashUtils;

public class UserDAO {

    /**
     * Retrieves all active users from the database.
     * 
     * @return A list of active users.
     */
    public List<User> getAllUsers() {
        String queryUser = "SELECT * FROM user WHERE active = 1";
        String queryAddress = "SELECT * FROM address WHERE user_id = ?";
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtUser = conn.prepareStatement(queryUser);
             ResultSet rsUser = stmtUser.executeQuery()) {

            while (rsUser.next()) {
                int id = rsUser.getInt("id");
                String email = rsUser.getString("email");
                String password = rsUser.getString("password");
                String firstName = rsUser.getString("firstname");
                String lastName = rsUser.getString("lastname");
                String role = rsUser.getString("role");
                int active = rsUser.getInt("active");

                User user = new User(id, email, password, firstName, lastName, role, active);

                // If the user is a client, fetch the associated address
                if ("client".equalsIgnoreCase(role)) {
                    try (PreparedStatement stmtAddress = conn.prepareStatement(queryAddress)) {
                        stmtAddress.setInt(1, id);
                        ResultSet rsAddress = stmtAddress.executeQuery();

                        if (rsAddress.next()) {
                            String street = rsAddress.getString("street");
                            String city = rsAddress.getString("city");
                            String postCode = rsAddress.getString("postcode");
                            String country = rsAddress.getString("country");

                            Address address = new Address(id, street, city, postCode, country, user);
                            user.setAddress(address);
                        }
                    }
                }

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
    
    
    /**
     * Retrieves a user by their ID from the database.
     * 
     * @param id The ID of the user to retrieve.
     * @return The corresponding user, or null if not found.
     */
    public User getUserById(int id) {
        String queryUser = "SELECT * FROM user WHERE id = ? AND active = 1";
        String queryAddress = "SELECT * FROM address WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtUser = conn.prepareStatement(queryUser)) {
            
            stmtUser.setInt(1, id);
            ResultSet rsUser = stmtUser.executeQuery();
            
            if (rsUser.next()) {
                String email = rsUser.getString("email");
                String password = rsUser.getString("password");
                String firstName = rsUser.getString("firstname");
                String lastName = rsUser.getString("lastname");
                String role = rsUser.getString("role");
                int active = rsUser.getInt("active");
                
                User user = new User(id, email, password, firstName, lastName, role, active);
                
                // If the user is a client, fetch the associated address
                if ("client".equalsIgnoreCase(role)) {
                    try (PreparedStatement stmtAddress = conn.prepareStatement(queryAddress)) {
                        stmtAddress.setInt(1, id);
                        ResultSet rsAddress = stmtAddress.executeQuery();
                        
                        if (rsAddress.next()) {
                            String street = rsAddress.getString("street");
                            String city = rsAddress.getString("city");
                            String postCode = rsAddress.getString("postcode");
                            String country = rsAddress.getString("country");
                            
                            Address address = new Address(id, street, city, postCode, country, user);
                            user.setAddress(address);
                        }
                    }
                }
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserts a new user into the database.
     * 
     * @param user The user to be inserted.
     * @return The generated user ID after insertion.
     */
    public int insertUser(User user) {
        String sqlUser = "INSERT INTO user (email, password, firstname, lastname, role) VALUES (?, ?, ?, ?, ?)";
        String sqlAddress = "INSERT INTO address (user_id, street, city, postcode, country) VALUES (?, ?, ?, ?, ?)";
        int generatedID = -1;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {

            // Hash the password before insertion
            String hashedPassword = HashUtils.sha256(user.getPassword());

            // Insert user with the hashed password
            stmtUser.setString(1, user.getEmail());
            stmtUser.setString(2, hashedPassword);
            stmtUser.setString(3, user.getFirstName());
            stmtUser.setString(4, user.getLastName());
            stmtUser.setString(5, user.getRole());

            int affectedRows = stmtUser.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmtUser.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedID = generatedKeys.getInt(1);
                    }
                }
            } else {
                throw new SQLException("User insertion failed, no rows affected.");
            }

            // If the user is a client and an address is provided, insert the address as well
            if ("client".equalsIgnoreCase(user.getRole())) {
                Address address = user.getAddress();
                try (PreparedStatement stmtAddress = conn.prepareStatement(sqlAddress)) {
                    stmtAddress.setInt(1, generatedID);
                    stmtAddress.setString(2, address.getStreet());
                    stmtAddress.setString(3, address.getCity());
                    stmtAddress.setString(4, address.getPostCode());
                    stmtAddress.setString(5, address.getCountry());

                    stmtAddress.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedID; // Return the generated ID
    }

    /**
     * Verifies if the login credentials are valid.
     * 
     * @param testEmail    The email to be verified.
     * @param testPassword The password to be verified.
     * @return true if login is valid, false otherwise.
     */
    public boolean isLoginValid(String testEmail, String testPassword) {
        String query = "SELECT password FROM user WHERE email = ? AND active = 1";
        // Hash the provided password to compare with the stored hashed password
        String hashedTestPassword = HashUtils.sha256(testPassword);

        System.out.println(hashedTestPassword);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, testEmail);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password"); // Stored hashed password
                    return hashedTestPassword.equals(storedPassword);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Retrieves a user based on their email.
     * 
     * @param email The email of the user to be fetched.
     * @return The user object if found, null otherwise.
     */
    public User setUser(String email) {
        String queryUser = "SELECT * FROM user WHERE email = ?";
        String queryAddress = "SELECT * FROM address WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtUser = conn.prepareStatement(queryUser)) {

            stmtUser.setString(1, email);
            ResultSet rsUser = stmtUser.executeQuery();

            if (rsUser.next()) {
                int id = rsUser.getInt("id");
                String emailDB = rsUser.getString("email");
                String password = rsUser.getString("password");
                String firstName = rsUser.getString("firstname");
                String lastName = rsUser.getString("lastname");
                String role = rsUser.getString("role");
                int active = rsUser.getInt("active");

                User user = new User(id, emailDB, password, firstName, lastName, role, active);

                // If the user is a client, fetch the associated address
                if ("client".equalsIgnoreCase(role)) {
                    try (PreparedStatement stmtAddress = conn.prepareStatement(queryAddress)) {
                        stmtAddress.setInt(1, id);
                        ResultSet rsAddress = stmtAddress.executeQuery();

                        if (rsAddress.next()) {
                            String street = rsAddress.getString("street");
                            String city = rsAddress.getString("city");
                            String postCode = rsAddress.getString("postCode");
                            String country = rsAddress.getString("country");

                            Address address = new Address(id, street, city, postCode, country, user);
                            user.setAddress(address);
                        }
                    }
                }
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Updates the details of an existing user in the database.
     * 
     * @param user The user with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateUser(User user) {
        String queryUser = "UPDATE user SET email = ?, password = ?, firstname = ?, lastname = ?, role = ? WHERE id = ?";
        String queryAddress = "UPDATE address SET street = ?, city = ?, postcode = ?, country = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtUser = conn.prepareStatement(queryUser)) {

            // Update user details
            stmtUser.setString(1, user.getEmail());
            stmtUser.setString(2, user.getPassword());
            stmtUser.setString(3, user.getFirstName());
            stmtUser.setString(4, user.getLastName());
            stmtUser.setString(5, user.getRole());
            stmtUser.setInt(6, user.getId()); // User ID to be updated

            int affectedRowsUser = stmtUser.executeUpdate();

            // If user is a client, update the address as well
            if (affectedRowsUser > 0 && "client".equalsIgnoreCase(user.getRole())) {
                Address address = user.getAddress();
                try (PreparedStatement stmtAddress = conn.prepareStatement(queryAddress)) {
                    stmtAddress.setString(1, address.getStreet());
                    stmtAddress.setString(2, address.getCity());
                    stmtAddress.setString(3, address.getPostCode());
                    stmtAddress.setString(4, address.getCountry());
                    stmtAddress.setInt(5, user.getId());

                    int affectedRowsAddress = stmtAddress.executeUpdate();
                    return affectedRowsAddress > 0;
                }
            }

            return affectedRowsUser > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Marks a user as inactive (soft delete) in the database.
     * 
     * @param user The user to be deactivated.
     * @return true if the user was successfully deactivated, false otherwise.
     */
    public boolean deleteUser(User user) {
        String deactivateUserQuery = "UPDATE user SET active = 0 WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtUser = conn.prepareStatement(deactivateUserQuery)) {

            // Begin transaction
            conn.setAutoCommit(false);

            // Deactivate the user instead of deleting
            stmtUser.setInt(1, user.getId());
            int affectedRowsUser = stmtUser.executeUpdate();

            // Commit the transaction
            conn.commit();

            return affectedRowsUser > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            // Rollback transaction in case of error
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Reset auto-commit
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
