package tables;

import java.sql.Timestamp;

/**
 * Represents an invoice related to an order.
 */
public class Invoice {

    private int invoiceID;
    private int orderID;
    private int clientID;
    private Timestamp invoiceDate;
    private double totalAmount;

    /**
     * Default constructor.
     */
    public Invoice() {
    }

    /**
     * Constructor to create a new invoice without specifying an invoice ID.
     *
     * @param orderID     the ID of the related order
     * @param clientID    the ID of the client
     * @param invoiceDate the date of the invoice
     * @param totalAmount the total amount of the invoice
     */
    public Invoice(int orderID, int clientID, Timestamp invoiceDate, double totalAmount) {
        this.orderID = orderID;
        this.clientID = clientID;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
    }

    /**
     * Constructor with all parameters.
     *
     * @param invoiceID   the unique invoice ID
     * @param orderID     the ID of the related order
     * @param clientID    the ID of the client
     * @param invoiceDate the date of the invoice
     * @param totalAmount the total amount of the invoice
     */
    public Invoice(int invoiceID, int orderID, int clientID, Timestamp invoiceDate, double totalAmount) {
        this.invoiceID = invoiceID;
        this.orderID = orderID;
        this.clientID = clientID;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
    }

    // Getters and setters

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

    /**
     * Displays the invoice details.
     */
    public void displayInvoiceDetails() {
        System.out.println("Invoice ID: " + invoiceID);
        System.out.println("Order ID: " + orderID);
        System.out.println("Client ID: " + clientID);
        System.out.println("Invoice Date: " + invoiceDate);
        System.out.println("Total Amount: " + totalAmount);
    }

    /**
     * Returns a string representation of the invoice.
     *
     * @return a string containing the invoice details
     */
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
