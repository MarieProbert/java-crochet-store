package tables;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import enums.Status;

/**
 * Represents an order placed by a client, including its ID, client information, status, timestamps, and cart items.
 */
public class Order {

    private int orderID;
    private int clientID;
    private Status status;
    private Timestamp purchaseDate;
    private Timestamp deliveryDate;
    private Map<Product, CartItem> cart;

    /**
     * Default constructor initializing an order with default values.
     */
    public Order() {
        this.orderID = -1;
        this.clientID = -1;
        this.status = Status.INPROGRESS;
        this.cart = new HashMap<>();
        this.purchaseDate = null;
        this.deliveryDate = null;
    }

    // Getters and setters

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
 
    public Status getStatus() {
        return status;
    }
 
    public void setStatus(Status status) {
        this.status = status;
    }
 
    /**
     * Sets the order's status using a string representation.
     *
     * @param statusStr the string representing the status
     */
    public void setStatusFromString(String statusStr) {
        this.status = Status.fromStringToStatus(statusStr);
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

    // Business logic methods

    /**
     * Adds a product to the cart.
     *
     * @param product  the product to add
     * @param quantity the quantity to add
     */
    public boolean addToCart(Product product, int quantity) {
        int currentStock = product.getStock();

        if (currentStock <= 0 || currentStock - quantity < 0) {
            return false;
        }

        if (cart.containsKey(product)) {
            CartItem existingItem = cart.get(product);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            cart.put(product, new CartItem(quantity, product.getPrice()));
        }

        product.setStock(currentStock - quantity);
        return true;
    }

    /**
     * Adds a product with a finalized price to the cart.
     *
     * @param product  the product to add
     * @param quantity the quantity to add
     * @param price    the price at the time of purchase
     */
    public void addToFinalisedCart(Product product, int quantity, double price) {
        cart.put(product, new CartItem(quantity, price));
    }

    /**
     * Removes a specified quantity of a product from the cart.
     *
     * @param product  the product to remove
     * @param quantity the quantity to remove
     */
    public boolean deleteFromCart(Product product, int quantity) {
        if (!cart.containsKey(product)) {
            return false;
        }

        CartItem item = cart.get(product);
        int currentQuantity = item.getQuantity();

        if (quantity >= currentQuantity) {
            cart.remove(product);
        } else {
            item.setQuantity(currentQuantity - quantity);
        }

        product.setStock(product.getStock() + quantity);
        return true;
    }

    /**
     * Calculates the total cost of the items in the cart.
     *
     * @return the total cost
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
