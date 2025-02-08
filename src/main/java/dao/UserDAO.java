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
	
	
    public int insertUser(User user) {
        String sqlUser = "INSERT INTO user (email, password, firstname, lastname, role) VALUES (?, ?, ?, ?, ?)";
        String sqlAddress = "INSERT INTO address (user_id, street, city, postcode, country) VALUES (?, ?, ?, ?, ?)";
        int generatedID = -1;  

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {

            // Hacher le mot de passe avant l'insertion
            String hashedPassword = HashUtils.sha256(user.getPassword());

            // Insérer l'utilisateur avec le mot de passe hashé
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
                throw new SQLException("L'insertion de l'utilisateur a échoué, aucune ligne affectée.");
            }

            // Si c'est un client et qu'une adresse est fournie, insérer aussi l'adresse
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

        return generatedID; // Retourner l'ID généré
    }

	
    public boolean isLoginValid(String testEmail, String testPassword) {
        String query = "SELECT password FROM user WHERE email = ? AND active = 1"; 
        // Hacher le mot de passe fourni pour pouvoir le comparer avec celui stocké en base
        String hashedTestPassword = HashUtils.sha256(testPassword);
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

        	System.out.println("Password : " + hashedTestPassword);
            stmt.setString(1, testEmail);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password"); // Mot de passe stocké sous forme hashée
                    System.out.println(storedPassword);
                    return hashedTestPassword.equals(storedPassword);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

	
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
	            String firstName = rsUser.getString("firstname"); // Vérifie bien le nom des colonnes
	            String lastName = rsUser.getString("lastname");
	            String role = rsUser.getString("role"); // Vérifie si c'est un client
	            int active = rsUser.getInt("active");

	            User user = new User(id, emailDB, password, firstName, lastName, role, active);
	            
	            if ("client".equalsIgnoreCase(role)) {
	            	System.out.println("Halo");
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

	
	public boolean updateUser(User user) {
	    String queryUser = "UPDATE user SET email = ?, password = ?, firstname = ?, lastname = ?, role = ? WHERE id = ?";
	    String queryAddress = "UPDATE address SET street = ?, city = ?, postcode = ?, country = ? WHERE user_id = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmtUser = conn.prepareStatement(queryUser)) {

	        // Mettre à jour l'utilisateur
	        stmtUser.setString(1, user.getEmail());
	        stmtUser.setString(2, user.getPassword());
	        stmtUser.setString(3, user.getFirstName());
	        stmtUser.setString(4, user.getLastName());
	        stmtUser.setString(5, user.getRole()); // "admin" ou "client"
	        stmtUser.setInt(6, user.getId()); // L'ID de l'utilisateur à mettre à jour

	        int affectedRowsUser = stmtUser.executeUpdate();
	        
	        // Si l'utilisateur a bien été mis à jour et qu'il s'agit d'un client, on met aussi à jour son adresse
	        if (affectedRowsUser > 0 && "client".equalsIgnoreCase(user.getRole())) {
	            Address address = user.getAddress();
	            try (PreparedStatement stmtAddress = conn.prepareStatement(queryAddress)) {
	                stmtAddress.setString(1, address.getStreet());
	                stmtAddress.setString(2, address.getCity());
	                stmtAddress.setString(3, address.getPostCode());
	                stmtAddress.setString(4, address.getCountry());
	                stmtAddress.setInt(5, user.getId()); // L'ID de l'utilisateur pour mettre à jour l'adresse

	                int affectedRowsAddress = stmtAddress.executeUpdate();
	                return affectedRowsAddress > 0; // Retourne vrai si l'adresse a été mise à jour
	            }
	        }

	        return affectedRowsUser > 0; // Retourne vrai si l'utilisateur a été mis à jour

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public boolean deleteUser(User user) {
	    String deactivateUserQuery = "UPDATE user SET active = 0 WHERE id = ?";

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmtUser = conn.prepareStatement(deactivateUserQuery)) {

	        // Commencer une transaction
	        conn.setAutoCommit(false);

	        // Désactiver l'utilisateur au lieu de le supprimer
	        stmtUser.setInt(1, user.getId());
	        int affectedRowsUser = stmtUser.executeUpdate();

	        // Valider la transaction
	        conn.commit();

	        return affectedRowsUser > 0; // Retourne vrai si l'utilisateur a été désactivé

	    } catch (SQLException e) {
	        e.printStackTrace();
	        // En cas d'erreur, annuler la transaction
	        try (Connection conn = DatabaseConnection.getConnection()) {
	            conn.rollback();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        return false;
	    } finally {
	        // Rétablir l'auto-commit
	        try (Connection conn = DatabaseConnection.getConnection()) {
	            conn.setAutoCommit(true);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}



}
