package controllers;

import javafx.collections.MapChangeListener;
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
import managers.SceneManager;
import tables.Product;

public class CartController extends BaseController {
    @FXML private GridPane productGrid;
    @FXML private Pagination pagination; 
    @FXML private Button searchCatalog;
    
    @FXML
    public void initialize() {
    	bannerImage.setImage(loadImage(bannerPath, defaultImagePath));
    	
    	order.isUpdatedProperty().addListener((observable, oldValue, newValue) -> {
        	System.out.println("updated ?");
            if (newValue) {
                // If "isUpdated" becomes true, update the display
                displayCartItems();
            }
        });
        // Add a listener to the "isUpdated" boolean
    	/*
        order.isUpdatedProperty().addListener((observable, oldValue, newValue) -> {
        	System.out.println("updated ?");
            if (newValue) {
                // If "isUpdated" becomes true, update the display
                displayCartItems();
            }
        });*/

        System.out.println(order.getCart().toString());
        // Display the existing products in the cart
        displayCartItems();
    }
    
    private void displayCartItems() {
        productGrid.getChildren().clear();
        int i = 0;

        for (Product p : order.getCart().keySet()) {
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
            
            Label quantityLabel = new Label(String.format("Quantity: %d", order.getCart().get(p)));

            // Button "+" to increase the quantity
            Button addButton = new Button("+");
            addButton.setOnAction(e -> {
                // Call a method to handle increasing the quantity
                order.addToCart(p, 1);
                //quantityLabel.setText("Quantity: " + order.getCart().get(p));
            });

            // Button "-" to decrease the quantity
            Button subtractButton = new Button("-");
            subtractButton.setOnAction(e -> {
                // Call a method to handle decreasing the quantity
                order.deleteFromCart(p, 1);
                //quantityLabel.setText("Quantity: " + order.getCart().get(p));
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
}
