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

/**
 * Data Access Object for user-related operations.
 */
public class UserDAO {

    /**
     * Retrieves all active users from the database.
     *
     * @return a list of active users.
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

                if ("client".equalsIgnoreCase(role)) {
                    try (PreparedStatement stmtAddress = conn.prepareStatement(queryAddress)) {
                        stmtAddress.setInt(1, id);
                        try (ResultSet rsAddress = stmtAddress.executeQuery()) {
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
                }
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the user ID.
     * @return the user if found, or null otherwise.
     */
    public User getUserById(int id) {
        String queryUser = "SELECT * FROM user WHERE id = ? AND active = 1";
        String queryAddress = "SELECT * FROM address WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtUser = conn.prepareStatement(queryUser)) {
            
            stmtUser.setInt(1, id);
            try (ResultSet rsUser = stmtUser.executeQuery()) {
                if (rsUser.next()) {
                    String email = rsUser.getString("email");
                    String password = rsUser.getString("password");
                    String firstName = rsUser.getString("firstname");
                    String lastName = rsUser.getString("lastname");
                    String role = rsUser.getString("role");
                    int active = rsUser.getInt("active");
                    
                    User user = new User(id, email, password, firstName, lastName, role, active);
                    
                    if ("client".equalsIgnoreCase(role)) {
                        try (PreparedStatement stmtAddress = conn.prepareStatement(queryAddress)) {
                            stmtAddress.setInt(1, id);
                            try (ResultSet rsAddress = stmtAddress.executeQuery()) {
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
                    }
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserts a new user into the database.
     *
     * @param user the user to insert.
     * @return the generated user ID, or -1 if insertion failed.
     */
    public int insertUser(User user) {
        String sqlUser = "INSERT INTO user (email, password, firstname, lastname, role) VALUES (?, ?, ?, ?, ?)";
        String sqlAddress = "INSERT INTO address (user_id, street, city, postcode, country) VALUES (?, ?, ?, ?, ?)";
        int generatedID = -1;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {

            String hashedPassword = HashUtils.sha256(user.getPassword());
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

        return generatedID;
    }

    /**
     * Validates the login credentials.
     *
     * @param testEmail    the email to check.
     * @param testPassword the password to check.
     * @return true if credentials are valid, false otherwise.
     */
    public boolean isLoginValid(String testEmail, String testPassword) {
        String query = "SELECT password FROM user WHERE email = ? AND active = 1";
        String hashedTestPassword = HashUtils.sha256(testPassword);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, testEmail);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return hashedTestPassword.equals(storedPassword);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Retrieves a user by email.
     *
     * @param email the email of the user.
     * @return the corresponding user, or null if not found.
     */
    public User setUser(String email) {
        String queryUser = "SELECT * FROM user WHERE email = ?";
        String queryAddress = "SELECT * FROM address WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtUser = conn.prepareStatement(queryUser)) {

            stmtUser.setString(1, email);
            try (ResultSet rsUser = stmtUser.executeQuery()) {
                if (rsUser.next()) {
                    int id = rsUser.getInt("id");
                    String emailDB = rsUser.getString("email");
                    String password = rsUser.getString("password");
                    String firstName = rsUser.getString("firstname");
                    String lastName = rsUser.getString("lastname");
                    String role = rsUser.getString("role");
                    int active = rsUser.getInt("active");

                    User user = new User(id, emailDB, password, firstName, lastName, role, active);

                    if ("client".equalsIgnoreCase(role)) {
                        try (PreparedStatement stmtAddress = conn.prepareStatement(queryAddress)) {
                            stmtAddress.setInt(1, id);
                            try (ResultSet rsAddress = stmtAddress.executeQuery()) {
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
                    }
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Updates the specified user's details in the database.
     *
     * @param user the user with updated information.
     * @return true if update succeeded, false otherwise.
     */
    public boolean updateUser(User user) {
        String queryUser = "UPDATE user SET email = ?, password = ?, firstname = ?, lastname = ?, role = ? WHERE id = ?";
        String queryAddress = "UPDATE address SET street = ?, city = ?, postcode = ?, country = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtUser = conn.prepareStatement(queryUser)) {

            stmtUser.setString(1, user.getEmail());
            stmtUser.setString(2, user.getPassword());
            stmtUser.setString(3, user.getFirstName());
            stmtUser.setString(4, user.getLastName());
            stmtUser.setString(5, user.getRole());
            stmtUser.setInt(6, user.getId());

            int affectedRowsUser = stmtUser.executeUpdate();

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
     * Performs a soft delete of the specified user by marking them as inactive.
     *
     * @param user the user to deactivate.
     * @return true if deactivation succeeded, false otherwise.
     */
    public boolean deleteUser(User user) {
        String deactivateUserQuery = "UPDATE user SET active = 0 WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtUser = conn.prepareStatement(deactivateUserQuery)) {

            conn.setAutoCommit(false);
            stmtUser.setInt(1, user.getId());
            int affectedRowsUser = stmtUser.executeUpdate();
            conn.commit();
            return affectedRowsUser > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
