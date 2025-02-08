package controllers;

import java.util.List;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tables.Order;
import tables.Product;
import util.DataSingleton;
import util.SceneManager;
import util.UserSession;
import javafx.scene.control.Separator;
import javafx.scene.layout.Region;

public class OrderHistoryController extends BaseController {

	@FXML private GridPane orderGrid;
	
	@FXML
	public void initialize() {
		super.initialize();
		
		displayOrders();
	}
	


	private void displayOrders() {
	    List<Order> orders = DataSingleton.getInstance().getOrderDAO().getOrdersByClientId(UserSession.getInstance().getUser().getId());

	    orderGrid.getChildren().clear();  // Nettoyer l'ancien contenu
	    int i = 0;  // Compteur pour gérer les lignes dans le GridPane

	    for (Order order : orders) {
	        double totalPrice = 0;
	        
	        // Créer un VBox pour chaque commande
	        VBox vbox = new VBox(5);
	        vbox.setPadding(new Insets(10));

	        // Ajouter les informations de la commande (ID et statut)
	        Label orderIDLabel = new Label("Order ID: " + order.getOrderID());
	        Label statusLabel = new Label("Status: " + order.getStatus());
	        
	        HBox hbox = new HBox(5);
	        hbox.setPadding(new Insets(10));
	        hbox.getChildren().addAll(orderIDLabel, statusLabel);
	        
	        vbox.getChildren().add(hbox);  // Ajouter l'HBox au VBox

	        // Afficher les produits de la commande : nom, quantité, prix total
	        for (Product product : order.getCart().keySet()) {
	            HBox hboxproduct = new HBox(10);
	            hboxproduct.setPadding(new Insets(10));
	            
	            // Load the product image
	            Image image = loadImage(product.getImagePath(), defaultImagePath);
	            ImageView imageView = new ImageView(image);
	            imageView.setFitWidth(50);  // Adjust the width
	            imageView.setPreserveRatio(true); // Preserve the aspect ratio
	            imageView.setSmooth(true); // Enable smoothing
	            
	            Label nameLabel = new Label(product.getName());
	            Label quantityLabel = new Label("Quantity: " + order.getCart().get(product).getQuantity());
	            Label priceLabel = new Label("Price: " + String.format("%.2f €", order.getCart().get(product).getPriceAtPurchase() * order.getCart().get(product).getQuantity()));

	            hboxproduct.getChildren().addAll(imageView, nameLabel, quantityLabel, priceLabel);
	            totalPrice += order.getCart().get(product).getPriceAtPurchase() * order.getCart().get(product).getQuantity();

	            vbox.getChildren().add(hboxproduct);  // Ajouter chaque produit au VBox
	        }

	        // Afficher le prix total de la commande
	        Label totalPriceLabel = new Label("Total price: " + String.format("%.2f €", totalPrice));
	        vbox.getChildren().add(totalPriceLabel);

	        // Ajouter un Separator pour la division entre les commandes
	        Separator separator = new Separator();
	        separator.setPadding(new Insets(10, 0, 10, 0));  // Espacement autour du separator
	        vbox.getChildren().add(separator);

	        // Ajouter le VBox complet de la commande au GridPane
	        orderGrid.add(vbox, 0, i);  // Ajouter le VBox dans la ligne 'i' de la colonne 0

	        i++;  // Incrémenter la ligne pour la prochaine commande
	    }
	}


}
