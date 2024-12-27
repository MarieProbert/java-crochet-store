package managers;

import java.util.List;

import tables.Catalog;
import tables.Product;


public class CatalogManager {
	private Catalog catalog;
	
	public CatalogManager() {
		this.catalog = Catalog.getInstance();
	}
	
	public void addToCatalog(Product product) {
		catalog.addProduct(product);
	}
	
	public List<Product> getProducts() {
		return catalog.getProducts();
	}
	
	@Override
	public String toString() {
		System.out.println("Items in the catalog : ");
		StringBuilder sb = new StringBuilder();
    	sb.append(catalog.toString());
    	String result = sb.toString();
    	return result;

	}

}

