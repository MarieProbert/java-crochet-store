package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import tables.Catalog;
import tables.Product;
import util.DatabaseConnection;

/**
 * Data Access Object (DAO) class for managing Product-related database operations.
 * Provides methods to get all products, update product stock, and fetch products by ID.
 */
public class ProductDAO {

    /**
     * Retrieves all products from the database.
     * 
     * @return A Catalog object containing all products.
     */
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
                product.setStock(rs.getInt("stock"));
                product.setSizeFromString(rs.getString("size"));
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

    /**
     * Updates the stock for a specific product in the database.
     * 
     * @param productID The product's ID to update.
     * @param quantity The quantity to reduce from the stock.
     * @return true if stock is updated successfully, false otherwise.
     */
    public boolean updateStock(int productID, int quantity) {
        String query = "UPDATE Product SET stock = stock - ? WHERE productID = ? AND stock >= ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, productID);
            stmt.setInt(3, quantity);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Returns true if stock was successfully updated.
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a product by its ID from the database.
     * 
     * @param productId The ID of the product to fetch.
     * @return A Product object representing the product, or null if not found.
     */
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
                    product.setImagePath(rs.getString("imagePath"));
                    product.setCategoryFromString(rs.getString("category"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }
    
    
    /**
     * Updates a product in the database with the provided product details.
     * 
     * @param product The product object containing the updated details.
     * @return true if the product was successfully updated, false otherwise.
     */
    public boolean updateProduct(Product product) {
        String query = "UPDATE Product SET name = ?, creator = ?, price = ?, description = ?, color = ?, stock = ?, size = ?, imagePath = ?, category = ? WHERE productID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCreator());
            stmt.setDouble(3, product.getPrice());
            stmt.setString(4, product.getDescription());
            stmt.setString(5, product.getColor().toString());
            stmt.setInt(6, product.getStock());
            stmt.setString(7, product.getSize().toString());
            stmt.setString(8, product.getImagePath());
            stmt.setString(9, product.getCategory().toString());
            stmt.setInt(10, product.getProductID());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Returns true if the product was successfully updated.

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertProduct(Product product) {
        // Requête SQL d'insertion
        String query = "INSERT INTO Product (name, creator, price, description, color, stock, size, imagePath, category) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             // On demande à récupérer la clé générée lors de l'insertion
        	PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Affectation des valeurs aux paramètres de la requête
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCreator());
            stmt.setDouble(3, product.getPrice());
            stmt.setString(4, product.getDescription());
            stmt.setString(5, product.getColor().toString());
            stmt.setInt(6, product.getStock());
            stmt.setString(7, product.getSize().toString());
            stmt.setString(8, product.getImagePath());
            stmt.setString(9, product.getCategory().toString());

            // Exécution de la requête
            int rowsAffected = stmt.executeUpdate();

            // Si l'insertion a réussi, on récupère la clé générée et on la positionne dans l'objet product
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        product.setProductID(generatedId);
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
