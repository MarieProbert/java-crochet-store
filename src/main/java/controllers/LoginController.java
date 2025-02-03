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
        
        if (!DataSingleton.getInstance().getClientDAO().isLoginValid(email, password)) {
        	errorLabel.setText("Invalid username or password.");
        } else {
        	UserSession.getInstance().setUser(DataSingleton.getInstance().getClientDAO().setClient(email));
        	UserSession.getInstance().getOrder().setClientID(UserSession.getInstance().getUser().getId());
        	
        	/*
        	Order o = DataSingleton.getInstance().getOrderDAO().getInProgressOrderByClientID(UserSession.getInstance().getUser().getId());
        	System.out.println("is it good ?");
        	System.out.println(o.getCart().toString());
        	if (o != null) {
        		System.out.println("yeah");
        		UserSession.getInstance().setOrder(o);
        		System.out.println(UserSession.getInstance().getOrder().getCart().toString());
        	}
        	*/
        	
        	SceneManager.getInstance().showScene(
        		    UserSession.getInstance().isValidate() ? "OrderSummary" : "Catalog"
        	);

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
