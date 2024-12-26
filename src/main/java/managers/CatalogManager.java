package managers;

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
	
	@Override
	public String toString() {
		System.out.println("Items in the catalog : ");
		StringBuilder sb = new StringBuilder();
    	sb.append(catalog.toString());
    	String result = sb.toString();
    	return result;

	}

}

