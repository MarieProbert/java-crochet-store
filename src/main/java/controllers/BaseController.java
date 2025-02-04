package controllers;


import java.io.File;

import dao.UserDAO;
import dao.ProductDAO;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;
import tables.Catalog;
import tables.Order;
import tables.User;
import util.DataSingleton;
import util.SceneManager;
import util.UserSession;

public class BaseController {
	
	protected String defaultImagePath = "C:/Users/marie/eclipse-workspace/projet-java/pictures/others/no_picture.jpg";
	protected String bannerPath = "C:/Users/marie/eclipse-workspace/projet-java/pictures/others/banniere.jpg";
	@FXML protected ImageView bannerImage;
	@FXML protected ImageView personIcon;
	

    protected Popup tooltipPopup;
    protected VBox tooltipBox; 

    protected PauseTransition hideTooltipDelay;
    
    public void initialize() {
        bannerImage.setImage(loadImage(bannerPath, defaultImagePath));
        personIcon.setImage(loadImage("C:/Users/marie/eclipse-workspace/projet-java/pictures/others/user_icon.png", defaultImagePath));
        
        createTooltipPopup();

        // Afficher le popup au survol de l'icône
        personIcon.setOnMouseEntered(event -> showTooltip());

        // Fermer seulement si la souris quitte l'icône ET le popup
        personIcon.setOnMouseExited(event -> {
            if (!tooltipBox.isHover()) { // Vérifie si la souris est encore sur le popup
                startHideTooltipDelay();
            }
        });
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
    
    protected void createTooltipPopup() {
        String cssFile = getClass().getResource("/style.css").toExternalForm();

        tooltipPopup = new Popup();

        tooltipBox = new VBox(5); // Réduire l'espace entre les boutons
        tooltipBox.getStylesheets().add(cssFile);
        tooltipBox.getStyleClass().add("tooltip-box");

        // Vérifier si l'utilisateur est connecté
        boolean isLoggedIn = UserSession.getInstance().getUser().getId() != -1;

        if (isLoggedIn) {
        	if ("client".equals(UserSession.getInstance().getUser().getRole())) {
	            // Utilisateur connecté : afficher Account, Past Orders et Logout
	            Button ordersButton = new Button("Past Orders");
	            ordersButton.getStyleClass().add("text-like-button");

	            ordersButton.setOnAction(event -> {
	                handlePastOrders();
	                tooltipPopup.hide();
	            });
	            
	            tooltipBox.getChildren().add(ordersButton);
	
        	}

    		// Utilisateur connecté : afficher Account, Past Orders et Logout
            Button accountButton = new Button("Account");
            Button logoutButton = new Button("Logout");

            // Appliquer la classe CSS aux boutons
            accountButton.getStyleClass().add("text-like-button");
            logoutButton.getStyleClass().add("text-like-button");

            accountButton.setOnAction(event -> {
                handleAccount();
                tooltipPopup.hide();
            });
            
            logoutButton.setOnAction(event -> {
                handleLogout(); // Gérer la déconnexion
                tooltipPopup.hide();
            });

            tooltipBox.getChildren().addAll(accountButton, logoutButton);
            
        } else {
            // Utilisateur non connecté : afficher uniquement Login
            Button loginButton = new Button("Login");

            // Appliquer la classe CSS au bouton
            loginButton.getStyleClass().add("text-like-button");

            loginButton.setOnAction(event -> {
                handleAccount(); // Rediriger vers la page de connexion
                tooltipPopup.hide();
            });

            tooltipBox.getChildren().add(loginButton);
        }

        tooltipPopup.getContent().add(tooltipBox);

        // 🌟 Garde le popup ouvert si la souris est dessus
        tooltipBox.setOnMouseEntered(event -> {
            if (hideTooltipDelay != null) {
                hideTooltipDelay.stop();
            }
        });

        tooltipBox.setOnMouseExited(event -> {
            if (!personIcon.isHover()) {
                startHideTooltipDelay();
            }
        });
    }
    
    protected void showTooltip() {
        if (!tooltipPopup.isShowing()) {
            Window window = personIcon.getScene().getWindow();
            double x = window.getX() + personIcon.localToScene(0, 0).getX() + personIcon.getScene().getX();
            double y = window.getY() + personIcon.localToScene(0, 0).getY() + personIcon.getScene().getY() + personIcon.getFitHeight() + 5;

            tooltipPopup.show(personIcon, x, y);
        }
    }

    protected void startHideTooltipDelay() {
        if (hideTooltipDelay == null) {
            hideTooltipDelay = new PauseTransition(Duration.seconds(0.5));
            hideTooltipDelay.setOnFinished(event -> hideTooltip());
        }
        hideTooltipDelay.playFromStart();
    }

    protected void hideTooltip() {
        tooltipPopup.hide();
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
    protected void handleAccount() {
    	String sceneToShow;

    	if (UserSession.getInstance().getUser().getId() != -1) {
    	    if ("admin".equals(UserSession.getInstance().getUser().getRole())) {
    	        sceneToShow = "AdminAccount";
    	    } else {
    	        sceneToShow = "ClientAccount";
    	    }
    	} else {
    	    sceneToShow = "Login";
    	}
        
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

    
	protected void handleLogout() {
		// Au logout on devient guest. Le panier est enregistré dans la bdd
		//System.out.println(UserSession.getInstance().getUser().getId());
		//DataSingleton.getInstance().getOrderDAO().updateOrCreateOrder(UserSession.getInstance().getOrder());
		UserSession.getInstance().setOrder(new Order());
		UserSession.getInstance().setUser(new User());
		
		handleCatalog();
	}
	
	protected void handlePastOrders() {
    	try {
            SceneManager.getInstance().showScene("OrderHistory");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
    
}

