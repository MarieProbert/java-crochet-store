package controllers;

import dao.ClientDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tables.Client;

public class AccountCreationController {
	@FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField addressField;
    @FXML private TextField cityField;
    @FXML private TextField postCodeField;
    @FXML private TextField countryField;
    

    @FXML
    private void handleSubmit() {
    	String email = emailField.getText();
        String password = passwordField.getText();

        
        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Tous les champs doivent être remplis !");
            return;
        }

        Client client = new Client(email, password);
        ClientDAO clientDAO = new ClientDAO();
        clientDAO.insertClient(client);
        handleReturn();
    }
    
    
    @FXML
    private void handleReturn() {
    	try {
            // Charger le fichier FXML pour Création de compte
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
            Parent root = loader.load();

            // Remplacer la scène actuelle
            Stage stage = (Stage) emailField.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
