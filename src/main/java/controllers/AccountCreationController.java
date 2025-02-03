package controllers;

import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tables.Client;
import util.DataSingleton;
import util.ValidationUtils;

public class AccountCreationController extends BaseController {
	@FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField streetField;
    @FXML private TextField cityField;
    @FXML private TextField postCodeField;
    @FXML private TextField countryField;
    @FXML private Label errorLabel;

    
    @FXML
    public void initialize() {
    	super.initialize();
    }

    
    @FXML
    private void handleSubmit() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String street = streetField.getText();
        String city = cityField.getText();
        String postCode = postCodeField.getText();
        String country = countryField.getText();

        // Appel de la méthode verifySubmission et récupération du message d'erreur
        String errorMessage = ValidationUtils.verifySubmission(email, password, firstName, lastName, street, city, postCode, country);

        // Si une erreur est détectée, on l'affiche et on arrête l'exécution
        if (errorMessage != null) {
            errorLabel.setText(errorMessage);
            return;
        }

        // Effacer le message d'erreur si tout est valide
        errorLabel.setText("");
        
        int intPostCode = Integer.parseInt(postCodeField.getText());
        // Add the client to the database
        Client client = new Client(email, password, firstName, lastName, street, city, intPostCode, country);
        DataSingleton.getInstance().getClientDAO().insertClient(client);
        handleReturn();
        
    }
    
    
    @FXML
    private void handleReturn() {
    	errorLabel.setText("");
    	try {
            // Charge the FXML file for login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
            Parent root = loader.load();

            // Replace the current scene
            Stage stage = (Stage) emailField.getScene().getWindow(); // Get the current window
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("There was an issue, please restart the app."); 
        }
    }
    
}
