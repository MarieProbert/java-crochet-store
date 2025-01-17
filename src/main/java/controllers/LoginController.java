package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import managers.SceneManager;
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
    	bannerImage.setImage(loadImage(bannerPath, defaultImagePath));
    }

    // Vérifie si le login est bon, si c'est le cas créé le Client dans java (à modifier pour faire admin aussi)
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        errorLabel.setText(""); 
        
        if (!clientDAO.isLoginValid(email, password)) {
        	errorLabel.setText("Invalid username or password.");
        } else {
        	userManager.setUser(clientDAO.setClient(email));
        	order.setClientID(userManager.getUser().getId());
        	SceneManager.getInstance().showScene("Catalog");
        }
    }
    
    @FXML
    private void handleGuest() {
    	SceneManager.getInstance().showScene("Catalog");
    }
    
    
    @FXML
    private void handleRegister() {
    	errorLabel.setText(""); 
        try {
            // Charger le fichier FXML pour Création de compte
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AccountCreationView.fxml"));
            Parent root = loader.load();

            // Remplacer la scène actuelle
            Stage stage = (Stage) emailField.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (Exception e) {
        	e.printStackTrace();
        	errorLabel.setText("There was an issue with the registration. Please restart the app or continue as a guest.");;
        }
    }
   

}
