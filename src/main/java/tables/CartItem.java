package tables;

/**
 * Represents an item in the shopping cart, including quantity and the price at the time of purchase.
 */
public class CartItem {

    private int quantity;
    private double priceAtPurchase;

    /**
     * Constructor to initialize a cart item.
     *
     * @param quantity         the quantity of the product
     * @param priceAtPurchase  the price at the time of purchase
     */
    public CartItem(int quantity, double priceAtPurchase) {
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    public int getQuantity() {
        return quantity;
    }
 
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
 
    public double getPriceAtPurchase() {
        return priceAtPurchase;
    }
 
    public void setPriceAtPurchase(double priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }
}
