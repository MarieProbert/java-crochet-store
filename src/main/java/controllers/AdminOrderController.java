package controllers;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import enums.Status;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import tables.Invoice;
import tables.Order;
import tables.User;
import util.DataSingleton;
import util.InvoicePDFGenerator;
import util.SceneManager;

/**
 * Controller for managing orders in the admin system.
 * Displays all orders and provides functionality to update order status,
 * generate or delete invoices, and update the delivery date when necessary.
 */
public class AdminOrderController extends BaseController {

    @FXML
    private GridPane orderGrid;

    @FXML
    public void initialize() {
        super.initialize();
        displayOrders();
    }
    
    /**
     * Retrieves all orders and displays them in the grid.
     */
    private void displayOrders() {
        clearMessage();
        
        List<Order> orders = DataSingleton.getInstance().getOrderDAO().getAllOrders();
        orderGrid.getChildren().clear();
        int rowIndex = 0;
        
        for (Order order : orders) {
            // Main row display with order information and status selection.
            HBox rowBox = new HBox(10);
            rowBox.setPadding(new Insets(5));
            
            Label orderIdLabel = new Label("Order ID: " + order.getOrderID());
            Label userIdLabel = new Label("User ID: " + order.getClientID());
            Label purchaseDateLabel = new Label("Purchase: " + order.getPurchaseDate().toString());
            String deliveryText = (order.getDeliveryDate() != null) 
                    ? order.getDeliveryDate().toString() 
                    : "N/A";
            Label deliveryDateLabel = new Label("Delivery: " + deliveryText);
            
            ComboBox<Status> statusCombo = new ComboBox<>();
            statusCombo.getItems().addAll(Status.INPROGRESS, Status.CONFIRMED, Status.DELIVERED);
            statusCombo.setValue(order.getStatus());
            
            rowBox.getChildren().addAll(orderIdLabel, userIdLabel, purchaseDateLabel, deliveryDateLabel, statusCombo);
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            rowBox.getChildren().add(spacer);
            
            // "Generate Invoice" button if order status is not INPROGRESS.
            if (order.getStatus() != Status.INPROGRESS) {
                Button generateInvoiceBtn = new Button("Generate Invoice");
                generateInvoiceBtn.setOnAction(e -> {
                    Stage stage = SceneManager.getInstance().getStage();
                    User user = DataSingleton.getInstance().getUserDAO().getUserById(order.getClientID());
                    Invoice invoice = DataSingleton.getInstance().getInvoiceDAO().getOrCreateInvoiceByOrderId(order.getOrderID());
                    try {
                        InvoicePDFGenerator.generateInvoicePDF(stage, user, order, invoice);
                    } catch (Exception ex) {
                        showErrorMessage("Error: Failed to generate the invoice.");
                    }
                });
                rowBox.getChildren().add(generateInvoiceBtn);
            }
            
            // "Delete Invoice" button if an invoice exists.
            Invoice existingInvoice = DataSingleton.getInstance().getInvoiceDAO().getInvoiceByOrderId(order.getOrderID());
            if (existingInvoice != null) { 
                Button deleteInvoiceBtn = new Button("Delete Invoice");
                deleteInvoiceBtn.setOnAction(e -> {
                    boolean deleted = DataSingleton.getInstance().getInvoiceDAO().deleteInvoiceByOrderID(order.getOrderID());
                    if (deleted) {
                        displayOrders();
                    } else {
                        showErrorMessage("Failed to delete invoice for Order ID: " + order.getOrderID());
                    }
                });
                rowBox.getChildren().add(deleteInvoiceBtn);
            }
            
            orderGrid.add(rowBox, 0, rowIndex++);
            
            // Row for saving modifications.
            HBox saveBox = new HBox(10);
            saveBox.setPadding(new Insets(5, 5, 15, 5));
            Button saveBtn = new Button("Save modifications");
            saveBtn.setOnAction(e -> {
                Status newStatus = statusCombo.getValue();
                if (newStatus == Status.DELIVERED) {
                    // Open dialog to update delivery date if status is DELIVERED.
                    Stage dialogStage = new Stage();
                    dialogStage.setTitle("Update Delivery Date");
                    
                    HBox dialogBox = new HBox(10);
                    dialogBox.setPadding(new Insets(10));
                    Label promptLabel = new Label("Select new delivery date (optional):");
                    DatePicker datePicker = new DatePicker();
                    if (order.getDeliveryDate() != null) {
                        LocalDate existingDate = order.getDeliveryDate().toLocalDateTime().toLocalDate();
                        datePicker.setValue(existingDate);
                    }
                    Button okBtn = new Button("OK");
                    Button cancelBtn = new Button("Cancel");
                    dialogBox.getChildren().addAll(promptLabel, datePicker, okBtn, cancelBtn);
                    
                    Scene dialogScene = new Scene(dialogBox);
                    dialogStage.setScene(dialogScene);
                    
                    okBtn.setOnAction(ev -> {
                        if (datePicker.getValue() != null) {
                            LocalDateTime ldt = datePicker.getValue().atTime(12, 0);
                            Timestamp ts = Timestamp.valueOf(ldt);
                            order.setDeliveryDate(ts);
                        }
                        order.setStatus(newStatus);
                        DataSingleton.getInstance().getOrderDAO().updateOrder(order);
                        dialogStage.close();
                        displayOrders();
                    });
                    
                    cancelBtn.setOnAction(ev -> {
                        dialogStage.close();
                        displayOrders();
                    });
                    
                    dialogStage.showAndWait();
                } else {
                    order.setStatus(newStatus);
                    DataSingleton.getInstance().getOrderDAO().updateOrder(order);
                    displayOrders();
                }
            });
            saveBox.getChildren().add(saveBtn);
            orderGrid.add(saveBox, 0, rowIndex++);
        }
    }
}
