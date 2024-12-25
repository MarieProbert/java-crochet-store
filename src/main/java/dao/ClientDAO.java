package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tables.Client;
import util.DatabaseConnection;

public class ClientDAO {

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
            	String address = rs.getString("address");
            	String city = rs.getString("city");
            	int postCode = rs.getInt("postCode");
            	String country = rs.getString("country");

                System.out.println("ID: " + clientID + ", Email: " + email + ", First Name: " + firstName);
         }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public boolean isLoginValid(String testEmail, String testPassword) {
		String query = "SELECT * FROM Client";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String email = rs.getString("email");
                String password = rs.getString("password");
                
                System.out.println(testEmail + " " + testPassword);
                System.out.println(email + " " + password);

                if ((testEmail.equals(email)) && (testPassword.equals(password))){
                	return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
		
	}
}
