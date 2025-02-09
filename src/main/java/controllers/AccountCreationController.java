package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tables.Address;
import tables.User;
import util.DataSingleton;
import util.SceneManager;
import util.ValidationUtils;

/**
 * Controller for handling user account creation.
 * Manages form submission, validation, and navigation to the login screen.
 */
public class AccountCreationController extends BaseController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField streetField;
    @FXML private TextField cityField;
    @FXML private TextField postCodeField;
    @FXML private TextField countryField;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        super.initialize();
    }

    /**
     * Handles the account creation form submission.
     * Validates input fields and creates a new user if validation passes.
     */
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

        // Validate input fields
        String errorMessage = ValidationUtils.verifySubmission(email, password, firstName, lastName, street, city, postCode, country);

        if (errorMessage != null) {
            showErrorMessage(errorMessage);
            return;
        }

        // Create and save the new user
        User user = createClient(email, password, firstName, lastName, street, city, postCode, country);
        DataSingleton.getInstance().getUserDAO().insertUser(user);

        // Navigate back to the login screen
        handleReturn();
    }

    /**
     * Creates a new User object with the client role with the provided details.
     *
     * @param email     User's email
     * @param password  User's password
     * @param firstName User's first name
     * @param lastName  User's last name
     * @param street    User's street address
     * @param city      User's city
     * @param postCode  User's postal code
     * @param country   User's country
     * @return A new User object
     */
    private User createClient(String email, String password, String firstName, String lastName,
                            String street, String city, String postCode, String country) {
        User user = new User(email, password, firstName, lastName, "client");
        user.setAddress(new Address(street, city, postCode, country, user));
        return user;
    }

    /**
     * Handles navigation back to the login screen.
     */
    @FXML
    private void handleReturn() {
    	
        try {
            SceneManager.getInstance().showScene("Login");
        } catch (Exception e) {
        	showErrorMessage("There was an issue loading the next scene.");
        }
    }
}