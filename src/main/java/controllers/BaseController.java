package controllers;


import java.io.File;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import managers.CatalogManager;
import managers.UserManager;
import tables.User;
import tables.Order;
import tables.Product;

public class BaseController {
	
	protected String defaultImagePath = "C:/Users/marie/eclipse-workspace/projet-java/pictures/others/no_picture.jpg";
	protected String bannerPath = "C:/Users/marie/eclipse-workspace/projet-java/pictures/others/banniere.jpg";
	@FXML protected ImageView bannerImage;

	protected static CatalogManager catalogManagement;
	protected static UserManager userManagement;
	protected static Order order;
	// Eventuellement separer les controllers pour avoir ceux clients, ceux admins et ceux partagés
	// En effet, on ne crée la commande vide que si c'est un client
    
	public BaseController() {
    	catalogManagement = new CatalogManager();
    	userManagement = new UserManager();
    	order = new Order();
	}
    
    
    // Récupérer une image à partir de l'URL
    protected Image loadImage(String imagePath, String defaultPath) {
        if (imagePath != null && new File(imagePath).exists()) {
            try {
                return new Image("file:" + imagePath);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            }
        }
        return new Image("file:" + defaultPath);
    }
}

