package controllers;

import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tables.Address;
import tables.User;
import util.DataSingleton;
import util.ValidationUtils;

/**
 * Controller for managing the admin client system.
 * Provides functionalities for displaying, modifying, deleting, and adding users.
 */
public class AdminClientsController extends BaseController {

    @FXML private GridPane clientGrid;
    @FXML private TextField searchField;

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField streetField;
    private TextField cityField;
    private TextField postCodeField;
    private TextField countryField;
    private Label errorLabel;

    @FXML
    public void initialize() {
        super.initialize();
        displayClients();
    }

    /**
     * Displays the list of clients in the grid.
     */
    private void displayClients() {
        clearMessage();
        clientGrid.getChildren().clear();
        int i = 0;

        List<User> userCatalog = DataSingleton.getInstance().getUserDAO().getAllUsers();

        for (User u : userCatalog) {
            VBox vbox = new VBox(5);
            vbox.setPadding(new Insets(10));

            Label roleLabel = new Label("Role: " + u.getRole());
            Label idLabel = new Label("ID: " + u.getId());
            Label firstNameLabel = new Label("First Name: " + u.getFirstName());
            Label lastNameLabel = new Label("Last Name: " + u.getLastName());

            Button modifyButton = new Button("Modify");
            modifyButton.setOnAction(e -> openModifyPopup(u));

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e -> deleteUser(u));

            HBox generalInfoBox = new HBox(5, roleLabel, idLabel);
            HBox nameBox = new HBox(5, firstNameLabel, lastNameLabel);
            HBox buttonBox = new HBox(5, modifyButton, deleteButton);
            HBox hbox = new HBox(10, generalInfoBox, nameBox, buttonBox);
            hbox.setPadding(new Insets(10));

            clientGrid.add(hbox, 0, i++);
        }
    }
    
    /**
     * Deletes the specified user after confirmation.
     * @param user The user to delete.
     */
    private void deleteUser(User user) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Delete Confirmation");
        confirmationDialog.setHeaderText("Are you sure you want to delete this user?");
        confirmationDialog.setContentText("This action is irreversible.");

        confirmationDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean isDeleted = DataSingleton.getInstance().getUserDAO().deleteUser(user);
                if (isDeleted) {
                    displayClients();
                }
            }
        });
    }

    /**
     * Opens a popup window to modify the specified user's information.
     * @param user The user to modify.
     */
    private void openModifyPopup(User user) {
        Stage popupStage = new Stage();
        popupStage.setTitle("Modify User");
        popupStage.initModality(Modality.APPLICATION_MODAL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red;");
        grid.add(errorLabel, 0, 0, 2, 1);

        // Non-editable fields
        grid.add(new Label("ID:"), 0, 1);
        grid.add(new Label(String.valueOf(user.getId())), 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(new Label(user.getEmail()), 1, 2);
        grid.add(new Label("Role:"), 0, 3);
        grid.add(new Label(user.getRole()), 1, 3);

        // Editable fields
        firstNameField = new TextField(user.getFirstName());
        lastNameField = new TextField(user.getLastName());
        grid.add(new Label("First Name:"), 0, 4);
        grid.add(firstNameField, 1, 4);
        grid.add(new Label("Last Name:"), 0, 5);
        grid.add(lastNameField, 1, 5);

        // If the user is a client, add address fields
        if ("client".equalsIgnoreCase(user.getRole())) {
            streetField = new TextField(user.getAddress().getStreet());
            cityField = new TextField(user.getAddress().getCity());
            postCodeField = new TextField(user.getAddress().getPostCode());
            countryField = new TextField(user.getAddress().getCountry());

            grid.add(new Label("Street:"), 0, 6);
            grid.add(streetField, 1, 6);
            grid.add(new Label("City:"), 0, 7);
            grid.add(cityField, 1, 7);
            grid.add(new Label("Post Code:"), 0, 8);
            grid.add(postCodeField, 1, 8);
            grid.add(new Label("Country:"), 0, 9);
            grid.add(countryField, 1, 9);
        }
        
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) {
                errorLabel.setText("First Name and Last Name are required.");
            } else {
                user.setFirstName(firstNameField.getText());
                user.setLastName(lastNameField.getText());

                if ("client".equalsIgnoreCase(user.getRole())) {
                    user.getAddress().setStreet(streetField.getText());
                    user.getAddress().setCity(cityField.getText());
                    user.getAddress().setPostCode(postCodeField.getText());
                    user.getAddress().setCountry(countryField.getText());
                }
                handleSave(user);
            }
        });
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> popupStage.close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        grid.add(buttonBox, 1, 10);
        
        Scene scene = new Scene(grid);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    /**
     * Saves the modified user information.
     * @param user The user to update.
     */
    private void handleSave(User user) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String errorMessage;

        if ("client".equalsIgnoreCase(user.getRole())) {
            String street = streetField.getText();
            String city = cityField.getText();
            String postCode = postCodeField.getText();
            String country = countryField.getText();
            errorMessage = ValidationUtils.verifyModifications(firstName, lastName, street, city, postCode, country);
        } else {
            errorMessage = ValidationUtils.verifyModifications(firstName, lastName);
        }

        if (errorMessage != null) {
            showErrorMessage(errorMessage);
            return;
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);
        if ("client".equalsIgnoreCase(user.getRole())) {
            user.getAddress().setStreet(streetField.getText());
            user.getAddress().setCity(cityField.getText());
            user.getAddress().setPostCode(postCodeField.getText());
            user.getAddress().setCountry(countryField.getText());
        }
        boolean updateSuccess = DataSingleton.getInstance().getUserDAO().updateUser(user);
        if (updateSuccess) {
            showInfoMessage("Modifications have been saved successfully!");
        } else {
            errorLabel.setText("Error updating account!");
            user = DataSingleton.getInstance().getUserDAO().setUser(user.getEmail());
        }
        displayClients();
    }
    
    /**
     * Opens a popup window to add a new user.
     */
    @FXML
    public void handleAddUser() {
        Stage addUserStage = new Stage();
        addUserStage.setTitle("Add User");
        addUserStage.initModality(Modality.APPLICATION_MODAL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red;");
        grid.add(errorLabel, 0, 0, 2, 1);

        grid.add(new Label("Email:"), 0, 1);
        TextField emailField = new TextField();
        grid.add(emailField, 1, 1);

        grid.add(new Label("Password:"), 0, 2);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        grid.add(new Label("Role:"), 0, 3);
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("client", "admin");
        roleComboBox.setValue("client");
        grid.add(roleComboBox, 1, 3);

        grid.add(new Label("First Name:"), 0, 4);
        TextField firstNameField = new TextField();
        grid.add(firstNameField, 1, 4);

        grid.add(new Label("Last Name:"), 0, 5);
        TextField lastNameField = new TextField();
        grid.add(lastNameField, 1, 5);

        // Address fields for client role
        grid.add(new Label("Street:"), 0, 6);
        TextField streetField = new TextField();
        grid.add(streetField, 1, 6);

        grid.add(new Label("City:"), 0, 7);
        TextField cityField = new TextField();
        grid.add(cityField, 1, 7);

        grid.add(new Label("Post Code:"), 0, 8);
        TextField postCodeField = new TextField();
        grid.add(postCodeField, 1, 8);

        grid.add(new Label("Country:"), 0, 9);
        TextField countryField = new TextField();
        grid.add(countryField, 1, 9);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(e -> {
            if (emailField.getText().isEmpty() ||
                passwordField.getText().isEmpty() ||
                firstNameField.getText().isEmpty() ||
                lastNameField.getText().isEmpty()) {
                errorLabel.setText("Email, Password, First Name and Last Name are required.");
                return;
            }

            String role = roleComboBox.getValue();
            if ("client".equalsIgnoreCase(role)) {
                if (streetField.getText().isEmpty() || cityField.getText().isEmpty() ||
                    postCodeField.getText().isEmpty() || countryField.getText().isEmpty()) {
                    errorLabel.setText("All address fields are required for a client.");
                    return;
                }
            }

            User newUser = new User();
            newUser.setEmail(emailField.getText());
            newUser.setPassword(passwordField.getText());
            newUser.setRole(role);
            newUser.setFirstName(firstNameField.getText());
            newUser.setLastName(lastNameField.getText());

            if ("client".equalsIgnoreCase(role)) {
                Address address = new Address();
                address.setStreet(streetField.getText());
                address.setCity(cityField.getText());
                address.setPostCode(postCodeField.getText());
                address.setCountry(countryField.getText());
                newUser.setAddress(address);
            }

            int insertSuccess = DataSingleton.getInstance().getUserDAO().insertUser(newUser);
            if (insertSuccess != -1) {
                displayClients();
                addUserStage.close();
            } else {
                errorLabel.setText("Error adding user. Please try again.");
            }
        });

        cancelButton.setOnAction(e -> addUserStage.close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        grid.add(buttonBox, 1, 10);

        Scene scene = new Scene(grid);
        addUserStage.setScene(scene);
        addUserStage.showAndWait();
    }
}
