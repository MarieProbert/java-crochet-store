package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tables.User;
import util.DataSingleton;
import util.HashUtils;
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

    private User user;

    @FXML
    public void initialize() {
        super.initialize();

        // Récupérer l'utilisateur connecté
        user = UserSession.getInstance().getUser();

        // Afficher les valeurs actuelles dans les champs
        emailField.setText(user.getEmail());
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        
        streetField.setText(user.getAddress().getStreet());
        cityField.setText(user.getAddress().getCity());
        postCodeField.setText(user.getAddress().getPostCode()); 
        countryField.setText(user.getAddress().getCountry());

    }

    @FXML
    private void handleSave() {
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
            showErrorMessage(errorMessage);
            return;
        }
        
        // --- Mise à jour des informations de l'utilisateur ---
        user.setEmail(email);
        if (!password.isEmpty()) { // Mettre à jour le mot de passe seulement s'il est renseigné
            user.setPassword(HashUtils.sha256(password));
        }
        user.setFirstName(firstName);
        user.setLastName(lastName);
        
        // Mise à jour de l'adresse
        user.getAddress().setStreet(street);
        user.getAddress().setCity(city);
        user.getAddress().setPostCode(postCode);
        user.getAddress().setCountry(country);

        // --- Mise à jour des informations du user ---
        boolean updateSuccess = DataSingleton.getInstance().getUserDAO().updateUser(user);

        if (updateSuccess) {
            showInfoMessage("Les modifications ont été enregistrées avec succès !");
        } else {
            showErrorMessage("Erreur lors de la mise à jour du compte !");
        }
    }


}
