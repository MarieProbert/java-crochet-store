package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tables.Order;
import util.DataSingleton;
import util.SceneManager;
import util.UserSession;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;


public class LoginController extends BaseController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    
    @FXML
    public void initialize() {
    	super.initialize();
    }

    // Vérifie si le login est bon, si c'est le cas créé le Client dans java (à modifier pour faire admin aussi)
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        errorLabel.setText(""); 
        
        if (!DataSingleton.getInstance().getUserDAO().isLoginValid(email, password)) {
        	errorLabel.setText("Invalid username or password.");
        } else {
        	UserSession.getInstance().setUser(DataSingleton.getInstance().getUserDAO().setUser(email));
        	
        	System.out.println("je suis ici");
        	// Si on est un client on a un panier et en + on arrive sur la page du catalogue (ou du récapitulatif de commande)
        	if ("client".equals(UserSession.getInstance().getUser().getRole())){
        		System.out.println("je suis là");
        		UserSession.getInstance().getOrder().setClientID(UserSession.getInstance().getUser().getId());
            	SceneManager.getInstance().showScene(
            		    UserSession.getInstance().isValidate() ? "OrderSummary" : "Catalog"
            	);
        	}
        	// l'admin arrive sur des pages admin
        	else {
        		// On affiche l'interface de l'admin
        	}

        }
    }
    
    @FXML
    private void handleGuest() {
    	SceneManager.getInstance().showScene("Catalog");
    }
    
    
    @FXML
    private void handleRegister() {
    	SceneManager.getInstance().showScene("AccountCreation");
    }
   

}
