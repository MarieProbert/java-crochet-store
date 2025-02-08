package util;

import dao.UserDAO;
import dao.InvoiceDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import tables.Catalog;

/**
 * Singleton class responsible for managing and providing access to various DAOs and the product catalog.
 */
public class DataSingleton {
    private static DataSingleton instance;
    private final Catalog catalog;
    private final ProductDAO productDAO;
    private final OrderDAO orderDAO;
    private final UserDAO userDAO;
    private final InvoiceDAO invoiceDAO;

    /**
     * Private constructor to initialize DAOs and catalog.
     */
    private DataSingleton() {
        this.productDAO = new ProductDAO();
        this.userDAO = new UserDAO();
        this.orderDAO = new OrderDAO();
        this.catalog = productDAO.getAllProducts();
        this.invoiceDAO = new InvoiceDAO();
    }

    /**
     * Returns the singleton instance of DataSingleton.
     * 
     * @return The singleton instance.
     */
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

    /**
     * Returns the product catalog.
     * 
     * @return The catalog of products.
     */
    public Catalog getCatalog() {
        return catalog;
    }

    /**
     * Returns the ProductDAO instance.
     * 
     * @return The ProductDAO instance.
     */
    public ProductDAO getProductDAO() {
        return productDAO;
    }

    /**
     * Returns the OrderDAO instance.
     * 
     * @return The OrderDAO instance.
     */
    public OrderDAO getOrderDAO() {
        return orderDAO;
    }

    /**
     * Returns the InvoiceDAO instance.
     * 
     * @return The InvoiceDAO instance.
     */
    public InvoiceDAO getInvoiceDAO() {
        return invoiceDAO;
    }

    /**
     * Returns the UserDAO instance.
     * 
     * @return The UserDAO instance.
     */
    public UserDAO getUserDAO() {
        return userDAO;
    }
}
