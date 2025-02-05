package controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tables.Product;
import util.DataSingleton;
import util.SceneManager;
import util.UserSession;

// Ne pas utiliser en l'état
public class AdminCatalogController extends BaseController {
    @FXML private GridPane productGrid;
    @FXML private TextField searchField;
    
    @FXML
    public void initialize() {
    	super.initialize();
    }
    
    private void afficherProduits() {
        if (productGrid.getScene() == null) {
            return; // La scène n'est pas encore prête
        }
        productGrid.getChildren().clear();
        int i = 0;


        for (Product p : DataSingleton.getInstance().getCatalog().getProducts()) {

            VBox vbox = new VBox(5);
            vbox.setPadding(new Insets(10));
            
            Image image = loadImage(p.getImagePath(), defaultImagePath);

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);  // Adjust the width
            imageView.setPreserveRatio(true); // Preserve the aspect ratio
            imageView.setSmooth(true); // Enable smoothing

            // Create labels for the name and price
            Label idLabel = new Label("ID : " + p.getProductID());
            
            Label nameLabel = new Label("Name" + p.getName());

            Label priceLabel = new Label(String.format("Price : %.2f €", p.getPrice()));
            
            Label quantityLabel = new Label(String.format("Quantity: %d", p.getStock()));

            // Button "+" to increase the quantity
            Button modifyButton = new Button("Modify");
            modifyButton.setOnAction(e -> {

            });


            // Button "-" to decrease the quantity
            Button deleteButton = new Button("Delete");

            deleteButton.setOnAction(e -> {
            });

            // HBox to align the buttons side by side
            HBox buttonBox = new HBox(5); // 5 px spacing between the buttons
            buttonBox.getChildren().addAll(modifyButton, deleteButton);

            // VBox to organize all the information vertically
            VBox infoBox = new VBox(5);
            infoBox.getChildren().addAll(idLabel, nameLabel, priceLabel, quantityLabel, buttonBox);

            // Create an HBox to align the image and information horizontally
            HBox hbox = new HBox(10); // 10 px spacing
            hbox.setPadding(new Insets(10));
            hbox.getChildren().addAll(imageView, infoBox);

            // Add the HBox to the GridPane
            productGrid.add(hbox, 0, i);
            

            i++;
    }

    }
}
