package controllers;

import dao.ClientDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.DatabaseConnection;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;


public class LoginController extends BaseController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    
    private ClientDAO clientDAO;

    public LoginController() {
        // Initialisation du ClientDAO avec une connexion à la base de données
        this.clientDAO = new ClientDAO(); // Assure-toi d'avoir une classe DBConnection qui gère la connexion
    }

    @FXML
    private void handleLogin() {
        String username = emailField.getText();
        String password = passwordField.getText();
        if (!clientDAO.isLoginValid(username, password)) {
        	System.out.println("Invalid username or password.");
        } else {
            System.out.println("Login successful");
            goToCatalog();
        }
    }
    
    @FXML
    private void handleRegister() {
        try {
            // Charger le fichier FXML pour Création de compte
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AccountCreationView.fxml"));
            Parent root = loader.load();

            // Remplacer la scène actuelle
            Stage stage = (Stage) emailField.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void goToCatalog() {
    	try {
            // Charger le fichier FXML pour Création de compte
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CatalogView.fxml"));
            Parent root = loader.load();

            // Remplacer la scène actuelle
            Stage stage = (Stage) emailField.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }

}
