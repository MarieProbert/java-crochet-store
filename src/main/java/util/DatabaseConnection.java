package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gère la connexion à la base de données MySQL pour l'application.
 */
public class DatabaseConnection {

    // Détails de la connexion à la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/crochet_store"; // URL de la base de données
    private static final String USER = "root"; // Nom d'utilisateur de la base de données
    private static final String PASSWORD = "admin"; // Mot de passe de la base de données

    // Constructeur privé pour empêcher l'instanciation
    private DatabaseConnection() {}

    /**
     * Établit une connexion à la base de données.
     * 
     * @return La connexion établie à la base de données.
     * @throws SQLException Si la connexion échoue.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Charger le driver JDBC pour MySQL
            Class.forName("com.mysql.cj.jdbc.Driver"); // Pour MySQL
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("JDBC Driver non trouvé");
        }

        // Retourner la connexion à la base de données
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
