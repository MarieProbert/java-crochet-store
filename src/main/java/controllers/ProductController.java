package controllers;

import enums.Color;
import enums.Size;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tables.Product;
import util.SceneManager;
import util.UserSession;

public class ProductController extends BaseController {
	
	private Product product;
	
    @FXML private ImageView productImage;
    @FXML private Label productName;
    @FXML private Label productPrice;
    @FXML private Button addToCartButton;

   
    @FXML private Label creatorLabel;
    @FXML private Label productCreator;
    
    @FXML private Label descriptionLabel;
    @FXML private TextArea productDescription;

    @FXML private Label colorLabel;
    @FXML private Label productColor;
    
    @FXML private Label sizeLabel;
    @FXML private Label productSize;
    
    @FXML
    public void initialize() {

    	super.initialize();
    }
    
    public void displayProductName(String name) {
        productName.setText(name);
    }

    public void displayProductPrice(double price) {
        productPrice.setText(String.format("%.2f €", price));
    }

    public void displayProductImage(Image image) {
        productImage.setImage(image);
    }    

    public void displayProductCreator(String creator) {
        if (creator != null) {
            productCreator.setText(creator);
            creatorLabel.setVisible(true); // Affiche le label "Couleur :"
        } else {
            productCreator.setText(""); // Vide le champ de couleur
            creatorLabel.setVisible(false); // Cache le label "Couleur :"
        }
    }
    
    public void displayProductDescription(String description) {
        if (description != null) {
            productDescription.setText(description);
            descriptionLabel.setVisible(true); // Affiche le label "Couleur :"
        } else {
            productDescription.setText(""); // Vide le champ de couleur
            descriptionLabel.setVisible(false); // Cache le label "Couleur :"
        }
    }
    
    public void displayProductColor(Color color) {
        if (color != null) {
            productColor.setText(color.toString());
            colorLabel.setVisible(true); // Affiche le label "Couleur :"
        } else {
            productColor.setText(""); // Vide le champ de couleur
            colorLabel.setVisible(false); // Cache le label "Couleur :"
        }
    }
    
    public void displayProductSize(Size size) {
        if (size != null) {
            productSize.setText(size.toString());
            sizeLabel.setVisible(true); // Affiche le label "Couleur :"
        } else {
            productSize.setText(""); // Vide le champ de couleur
            sizeLabel.setVisible(false); // Cache le label "Couleur :"
        }
    }

    
	public void setProduct(Product product) {
        
		this.product = product;
		
		Image image = loadImage(product.getImagePath(), defaultImagePath);
		

        // Affichage des informations du produit
		displayProductName(product.getName());
		displayProductPrice(product.getPrice());
		displayProductCreator(product.getCreator());
		displayProductDescription(product.getDescription());
		displayProductImage(image);
        displayProductColor(product.getColor());
        displayProductSize(product.getSize());

        // Ajouter l'action du bouton
        //addToCartButton.setOnAction(e -> order.addToCart(product, 1));
	}
	
	@FXML
	public void handleAddToCart() {
		UserSession.getInstance().getOrder().addToCart(product, 1);
	}


    
}
