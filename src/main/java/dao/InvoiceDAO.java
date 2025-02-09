package dao;

import tables.Invoice;
import tables.Order;
import tables.User;
import util.DataSingleton;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Data Access Object (DAO) class for managing invoice-related database operations.
 * Provides method to insert an invoice into the database.
 */
public class InvoiceDAO {

    // SQL query to insert an invoice
    private static final String INSERT_INVOICE_QUERY = 
        "INSERT INTO invoice (orderID, userID, invoiceDate, totalAmount) VALUES (?, ?, ?, ?)";

    /**
     * Inserts an invoice into the database.
     * 
     * @param invoice The invoice object to insert.
     * @return The generated invoice ID if insertion is successful, or -1 if an error occurs.
     */
    public int insertInvoice(Invoice invoice) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;

        try {
            // Get a connection to the database
            conn = DatabaseConnection.getConnection();

            // Prepare the SQL query with RETURN_GENERATED_KEYS to retrieve the generated ID
            pstmt = conn.prepareStatement(INSERT_INVOICE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);

            // Set the parameters for the query
            pstmt.setInt(1, invoice.getOrderID());
            pstmt.setInt(2, invoice.getClientID());
            pstmt.setTimestamp(3, new java.sql.Timestamp(invoice.getInvoiceDate().getTime()));
            pstmt.setDouble(4, invoice.getTotalAmount());

            // Execute the query
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to insert invoice, no rows affected.");
            }

            // Retrieve the generated invoice ID
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int invoiceID = generatedKeys.getInt(1);
                invoice.setInvoiceID(invoiceID); // Update the invoice object with the generated ID
                return invoiceID; // Return the generated invoice ID
            } else {
                throw new SQLException("Failed to obtain the invoice ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 in case of error
        } finally {
            // Close resources
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
    
    public Invoice getInvoiceByOrderId(int orderId) {
        Invoice invoice = null;
        String sql = "SELECT * FROM Invoice WHERE orderID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                invoice = new Invoice();
                invoice.setInvoiceID(rs.getInt("invoiceID")); 
                invoice.setOrderID(rs.getInt("orderID"));
                invoice.setClientID(rs.getInt("userID"));
                invoice.setInvoiceDate(rs.getTimestamp("invoiceDate"));
                invoice.setTotalAmount(rs.getDouble("totalAmount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return invoice;
    }
    
    public Invoice getOrCreateInvoiceByOrderId(int orderId) {
        Invoice invoice = getInvoiceByOrderId(orderId);
        
        // Si la facture n'existe pas encore, on la crée.
        if (invoice == null) {
            // Récupérer la commande correspondante
            Order order = DataSingleton.getInstance().getOrderDAO().getOrderByOrderID(orderId);
            if (order == null) {
                System.err.println("Aucune commande trouvée pour l'orderID : " + orderId);
                return null;
            }
            
            // Récupérer l'utilisateur correspondant à la commande
            User user = DataSingleton.getInstance().getUserDAO().getUserById(order.getClientID());
            if (user == null) {
                System.err.println("Aucun utilisateur trouvé pour l'orderID : " + orderId);
                return null;
            }
            
            // Calculer le montant total de la commande
            double totalAmount = order.calculateCartTotal();
            
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            
            // Créer la nouvelle facture
            invoice = new Invoice();
            invoice.setOrderID(order.getOrderID());
            invoice.setClientID(order.getClientID());
            invoice.setInvoiceDate(currentTimestamp); // Date de création = maintenant
            invoice.setTotalAmount(totalAmount);
            
            // Persister la facture dans la base de données
            insertInvoice(invoice);
        }
        
        return invoice;
    }
    
    public boolean deleteInvoiceByOrderID(int orderId) {
        String query = "DELETE FROM invoice WHERE orderID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, orderId);
            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0; // Retourne true si au moins une ligne a été supprimée

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retourne false en cas d'erreur ou si aucune facture n'a été supprimée
    }



}
