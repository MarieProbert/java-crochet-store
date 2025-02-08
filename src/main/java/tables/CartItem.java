package tables;

/**
 * Represents an item in the shopping cart, including the quantity and the price at the time of purchase.
 */
public class CartItem {

    /** The quantity of the product in the cart. */
    private int quantity;

    /** The price of the product at the time of purchase. */
    private double priceAtPurchase;

    /**
     * Constructor to initialize a CartItem with a specific quantity and price at purchase.
     *
     * @param quantity The quantity of the product in the cart.
     * @param priceAtPurchase The price of the product at the time of purchase.
     */
    public CartItem(int quantity, double priceAtPurchase) {
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    /**
     * Returns the quantity of the product in the cart.
     *
     * @return The quantity of the product.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product in the cart.
     *
     * @param quantity The quantity to be set.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns the price of the product at the time of purchase.
     *
     * @return The price at purchase.
     */
    public double getPriceAtPurchase() {
        return priceAtPurchase;
    }

    /**
     * Sets the price of the product at the time of purchase.
     *
     * @param priceAtPurchase The price to be set.
     */
    public void setPriceAtPurchase(double priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }
}
