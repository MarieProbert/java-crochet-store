package tables;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Invoice {
    private int invoiceID;
    private int orderID;
    private int clientID;
    private Timestamp invoiceDate;
    private double totalAmount;

    public Invoice() {
    	
    }
    // Constructeur sans ID de facture, utile lors de la création de nouvelles factures
    public Invoice(int orderID, int clientID, Timestamp invoiceDate, double totalAmount) {
        this.orderID = orderID;
        this.clientID = clientID;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
    }

    // Constructeur avec tous les paramètres, utilisé pour récupérer une facture existante de la base de données
    public Invoice(int invoiceID, int orderID, int clientID, Timestamp invoiceDate, double totalAmount) {
        this.invoiceID = invoiceID;
        this.orderID = orderID;
        this.clientID = clientID;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
    }

    // Getters et setters pour tous les attributs
    public int getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(int invoiceID) {
        this.invoiceID = invoiceID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public Timestamp getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Timestamp invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    // Méthode pour afficher les informations de la facture
    public void displayInvoiceDetails() {
        System.out.println("Invoice ID: " + invoiceID);
        System.out.println("Order ID: " + orderID);
        System.out.println("Client ID: " + clientID);
        System.out.println("Invoice Date: " + invoiceDate);
        System.out.println("Total Amount: " + totalAmount);
    }

    // ToString pour faciliter l'affichage sous forme de chaîne de caractères
    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceID=" + invoiceID +
                ", orderID=" + orderID +
                ", clientID=" + clientID +
                ", invoiceDate=" + invoiceDate +
                ", totalAmount=" + totalAmount +
                '}';
    }
}

