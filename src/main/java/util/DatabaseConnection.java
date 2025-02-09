package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the connection to the MySQL database for the application.
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/crochet_store";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    // Private constructor to prevent instantiation.
    private DatabaseConnection() {}

    /**
     * Establishes and returns a connection to the database.
     *
     * @return a Connection to the database
     * @throws SQLException if a database access error occurs or the driver is not found
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load the MySQL JDBC driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("JDBC Driver not found");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
