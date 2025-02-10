package controllers;

import enums.Color;
import enums.Size;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tables.Product;
import util.UserSession;

/**
 * Controller for displaying detailed information about a product.
 * Provides methods to update UI components with product data and handle adding the product to the cart.
 */
public class ProductController extends BaseController {

    private Product product;

    @FXML private ImageView productImage;
    @FXML private Label productName;
    @FXML private Label productPrice;
    @FXML private Button addToCartButton;

    @FXML private Label creatorLabel;
    @FXML private Label productCreator;

    @FXML private Label descriptionLabel;
    @FXML private Label productDescription;

    @FXML private Label colorLabel;
    @FXML private Label productColor;

    @FXML private Label sizeLabel;
    @FXML private Label productSize;

    @FXML
    public void initialize() {
        super.initialize();
    }

    /**
     * Sets the current product and updates the UI with its details.
     *
     * @param product the product to display
     */
    public void setProduct(Product product) {
        this.product = product;
        Image image = loadImage(product.getImagePath(), defaultImagePath);

        displayProductName(product.getName());
        displayProductPrice(product.getPrice());
        displayProductCreator(product.getCreator());
        displayProductDescription(product.getDescription());
        displayProductImage(image);
        displayProductColor(product.getColor());
        displayProductSize(product.getSize());
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
        productCreator.setText(creator);
        creatorLabel.setVisible(true);
    }

    public void displayProductDescription(String description) {
        productDescription.setText(description);
        descriptionLabel.setVisible(true);
    }

    public void displayProductColor(Color color) {
        productColor.setText(color.toString());
        colorLabel.setVisible(true);
    }

    public void displayProductSize(Size size) {
        productSize.setText(size.toString());
        sizeLabel.setVisible(true);
    }

    /**
     * Handles the "Add to Cart" action for the current product.
     */
    @FXML
    private void handleAddToCart() {
        boolean success = UserSession.getInstance().getOrder().addToCart(product, 1);
        if (success) {
            showInfoMessage("Product added to cart");
        } else {
            showErrorMessage("Not enough stock");
        }
    }
}
