package controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import managers.SceneManager;
import managers.UserSession;
import tables.Product;
import java.util.List;

public class CatalogController extends BaseController {
    @FXML private GridPane productGrid;
    @FXML private Pagination pagination;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button searchCart;
    @FXML private Button searchAccount;

    private List<Product> products;
    private static int PRODUCTS_PER_PAGE = 4;
    
   
    public CatalogController() {
    }


    @FXML
    public void initialize() {
        productGrid.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                // La scène est maintenant attachée, on peut accéder à ses dimensions
                newScene.widthProperty().addListener((obs, oldWidth, newWidth) -> updateGrid());
                newScene.heightProperty().addListener((obs, oldHeight, newHeight) -> updateGrid());
                
                // Afficher les produits pour la première fois après l'attachement de la scène
                afficherProduits(pagination.getCurrentPageIndex());
            }
        });
        
        productDAO.getAllProducts(); 
    	bannerImage.setImage(loadImage(bannerPath, defaultImagePath));
    	products = catalog.getProducts();

        // Configurer la pagination
        pagination.setPageCount((int) Math.ceil((double) products.size() / PRODUCTS_PER_PAGE));
        pagination.setCurrentPageIndex(0);
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> afficherProduits(newIndex.intValue()));
    
        
    }

        
    
    private void updateGrid() {
        // Taille de la fenêtre
        double gridWidth = productGrid.getScene().getWidth() - 200;
        double gridHeight = productGrid.getScene().getHeight() - 200;

        // Dimensions d'un produit (estimation)
        double productWidth = 200; // Largeur estimée d'un produit
        double productHeight = 200; // Hauteur estimée d'un produit

        // Calculer le nombre de colonnes et de lignes
        int cols = Math.max(1, (int) (gridWidth / productWidth));
        int rows = Math.max(1, (int) (gridHeight / productHeight));

        // Mettre à jour le nombre de produits par page
        PRODUCTS_PER_PAGE = cols * rows;
        pagination.setPageCount((int) Math.ceil((double) products.size() / PRODUCTS_PER_PAGE));

        // Recharger les produits pour la page actuelle
        afficherProduits(pagination.getCurrentPageIndex());
    }
    
    
    private void afficherProduits(int pageIndex) {
        if (productGrid.getScene() == null) {
            return; // La scène n'est pas encore prête
        }
        

        double sceneWidth = productGrid.getScene().getWidth() - 200; //corriger l'effet bannière et filtre
        double sceneHeight = productGrid.getScene().getHeight() - 200; //corriger l'effet bannière et filtre

        // Votre logique actuelle pour calculer les colonnes/lignes en fonction de la taille de la scène
        int cols = Math.max(1, (int) (sceneWidth / 200)); // Exemple : 300px par produit
        int rows = Math.max(1, (int) (sceneHeight / 200)); // Exemple : 400px par produit
        int produitsParPage = cols * rows;

        productGrid.getChildren().clear();
        int startIndex = pageIndex * produitsParPage;
        int endIndex = Math.min(startIndex + produitsParPage, products.size());

        for (int i = startIndex; i < endIndex; i++) {
           
            Product product = products.get(i);

            VBox vbox = new VBox(5);
            vbox.setPadding(new Insets(10));
            
            Image image = loadImage(product.getImagePath(), defaultImagePath);

            // Configuration de l'ImageView
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(150);  // Ajustez la largeur
            imageView.setPreserveRatio(true); // Conserver le ratio
            imageView.setSmooth(true); // Activer le lissage
            
            vbox.getChildren().add(imageView);

            // Adding name
            Label nomLabel = new Label(product.getName());
            nomLabel.setStyle("-fx-underline: true;"); // Style pour indiquer un lien
            nomLabel.setOnMouseClicked(e -> SceneManager.getInstance().showProductScene(product));
            
            // Adding price
            Label prixLabel = new Label(String.format("%.2f €", product.getPrice()));
            vbox.getChildren().addAll(nomLabel, prixLabel);
            
            // Adding button 'Add to cart'
            Button addToCartButton = new Button("Add to cart");
            //addToCartButton.setOnAction(e -> order.addToCart(product, 1));
            addToCartButton.setOnAction(e -> {
                UserSession.getInstance().getOrder().addToCart(product, 1);
                System.out.println(UserSession.getInstance().getOrder().getCart().keySet());
            });
            vbox.getChildren().add(addToCartButton);

            int colIndex = (i - startIndex) % cols;
            int rowIndex = (i - startIndex) / cols;
            productGrid.add(vbox, colIndex, rowIndex);
        }
    }

    
    //methode qui rajoute au panier et si la modif s'est bien faite (true), modif la page du panier
    
    
    @FXML
    private void handleCart() {
    	try {
    		System.out.println("4");
            SceneManager.getInstance().showScene("Cart");

            System.out.println("5");
            
        } catch (Exception e) {
        	System.out.println("erreur");
            e.printStackTrace();
        }

    }
    
    
    @FXML
    private void handleAccount() {
        String sceneToShow = UserSession.getInstance().getUserManager().getUser().getId() == -1 
                             ? "Login" 
                             : "Account";
        
        try {
            SceneManager.getInstance().showScene(sceneToShow);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
