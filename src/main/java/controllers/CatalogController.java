package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import managers.CatalogManager;
import tables.Product;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dao.ClientDAO;
import dao.ProductDAO;
import enums.Color;

public class CatalogController extends BaseController {
    @FXML private GridPane productGrid;
    @FXML private Pagination pagination;
    @FXML private TextField searchField;
    @FXML private Button searchButton;

    private List<Product> products;
    private CatalogManager catalogManager;
    private static final int PRODUCTS_PER_PAGE = 4;
    
    
    private ProductDAO productDAO;

    public CatalogController() {
        this.productDAO = new ProductDAO(); 
        this.catalogManager = productDAO.getCatalogManager();
    }

    @FXML
    public void initialize() {
    	productDAO.getAllProducts();
        // Initialiser les produits (exemple d'initialisation)
    	
    	bannerImage.setImage(loadImage( "C:/Users/marie/eclipse-workspace/projet-java/pictures/others/no_picture.jpg", "C:/Users/marie/eclipse-workspace/projet-java/pictures/others/no_picture.jpg"));
    	products = catalogManager.getProducts();
    	pagination.setPageCount((int) Math.ceil((double) products.size() / PRODUCTS_PER_PAGE));
        pagination.setCurrentPageIndex(0);
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> afficherProduits(newIndex.intValue()));

        afficherProduits(0);
    }

    private void afficherProduits(int pageIndex) {
        productGrid.getChildren().clear();
        int startIndex = pageIndex * PRODUCTS_PER_PAGE;
        int endIndex = Math.min(startIndex + PRODUCTS_PER_PAGE, products.size());
        String defaultImagePath = "C:/Users/marie/eclipse-workspace/projet-java/pictures/others/no_picture.jpg";
        

        for (int i = startIndex; i < endIndex; i++) {
            Product product = products.get(i);

            VBox vbox = new VBox(5);
            vbox.setPadding(new Insets(10));
            
            Image image = loadImage(product.getImagePath(), defaultImagePath);

            // Configuration de l'ImageView
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200);  // Ajustez la largeur
            imageView.setPreserveRatio(true); // Conserver le ratio
            imageView.setSmooth(true); // Activer le lissage
            
            vbox.getChildren().add(imageView);

            // Adding name
            Label nomLabel = new Label(product.getName());
            nomLabel.setStyle("-fx-underline: true;"); // Style pour indiquer un lien
            nomLabel.setOnMouseClicked(e -> openProductPage(product));
            
            // Adding price
            Label prixLabel = new Label(product.getPrice() + " €");
            vbox.getChildren().addAll(nomLabel, prixLabel);
            
            // Adding button 'Add to cart'
            Button addToCartButton = new Button("Add to cart");
            addToCartButton.setOnAction(e -> handleAddToCart(product));
            vbox.getChildren().add(addToCartButton);

            int colIndex = (i - startIndex) % 2;
            int rowIndex = (i - startIndex) / 2;
            productGrid.add(vbox, colIndex, rowIndex);
        }
    }
    
    
    private void openProductPage(Product product) {
        
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProductView.fxml"));
            Parent productPage = loader.load();

            // Passer le produit au contrôleur de la page produit
            ProductController productController = loader.getController();
            productController.setProduct(product);

            Stage stage = (Stage) productGrid.getScene().getWindow(); // Récupérer la fenêtre actuelle
            stage.setScene(new Scene(productPage));
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    
    private static void handleAddToCart(Product product) {
    	
    }
    
    
    // Méthode appelée lors du clic sur le bouton de recherche
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();
        
        if (!searchText.isEmpty()) {
            // Appeler une méthode pour rechercher des produits dans votre base de données ou filtrer les produits
            System.out.println("Recherche de produit pour : " + searchText);
            // Par exemple, filtrer les produits en fonction du texte de recherche
            filterProductsBySearch(searchText);
        } else {
            System.out.println("Aucun texte de recherche saisi.");
        }
    }

    private void filterProductsBySearch(String searchText) {
        // Implémentez ici la logique de recherche ou de filtrage des produits selon votre base de données
        // Exemple : appeler une méthode pour filtrer les produits en fonction de searchText
    }
}
