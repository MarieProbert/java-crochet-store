package controllers;


import java.io.File;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import tables.Person;
import tables.Product;

public abstract class BaseController {
	
	protected String defaultImagePath = "C:/Users/marie/eclipse-workspace/projet-java/pictures/others/no_picture.jpg";
	protected String bannerPath = "C:/Users/marie/eclipse-workspace/projet-java/pictures/others/banniere.jpg";
	@FXML protected ImageView bannerImage;
	
    // Utilisateur actuellement connecté (partagé entre tous les contrôleurs)
    protected static Person currentUser;

    // Définir l'utilisateur connecté
    public static void setCurrentUser(Person user) {
        currentUser = user;
    }

    // Récupérer l'utilisateur connecté
    public static Person getCurrentUser() {
        return currentUser;
    }

    // Vérifier si un utilisateur est connecté
    protected boolean isUserLoggedIn() {
        return currentUser != null;
    }

    /*
    
    // Exemple : Vérifier si l'utilisateur a une permission spécifique
    protected boolean hasPermission(String permission) {
        return currentUser != null && currentUser.getPermissions().contains(permission);
    }*/
    
    
    
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
