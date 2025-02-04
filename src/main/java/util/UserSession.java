package util;


import tables.Invoice;
import tables.Order;
import tables.User;

public class UserSession {
    private static UserSession instance;

    // Variables pour l'utilisateur et son panier
    private User user;
    private Order order;
    private Invoice invoice;
    private boolean validate;

    // Constructeur privé pour empêcher l'instanciation extérieure
    private UserSession() {
        order = new Order();
        user = new User();
        validate = false;
        invoice = new Invoice();
    }

    // Méthode pour récupérer l'instance unique de UserSession (Singleton)
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    public void setUser(User user) {
        // Remplace l'utilisateur actuel par un nouvel utilisateur
        this.user = user; 
    }
    
    public User getUser() {
        return user;
    }


	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	// Est validate si l'utilisateur veut valider sa commande et qu'il faudra donc adapter la fenêtre après le login
	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	
	
    
    
}
