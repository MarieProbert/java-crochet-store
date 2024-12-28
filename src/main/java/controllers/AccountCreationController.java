package controllers;

import java.util.regex.Pattern;

import dao.ClientDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tables.Client;

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
    	bannerImage.setImage(loadImage(bannerPath, defaultImagePath));
    }

    @FXML
    private void handleSubmit() {
    	String email = emailField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String street = streetField.getText();
        String city = cityField.getText();
        int postCode;
        String country = countryField.getText();

        // Initialize the error message as empty
        errorLabel.setText(""); 

        // Verify if there are empty fields
        if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
            street.isEmpty() || city.isEmpty() || postCodeField.getText().isEmpty() || country.isEmpty()) {
            errorLabel.setText("All the fields must be filled !");
            return;
        }
        
        // Verify the validity of an email
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
            
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regexPattern);
        java.util.regex.Matcher m = p.matcher(email);
        
        if (!m.matches()) {
            errorLabel.setText("The email format is invalid !");
            return;
        }

        // Verify the length of a password
        if (password.length() <= 8) {
            errorLabel.setText("The password must be longer than 8 characters !");
            return;
        }
        
        // Verify and convert the postcode
        try {
            postCode = Integer.parseInt(postCodeField.getText());
        } catch (NumberFormatException e) {
            errorLabel.setText("The postcode must be a number !");
            return;
        }
       

        // Add the client to the database
        Client client = new Client(email, password, firstName, lastName, street, city, postCode, country);
        ClientDAO clientDAO = new ClientDAO();
        clientDAO.insertClient(client);
        handleReturn();
    }
    
    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
          .matcher(emailAddress)
          .matches();
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
