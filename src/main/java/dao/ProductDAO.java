package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DatabaseConnection;

public class ProductDAO {

    public void getAllProducts() {
    	/*
        String query = "SELECT * FROM Client";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int clientID = rs.getInt("clientID");
                String email = rs.getString("email");
                String firstName = rs.getString("firstName");

                System.out.println("ID: " + clientID + ", Email: " + email + ", First Name: " + firstName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
    }
}
