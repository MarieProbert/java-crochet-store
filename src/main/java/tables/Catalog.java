package tables;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a catalog of products available in the system.
 */
public class Catalog {

    /** List containing all the products in the catalog. */
    private List<Product> products;

    /**
     * Default constructor that initializes an empty product list.
     */
    public Catalog() {
        products = new ArrayList<>();
    }

    /**
     * Returns the list of products in the catalog.
     *
     * @return A list of {@link Product} objects.
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Adds a new product to the catalog.
     *
     * @param product The {@link Product} to be added to the catalog.
     */
    public void addProduct(Product product) {
        products.add(product);
    }

    /**
     * Returns a string representation of all the products in the catalog.
     *
     * @return A string containing details of all products in the catalog.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Items in the catalog:\n");
        for (Product product : products) {
            sb.append(product.toString()).append("\n");
        }
        return sb.toString();
    }
}
