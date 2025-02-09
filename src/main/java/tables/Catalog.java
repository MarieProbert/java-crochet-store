package tables;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a catalog of available products.
 */
public class Catalog {

    private List<Product> products;

    /**
     * Default constructor initializing an empty catalog.
     */
    public Catalog() {
        this.products = new ArrayList<>();
    }

    /**
     * Returns the list of products in the catalog.
     *
     * @return the list of products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Adds a product to the catalog.
     *
     * @param product the product to add
     */
    public void addProduct(Product product) {
        products.add(product);
    }

    /**
     * Returns a string representation of the catalog.
     *
     * @return a string containing all products in the catalog
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Items in the catalog:\n");
        for (Product product : products) {
            sb.append(product.toString()).append("\n");
        }
        return sb.toString();
    }
}
