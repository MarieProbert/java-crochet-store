package controllers;

import java.util.List;

import enums.Status;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Separator;
import tables.Order;
import tables.Product;
import tables.User;
import util.DataSingleton;
import util.InvoicePDFGenerator;
import util.SceneManager;
import util.UserSession;

/**
 * Controller for displaying the order history of a user.
 * Shows each order with its details, including product images, quantities, and total price.
 */
public class OrderHistoryController extends BaseController {

    @FXML private GridPane orderGrid;

    /**
     * Initializes the order history screen by displaying the user's past orders.
     */
    @FXML
    public void initialize() {
        super.initialize();
        displayOrders();
    }

    /**
     * Retrieves and displays all orders for the current user.
     * For orders with status CONFIRMED or DELIVERED, a "Generate Invoice" button is added to allow
     * PDF invoice generation. The purchase date and delivery date are displayed; if the delivery date
     * is not set, "N/A" is shown.
     */
    private void displayOrders() {
        List<Order> orders = DataSingleton.getInstance()
                .getOrderDAO()
                .getOrdersByClientId(UserSession.getInstance().getUser().getId());

        orderGrid.getChildren().clear();
        int rowIndex = 0;

        for (Order order : orders) {
            double totalPrice = 0;

            VBox orderBox = new VBox(5);
            orderBox.setPadding(new Insets(10));

            Label orderIDLabel = new Label("Order ID: " + order.getOrderID());
            Label statusLabel = new Label("Status: " + order.getStatus());
            Label purchaseDateLabel = new Label("Purchase Date: " + order.getPurchaseDate().toString());
            String deliveryDateStr = (order.getDeliveryDate() != null) ? order.getDeliveryDate().toString() : "N/A";
            Label deliveryDateLabel = new Label("Delivery Date: " + deliveryDateStr);

            HBox headerBox = new HBox(5);
            headerBox.setPadding(new Insets(10));
            headerBox.getChildren().addAll(orderIDLabel, statusLabel, purchaseDateLabel, deliveryDateLabel);
            orderBox.getChildren().add(headerBox);
            
            // Display each product in the order
            for (Product product : order.getCart().keySet()) {
                HBox productBox = new HBox(10);
                productBox.setPadding(new Insets(10));

                Image image = loadImage(product.getImagePath(), defaultImagePath);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(50);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);

                Label nameLabel = new Label(product.getName());
                Label quantityLabel = new Label("Quantity: " + order.getCart().get(product).getQuantity());
                double productTotal = order.getCart().get(product).getPriceAtPurchase()
                        * order.getCart().get(product).getQuantity();
                Label priceLabel = new Label("Price: " + String.format("%.2f €", productTotal));

                productBox.getChildren().addAll(imageView, nameLabel, quantityLabel, priceLabel);
                totalPrice += productTotal;
                orderBox.getChildren().add(productBox);
            }

            Label totalPriceLabel = new Label("Total Price: " + String.format("%.2f €", totalPrice));
            orderBox.getChildren().add(totalPriceLabel);

            // Add "Generate Invoice" button for orders with CONFIRMED or DELIVERED status
            if (order.getStatus() == Status.CONFIRMED || order.getStatus() == Status.DELIVERED) {
                Button generateInvoiceBtn = new Button("Generate Invoice");
                generateInvoiceBtn.setOnAction(e -> {
                    Stage stage = SceneManager.getInstance().getStage();
                    User user = DataSingleton.getInstance().getUserDAO().getUserById(order.getClientID());
                    tables.Invoice invoice = DataSingleton.getInstance()
                            .getInvoiceDAO()
                            .getOrCreateInvoiceByOrderId(order.getOrderID());
                    try {
                        InvoicePDFGenerator.generateInvoicePDF(stage, user, order, invoice);
                    } catch (Exception ex) {
                        showErrorMessage("Error: Failed to generate the invoice.");
                    }
                });
                orderBox.getChildren().add(generateInvoiceBtn);
            }

            Separator separator = new Separator();
            separator.setPadding(new Insets(10, 0, 10, 0));
            orderBox.getChildren().add(separator);

            orderGrid.add(orderBox, 0, rowIndex);
            rowIndex++;
        }
    }
}
