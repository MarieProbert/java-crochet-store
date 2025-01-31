package controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tables.Product;
import util.SceneManager;
import util.UserSession;

public class CartController extends BaseController {
    @FXML private GridPane productGrid;
    @FXML private Pagination pagination; 
    @FXML private Button searchCatalog;
    @FXML private Button searchAccount;
    @FXML private Button checkout;
    
    @FXML
    public void initialize() {
    	bannerImage.setImage(loadImage(bannerPath, defaultImagePath));


        System.out.println("hello");
        // Display the existing products in the cart
        displayCartItems();
        System.out.println("end");
    }

    
    private void displayCartItems() {
        productGrid.getChildren().clear();
        int i = 0;

        System.out.println("GridPane children count: " + productGrid.getChildren().size());
        System.out.println(UserSession.getInstance().getOrder().getCart().keySet());
        for (Product p : UserSession.getInstance().getOrder().getCart().keySet()) {
            System.out.println("1");

            // Load the product image
            Image image = loadImage(p.getImagePath(), defaultImagePath);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(150);  // Adjust the width
            imageView.setPreserveRatio(true); // Preserve the aspect ratio
            imageView.setSmooth(true); // Enable smoothing

            // Create labels for the name and price
            Label nameLabel = new Label(p.getName());
            nameLabel.setStyle("-fx-underline: true;"); // Style to indicate a clickable link
            nameLabel.setOnMouseClicked(e -> SceneManager.getInstance().showProductScene(p));

            Label priceLabel = new Label(String.format("%.2f €", p.getPrice()));
            
            Label quantityLabel = new Label(String.format("Quantity: %d", UserSession.getInstance().getOrder().getCart().get(p)));

            // Button "+" to increase the quantity
            Button addButton = new Button("+");
            addButton.setOnAction(e -> {
            	UserSession.getInstance().getOrder().addToCart(p, 1);
                displayCartItems();  // Redessine l'interface
            });


            // Button "-" to decrease the quantity
            Button subtractButton = new Button("-");

            subtractButton.setOnAction(e -> {
            	UserSession.getInstance().getOrder().deleteFromCart(p, 1);
                displayCartItems();  // Redessine l'interface
            });

            // HBox to align the buttons side by side
            HBox buttonBox = new HBox(5); // 5 px spacing between the buttons
            buttonBox.getChildren().addAll(subtractButton, addButton);

            // VBox to organize all the information vertically
            VBox infoBox = new VBox(5);
            infoBox.getChildren().addAll(nameLabel, priceLabel, quantityLabel, buttonBox);

            // Create an HBox to align the image and information horizontally
            HBox hbox = new HBox(10); // 10 px spacing
            hbox.setPadding(new Insets(10));
            hbox.getChildren().addAll(imageView, infoBox);

            // Add the HBox to the GridPane
            productGrid.add(hbox, 0, i);
            

            i++;
            System.out.println("3");
        }
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
    
    @FXML
    private void handleValidate() {
    	
    	// Si l'utilisateur est connecté -> le renvoie à la page pour valider les infos
    	// S'il n'est pas connecté -> le renvoie au login
    	// Fixer le booleen qui indique qu'on veut passer à la validation à true
    	UserSession.getInstance().setValidate(true);
    	
        String sceneToShow = UserSession.getInstance().getUser().getId() == -1 
                ? "Login" 
                : "OrderSummary";

		try {
			SceneManager.getInstance().showScene(sceneToShow);
		} catch (Exception e) {
			e.printStackTrace();
		}


    	
    	
    }
}
