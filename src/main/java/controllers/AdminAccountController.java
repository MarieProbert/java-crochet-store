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
 * Controller for the admin account screen.
 * Allows an admin to update their account information.
 */
public class AdminAccountController extends BaseController {

    @FXML private Label emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;

    private User user;

    @FXML
    public void initialize() {
        super.initialize();
        user = UserSession.getInstance().getUser();
        emailField.setText(user.getEmail());
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
    }

    /**
     * Handles saving the admin's account modifications.
     */
    @FXML
    private void handleSave() {
        String email = emailField.getText();
        String password = passwordField.getText(); // May be empty
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        String errorMessage = ValidationUtils.verifyModifications(password, firstName, lastName);
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

        boolean updateSuccess = DataSingleton.getInstance().getUserDAO().updateUser(user);
        if (updateSuccess) {
            showInfoMessage("Modifications have been saved successfully!");
        } else {
            showErrorMessage("Error updating account!");
        }
    }
}
