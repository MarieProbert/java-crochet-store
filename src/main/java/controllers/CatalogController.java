package controllers;

import java.util.List;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import tables.Product;

public class CatalogController {
	@FXML
    private GridPane productGrid;
    @FXML
    private Pagination pagination;

    private List<Product> products;
    private static final int PRODUCTS_PER_PAGE = 4; // Nombre de produits par page
    
    
    
    private void afficherProduits(int pageIndex) {
        productGrid.getChildren().clear(); // Effacer les anciens produits affichés
        int startIndex = pageIndex * PRODUCTS_PER_PAGE;
        int endIndex = Math.min(startIndex + PRODUCTS_PER_PAGE, products.size());

        for (int i = startIndex; i < endIndex; i++) {
        	Product product = products.get(i);
            
            // Créer une VBox pour chaque produit
            VBox vbox = new VBox(5);
            vbox.setPadding(new Insets(10));
            
            // Ajouter l'image du produit
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(product.getImagePath())));
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
            vbox.getChildren().add(imageView);
            
            // Ajouter le nom et le prix
            Label nomLabel = new Label(product.getName());
            Label prixLabel = new Label(product.getPrice() + " €");
            vbox.getChildren().addAll(nomLabel, prixLabel);
            // Ajouter la VBox dans le GridPane
            int colIndex = (i - startIndex) % 2;
            int rowIndex = (i - startIndex) / 2;
            productGrid.add(vbox, colIndex, rowIndex);
        }
    }
}
