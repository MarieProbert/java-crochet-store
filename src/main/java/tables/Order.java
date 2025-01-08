package tables;

import java.util.HashMap;
import java.util.Map;

import enums.Color;
import enums.Status;

/**
 * Represents an order placed by a client, including its ID, client information, and a list of purchased products.
 */
public class Order {

    /** Unique identifier for the order (primary key). */
    private int orderID;

    /** Identifier for the client who placed the order (foreign key). */
    private int clientID;

    /** Status of the order (e.g., in-progress, confirmed, delivered). */
    private Status status;

    /** Map storing product references and their quantities. */
    private Map<Integer, Integer> cart;

    // Debut de commande quand on n'a aucune informations de panier précédent en cours à récupérer
    // Et qu'on ne sait pas forcément qui est le client
    public Order() {
    	orderID = -1;
    	clientID = -1;
    	status = Status.INPROGRESS;
    	cart = new HashMap<>();
    }
    
    /**
     * Returns the unique order ID.
     *
     * @return The order ID.
     */
    public int getOrderID() {
        return orderID;
    }

    /**
     * Sets the unique order ID.
     *
     * @param orderID The new order ID.
     */
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    /**
     * Returns the client's ID associated with the order.
     *
     * @return The client's ID.
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * Sets the client's ID for the order.
     *
     * @param clientID The new client ID.
     */
    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    /**
     * Returns the status of the order.
     *
     * @return The order's status.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status of the order.
     *
     * @param status The new status of the order.
     */
    public void setStatus(Status status) {
        this.status = status;
    }
    
    /**
     * Sets the product's status from a string.
     * 
     * @param color The status string to be converted.
     */
    public void setStatusFromString(String status) {
        this.status = Status.fromStringToStatus(status);
    }

    /**
     * Returns the map of products and their quantities.
     *
     * @return A map with product references as keys and quantities as values.
     */
    public Map<Integer, Integer> getCart() {
        return cart;
    }

    /**
     * Sets the map of products and their quantities.
     *
     * @param map A map with product references as keys and quantities as values.
     */
    public void setCart(Map<Integer, Integer> cart) {
        this.cart = cart;
    }
    
    public void addToCart(Product product, int quantity) {

    	int currentStock = product.getStock();

        if (currentStock == 0) {
            System.out.println("Error : There is no stock.");
            return;
        }

        if (currentStock - quantity < 0) {
            System.out.println("Error : There is not enough stock for this quantity.");
            return;
        }

        cart.put(product.getProductID(), cart.getOrDefault(product.getProductID(), 0) + quantity);

        product.setStock(currentStock - quantity);

        System.out.println("Produit added to cart : " + product.getName() + " Quantity : " + quantity);
    }
    
}
