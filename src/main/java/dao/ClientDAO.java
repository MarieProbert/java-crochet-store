package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import managers.UserManager;
import tables.Client;
import util.DatabaseConnection;

public class ClientDAO {

	// Permet d'afficher tous les clients de la base
	public void getAllClients() {
        String query = "SELECT * FROM Client";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int clientID = rs.getInt("clientID");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String firstName = rs.getString("firstName");
            	String lastName = rs.getString("lastName");
            	String street = rs.getString("street");
            	String city = rs.getString("city");
            	int postCode = rs.getInt("postCode");
            	String country = rs.getString("country");

                System.out.println("ID: " + clientID + ", Email: " + email + ", First Name: " + firstName);
         }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	//Permet l'ajout d'un client dans la base de données
	public int insertClient(Client client) {
		 String sql = "INSERT INTO client (email, password, firstName, lastName, street, city, postCode, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		 int generatedID = -1;   
		 
		 try (Connection conn = DatabaseConnection.getConnection();
		         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

		        stmt.setString(1, client.getEmail());
		        stmt.setString(2, client.getPassword());
		        stmt.setString(3, client.getFirstName());
		        stmt.setString(4, client.getLastName());
		        stmt.setString(5, client.getStreet());
		        stmt.setString(6, client.getCity());
		        stmt.setInt(7, client.getPostCode());
		        stmt.setString(8, client.getCountry());

	            // Exécuter la requête
	            int affectedRows = stmt.executeUpdate();

	            if (affectedRows > 0) {
	                // Récupérer l'ID généré
	                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	                    if (generatedKeys.next()) {
	                        generatedID = generatedKeys.getInt(1);
	                    }
	                }
	            } else {
	                throw new SQLException("L'insertion du client a échoué, aucune ligne affectée.");
	            }

		    
		 } catch (SQLException e) {
		        e.printStackTrace();
		    
		 }
		 
		 return generatedID; // Retourner l'ID généré
    }
	
	// Teste si à partir d'un email et d'un mot de passe le login fonctionne
	public boolean isLoginValid(String testEmail, String testPassword) {
		String query = "SELECT * FROM Client";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String email = rs.getString("email");
                String password = rs.getString("password");
               

                if ((testEmail.equals(email)) && (testPassword.equals(password))){
                	return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
		
	}
	
	// A partir d'un email, récupère les informations d'un client dans la BDD et les stocke sous forme d'un Client dans Java
	public Client setClient(String email) {
		String query = "SELECT * FROM Client";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String emailDB = rs.getString("email");

                if (email.equals(emailDB)){
                    int clientID = rs.getInt("clientID");
                    String password = rs.getString("password");
                    String firstName = rs.getString("firstName");
                	String lastName = rs.getString("lastName");
                	String street = rs.getString("street");
                	String city = rs.getString("city");
                	int postCode = rs.getInt("postCode");
                	String country = rs.getString("country");   
                	
                	return new Client(clientID, emailDB, password, firstName, lastName, street, city, postCode, country);
      
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
		
	}
}
