package controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tables.Invoice;
import tables.Order;
import tables.Product;
import tables.User;
import util.DataSingleton;
import util.SceneManager;
import util.UserSession;
import util.ValidationUtils;

/**
 * Controller for the order summary screen.
 * Displays the items in the user's order, shows contact information, and handles the checkout process.
 */
public class OrderSummaryController extends BaseController {

    @FXML private GridPane productGrid;
    @FXML private Label price;
    @FXML private Label emailField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField streetField;
    @FXML private TextField cityField;
    @FXML private TextField postCodeField;
    @FXML private TextField countryField;
    
    private User user;
    private double totalPrice;

    @FXML
    public void initialize() {
        super.initialize();
        
        user = UserSession.getInstance().getUser();
        
        emailField.setText(user.getEmail());
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        streetField.setText(user.getAddress().getStreet());
        cityField.setText(user.getAddress().getCity());
        postCodeField.setText(String.valueOf(user.getAddress().getPostCode()));
        countryField.setText(user.getAddress().getCountry());

        displayItems();
    }
    
    /**
     * Displays all items in the user's order and calculates the total price.
     */
    private void displayItems() {
        productGrid.getChildren().clear();
        totalPrice = 0;
        int rowIndex = 0;
        
        Order order = UserSession.getInstance().getOrder();
        for (Product p : order.getCart().keySet()) {
            // Load product image
            Image image = loadImage(p.getImagePath(), defaultImagePath);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            // Create labels for product information
            Label nameLabel = new Label(p.getName());
            Label priceLabel = new Label(String.format("%.2f €", order.getCart().get(p).getPriceAtPurchase()));
            Label quantityLabel = new Label(String.format("Quantity: %d", order.getCart().get(p).getQuantity()));
            Label totalPriceLabel = new Label(String.format("%.2f €", 
                    order.getCart().get(p).getPriceAtPurchase() * order.getCart().get(p).getQuantity()));

            totalPrice += order.getCart().get(p).getPriceAtPurchase() * order.getCart().get(p).getQuantity();
            
            // Organize product information vertically and horizontally
            VBox infoBox = new VBox(5);
            infoBox.getChildren().addAll(nameLabel, priceLabel, quantityLabel, totalPriceLabel);
            HBox hbox = new HBox(10);
            hbox.setPadding(new Insets(10));
            hbox.getChildren().addAll(imageView, infoBox);

            productGrid.add(hbox, 0, rowIndex++);
        }
        price.setText(String.format("%.2f €", totalPrice));
    }

    /**
     * Handles the checkout process by validating modifications, updating user and order data,
     * inserting the order and invoice into the database, and navigating to the valid order scene.
     */
    @FXML
    private void handleCheckout() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String street = streetField.getText();
        String city = cityField.getText();
        String postCode = postCodeField.getText();
        String country = countryField.getText();

        String errorMessage = ValidationUtils.verifyModifications(firstName, lastName, street, city, postCode, country);
        if (errorMessage != null) {
            showErrorMessage(errorMessage);
            return;
        }
        
        // Update client information
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.getAddress().setStreet(street);
        user.getAddress().setCity(city);
        user.getAddress().setPostCode(postCode);

        // Update order status and persist client and order data
        boolean updateSuccess = true;
        Order order = UserSession.getInstance().getOrder();
        order.setStatusFromString("Confirmed");

        updateSuccess &= DataSingleton.getInstance().getUserDAO().updateUser(user);
        int orderID = DataSingleton.getInstance().getOrderDAO().insertOrder(order);

        if (updateSuccess && orderID != -1) {
            order.setOrderID(orderID);
            Invoice invoice = new Invoice(order.getOrderID(), order.getClientID(), order.getPurchaseDate(), order.calculateCartTotal());
            int invoiceID = DataSingleton.getInstance().getInvoiceDAO().insertInvoice(invoice);
            invoice.setInvoiceID(invoiceID);
            UserSession.getInstance().setInvoice(invoice);
            
            try {
                SceneManager.getInstance().showScene("ValidOrder");
            } catch (Exception e) {
                showErrorMessage("Error: there was an issue loading the next scene.");
            }
        } else {
            showErrorMessage("Error updating account or inserting order.");
        }
    }
}
