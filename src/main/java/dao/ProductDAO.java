package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DatabaseConnection;

public class ProductDAO {

	public void getAllProducts() {
        String query = "SELECT * FROM Product";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int productID = rs.getInt("productID");
                String name = rs.getString("name");
                String creator = rs.getString("creator");
                int price = rs.getInt("price");
            	String description = rs.getString("description");
            	String color = rs.getString("color");
            	int stock = rs.getInt("stock");
            	String size = rs.getString("size");
            	String theme = rs.getString("theme");
            	String imagePath = rs.getString("imagePath");
            	String category = rs.getString("category");

           }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
