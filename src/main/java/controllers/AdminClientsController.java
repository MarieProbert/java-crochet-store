package controllers;

import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
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
import util.SceneManager;
import util.ValidationUtils;

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

    private void displayClients() {

        clientGrid.getChildren().clear();
        int i = 0;

        List<User> userCatalog = DataSingleton.getInstance().getUserDAO().getAllUsers();

        for (User u : userCatalog) {
        	System.out.println(u.getFirstName());
            VBox vbox = new VBox(5);
            vbox.setPadding(new Insets(10));

            Label roleLabel = new Label("Role : " + u.getRole());
            Label idLabel = new Label("ID : " + u.getId());
            Label firstNameLabel = new Label("First Name : " + u.getFirstName());
            Label lastNameLabel = new Label("Last Name : " + u.getLastName());

            Button modifyButton = new Button("Modify");
            modifyButton.setOnAction(e -> openModifyPopup(u)); // Appel de la méthode pour ouvrir le pop-up

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e -> deleteUser(u));

            HBox generalInfoBox = new HBox(5);
            generalInfoBox.getChildren().addAll(roleLabel, idLabel);

            HBox nameBox = new HBox(5);
            nameBox.getChildren().addAll(firstNameLabel, lastNameLabel);

            HBox buttonBox = new HBox(5);
            buttonBox.getChildren().addAll(modifyButton, deleteButton);

            HBox hbox = new HBox(10);
            hbox.setPadding(new Insets(10));
            hbox.getChildren().addAll(generalInfoBox, nameBox, buttonBox);

            clientGrid.add(hbox, 0, i);
            i++;
        }
    }
    
    private void deleteUser(User user) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation de suppression");
        confirmationDialog.setHeaderText("Êtes-vous sûr de vouloir supprimer cet utilisateur ?");
        confirmationDialog.setContentText("Cette action est irréversible.");

        // Attendre la réponse de l'utilisateur
        confirmationDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Si l'utilisateur confirme, supprimer l'utilisateur
                boolean isDeleted = DataSingleton.getInstance().getUserDAO().deleteUser(user);
                if (isDeleted) {
                    System.out.println("Utilisateur supprimé avec succès.");
                    displayClients(); // Rafraîchir la liste des utilisateurs
                } else {
                    System.out.println("La suppression a échoué.");
                }
            }
        }); 
    }

    private void openModifyPopup(User user) {
        
        Stage popupStage = new Stage();
        popupStage.setTitle("Modify User");
        
        // On définit le mode de la fenêtre pour qu'elle bloque l'interaction avec l'application principale
        popupStage.initModality(Modality.APPLICATION_MODAL);
        
        
        // Création du GridPane pour organiser les éléments
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Ajouter un Label pour les erreurs (initialement vide)
        errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red;"); // Optionnel : style pour les erreurs
        grid.add(errorLabel, 0, 0, 2, 1); // Span sur 2 colonnes

        // Ajouter les champs non modifiables (ID, email, rôle)
        grid.add(new Label("ID:"), 0, 1);
        grid.add(new Label(String.valueOf(user.getId())), 1, 1);

        grid.add(new Label("Email:"), 0, 2);
        grid.add(new Label(user.getEmail()), 1, 2);

        grid.add(new Label("Role:"), 0, 3);
        grid.add(new Label(user.getRole()), 1, 3);

        // Ajouter les champs modifiables (prénom, nom)
        firstNameField = new TextField(user.getFirstName());
        lastNameField = new TextField(user.getLastName());

        grid.add(new Label("First Name:"), 0, 4);
        grid.add(firstNameField, 1, 4);

        grid.add(new Label("Last Name:"), 0, 5);
        grid.add(lastNameField, 1, 5);

        // Si l'utilisateur est un client, ajouter les champs d'adresse
        if ("client".equalsIgnoreCase(user.getRole())) {
            streetField = new TextField(user.getAddress().getStreet()); // Initialisation de streetField
            cityField = new TextField(user.getAddress().getCity());     // Initialisation de cityField
            postCodeField = new TextField(user.getAddress().getPostCode()); // Initialisation de postCodeField
            countryField = new TextField(user.getAddress().getCountry());   // Initialisation de countryField


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
            // Valider les champs avant de sauvegarder
            if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) {
                errorLabel.setText("First Name and Last Name are required.");
            } else {
                // Mettre à jour les informations de l'utilisateur
                user.setFirstName(firstNameField.getText());
                user.setLastName(lastNameField.getText());

                if ("client".equalsIgnoreCase(user.getRole())) {
                    user.getAddress().setStreet(streetField.getText());
                    user.getAddress().setCity(cityField.getText());
                    user.getAddress().setPostCode(postCodeField.getText());
                    user.getAddress().setCountry(countryField.getText());
                }

                // Appeler la méthode handleSave pour sauvegarder les modifications
                handleSave(user);

            }
            
        });
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> popupStage.close());


        // On peut placer les boutons dans une HBox pour les aligner
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(saveButton, cancelButton);
        grid.add(buttonBox, 1, 10);
        
        // Création de la scène et affectation au Stage
        Scene scene = new Scene(grid);
        popupStage.setScene(scene);
        
        // Affichage du pop-up en mode bloquant
        popupStage.showAndWait();
    }

    private void handleSave(User user) {
    	
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        String errorMessage = null;

        if ("client".equalsIgnoreCase(user.getRole())) {
            String street = streetField.getText();
            String city = cityField.getText();
            String postCode = postCodeField.getText();
            String country = countryField.getText();
            
            errorMessage = ValidationUtils.verifyModifications(firstName, lastName, street, city, postCode, country);

        }
        else {
            errorMessage = ValidationUtils.verifyModifications(firstName, lastName);

        }

        // Si une erreur est détectée, on l'affiche et on arrête l'exécution
        if (errorMessage != null) {
            errorLabel.setText(errorMessage);
            return;
        }

        // Effacer le message d'erreur si tout est valide
        errorLabel.setText("");

        user.setFirstName(firstName);
        user.setLastName(lastName);

        // Si l'utilisateur est un client, on met aussi à jour son adresse
        if ("client".equalsIgnoreCase(user.getRole())) {

            // Mise à jour de l'adresse
            user.getAddress().setStreet(streetField.getText());
            user.getAddress().setCity(cityField.getText());
            user.getAddress().setPostCode(postCodeField.getText());
            user.getAddress().setCountry(countryField.getText());


        }
        // --- Mise à jour des informations du user ---
        boolean updateSuccess = DataSingleton.getInstance().getUserDAO().updateUser(user);

        if (updateSuccess) {
            System.out.println("Les modifications ont été enregistrées avec succès !");
        } else {
            errorLabel.setText("Erreur lors de la mise à jour du compte !");
            user = DataSingleton.getInstance().getUserDAO().setUser(user.getEmail());
        }
        displayClients(); // Rafraîchir la liste des utilisateurs après la modification
    }
    

    
    @FXML
    public void handleAddUser() {
        // Création d'un nouveau Stage pour le formulaire d'ajout d'utilisateur
        Stage addUserStage = new Stage();
        addUserStage.setTitle("Add User");
        addUserStage.initModality(Modality.APPLICATION_MODAL);

        // Création du GridPane pour organiser les éléments du formulaire
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Label d'erreur
        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red;");
        grid.add(errorLabel, 0, 0, 2, 1);

        // Champ Email
        grid.add(new Label("Email:"), 0, 1);
        TextField emailField = new TextField();
        grid.add(emailField, 1, 1);

        // Champ Password (PasswordField) juste en dessous de l'email
        grid.add(new Label("Password:"), 0, 2);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        // Liste déroulante pour le rôle (client ou admin)
        grid.add(new Label("Role:"), 0, 3);
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("client", "admin");
        roleComboBox.setValue("client");  // valeur par défaut si souhaité
        grid.add(roleComboBox, 1, 3);

        // Champ First Name
        grid.add(new Label("First Name:"), 0, 4);
        TextField firstNameField = new TextField();
        grid.add(firstNameField, 1, 4);

        // Champ Last Name
        grid.add(new Label("Last Name:"), 0, 5);
        TextField lastNameField = new TextField();
        grid.add(lastNameField, 1, 5);

        // Champs d'adresse (pour le cas d'un client)
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

        // Boutons Save et Cancel
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        // Action du bouton Save
        saveButton.setOnAction(e -> {
            // Vérification des champs obligatoires
            if (emailField.getText().isEmpty() ||
                passwordField.getText().isEmpty() ||
                firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) {
                errorLabel.setText("Email, Password, First Name and Last Name are required.");
                return;
            }

            // Récupération de la valeur du rôle
            String role = roleComboBox.getValue();
            // Si le rôle est "client", les champs d'adresse sont obligatoires
            if ("client".equalsIgnoreCase(role)) {
                if (streetField.getText().isEmpty() || cityField.getText().isEmpty() ||
                    postCodeField.getText().isEmpty() || countryField.getText().isEmpty()) {
                    errorLabel.setText("All address fields are required for a client.");
                    return;
                }
            }

            // Création d'un nouvel objet User
            User newUser = new User();
            newUser.setEmail(emailField.getText());
            // Vous pouvez ajouter ici un setter pour le mot de passe si nécessaire :
            newUser.setPassword(passwordField.getText());
            newUser.setRole(role);
            newUser.setFirstName(firstNameField.getText());
            newUser.setLastName(lastNameField.getText());

            // Si le rôle est "client", création et affectation de l'adresse
            if ("client".equalsIgnoreCase(role)) {
                // Remarque : adapter la création de l'adresse selon votre modèle
                tables.Address address = new tables.Address();
                address.setStreet(streetField.getText());
                address.setCity(cityField.getText());
                address.setPostCode(postCodeField.getText());
                address.setCountry(countryField.getText());
                newUser.setAddress(address);
            }

            // Insertion du nouvel utilisateur via le DAO
            int insertSuccess = DataSingleton.getInstance().getUserDAO().insertUser(newUser);
            if (insertSuccess != -1) {
                System.out.println("User added successfully!");
                displayClients();  // Rafraîchit la grille des utilisateurs
                addUserStage.close();
            } else {
                errorLabel.setText("Error adding user. Please try again.");
            }
        });

        // Action du bouton Cancel : ferme simplement le Stage
        cancelButton.setOnAction(e -> addUserStage.close());

        // Ajout des boutons dans une HBox
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(saveButton, cancelButton);
        grid.add(buttonBox, 1, 10);

        // Création de la scène et affichage du Stage
        Scene scene = new Scene(grid);
        addUserStage.setScene(scene);
        addUserStage.showAndWait();
    }

}