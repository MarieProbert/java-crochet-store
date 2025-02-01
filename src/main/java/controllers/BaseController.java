package controllers;


import java.io.File;

import dao.ClientDAO;
import dao.ProductDAO;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tables.Catalog;
import tables.Order;
import util.SceneManager;
import util.UserSession;

public class BaseController {
	
	protected String defaultImagePath = "C:/Users/marie/eclipse-workspace/projet-java/pictures/others/no_picture.jpg";
	protected String bannerPath = "C:/Users/marie/eclipse-workspace/projet-java/pictures/others/banniere.jpg";
	@FXML protected ImageView bannerImage;

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
    
    @FXML
    private void handleCart() {
    	try {
            SceneManager.getInstance().showScene("Cart");

            
        } catch (Exception e) {
        	System.out.println("erreur");
            e.printStackTrace();
        }

    }
    
    @FXML
    private void handleAccount() {
        String sceneToShow = UserSession.getInstance().getUser().getId() == -1 
                             ? "Login" 
                             : "Account";
        
        try {
            SceneManager.getInstance().showScene(sceneToShow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	@FXML
	public void handleCatalog() {
    	try {
            SceneManager.getInstance().showScene("Catalog");
        } catch (Exception e) {
            e.printStackTrace();
        }
    
	}
    
    
}

