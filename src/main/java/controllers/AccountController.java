package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tables.User;
import util.DataSingleton;
import util.HashUtils;
import util.UserSession;
import util.ValidationUtils;

/**
 * Controller for managing the user account page.
 * Handles displaying and saving account modifications.
 */
public class AccountController extends BaseController {

    @FXML 
    private Label emailField;
    
    @FXML 
    private PasswordField passwordField;
    
    @FXML 
    private TextField firstNameField;
    
    @FXML 
    private TextField lastNameField;
    
    @FXML 
    private TextField streetField;
    
    @FXML 
    private TextField cityField;
    
    @FXML 
    private TextField postCodeField;
    
    @FXML 
    private TextField countryField;

    private User user;

    /**
     * Initializes the account controller and populates fields with the current user data.
     */
    @FXML
    public void initialize() {
        super.initialize();
        user = UserSession.getInstance().getUser();

        emailField.setText(user.getEmail());
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        streetField.setText(user.getAddress().getStreet());
        cityField.setText(user.getAddress().getCity());
        postCodeField.setText(user.getAddress().getPostCode());
        countryField.setText(user.getAddress().getCountry());
    }

    /**
     * Saves the modified account details.
     * Validates input and updates the user information.
     */
    @FXML
    private void handleSave() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String street = streetField.getText();
        String city = cityField.getText();
        String postCode = postCodeField.getText();
        String country = countryField.getText();

        String errorMessage = ValidationUtils.verifyModifications(email, password, firstName, lastName, street, city, postCode, country);
        if (errorMessage != null) {
            showErrorMessage(errorMessage);
            return;
        }

        user.setEmail(email);
        if (!password.isEmpty()) {
            String hashedPassword = HashUtils.sha256(password);
            if (hashedPassword == null) {
                showErrorMessage("Error during SHA-256 hashing");
                return;
            }
            user.setPassword(hashedPassword);
        }
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.getAddress().setStreet(street);
        user.getAddress().setCity(city);
        user.getAddress().setPostCode(postCode);
        user.getAddress().setCountry(country);

        boolean updateSuccess = DataSingleton.getInstance().getUserDAO().updateUser(user);
        if (updateSuccess) {
            showInfoMessage("Modifications have been saved successfully!");
        } else {
            showErrorMessage("Error updating account!");
        }
    }
}
