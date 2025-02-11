package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tables.CartItem;
import tables.Order;
import tables.Product;
import util.DataSingleton;
import util.DatabaseConnection;
import util.UserSession;

/**
 * Data Access Object (DAO) class for managing Order-related database operations.
 * Provides methods to insert, update, and fetch orders and associated products.
 */
public class OrderDAO {

    /**
     * Inserts a new order into the database, including the order details and associated products.
     * The stock is updated for each product in the order.
     * 
     * @param order The order object to insert.
     * @return The generated order ID if the order was inserted successfully, or -1 if an error occurred.
     */
    public int insertOrder(Order order) {
        String insertOrderQuery = "INSERT INTO `order` (userID, orderStatus, purchaseDate) VALUES (?, ?, ?)";
        String insertOrderXProductQuery = "INSERT INTO orderxproduct (orderID, productID, quantity, priceAtPurchase) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement orderXProductStmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start a transaction

            // Set purchase date
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            UserSession.getInstance().getOrder().setPurchaseDate(currentTimestamp);

            // Insert the order
            orderStmt = conn.prepareStatement(insertOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, order.getClientID());
            orderStmt.setString(2, order.getStatus().toString());
            orderStmt.setTimestamp(3, currentTimestamp);

            int affectedRows = orderStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to insert the order, no rows affected.");
            }

            // Retrieve the generated order ID
            generatedKeys = orderStmt.getGeneratedKeys();
            int orderId = -1; // Default to -1 if ID not found
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
                order.setOrderID(orderId);
            } else {
                throw new SQLException("Failed to retrieve the generated order ID.");
            }

            // Insert the products for this order
            orderXProductStmt = conn.prepareStatement(insertOrderXProductQuery);
            for (Map.Entry<Product, CartItem> entry : order.getCart().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue().getQuantity();
                double price = entry.getValue().getPriceAtPurchase();

                orderXProductStmt.setInt(1, orderId);
                orderXProductStmt.setInt(2, product.getProductID());
                orderXProductStmt.setInt(3, quantity);
                orderXProductStmt.setDouble(4, price);
                orderXProductStmt.addBatch();

                // Update stock for the product
                boolean stockUpdated = DataSingleton.getInstance().getProductDAO().updateStock(product.getProductID(), quantity);
                if (!stockUpdated) {
                    conn.rollback();  // Rollback the transaction if stock update fails
                    return -1;
                }
            }

            orderXProductStmt.executeBatch(); // Execute all product insertions
            conn.commit(); // Commit the transaction
            return orderId; // Return the generated order ID

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction in case of error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return -1; // Return -1 on error

        } finally {
            // Close resources
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (orderStmt != null) orderStmt.close();
                if (orderXProductStmt != null) orderXProductStmt.close();
                if (conn != null) conn.setAutoCommit(true); // Reset auto-commit
                if (conn != null) conn.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }

    /**
     * Retrieves all orders for a specific client from the database.
     * 
     * @param clientId The client ID to fetch orders for.
     * @return A list of Order objects associated with the given client.
     */
    public List<Order> getOrdersByClientId(int clientId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM `order` WHERE userID = ?";
        String queryOrderXProduct = "SELECT * FROM orderxproduct WHERE orderID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(query);
             PreparedStatement orderXProductStmt = conn.prepareStatement(queryOrderXProduct)) {

            // Retrieve all orders for the client
            orderStmt.setInt(1, clientId);
            try (ResultSet rs = orderStmt.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("orderID");
                    Order order = new Order();
                    order.setOrderID(orderId);
                    order.setClientID(clientId);
                    order.setStatusFromString(rs.getString("orderStatus"));
                    order.setPurchaseDate(rs.getTimestamp("purchaseDate"));
                    order.setDeliveryDate(rs.getTimestamp("deliveryDate"));

                    // Retrieve products associated with the order
                    orderXProductStmt.setInt(1, orderId);
                    try (ResultSet rsProducts = orderXProductStmt.executeQuery()) {
                        while (rsProducts.next()) {
                            int productId = rsProducts.getInt("productID");
                            int quantity = rsProducts.getInt("quantity");
                            double priceAtPurchase = rsProducts.getDouble("priceAtPurchase");

                            // Fetch product and add to order's cart
                            Product product = DataSingleton.getInstance().getProductDAO().getProductById(productId);
                            if (product != null) {
                                order.addToFinalisedCart(product, quantity, priceAtPurchase);
                            }
                        }
                    }

                    // Add the order to the list
                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    public Order getOrderByOrderID(int orderId) {
        String query = "SELECT * FROM `order` WHERE orderID = ?";
        String queryOrderXProduct = "SELECT * FROM orderxproduct WHERE orderID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(query);
             PreparedStatement orderXProductStmt = conn.prepareStatement(queryOrderXProduct)) {

            // Récupérer la commande principale
            orderStmt.setInt(1, orderId);
            try (ResultSet rs = orderStmt.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();
                    order.setOrderID(orderId);
                    order.setClientID(rs.getInt("userID"));
                    order.setStatusFromString(rs.getString("orderStatus"));
                    order.setPurchaseDate(rs.getTimestamp("purchaseDate"));
                    order.setDeliveryDate(rs.getTimestamp("deliveryDate"));

                    // Récupérer les produits associés à la commande
                    orderXProductStmt.setInt(1, orderId);
                    try (ResultSet rsProducts = orderXProductStmt.executeQuery()) {
                        while (rsProducts.next()) {
                            int productId = rsProducts.getInt("productID");
                            int quantity = rsProducts.getInt("quantity");
                            double price = rsProducts.getDouble("priceAtPurchase");

                            // Récupérer le produit et l'ajouter au panier de la commande
                            Product product = DataSingleton.getInstance().getProductDAO().getProductById(productId);
                            if (product != null) {
                                order.addToFinalisedCart(product, quantity, price);
                            }
                        }
                    }
                    return order; // Retourner la commande trouvée
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retourne null si la commande n'est pas trouvée
    }

    
    /**
     * Retrieves all orders from the database.
     *
     * @return A list of Order objects.
     */
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM `order`";
        String queryOrderXProduct = "SELECT * FROM orderxproduct WHERE orderID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(query);
             PreparedStatement orderXProductStmt = conn.prepareStatement(queryOrderXProduct)) {

            try (ResultSet rs = orderStmt.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("orderID");
                    Order order = new Order();
                    order.setOrderID(orderId);
                    order.setClientID(rs.getInt("userID"));
                    order.setStatusFromString(rs.getString("orderStatus"));
                    order.setPurchaseDate(rs.getTimestamp("purchaseDate"));
                    order.setDeliveryDate(rs.getTimestamp("deliveryDate"));

                    // Retrieve products associated with the order
                    orderXProductStmt.setInt(1, orderId);
                    try (ResultSet rsProducts = orderXProductStmt.executeQuery()) {
                        while (rsProducts.next()) {
                            int productId = rsProducts.getInt("productID");
                            int quantity = rsProducts.getInt("quantity");
                            double price = rsProducts.getDouble("priceAtPurchase");

                            // Fetch product and add to order's cart
                            Product product = DataSingleton.getInstance().getProductDAO().getProductById(productId);
                            if (product != null) {
                                order.addToFinalisedCart(product, quantity, price);
                            }
                        }
                    }

                    // Add the order to the list
                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Updates an existing order and its associated products in the database.
     * 
     * @param order The order object to update.
     * @return true if the order was updated successfully, false otherwise.
     */
    public boolean updateOrder(Order order) {
        String sql = "UPDATE `order` SET orderStatus = ?, deliveryDate = ? WHERE orderID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Mise à jour du statut
            stmt.setString(1, order.getStatus().toString());
            
            // Mise à jour de la date de livraison
            if (order.getDeliveryDate() != null) {
                stmt.setTimestamp(2, order.getDeliveryDate());
            } else {
                stmt.setNull(2, java.sql.Types.TIMESTAMP);
            }
            
            stmt.setInt(3, order.getOrderID());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
