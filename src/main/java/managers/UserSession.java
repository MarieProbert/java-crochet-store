package managers;



import tables.Order;

public class UserSession {
    private static UserSession instance;

    // Variables pour l'utilisateur et son panier
    private UserManager userManager;
    private Order order;
    private boolean validate;

    // Constructeur privé pour empêcher l'instanciation extérieure
    private UserSession() {
        order = new Order();
        userManager = new UserManager();
        validate = false;
    }

    // Méthode pour récupérer l'instance unique de UserSession (Singleton)
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
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
	
	
    
    
}
