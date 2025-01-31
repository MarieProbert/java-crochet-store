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
import tables.Product;
import util.SceneManager;
import util.UserSession;

public class OrderSummaryController extends BaseController {

    @FXML private Button back;
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
    	
    	// If infos valides :
    	// Sauvegarder les infos client dans la bdd
    	// Sauvegarder l'info de commande dans la bdd
    	// passer à la fenêtre de validation de commande
    	
    }
}
