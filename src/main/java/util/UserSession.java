package util;

import tables.Invoice;
import tables.Order;
import tables.User;

public class UserSession {
    private static UserSession instance;

    // Variables for the user and their cart
    private User user;
    private Order order;
    private Invoice invoice;
    private boolean validate;

    /**
     * Private constructor to prevent external instantiation.
     */
    private UserSession() {
        order = new Order();
        user = new User();
        validate = false;
        invoice = new Invoice();
    }

    /**
     * Method to retrieve the unique instance of UserSession (Singleton).
     * 
     * @return The unique instance of UserSession.
     */
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    /**
     * Replaces the current user with a new user.
     * 
     * @param user The new user to set.
     */
    public void setUser(User user) {
        this.user = user; 
    }
    
    /**
     * Retrieves the current user.
     * 
     * @return The current user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Retrieves the current order.
     * 
     * @return The current order.
     */
	public Order getOrder() {
		return order;
	}

    /**
     * Sets a new order.
     * 
     * @param order The new order to set.
     */
	public void setOrder(Order order) {
		this.order = order;
	}

    /**
     * Checks if the user wants to validate their order and if the window should be adapted after login.
     * 
     * @return true if the order is to be validated, false otherwise.
     */
	public boolean isValidate() {
		return validate;
	}

    /**
     * Sets the validation state for the order.
     * 
     * @param validate The validation state to set.
     */
	public void setValidate(boolean validate) {
		this.validate = validate;
	}

    /**
     * Retrieves the current invoice.
     * 
     * @return The current invoice.
     */
	public Invoice getInvoice() {
		return invoice;
	}

    /**
     * Sets a new invoice.
     * 
     * @param invoice The new invoice to set.
     */
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
}
