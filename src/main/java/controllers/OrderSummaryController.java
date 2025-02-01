package controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tables.Client;
import tables.Order;
import tables.Product;
import util.DataSingleton;
import util.SceneManager;
import util.UserSession;
import util.ValidationUtils;

public class OrderSummaryController extends BaseController {
    @FXML private GridPane productGrid;
    
    @FXML private Label price;
    private double totalprice;
    
    @FXML private Label emailField;
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
    	
    	c = (Client) UserSession.getInstance().getUser();
    	
    	emailField.setText(c.getEmail());
        lastNameField.setText(c.getLastName());
        firstNameField.setText(c.getFirstName());
        streetField.setText(c.getStreet());
        cityField.setText(c.getCity());
        postCodeField.setText(String.valueOf(c.getPostCode())); // Convertir int en String
        countryField.setText(c.getCountry());
        displayItems();
    }
    
    private void displayItems() {
    	productGrid.getChildren().clear();
    	totalprice = 0;
        int i = 0;

        System.out.println("GridPane children count: " + productGrid.getChildren().size());
        System.out.println(UserSession.getInstance().getOrder().getCart().keySet());
        for (Product p : UserSession.getInstance().getOrder().getCart().keySet()) {

            // Load the product image
            Image image = loadImage(p.getImagePath(), defaultImagePath);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);  // Adjust the width
            imageView.setPreserveRatio(true); // Preserve the aspect ratio
            imageView.setSmooth(true); // Enable smoothing

            // Create labels for the name and price
            Label nameLabel = new Label(p.getName());

            Label priceLabel = new Label(String.format("%.2f €", p.getPrice()));
          
            Label quantityLabel = new Label(String.format("Quantity: %d", UserSession.getInstance().getOrder().getCart().get(p)));

            
            Label totalPriceLabel = new Label(String.format("%.2f €",p.getPrice()*UserSession.getInstance().getOrder().getCart().get(p)));

            totalprice += p.getPrice()*UserSession.getInstance().getOrder().getCart().get(p);
            
            // VBox to organize all the information vertically
            VBox infoBox = new VBox(5);
            infoBox.getChildren().addAll(nameLabel, priceLabel, quantityLabel, totalPriceLabel);

            // Create an HBox to align the image and information horizontally
            HBox hbox = new HBox(10); // 10 px spacing
            hbox.setPadding(new Insets(10));
            hbox.getChildren().addAll(imageView, infoBox);

            // Add the HBox to the GridPane
            productGrid.add(hbox, 0, i);
            
            i++;
        }
        price.setText(String.format("%.2f €", totalprice));
    }
    
    
    @FXML
    public void handleReturn() {
    	UserSession.getInstance().setValidate(false);
    	
    	try {
            SceneManager.getInstance().showScene("Cart");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleCheckout() {
    	
 	    String firstName = firstNameField.getText();
 	    String lastName = lastNameField.getText();
 	    String street = streetField.getText();
 	    String city = cityField.getText();
 	    String postCode = postCodeField.getText();
 	    String country = countryField.getText();

 	    // Vérification des modifications
 	    String errorMessage = ValidationUtils.verifyModifications(firstName, lastName, street, city, postCode, country);

 	    // Si une erreur est détectée, on l'affiche et on arrête l'exécution
 	    if (errorMessage != null) {
 	        errorLabel.setText(errorMessage);
 	        return;
 	    }

 	    // Effacer le message d'erreur si tout est valide
 	    errorLabel.setText("");

 	    // --- Mise à jour des informations du client ---
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
 	
 	// --- Mise à jour des informations du client et de la commande ---
 	   boolean updateSuccess = true;  // Variable pour suivre le succès global

 	   // Mise à jour du client
 	   updateSuccess &= DataSingleton.getInstance().getClientDAO().updateClient(c);  // On utilise &= pour s'assurer que toutes les étapes réussissent

 	   // Insertion de la commande
 	   updateSuccess &= DataSingleton.getInstance().getOrderDAO().insertOrder(UserSession.getInstance().getOrder());

 	   // Vérification de succès
 	   if (updateSuccess) {
 	       System.out.println("Les modifications du Client et de la commande ont été enregistrées avec succès !");
 	       
 	       // Commande terminée donc nouveau panier vide
 	       UserSession.getInstance().setOrder(new Order());
 	       
 	       // Passage à la scène suivante uniquement si tout a réussi
 	       try {
 	           SceneManager.getInstance().showScene("ValidOrder");
 	       } catch (Exception e) {
 	           e.printStackTrace();
 	       }
 	   } else {
 	       // Affichage d'un message d'erreur générique si l'une des étapes a échoué
 	       errorLabel.setText("Erreur lors de la mise à jour du compte ou de l'insertion de la commande !");
 	   }
 	    
 	
    }
    	
    
}
