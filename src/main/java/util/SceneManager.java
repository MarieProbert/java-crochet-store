package util;

import controllers.ProductController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import tables.Product;
import java.io.IOException;
import java.util.HashMap;

/**
 * Manages scene transitions and maintains scene configuration.
 * Implements singleton pattern to ensure single instance across application.
 */
public class SceneManager {
    private static SceneManager instance;
    private Stage stage;
    
    private static final HashMap<String, String> SCENE_PATHS = new HashMap<>() {/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
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
        put("AdminOrders", "/views/AdminOrderView.fxml");
    }};

    private SceneManager() {}

    /**
     * Gets the singleton instance of SceneManager
     * @return SceneManager instance
     */
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public Stage getStage() {
        return stage;
    }

    /**
     * Sets the primary stage for scene management
     * @param stage Primary application stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Loads and displays a scene by name
     * @param sceneName Name of the scene to load
     * @return true if successful, false otherwise
     */
    public boolean showScene(String sceneName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(SCENE_PATHS.get(sceneName)));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
            return true;
        } catch (IOException e) {
            handleSceneLoadError(sceneName, e);
            return false;
        }
    }

    /**
     * Displays product detail scene
     * @param product Product to display
     * @return true if successful, false otherwise
     */
    public boolean showProductScene(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProductView.fxml"));
            Parent root = loader.load();
            ProductController controller = loader.getController();
            controller.setProduct(product);
            stage.setScene(new Scene(root));
            stage.show();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void handleSceneLoadError(String sceneName, Exception e) {
        e.printStackTrace();
        showError("Scene Load Error", "Failed to load scene: " + sceneName);
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}