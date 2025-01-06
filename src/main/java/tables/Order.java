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
    private Map<Integer, Integer> map = new HashMap<>();

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
    public Map<Integer, Integer> getMap() {
        return map;
    }

    /**
     * Sets the map of products and their quantities.
     *
     * @param map A map with product references as keys and quantities as values.
     */
    public void setMap(Map<Integer, Integer> map) {
        this.map = map;
    }
}
