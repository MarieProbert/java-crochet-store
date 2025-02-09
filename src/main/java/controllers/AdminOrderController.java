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

public class AdminOrderController extends BaseController {

    @FXML
    private GridPane orderGrid;

    @FXML
    public void initialize() {
        super.initialize();
        displayOrders();
    }
    
    private void displayOrders() {
    	clearMessage();
    	
        // Récupérer toutes les commandes
        List<Order> orders = DataSingleton.getInstance().getOrderDAO().getAllOrders();

        orderGrid.getChildren().clear();
        int rowIndex = 0;
        
        for (Order order : orders) {
            // ===== Ligne principale d'affichage =====
            HBox rowBox = new HBox(10);
            rowBox.setPadding(new Insets(5));
            
            Label orderIdLabel = new Label("Order ID: " + order.getOrderID());
            Label userIdLabel = new Label("User ID: " + order.getClientID());
            Label purchaseDateLabel = new Label("Purchase: " + order.getPurchaseDate().toString());
            
            // Affichage en lecture seule de la delivery date
            String deliveryText = (order.getDeliveryDate() != null) 
                    ? order.getDeliveryDate().toString() 
                    : "N/A";
            Label deliveryDateLabel = new Label("Delivery: " + deliveryText);
            
            // ComboBox pour choisir le status
            ComboBox<Status> statusCombo = new ComboBox<>();
            statusCombo.getItems().addAll(Status.INPROGRESS, Status.CONFIRMED, Status.DELIVERED);
            statusCombo.setValue(order.getStatus());
            
            rowBox.getChildren().addAll(orderIdLabel, userIdLabel, purchaseDateLabel, deliveryDateLabel, statusCombo);
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            rowBox.getChildren().add(spacer);
            
            // Bouton "Generate Invoice" (si le status n'est pas INPROGRESS)
            if (order.getStatus() != Status.INPROGRESS) {
                Button generateInvoiceBtn = new Button("Generate Invoice");
                generateInvoiceBtn.setOnAction(e -> {
                    Stage stage = SceneManager.getInstance().getStage();
                    User user = DataSingleton.getInstance().getUserDAO().getUserById(order.getClientID());
                    Invoice invoice = DataSingleton.getInstance().getInvoiceDAO().getOrCreateInvoiceByOrderId(order.getOrderID());
                    try {
                        InvoicePDFGenerator.generateInvoicePDF(stage, user, order, invoice);
                    } catch (Exception ex) {
                    	showErrorMessage("Error : Failed to generate the invoice.");
                    }
                });
                rowBox.getChildren().add(generateInvoiceBtn);
            }
            
            // Bouton "Delete Invoice" (si une facture existe déjà)
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
            
            // ===== Ligne "Save modifications" =====
            HBox saveBox = new HBox(10);
            saveBox.setPadding(new Insets(5, 5, 15, 5));
            Button saveBtn = new Button("Save modifications");
            saveBtn.setOnAction(e -> {
                Status newStatus = statusCombo.getValue();
                // Si le nouveau status est DELIVERED, on ouvre la fenêtre de dialogue
                if (newStatus == Status.DELIVERED) {
                    Stage dialogStage = new Stage();
                    dialogStage.setTitle("Update Delivery Date");
                    
                    HBox dialogBox = new HBox(10);
                    dialogBox.setPadding(new Insets(10));
                    Label promptLabel = new Label("Select new delivery date (optional):");
                    DatePicker datePicker = new DatePicker();
                    // Pré-remplir avec la date existante (si présente)
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
                        // Si une nouvelle date est sélectionnée, mettre à jour l'objet order
                        if (datePicker.getValue() != null) {
                            LocalDateTime ldt = datePicker.getValue().atTime(12, 0); // heure fixe
                            Timestamp ts = Timestamp.valueOf(ldt);
                            System.out.println(ts.toString());
                            order.setDeliveryDate(ts);
                            System.out.println(order.getDeliveryDate().toString());
                        }
                        order.setStatus(newStatus);
                        // Mise à jour unique via le DAO pour le status et la date (éventuelle)
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
                    // Si le status est autre que DELIVERED, mise à jour immédiate via le DAO
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
