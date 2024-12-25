package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Remplacez par vos informations de connexion
    private static final String URL = "jdbc:mysql://localhost:3306/crochet_store"; // URL de la base de données
    private static final String USER = "root"; // Nom d'utilisateur de la base de données
    private static final String PASSWORD = "admin"; // Mot de passe
    
    private static Connection connection;

    private DatabaseConnection() {} // Empêche l'instanciation externe

    // Connexion à la base de données
    public static Connection getConnection() throws SQLException {
        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver"); // Pour MySQL
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("JDBC Driver non trouvé");
        }

        // Retourner la connexion
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
