package dao;

import java.sql.*;
import java.util.List;
import java.util.Map;

import tables.Order;
import tables.Product;
import util.DataSingleton;
import util.DatabaseConnection;

public class OrderDAO {

    public boolean insertOrder(Order order) {
        String insertOrderQuery = "INSERT INTO `order` (clientID, status) VALUES (?, ?)";
        String insertOrderXProductQuery = "INSERT INTO orderxproduct (orderID, productID, quantity) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement orderXProductStmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Démarrer une transaction

            // 🔹 Étape 1 : Insérer la commande
            orderStmt = conn.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, order.getClientID());
            orderStmt.setString(2, "In progress"); // Quand on insère une commande elle est forcément en progrès

            int affectedRows = orderStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Échec de l'insertion de la commande, aucune ligne affectée.");
            }

            // 🔹 Étape 2 : Récupérer l'ID de la commande créée
            generatedKeys = orderStmt.getGeneratedKeys();
            int orderId;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
                order.setOrderID(orderId); // Mettre à jour l'objet Order
            } else {
                throw new SQLException("Échec de l'obtention de l'ID de la commande.");
            }

            // 🔹 Étape 3 : Insérer chaque produit dans `orderxproduct`
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
                    return false;
                }
            }

            orderXProductStmt.executeBatch(); // Exécuter toutes les requêtes `orderxproduct` d’un coup

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
}
