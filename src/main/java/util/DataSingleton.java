package util;

import dao.ClientDAO;
import dao.ProductDAO;
import tables.Catalog;

public class DataSingleton {
    private static DataSingleton instance;
    private final Catalog catalog;
    private final ProductDAO productDAO;
    private final ClientDAO clientDAO;

    private DataSingleton() {
        this.productDAO = new ProductDAO();
        this.clientDAO = new ClientDAO();
        this.catalog = productDAO.getAllProducts();
    }

    public static DataSingleton getInstance() {
        if (instance == null) {
            synchronized (DataSingleton.class) {
                if (instance == null) {
                    instance = new DataSingleton();
                }
            }
        }
        return instance;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public ClientDAO getClientDAO() {
        return clientDAO;
    }

}
