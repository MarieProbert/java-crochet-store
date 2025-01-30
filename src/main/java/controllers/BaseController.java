package controllers;


import java.io.File;

import dao.ClientDAO;
import dao.ProductDAO;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import managers.UserManager;
import tables.Catalog;
import tables.Order;

public class BaseController {
	
	protected String defaultImagePath = "C:/Users/marie/eclipse-workspace/projet-java/pictures/others/no_picture.jpg";
	protected String bannerPath = "C:/Users/marie/eclipse-workspace/projet-java/pictures/others/banniere.jpg";
	@FXML protected ImageView bannerImage;

	protected static Catalog catalog;
	protected static ProductDAO productDAO;
	protected static ClientDAO clientDAO;
	
	// Eventuellement separer les controllers pour avoir ceux clients, ceux admins et ceux partagés
	// En effet, on ne crée la commande vide que si c'est un client
    
	public BaseController() {
        productDAO = new ProductDAO(); 
        clientDAO = new ClientDAO();
        catalog = productDAO.getCatalog();
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

