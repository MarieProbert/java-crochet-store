package controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import tables.Product;

public class ProductController extends BaseController {
	
    @FXML
    private ImageView productImage;

    @FXML
    private Label productName;

    @FXML
    private Label productPrice;

    @FXML
    private TextArea productDescription;

    @FXML
    private Button addToCartButton;


	public void setProduct(Product product) {
		String defaultImagePath = "C:/Users/marie/eclipse-workspace/projet-java/pictures/others/no_picture.jpg";
	       
		VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(10));
        
		Image image = loadImage(product.getImagePath(), defaultImagePath);

        // Affichage des informations du produit
        productName.setText(product.getName());
        productPrice.setText(String.format("%.2f €", product.getPrice()));
        productDescription.setText(product.getDescription());
        productImage.setImage(image);

        // Ajouter l'action du bouton
        addToCartButton.setOnAction(e -> handleAddToCart(product));
	}
	
	private static void handleAddToCart(Product product) {
    	
    }

}
