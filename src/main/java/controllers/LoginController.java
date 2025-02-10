package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import util.DataSingleton;
import util.SceneManager;
import util.UserSession;

/**
 * Controller class responsible for managing the login screen.
 * Handles login validation, guest login, and navigation to the registration screen.
 */
public class LoginController extends BaseController {

    @FXML 
    private TextField emailField;
    
    @FXML 
    private PasswordField passwordField;

    /**
     * Initializes the login controller.
     */
    @FXML
    public void initialize() {
        super.initialize();
    }

    /**
     * Validates the login credentials and loads the appropriate scene based on the user role.
     */
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (!DataSingleton.getInstance().getUserDAO().isLoginValid(email, password)) {
            showErrorMessage("Invalid username or password");
        } else {
            UserSession.getInstance().setUser(DataSingleton.getInstance().getUserDAO().setUser(email));

            if ("client".equals(UserSession.getInstance().getUser().getRole())) {
                UserSession.getInstance().getOrder().setClientID(UserSession.getInstance().getUser().getId());
                SceneManager.getInstance().showScene(
                        UserSession.getInstance().isValidate() ? "OrderSummary" : "Catalog"
                );
            } else {
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
     * Navigates to the account creation (registration) screen.
     */
    @FXML
    private void handleRegister() {
        SceneManager.getInstance().showScene("AccountCreation");
    }
}
