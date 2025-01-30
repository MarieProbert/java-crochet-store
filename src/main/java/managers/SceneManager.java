package managers;

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
        put("Account", "/views/AccountView.fxml");
        put("AccountCreation", "/views/AccountCreationView.fxml");
        put("OrderSummary", "/views/OrderSummaryView.fxml");
    }};

    
    /*
    private HashMap<String, Scene> scenes = new HashMap<>();
    private HashMap<String, Boolean> dirty = new HashMap<>();
    private HashMap<Product, Scene> productScenes = new HashMap<>();
    private HashMap<String, String> scenePaths = new HashMap<>(); // Associe un nom de scène à son fichier FXML

    private HashMap<String, Object> controllers = new HashMap<>();
*/
    
    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
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
    
/*
    public void setDirty(String name) {
        dirty.put(name, true);
    }

    public void loadScene(String name, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        scenes.put(name, scene);
        scenePaths.put(name, fxmlPath);
        dirty.put(name, false);

        // Enregistre le contrôleur associé à cette scène
        controllers.put(name, loader.getController());
    }


    public boolean showScene(String name) {
        try {
            if (!scenes.containsKey(name) || dirty.getOrDefault(name, false)) {
                if (!scenePaths.containsKey(name)) {
                    showError("Erreur", "La scène '" + name + "' n'a pas de chemin FXML associé.");
                    return false;
                }
                loadScene(name, scenePaths.get(name));
            }

            if (scenes.containsKey(name)) {
                stage.setScene(scenes.get(name));
                stage.show();

                // Si un contrôleur est associé, tente d'appeler reloadContent()
                Object controller = controllers.get(name);
                if (controller instanceof CartController) { // Vérifie si c'est bien un contrôleur du panier
                    ((CartController) controller).reloadContent();
                }

                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Impossible de charger la scène", "Erreur lors du chargement de la scène : " + name);
        }
        return false;
    }


    public boolean showProductScene(Product product) {
        try {
            if (!productScenes.containsKey(product)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProductView.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                ProductController controller = loader.getController();
                controller.setProduct(product);

                productScenes.put(product, scene);
            }
            stage.setScene(productScenes.get(product));
            stage.show();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            showError("Impossible de charger le produit", "Une erreur est survenue lors du chargement du produit : " + product.getName());
            return false;
        }
    }
*/
    
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
