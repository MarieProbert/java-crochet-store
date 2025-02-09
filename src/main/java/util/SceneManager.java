package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import tables.Order;
import tables.Product;

import java.io.IOException;
import java.util.HashMap;

import controllers.CartController;
import controllers.ProductController;

public class SceneManager {
    private static SceneManager instance;
    private Stage stage;
    
    private static final HashMap<String, String> scenePaths = new HashMap<>() {{
        put("Login", "/views/LoginView.fxml");
        
        put("Catalog", "/views/CatalogView.fxml");
        put("Cart", "/views/CartView.fxml");
        put("ClientAccount", "/views/AccountView.fxml");
        put("AccountCreation", "/views/AccountCreationView.fxml");
        put("OrderSummary", "/views/OrderSummaryView.fxml");
        put("ValidOrder", "/views/ValidOrderView.fxml");
        put("OrderHistory", "/views/OrderHistoryView.fxml");
        
        put("MenuAdmin", "/views/MenuAdminView.fxml");
        put("AdminAccount", "/views/AdminAccountView.fxml"); 
        put("AdminCatalog", "/views/AdminCatalogView.fxml");
        put("AdminClients", "/views/AdminClientsView.fxml");
        put("AdminOrder", "/views/AdminOrderView.fxml"); 
        
    }};

    
    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }
    

    public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public boolean showScene(String name) {
    	try {
    		
            FXMLLoader loader = new FXMLLoader(getClass().getResource(scenePaths.get(name)));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            return true;
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Impossible de charger la scène", "Erreur lors du chargement de la scène : " + name);
        }
        return false;
    }
 
    
    public boolean showProductScene(Product product) {
        try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProductView.fxml"));
	        Parent root = loader.load();
	        Scene scene = new Scene(root);
	
	        ProductController controller = loader.getController();
	        controller.setProduct(product);

            stage.setScene(scene);
            stage.show();
            return true;
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Impossible de charger le produit", "Une erreur est survenue lors du chargement du produit : " + product.getName());
            return false;
        }
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
 
}
