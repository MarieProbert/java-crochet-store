package controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import tables.Product;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dao.ClientDAO;
import dao.ProductDAO;
import enums.Color;

public class CatalogController {
    @FXML private GridPane productGrid;
    @FXML private Pagination pagination;

    private List<Product> products;
    private static final int PRODUCTS_PER_PAGE = 4;
    
    
    private ProductDAO productDAO;

    public CatalogController() {
        this.productDAO = new ProductDAO(); 
    }

    @FXML
    public void initialize() {
    	productDAO.getAllProducts();
        // Initialiser les produits (exemple d'initialisation)
    	products = new ArrayList<Product>();
    	pagination.setPageCount((int) Math.ceil((double) products.size() / PRODUCTS_PER_PAGE));
        pagination.setCurrentPageIndex(0);
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> afficherProduits(newIndex.intValue()));

        afficherProduits(0);
    }

    private void afficherProduits(int pageIndex) {
        productGrid.getChildren().clear();
        int startIndex = pageIndex * PRODUCTS_PER_PAGE;
        int endIndex = Math.min(startIndex + PRODUCTS_PER_PAGE, products.size());

        for (int i = startIndex; i < endIndex; i++) {
            Product product = products.get(i);

            VBox vbox = new VBox(5);
            vbox.setPadding(new Insets(10));

            // Adding image
            Image image = new Image(new File(product.getImagePath()).toURI().toString());
            ImageView imageView = new ImageView(image);

           // ImageView imageView = new ImageView(new Image(getClass().getResource(product.getImagePath()).toExternalForm()));
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
            vbox.getChildren().add(imageView);

            // Adding name
            Label nomLabel = new Label(product.getName());
            
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
    
    private static void handleAddToCart(Product product) {
    	
    }
}
