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
 * Manages form submission, input validation, and navigation to the login screen.
 */
public class AccountCreationController extends BaseController {

    @FXML 
    private TextField emailField;
    
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

    /**
     * Initializes the account creation controller.
     */
    @FXML
    public void initialize() {
        super.initialize();
    }

    /**
     * Handles the account creation form submission.
     * Validates the input and creates a new user if the input is valid.
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

        String errorMessage = ValidationUtils.verifySubmission(email, password, firstName, lastName, street, city, postCode, country);
        if (errorMessage != null) {
            showErrorMessage(errorMessage);
            return;
        }

        User user = createClient(email, password, firstName, lastName, street, city, postCode, country);
        DataSingleton.getInstance().getUserDAO().insertUser(user);

        handleReturn();
    }

    /**
     * Creates a new User object with the client role.
     *
     * @param email     the user's email
     * @param password  the user's password
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param street    the user's street address
     * @param city      the user's city
     * @param postCode  the user's postal code
     * @param country   the user's country
     * @return a new User object
     */
    private User createClient(String email, String password, String firstName, String lastName,
                              String street, String city, String postCode, String country) {
        User user = new User(email, password, firstName, lastName, "client");
        user.setAddress(new Address(street, city, postCode, country, user));
        return user;
    }

    /**
     * Navigates back to the login screen.
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
