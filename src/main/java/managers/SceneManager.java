package managers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import tables.Product;

import java.io.IOException;
import java.util.HashMap;

import controllers.ProductController;

public class SceneManager {
    private static SceneManager instance;
    private Stage stage;
    private HashMap<String, Scene> scenes = new HashMap<>();
    private HashMap<Product, Scene> productScenes = new HashMap<>(); // Gestion des scènes dynamiques

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

    public void loadScene(String name, String fxmlPath) throws IOException {
        if (!scenes.containsKey(name)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scenes.put(name, scene);
        }
    }

    public boolean showScene(String name) {
        if (scenes.containsKey(name)) {
            stage.setScene(scenes.get(name));
            stage.show();
            return true;
        } else {
            return false;
        }
    }

    public boolean showProductScene(Product product) {
        try {
            if (!productScenes.containsKey(product)) {
                // Charge la scène pour ce produit si elle n'existe pas encore
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProductView.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                // Récupère le contrôleur pour initialiser les données spécifiques du produit
                ProductController controller = loader.getController();
                controller.setProduct(product); // Passe l'objet produit au contrôleur

                productScenes.put(product, scene); // Stocke la scène dans la map
            }
            stage.setScene(productScenes.get(product)); // Affiche la scène
            stage.show();

            return true; // Succès
        } catch (IOException e) {
            e.printStackTrace(); // Log de l'erreur pour le débogage
            // Afficher une alerte
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de charger le produit");
            alert.setContentText("Une erreur est survenue lors du chargement du produit : " + product.getName());
            alert.showAndWait();
            return false; // Échec
        }
    }

}
