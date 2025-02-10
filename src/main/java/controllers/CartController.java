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
 * Controller for the shopping cart screen.
 * Manages display and updates of cart items, including adjusting quantities and navigating to order validation.
 */
public class CartController extends BaseController {

    @FXML private GridPane productGrid;
    @FXML private Pagination pagination;

    /**
     * Initializes the cart screen by displaying the current cart items.
     */
    @FXML
    public void initialize() {
        super.initialize();
        displayCartItems();
    }

    /**
     * Displays all items in the user's cart in the grid.
     */
    private void displayCartItems() {
        clearMessage();
        productGrid.getChildren().clear();
        int rowIndex = 0;

        for (Product p : UserSession.getInstance().getOrder().getCart().keySet()) {
            // Load product image.
            Image image = loadImage(p.getImagePath(), defaultImagePath);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(150);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            // Create clickable label for product name.
            Label nameLabel = new Label(p.getName());
            nameLabel.setStyle("-fx-underline: true;");
            nameLabel.setOnMouseClicked(e -> {
                boolean success = SceneManager.getInstance().showProductScene(p);
                if (!success) {
                    System.out.println("Error: Unable to display product scene.");
                }
            });

            Order order = UserSession.getInstance().getOrder();
            Label priceLabel = new Label(String.format("%.2f €", order.getCart().get(p).getPriceAtPurchase()));
            Label quantityLabel = new Label(String.format("Quantity: %d", order.getCart().get(p).getQuantity()));

            // Button to increase quantity.
            Button addButton = new Button("+");
            addButton.setOnAction(e -> {
                boolean success = UserSession.getInstance().getOrder().addToCart(p, 1);
                if (success) {
                    showInfoMessage("Product added to cart");
                } else {
                    showErrorMessage("Not enough stock");
                }
                displayCartItems();
            });

            // Button to decrease quantity.
            Button subtractButton = new Button("-");
            subtractButton.setOnAction(e -> {
                boolean success = UserSession.getInstance().getOrder().deleteFromCart(p, 1);
                if (success) {
                    showInfoMessage("Product removed from cart");
                } else {
                    showErrorMessage("Error: Product not found in cart");
                }
                displayCartItems();
            });

            HBox buttonBox = new HBox(5);
            buttonBox.getChildren().addAll(subtractButton, addButton);

            VBox infoBox = new VBox(5);
            infoBox.getChildren().addAll(nameLabel, priceLabel, quantityLabel, buttonBox);

            HBox hbox = new HBox(10);
            hbox.setPadding(new Insets(10));
            hbox.getChildren().addAll(imageView, infoBox);

            productGrid.add(hbox, 0, rowIndex);
            rowIndex++;
        }
    }

    /**
     * Handles the validation action. If the user is logged in, navigates to the order summary;
     * otherwise, navigates to the login screen.
     */
    @FXML
    private void handleValidate() {
        UserSession.getInstance().setValidate(true);
        String sceneToShow = (UserSession.getInstance().getUser().getId() == -1)
                ? "Login"
                : "OrderSummary";
        try {
            SceneManager.getInstance().showScene(sceneToShow);
        } catch (Exception e) {
            showErrorMessage("There was an issue loading the next scene.");
        }
    }
}
