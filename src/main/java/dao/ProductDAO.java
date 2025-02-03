package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tables.Catalog;
import tables.Product;
import util.DatabaseConnection;

public class ProductDAO {


	public Catalog getAllProducts() {
		Catalog catalog = new Catalog();
        String query = "SELECT * FROM Product";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int productID = rs.getInt("productID");
                Product product = new Product(productID);
                product.setName(rs.getString("name"));
                product.setCreator(rs.getString("creator"));
                product.setPrice(rs.getDouble("price"));
                product.setDescription(rs.getString("description"));
                product.setColorFromString(rs.getString("color"));
                product.setStock( rs.getInt("stock"));
                product.setSizeFromString(rs.getString("size"));
                product.setThemeFromString(rs.getString("theme"));
                product.setImagePath(rs.getString("imagePath"));
                product.setCategoryFromString(rs.getString("category"));
 
                catalog.addProduct(product);

           }
           System.out.println(catalog.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
		return catalog;
    }
	
    public boolean updateStock(int productID, int quantity) {
        String query = "UPDATE Product SET stock = stock - ? WHERE productID = ? AND stock >= ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, quantity);
            stmt.setInt(2, productID);
            stmt.setInt(3, quantity);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // True si le stock a bien été mis à jour
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Product getProductById(int productId) {
        Product product = null;
        String query = "SELECT * FROM Product WHERE productID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    product = new Product(productId);
                    product.setName(rs.getString("name"));
                    product.setCreator(rs.getString("creator"));
                    product.setPrice(rs.getDouble("price"));
                    product.setDescription(rs.getString("description"));
                    product.setColorFromString(rs.getString("color"));
                    product.setStock(rs.getInt("stock"));
                    product.setSizeFromString(rs.getString("size"));
                    product.setThemeFromString(rs.getString("theme"));
                    product.setImagePath(rs.getString("imagePath"));
                    product.setCategoryFromString(rs.getString("category"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

}
