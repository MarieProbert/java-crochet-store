package tables;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import enums.Status;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Represents an order placed by a client, including its ID, client information, and a list of purchased products.
 */
public class Order {
	
    /** Indicates whether the order has been updated. */
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

    /**
     * Default constructor to initialize an order without prior cart or client information.
     * The order is initialized with default values.
     */
    public Order() {
        orderID = -1;
        clientID = -1;
        status = Status.INPROGRESS;
        cart = new HashMap<>();
        isUpdated = new SimpleBooleanProperty(true);
        purchaseDate = null;
        deliveryDate = null;
    }
    
    /**
     * Returns the updated property.
     *
     * @return The BooleanProperty representing if the order has been updated.
     */
    public BooleanProperty isUpdatedProperty() {
        return isUpdated;
    }

    /**
     * Returns whether the order is updated.
     *
     * @return A boolean value indicating if the order has been updated.
     */
    public boolean isUpdated() {
        return isUpdated.get();
    }

    /**
     * Sets the update status of the order.
     *
     * @param updated The updated status to be set.
     */
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
     * @param status The status string to be converted.
     */
    public void setStatusFromString(String status) {
        this.status = Status.fromStringToStatus(status);
    }

    /**
     * Returns the order's purchase date.
     *
     * @return The purchase timestamp of the order.
     */
    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Sets the order's purchase date.
     *
     * @param purchaseDate The purchase timestamp to be set.
     */
    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    /**
     * Returns the order's delivery date.
     *
     * @return The delivery timestamp of the order.
     */
    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Sets the order's delivery date.
     *
     * @param deliveryDate The delivery timestamp to be set.
     */
    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * Returns the map of products and their cart items in the order.
     *
     * @return The map of products and their corresponding cart items.
     */
    public Map<Product, CartItem> getCart() {
        return cart;
    }

    /**
     * Sets the map of products and their cart items for the order.
     *
     * @param cart The new map of products and cart items.
     */
    public void setCart(Map<Product, CartItem> cart) {
        this.cart = cart;
    }

    /**
     * Removes a product from the cart.
     *
     * @param product The product to be removed.
     * @param quantity The quantity to remove.
     */
    public void deleteFromCart(Product product, int quantity) {
        if (!cart.containsKey(product)) {
            System.out.println("Error: Product not found in cart.");
            return;
        }

        CartItem item = cart.get(product);
        int currentQuantity = item.getQuantity();

        if (quantity >= currentQuantity) {
            // Remove completely if quantity becomes 0 or negative
            cart.remove(product);
        } else {
            // Reduce quantity
            item.setQuantity(currentQuantity - quantity);
        }

        // Restore product stock
        product.setStock(product.getStock() + quantity);
        setUpdated(true);

        System.out.println("Removed from cart: " + product.getName() + " Quantity: " + quantity);
    }

    /**
     * Adds a product to the cart.
     *
     * @param product The product to be added.
     * @param quantity The quantity of the product to be added.
     */
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

        // Check if the product is already in the cart
        if (cart.containsKey(product)) {
            CartItem existingItem = cart.get(product);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            // Record the price of the product at the time of addition
            cart.put(product, new CartItem(quantity, product.getPrice()));
        }

        product.setStock(currentStock - quantity);
    }
    
    /**
     * Adds a product to the cart.
     *
     * @param product The product to be added.
     * @param quantity The quantity of the product to be added.
     * @param price The price at purchase of the product
     */
    public void addToFinalisedCart(Product product, int quantity, double price) {
        cart.put(product, new CartItem(quantity, price));
        
    }

    /**
     * Calculates the total cost of the cart, considering the prices at the time of addition.
     *
     * @return The total cart value.
     */
    public double calculateCartTotal() {
        double total = 0;

        for (Map.Entry<Product, CartItem> entry : cart.entrySet()) {
            CartItem item = entry.getValue();
            total += item.getPriceAtPurchase() * item.getQuantity();
        }

        return total;
    }
}
