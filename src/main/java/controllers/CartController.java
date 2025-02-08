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
import tables.Order;
import tables.Product;
import util.SceneManager;
import util.UserSession;

/**
 * Controller for the shopping cart screen, handling the display and management of items in the cart.
 * Includes functionalities like displaying cart items, updating quantities, and navigating to order validation.
 */
public class CartController extends BaseController {
    @FXML private GridPane productGrid;
    @FXML private Pagination pagination; 
    
    /**
     * Initializes the cart screen, displaying the existing products in the user's cart.
     */
    @FXML
    public void initialize() {
        super.initialize();
        // Display the existing products in the cart
        displayCartItems();
    }

    /**
     * Displays all the items currently in the cart by adding them to the productGrid.
     * Updates the display of the cart with product images, names, prices, quantities, and buttons
     * for adjusting item quantities.
     */
    private void displayCartItems() {
        productGrid.getChildren().clear();
        int i = 0;

        // Print information for debugging purposes
        System.out.println("GridPane children count: " + productGrid.getChildren().size());
        System.out.println(UserSession.getInstance().getOrder().getCart().keySet());
        
        // Loop through all products in the cart
        for (Product p : UserSession.getInstance().getOrder().getCart().keySet()) {
            // Load the product image
            Image image = loadImage(p.getImagePath(), defaultImagePath);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(150);  // Adjust the width
            imageView.setPreserveRatio(true); // Preserve the aspect ratio
            imageView.setSmooth(true); // Enable smoothing

            // Create labels for the product's name and price
            Label nameLabel = new Label(p.getName());
            nameLabel.setStyle("-fx-underline: true;"); // Style to indicate it's clickable
            nameLabel.setOnMouseClicked(e -> SceneManager.getInstance().showProductScene(p));

            Order order = UserSession.getInstance().getOrder();
            Label priceLabel = new Label(String.format("%.2f €", order.getCart().get(p).getPriceAtPurchase()));
            Label quantityLabel = new Label(String.format("Quantity: %d", order.getCart().get(p).getQuantity()));

            // Button "+" to increase the quantity
            Button addButton = new Button("+");
            addButton.setOnAction(e -> {
                UserSession.getInstance().getOrder().addToCart(p, 1);
                displayCartItems();  // Redraw the interface
            });

            // Button "-" to decrease the quantity
            Button subtractButton = new Button("-");
            subtractButton.setOnAction(e -> {
                UserSession.getInstance().getOrder().deleteFromCart(p, 1);
                displayCartItems();  // Redraw the interface
            });

            // HBox to align the buttons side by side
            HBox buttonBox = new HBox(5); // 5 px spacing between the buttons
            buttonBox.getChildren().addAll(subtractButton, addButton);

            // VBox to organize the product info vertically
            VBox infoBox = new VBox(5);
            infoBox.getChildren().addAll(nameLabel, priceLabel, quantityLabel, buttonBox);

            // Create an HBox to align the image and information horizontally
            HBox hbox = new HBox(10); // 10 px spacing
            hbox.setPadding(new Insets(10));
            hbox.getChildren().addAll(imageView, infoBox);

            // Add the HBox to the GridPane
            productGrid.add(hbox, 0, i);

            i++;
        }
    }

    /**
     * Navigates the user to either the login scene or the order summary scene based on user login status.
     * If the user is logged in, it sets the validate flag to true and shows the order summary. Otherwise, it redirects to login.
     */
    @FXML
    private void handleValidate() {
        // If the user is logged in -> redirects to the page to validate information
        // If not logged in -> redirects to the login page
        // Sets the boolean flag indicating that the user wants to proceed to validation to true
        UserSession.getInstance().setValidate(true);

        // Determine which scene to show based on whether the user is logged in
        String sceneToShow = UserSession.getInstance().getUser().getId() == -1 
                ? "Login"  // If user is not logged in, show the login scene
                : "OrderSummary";  // Otherwise, show the order summary

        try {
            SceneManager.getInstance().showScene(sceneToShow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
