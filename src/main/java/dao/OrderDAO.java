package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tables.Order;
import tables.Product;
import util.DataSingleton;
import util.DatabaseConnection;
import util.UserSession;

public class OrderDAO {

	public int insertOrder(Order order) {
        String insertOrderQuery = "INSERT INTO `order` (clientID, status, purchaseDate) VALUES (?, ?, ?)";
        String insertOrderXProductQuery = "INSERT INTO orderxproduct (orderID, productID, quantity) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement orderXProductStmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Démarrer une transaction

            // 🔹 Étape 1 : Mettre à jour la date d'achat dans UserSession
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            UserSession.getInstance().getOrder().setPurchaseDate(currentTimestamp);

            // 🔹 Étape 2 : Insérer la commande
            orderStmt = conn.prepareStatement(insertOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, order.getClientID());
            orderStmt.setString(2, order.getStatus().toString()); 
            orderStmt.setTimestamp(3, currentTimestamp); // Date d'achat

            int affectedRows = orderStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Échec de l'insertion de la commande, aucune ligne affectée.");
            }

            // 🔹 Étape 3 : Récupérer l'ID de la commande créée
            generatedKeys = orderStmt.getGeneratedKeys();
            int orderId = -1; // Initialiser à -1 par défaut
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
                order.setOrderID(orderId); // Mettre à jour l'objet Order
            } else {
                throw new SQLException("Échec de l'obtention de l'ID de la commande.");
            }

            // 🔹 Étape 4 : Insérer chaque produit dans `orderxproduct`
            orderXProductStmt = conn.prepareStatement(insertOrderXProductQuery);

            for (Map.Entry<Product, Integer> entry : order.getCart().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                orderXProductStmt.setInt(1, orderId);
                orderXProductStmt.setInt(2, product.getProductID());
                orderXProductStmt.setInt(3, quantity);
                orderXProductStmt.addBatch();

                // Mettre à jour le stock
                boolean stockUpdated = DataSingleton.getInstance().getProductDAO().updateStock(product.getProductID(), quantity);
                if (!stockUpdated) {
                    conn.rollback();  // Annuler la transaction si stock insuffisant
                    return -1; // Retourner -1 si le stock n'a pas pu être mis à jour
                }
            }

            orderXProductStmt.executeBatch(); // Exécuter toutes les requêtes `orderxproduct` d’un coup

            conn.commit(); // Valider la transaction
            return orderId; // Retourner l'ID de la commande générée

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Annuler la transaction en cas d'erreur
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return -1; // Retourner -1 en cas d'erreur

        } finally {
            // Fermer les ressources proprement
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (orderStmt != null) orderStmt.close();
                if (orderXProductStmt != null) orderXProductStmt.close();
                if (conn != null) conn.setAutoCommit(true); // Remettre l'auto-commit par défaut
                if (conn != null) conn.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
    
    public List<Order> getOrdersByClientId(int clientId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM `order` WHERE clientID = ?";
        String queryOrderXProduct = "SELECT * FROM orderxproduct WHERE orderID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(query);
             PreparedStatement orderXProductStmt = conn.prepareStatement(queryOrderXProduct)) {

            // Récupérer toutes les commandes du client
            orderStmt.setInt(1, clientId);
            try (ResultSet rs = orderStmt.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("orderID");
                    Order order = new Order();
                    order.setOrderID(orderId);
                    order.setClientID(clientId);
                    order.setStatusFromString(rs.getString("status"));
                    System.out.println(order.getStatus());
                    

                    // Récupérer les produits associés à cette commande
                    orderXProductStmt.setInt(1, orderId);
                    try (ResultSet rsProducts = orderXProductStmt.executeQuery()) {
                        while (rsProducts.next()) {
                            int productId = rsProducts.getInt("productID");
                            int quantity = rsProducts.getInt("quantity");

                            // Créer un objet Product à partir de l'ID du produit
                            Product product = DataSingleton.getInstance().getProductDAO().getProductById(productId);
                            if (product != null) {
                                order.addToCart(product, quantity);  // Ajouter le produit au panier de la commande
                            }
                        }
                    }
                    
                    // Ajouter la commande à la liste des commandes du client
                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    
    
    public boolean updateOrder(Order order) {
        String updateOrderQuery = "UPDATE `order` SET clientID = ?, status = ? WHERE orderID = ?";
        String deleteOrderXProductQuery = "DELETE FROM orderxproduct WHERE orderID = ?";
        String insertOrderXProductQuery = "INSERT INTO orderxproduct (orderID, productID, quantity) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement deleteOrderXProductStmt = null;
        PreparedStatement orderXProductStmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Démarrer une transaction

            // 🔹 Étape 1 : Mettre à jour la commande
            orderStmt = conn.prepareStatement(updateOrderQuery);
            orderStmt.setInt(1, order.getClientID());
            orderStmt.setString(2, order.getStatus().toString());
            orderStmt.setInt(3, order.getOrderID()); // orderID à mettre à jour

            int affectedRows = orderStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Aucune commande trouvée avec l'ID spécifié pour la mise à jour.");
            }

            // 🔹 Étape 2 : Supprimer les anciens produits dans `orderxproduct` (si nécessaires)
            deleteOrderXProductStmt = conn.prepareStatement(deleteOrderXProductQuery);
            deleteOrderXProductStmt.setInt(1, order.getOrderID()); // orderID à supprimer
            deleteOrderXProductStmt.executeUpdate();

            // 🔹 Étape 3 : Insérer les nouveaux produits dans `orderxproduct`
            orderXProductStmt = conn.prepareStatement(insertOrderXProductQuery);

            for (Map.Entry<Product, Integer> entry : order.getCart().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                orderXProductStmt.setInt(1, order.getOrderID()); // orderID
                orderXProductStmt.setInt(2, product.getProductID()); // productID
                orderXProductStmt.setInt(3, quantity); // quantity
                orderXProductStmt.addBatch();
            }

            orderXProductStmt.executeBatch(); // Exécuter toutes les requêtes `orderxproduct`
            conn.commit(); // Valider la transaction
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Annuler la transaction en cas d'erreur
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;

        } finally {
            // Fermer les ressources proprement
            try {
                if (orderStmt != null) orderStmt.close();
                if (deleteOrderXProductStmt != null) deleteOrderXProductStmt.close();
                if (orderXProductStmt != null) orderXProductStmt.close();
                if (conn != null) conn.setAutoCommit(true); // Remettre l'auto-commit par défaut
                if (conn != null) conn.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
    
    public Order getInProgressOrderByClientID(int clientID) {
        String query = "SELECT * FROM `order` WHERE `clientID` = ? AND `status` = 'In progress' LIMIT 1";
        Order order = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, clientID);  // On remplace le ? par le clientID
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    order = new Order();
                    order.setOrderID(rs.getInt("orderID"));
                    order.setClientID(rs.getInt("clientID"));
                    order.setStatusFromString(rs.getString("status"));
                    order.setPurchaseDate(rs.getTimestamp("purchaseDate"));
                    order.setDeliveryDate(rs.getTimestamp("deliveryDate"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return order;  // Retourne la commande ou null si elle n'existe pas
    }
    

    public boolean updateOrCreateOrder(Order order) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Démarrer une transaction

            if (orderExists(conn, order.getOrderID())) {
                // Mise à jour de la commande et des produits dans le panier
                if (updateOrder(conn, order) && updateOrderxProduct(conn, order)) {
                    commitTransaction(conn);
                    return true;
                } else {
                    rollbackTransaction(conn);
                    return false;
                }
            } else {
                // Insertion de la commande et des produits dans le panier
                if (insertOrder(conn, order) && insertOrderxProduct(conn, order)) {
                    commitTransaction(conn);
                    return true;
                } else {
                    rollbackTransaction(conn);
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                rollbackTransaction(conn);
            }
            return false;
        } finally {
            closeResources(conn);
        }
    }

    private boolean orderExists(Connection conn, int orderID) throws SQLException {
        String checkOrderQuery = "SELECT * FROM `order` WHERE `orderID` = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkOrderQuery)) {
            checkStmt.setInt(1, orderID);
            try (ResultSet rs = checkStmt.executeQuery()) {
                return rs.next(); // Retourne vrai si la commande existe
            }
        }
    }

    private boolean updateOrder(Connection conn, Order order) throws SQLException {
        String updateOrderQuery = "UPDATE `order` SET clientID = ?, status = ?, purchaseDate = ?, deliveryDate = ? WHERE orderID = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateOrderQuery)) {
            updateStmt.setInt(1, order.getClientID());
            updateStmt.setString(2, order.getStatus().toString());
            updateStmt.setTimestamp(3, order.getPurchaseDate());
            updateStmt.setTimestamp(4, order.getDeliveryDate());
            updateStmt.setInt(5, order.getOrderID());
            int affectedRows = updateStmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    private boolean updateOrderxProduct(Connection conn, Order order) throws SQLException {
        // Supprimer les anciennes lignes dans orderxproduct pour cette commande
        String deleteOrderxProductQuery = "DELETE FROM `orderxproduct` WHERE `orderID` = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteOrderxProductQuery)) {
            deleteStmt.setInt(1, order.getOrderID());
            deleteStmt.executeUpdate();
        }

        // Ajouter les nouvelles lignes dans orderxproduct
        String insertOrderxProductQuery = "INSERT INTO `orderxproduct` (orderID, productID, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement insertProductStmt = conn.prepareStatement(insertOrderxProductQuery)) {
            for (Map.Entry<Product, Integer> entry : order.getCart().entrySet()) {
                insertProductStmt.setInt(1, order.getOrderID());
                insertProductStmt.setInt(2, entry.getKey().getProductID());
                insertProductStmt.setInt(3, entry.getValue());
                insertProductStmt.addBatch();
            }
            insertProductStmt.executeBatch();
            return true;
        }
    }

    private boolean insertOrder(Connection conn, Order order) throws SQLException {
        String insertOrderQuery = "INSERT INTO `order` (clientID, status, purchaseDate, deliveryDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertStmt.setInt(1, order.getClientID());
            insertStmt.setString(2, order.getStatus().toString());
            insertStmt.setTimestamp(3, order.getPurchaseDate());
            insertStmt.setTimestamp(4, order.getDeliveryDate());
            int affectedRows = insertStmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setOrderID(generatedKeys.getInt(1)); // Set the generated orderID
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private boolean insertOrderxProduct(Connection conn, Order order) throws SQLException {
        String insertOrderxProductQuery = "INSERT INTO `orderxproduct` (orderID, productID, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement insertProductStmt = conn.prepareStatement(insertOrderxProductQuery)) {
            for (Map.Entry<Product, Integer> entry : order.getCart().entrySet()) {
                insertProductStmt.setInt(1, order.getOrderID());
                insertProductStmt.setInt(2, entry.getKey().getProductID());
                insertProductStmt.setInt(3, entry.getValue());
                insertProductStmt.addBatch();
            }
            insertProductStmt.executeBatch();
            return true;
        }
    }

    private void commitTransaction(Connection conn) throws SQLException {
        conn.commit(); // Valider la transaction
    }

    private void rollbackTransaction(Connection conn) {
        try {
            conn.rollback(); // Annuler la transaction
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeResources(Connection conn) {
        try {
            if (conn != null) conn.setAutoCommit(true); // Remettre l'auto-commit par défaut
            if (conn != null) conn.close();
        } catch (SQLException closeEx) {
            closeEx.printStackTrace();
        }
    }



}
