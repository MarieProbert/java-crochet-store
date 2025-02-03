package dao;

import tables.Invoice;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InvoiceDAO {

    // Requête SQL pour insérer une facture
    private static final String INSERT_INVOICE_QUERY = 
        "INSERT INTO invoice (orderID, clientID, invoiceDate, totalAmount) VALUES (?, ?, ?, ?)";

    // Méthode pour insérer une facture dans la base de données
    public int insertInvoice(Invoice invoice) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;

        try {
            // Obtenir une connexion à la base de données
            conn = DatabaseConnection.getConnection();

            // Préparer la requête SQL avec RETURN_GENERATED_KEYS pour récupérer l'ID généré
            pstmt = conn.prepareStatement(INSERT_INVOICE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);

            // Remplir les paramètres de la requête
            pstmt.setInt(1, invoice.getOrderID());
            pstmt.setInt(2, invoice.getClientID());
            pstmt.setTimestamp(3, new java.sql.Timestamp(invoice.getInvoiceDate().getTime()));
            pstmt.setDouble(4, invoice.getTotalAmount());

            // Exécuter la requête
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Échec de l'insertion de la facture, aucune ligne affectée.");
            }

            // Récupérer l'ID généré pour la facture
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int invoiceID = generatedKeys.getInt(1);
                invoice.setInvoiceID(invoiceID); // Mettre à jour l'objet Invoice avec l'ID généré
                return invoiceID; // Retourner l'ID de la facture
            } else {
                throw new SQLException("Échec de l'obtention de l'ID de la facture.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Retourner -1 en cas d'erreur
        } finally {
            // Fermer les ressources
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
}