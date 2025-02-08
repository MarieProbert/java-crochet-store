package controllers;

import java.util.List;

import enums.Status;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import tables.Order;
import util.DataSingleton;
import util.InvoicePDFGenerator;
import util.SceneManager;
import util.UserSession;

public class AdminOrderController extends BaseController {

    @FXML
    private GridPane orderGrid;

    @FXML
    public void initialize() {
        super.initialize(); // Si BaseController fait d'autres initialisations
        displayOrders();
    }
    
    private void displayOrders() {
        // Récupérer toutes les commandes. (Adapter selon ton DAO)
        List<Order> orders = DataSingleton.getInstance().getOrderDAO().getAllOrders();

        orderGrid.getChildren().clear(); // Nettoyer d'éventuelles lignes précédentes
        int rowIndex = 0;
        
        // Pour chaque commande, créer une ligne d'affichage
        for (Order order : orders) {
            // Créer un conteneur horizontal pour une ligne
            HBox rowBox = new HBox(10);
            rowBox.setPadding(new Insets(5));
            
            // Créer les labels pour orderID, userID, purchaseDate et deliveryDate
            Label orderIdLabel = new Label("Order ID: " + order.getOrderID());
            Label userIdLabel = new Label("User ID: " + order.getClientID());
            Label purchaseDateLabel = new Label("Purchase: " + order.getPurchaseDate().toString());
            Label deliveryDateLabel = new Label("Delivery: " + 
                    (order.getDeliveryDate() != null ? order.getDeliveryDate().toString() : "N/A"));
            
            // Créer un ComboBox pour le status avec les 3 choix possibles
            ComboBox<Status> statusCombo = new ComboBox<>();
            statusCombo.getItems().addAll(Status.INPROGRESS, Status.CONFIRMED, Status.DELIVERED);
            statusCombo.setValue(order.getStatus());
            
            // Lorsqu'on modifie le status, mettre à jour l'objet order
            statusCombo.valueProperty().addListener(new ChangeListener<Status>() {
                @Override
                public void changed(ObservableValue<? extends Status> observable, Status oldValue, Status newValue) {
                    order.setStatus(newValue);
                    // Optionnel : mettre à jour la base de données via DAO
                }
            });
            
            // Ajouter ces éléments dans la ligne
            rowBox.getChildren().addAll(orderIdLabel, userIdLabel, purchaseDateLabel, deliveryDateLabel, statusCombo);
            
            // Espace flexible pour pousser les boutons à droite
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            rowBox.getChildren().add(spacer);
            
            // Si le status est Confirmed ou Delivered, ajouter les boutons
            if (order.getStatus() == Status.CONFIRMED || order.getStatus() == Status.DELIVERED) {
                // Bouton pour générer la facture
                Button generateInvoiceBtn = new Button("Generate Invoice");
                generateInvoiceBtn.setOnAction(e -> {
                    // Exemple d'appel à la méthode de génération de PDF
                    // On passe ici la commande et d'autres infos nécessaires (exemple simplifié)
            		Stage stage = SceneManager.getInstance().getStage();

            	    try {
            	    	InvoicePDFGenerator.generateInvoicePDF(stage, UserSession.getInstance().getUser(), UserSession.getInstance().getOrder(), UserSession.getInstance().getInvoice());
            	    } catch (Exception ex) {
            	        ex.printStackTrace();
            	    }
                });
                

                
                // Bouton pour supprimer la facture (ou l'annuler)
                Button deleteInvoiceBtn = new Button("Delete Invoice");
                deleteInvoiceBtn.setOnAction(e -> {
                    // Exemple de suppression de facture via DAO
                    // DataSingleton.getInstance().getInvoiceDAO().deleteInvoiceByOrderId(order.getOrderID());
                    System.out.println("Delete invoice for Order ID: " + order.getOrderID());
                });
                
                rowBox.getChildren().addAll(generateInvoiceBtn, deleteInvoiceBtn);
            }
            
            // Ajouter la ligne dans la GridPane à la colonne 0, ligne rowIndex
            orderGrid.add(rowBox, 0, rowIndex++);
        }
    }
}
