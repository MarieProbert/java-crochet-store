package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tables.Client;
import util.DataSingleton;
import util.SceneManager;
import util.UserSession;
import util.ValidationUtils;

public class AccountController extends BaseController {


    
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField streetField;
    @FXML private TextField cityField;
    @FXML private TextField postCodeField;
    @FXML private TextField countryField;
    @FXML private Label errorLabel;

    private Client c;

    @FXML
    public void initialize() {
    	super.initialize();
        // Récupérer l'utilisateur connecté
        c = (Client) UserSession.getInstance().getUser();

        // Afficher les valeurs actuelles dans les champs
        emailField.setText(c.getEmail());
        firstNameField.setText(c.getFirstName());
        lastNameField.setText(c.getLastName());
        streetField.setText(c.getStreet());
        cityField.setText(c.getCity());
        postCodeField.setText(String.valueOf(c.getPostCode())); // Convertir int en String
        countryField.setText(c.getCountry());
    }

    @FXML
    public void handleSave() {
    	
    	 String email = emailField.getText();
    	    String password = passwordField.getText(); // Peut être vide
    	    String firstName = firstNameField.getText();
    	    String lastName = lastNameField.getText();
    	    String street = streetField.getText();
    	    String city = cityField.getText();
    	    String postCode = postCodeField.getText();
    	    String country = countryField.getText();

    	    // Vérification des modifications
    	    String errorMessage = ValidationUtils.verifyModifications(email, password, firstName, lastName, street, city, postCode, country);

    	    // Si une erreur est détectée, on l'affiche et on arrête l'exécution
    	    if (errorMessage != null) {
    	        errorLabel.setText(errorMessage);
    	        return;
    	    }

    	    // Effacer le message d'erreur si tout est valide
    	    errorLabel.setText("");

    	    // --- Mise à jour des informations du client ---
    	    c.setEmail(email);
    	    if (!password.isEmpty()) { // Mettre à jour le mot de passe seulement s'il est renseigné
    	        c.setPassword(password);
    	    }
    	    c.setFirstName(firstName);
    	    c.setLastName(lastName);
    	    c.setStreet(street);
    	    c.setCity(city);

    	    // Vérifier si postCode est un entier valide avant de l'enregistrer
    	    try {
    	        int numericPostCode = Integer.parseInt(postCode);
    	        c.setPostCode(numericPostCode);
    	    } catch (NumberFormatException e) {
    	        errorLabel.setText("PostCode doit être un nombre valide !");
    	        return;
    	    }
    	    
    	    c.setCountry(country);
    	
    	    // --- Mise à jour des informations du client ---
    	    boolean updateSuccess = DataSingleton.getInstance().getClientDAO().updateClient(c);

    	    if (updateSuccess) {
    	        System.out.println("Les modifications ont été enregistrées avec succès !");
    	    } else {
    	        errorLabel.setText("Erreur lors de la mise à jour du compte !");
    	    }
    	
    }
   

}
