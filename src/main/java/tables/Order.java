package tables;

import java.sql.Timestamp;
import java.util.Date;
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
    
    private Timestamp purchaseDate; 
    private Timestamp deliveryDate; 

    /** Map storing product references and their quantities and price at purchase. */
    private Map<Product, CartItem> cart = new HashMap<>();

    // Debut de commande quand on n'a aucune informations de panier précédent en cours à récupérer
    // Et qu'on ne sait pas forcément qui est le client
    public Order() {
    	orderID = -1;
    	clientID = -1;
    	status = Status.INPROGRESS;
    	cart = new HashMap<>();
    	isUpdated = new SimpleBooleanProperty(true);
    	purchaseDate = null;
    	deliveryDate = null;
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
    
    
    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Map<Product, CartItem> getCart() {
        return cart;
    }

    public void setCart(Map<Product, CartItem> cart) {
        this.cart = cart;
    }

    
    public void deleteFromCart(Product product, int quantity) {
        if (!cart.containsKey(product)) {
            System.out.println("Error: Product not found in cart.");
            return;
        }

        CartItem item = cart.get(product);
        int currentQuantity = item.getQuantity();

        if (quantity >= currentQuantity) {
            // Supprimer complètement l'élément si la quantité devient 0 ou négative
            cart.remove(product);
        } else {
            // Réduire seulement la quantité du produit
            item.setQuantity(currentQuantity - quantity);
        }

        // Rétablir le stock du produit
        product.setStock(product.getStock() + quantity);
        setUpdated(true);

        System.out.println("Removed from cart: " + product.getName() + " Quantity: " + quantity);
    }


    public void addToCart(Product product, int quantity) {
        int currentStock = product.getStock();

        if (currentStock <= 0) {
            System.out.println("Error: No stock available.");
            return;
        }

        if (currentStock - quantity < 0) {
            System.out.println("Error: Not enough stock.");
            return;
        }

        // Vérifier si le produit est déjà dans le panier
        if (cart.containsKey(product)) {
            CartItem existingItem = cart.get(product);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            // Enregistrer le prix du produit au moment de l'ajout
            cart.put(product, new CartItem(quantity, product.getPrice()));
        }

        product.setStock(currentStock - quantity);
        System.out.println("Added to cart: " + product.getName() + " Quantity: " + quantity);
    }

    // Calculer le total du panier en fonction des prix enregistrés au moment de l'ajout
    public double calculateCartTotal() {
        double total = 0;

        for (Map.Entry<Product, CartItem> entry : cart.entrySet()) {
            CartItem item = entry.getValue();
            total += item.getPriceAtPurchase() * item.getQuantity();
        }

        return total;
    }

    
}
