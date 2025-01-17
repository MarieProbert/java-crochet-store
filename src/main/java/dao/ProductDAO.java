package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tables.Catalog;
import tables.Product;
import util.DatabaseConnection;

public class ProductDAO {
	
	private Catalog catalog;

    // Constructeur qui initialise le Catalog
    public ProductDAO() {
        this.catalog = Catalog.getInstance(); 
    }
    

	public Catalog getCatalog() {
		return catalog;
	}


	public void getAllProducts() {
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
    }
}
