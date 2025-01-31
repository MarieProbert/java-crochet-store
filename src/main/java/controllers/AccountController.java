package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tables.Client;
import util.SceneManager;
import util.UserSession;

public class AccountController extends BaseController {

	
    @FXML private Button searchCatalog;
    @FXML private Button searchCart;
    @FXML private Button searchAccount;
    
    @FXML private TextField emailField;
    //@FXML private PasswordField passwordField;
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
    	bannerImage.setImage(loadImage(bannerPath, defaultImagePath));
        // Récupérer l'utilisateur connecté
        c = (Client) UserSession.getInstance().getUser();

        // Afficher les valeurs actuelles dans les champs
        emailField.setText(c.getEmail());
        //passwordField.setText(c.getPassword());
        firstNameField.setText(c.getFirstName());
        lastNameField.setText(c.getLastName());
        streetField.setText(c.getStreet());
        cityField.setText(c.getCity());
        postCodeField.setText(String.valueOf(c.getPostCode())); // Convertir int en String
        countryField.setText(c.getCountry());
    }

    @FXML
    public void handleSave() {
        // Mettre à jour les informations du client avec les nouvelles valeurs saisies
        c.setEmail(emailField.getText());
        //c.setPassword(passwordField.getText());
        c.setFirstName(firstNameField.getText());
        c.setLastName(lastNameField.getText());
        c.setStreet(streetField.getText());
        c.setCity(cityField.getText());
        // Vérifier si postCode est un entier valide avant de l'enregistrer
        try {
            int postCode = Integer.parseInt(postCodeField.getText());
            c.setPostCode(postCode);
        } catch (NumberFormatException e) {
            errorLabel.setText("PostCode doit être un nombre valide !");
        }
        c.setCountry(countryField.getText());
    }
    
    
	@FXML
	public void handleCatalog() {
    	try {
            SceneManager.getInstance().showScene("Catalog");
        } catch (Exception e) {
            e.printStackTrace();
        }
    
	}
	
    @FXML
    private void handleCart() {
    	try {
    		System.out.println("4");
            SceneManager.getInstance().showScene("Cart");

            System.out.println("5");
            
        } catch (Exception e) {
        	System.out.println("erreur");
            e.printStackTrace();
        }

    }
    
    
    @FXML
    private void handleAccount() {
    	if (UserSession.getInstance().getUser().getId() == -1) {
    		try {
                SceneManager.getInstance().showScene("Login");
            } catch (Exception e) {
                e.printStackTrace();
            }
    	}
    	else {
    		try {
                SceneManager.getInstance().showScene("Account");
            } catch (Exception e) {
                e.printStackTrace();
            }
    	}
    }
}
