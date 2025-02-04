package util;

import dao.UserDAO;
import dao.InvoiceDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import tables.Catalog;

public class DataSingleton {
    private static DataSingleton instance;
    private final Catalog catalog;
    private final ProductDAO productDAO;
    private final OrderDAO orderDAO;
    private final UserDAO userDAO;
    private final InvoiceDAO invoiceDAO;

    private DataSingleton() {
        this.productDAO = new ProductDAO();
        this.userDAO = new UserDAO();
        this.orderDAO = new OrderDAO();
        this.catalog = productDAO.getAllProducts();
        this.invoiceDAO = new InvoiceDAO();
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
    
    public OrderDAO getOrderDAO() {
    	return orderDAO;
    }

	public InvoiceDAO getInvoiceDAO() {
		return invoiceDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}
	

}
