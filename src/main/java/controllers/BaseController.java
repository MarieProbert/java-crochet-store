package controllers;


import java.io.File;

import dao.ClientDAO;
import dao.ProductDAO;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tables.Catalog;
import tables.Order;

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
}

