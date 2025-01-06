package tables;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the catalog of products, implemented as a thread-safe singleton.
 */
public class Catalog {

    /** Singleton instance of the catalog. */
    private static Catalog instance;

    /** List of products in the catalog. */
    private List<Product> products;

    /**
     * Private constructor to initialize the product list.
     */
    private Catalog() {
        products = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of the catalog.
     * Uses double-checked locking for thread safety.
     *
     * @return The singleton instance of the catalog.
     */
    public static Catalog getInstance() {
        if (instance == null) {
            synchronized (Catalog.class) {
                if (instance == null) {
                    instance = new Catalog();
                }
            }
        }
        return instance;
    }

    /**
     * Returns the list of products in the catalog.
     *
     * @return A list of products.
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Adds a product to the catalog.
     *
     * @param product The product to be added.
     */
    public void addProduct(Product product) {
        products.add(product);
    }

    /**
     * Returns a string representation of all products in the catalog.
     *
     * @return A string containing details of all products.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Product p : products) {
            sb.append(p.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
