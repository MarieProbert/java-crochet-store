package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import util.DataSingleton;
import util.SceneManager;
import util.UserSession;
import javafx.scene.control.PasswordField;

/**
 * Controller class responsible for managing the login screen.
 * Handles the login functionality, guest login, and user registration navigation.
 */
public class LoginController extends BaseController {
    
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    
    /**
     * Initializes the LoginController by calling the parent class' initialize method.
     */
    @FXML
    public void initialize() {
        super.initialize();
    }

    /**
     * Validates the login credentials entered by the user. If the login is valid, it proceeds
     * to load the appropriate user interface based on the user role.
     * 
     * If the user is a client, it loads either the order summary or the product catalog.
     * If the user is an admin, it loads the admin menu.
     */
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        
        // Check if the login credentials are valid
        if (!DataSingleton.getInstance().getUserDAO().isLoginValid(email, password)) {
            showErrorMessage("Invalid username or password");
        } else {
            // Set the user session after successful login
            UserSession.getInstance().setUser(DataSingleton.getInstance().getUserDAO().setUser(email));
            
            // If the user is a client, they will be directed to the catalog or order summary page
            if ("client".equals(UserSession.getInstance().getUser().getRole())) {
                UserSession.getInstance().getOrder().setClientID(UserSession.getInstance().getUser().getId());
                SceneManager.getInstance().showScene(
                    UserSession.getInstance().isValidate() ? "OrderSummary" : "Catalog"
                );
            }
            // If the user is an admin, they will be directed to the admin menu
            else {
                SceneManager.getInstance().showScene("MenuAdmin");
            }
        }
    }

    /**
     * Handles guest login by redirecting to the product catalog.
     */
    @FXML
    private void handleGuest() {
        SceneManager.getInstance().showScene("Catalog");
    }

    /**
     * Redirects the user to the account creation page for registration.
     */
    @FXML
    private void handleRegister() {
        SceneManager.getInstance().showScene("AccountCreation");
    }
}
