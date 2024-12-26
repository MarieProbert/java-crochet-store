package tables;

import java.util.ArrayList;
import java.util.List;

public class Catalog {
	
	private static Catalog instance;

    private List<Product> products;

    private Catalog() {
        products = new ArrayList<>();
    }

    // pourquoi s'écrit comme ça ? 
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

    public List<Product> getProducts() {
        return products;
    }
    
    public void addProduct(Product product) {
    	products.add(product);
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	for (Product p : products) {
    		sb.append(p.toString());
    		sb.append("\n");
    	}
    	String result = sb.toString();
    	return result;
    }
    

}
