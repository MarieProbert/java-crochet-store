package tables;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the catalog of products.
 */
public class Catalog {

    /** List of products in the catalog. */
    private List<Product> products;

    /**
     * Constructor to initialize the product list.
     */
    public Catalog() {
        products = new ArrayList<>();
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
        System.out.println("Items in the catalog : ");
        StringBuilder sb = new StringBuilder();
        for (Product p : products) {
            sb.append(p.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
