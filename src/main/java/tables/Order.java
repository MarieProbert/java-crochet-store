package tables;

import java.util.HashMap;
import java.util.Map;

import enums.Status;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import util.SceneManager;

/**
 * Represents an order placed by a client, including its ID, client information, and a list of purchased products.
 */
public class Order {
	
	private BooleanProperty isUpdated;

    /** Unique identifier for the order (primary key). */
    private int orderID;

    /** Identifier for the client who placed the order (foreign key). */
    private int clientID;

    /** Status of the order (e.g., in-progress, confirmed, delivered). */
    private Status status;

    /** Map storing product references and their quantities. */
    private Map<Product, Integer> cart;

    // Debut de commande quand on n'a aucune informations de panier précédent en cours à récupérer
    // Et qu'on ne sait pas forcément qui est le client
    public Order() {
    	orderID = -1;
    	clientID = -1;
    	status = Status.INPROGRESS;
    	cart = new HashMap<>();
    	isUpdated = new SimpleBooleanProperty(true);
    }
    
    public BooleanProperty isUpdatedProperty() {
        return isUpdated;
    }

    public boolean isUpdated() {
        return isUpdated.get();
    }

    public void setUpdated(boolean updated) {
        isUpdated.set(updated);
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
    public Map<Product, Integer> getCart() {
        return cart;
    }

    /**
     * Sets the map of products and their quantities.
     *
     * @param map A map with product references as keys and quantities as values.
     */
    public void setCart(Map<Product, Integer> cart) {
        this.cart = cart;
    }
    
    public void deleteFromCart(Product product, int quantity) {
    	int currentStock = product.getStock();
         
        cart.put(product, cart.get(product) - quantity);
        
        if (cart.get(product) <= 0) {
        	cart.remove(product);
        }
        setUpdated(false);
        
        product.setStock(currentStock + quantity);

        setUpdated(true);
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

         
        cart.put(product, cart.getOrDefault(product, 0) + quantity);
        
        product.setStock(currentStock - quantity);

        System.out.println("Produit added to cart : " + product.getName() + " Quantity : " + quantity);
        
        System.out.println(getCart().keySet());
    }
    
}
