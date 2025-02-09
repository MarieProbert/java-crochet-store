package controllers;

import java.io.File;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;
import tables.Order;
import tables.User;
import util.SceneManager;
import util.UserSession;

/**
 * Base controller for the application.
 * Provides common functionality such as image loading, tooltip management for the user icon,
 * and handling scene transitions.
 */
public class BaseController {

    protected String defaultImagePath = "C:/Users/marie/eclipse-workspace/projet-java/pictures/others/no_picture.jpg";
    protected String bannerPath = "C:/Users/marie/eclipse-workspace/projet-java/pictures/others/banniere.jpg";

    @FXML 
    protected ImageView bannerImage;
    @FXML 
    protected ImageView personIcon;
    
    @FXML
    protected Label messageLabel;

    protected Popup tooltipPopup;
    protected VBox tooltipBox;
    protected PauseTransition hideTooltipDelay;
    protected boolean needToClearCart = false;

    /**
     * Initializes the controller.
     * Sets up the banner and user icon images, creates the tooltip popup,
     * and attaches mouse event handlers for the tooltip.
     */
    public void initialize() {
    	clearMessage();
        bannerImage.setImage(loadImage(bannerPath, defaultImagePath));
        personIcon.setImage(loadImage("C:/Users/marie/eclipse-workspace/projet-java/pictures/others/user_icon.png", defaultImagePath));

        createTooltipPopup();

        // Show tooltip when the mouse enters the user icon.
        personIcon.setOnMouseEntered(event -> showTooltip());

        // Start delay to hide tooltip when the mouse exits the icon and is not over the tooltip.
        personIcon.setOnMouseExited(event -> {
            if (!tooltipBox.isHover()) {
                startHideTooltipDelay();
            }
        });
    }

    /**
     * Loads an image from the specified path. Returns a default image if the image is not found.
     *
     * @param imagePath   the path to the image
     * @param defaultPath the default image path if the image is not found
     * @return the loaded Image
     */
    protected Image loadImage(String imagePath, String defaultPath) {
        if (imagePath != null && new File(imagePath).exists()) {
            try {
                return new Image("file:" + imagePath);
            } catch (Exception e) {
                return new Image("file:" + defaultPath);
            }
        }
        return new Image("file:" + defaultPath);
    }

    /**
     * Creates the tooltip popup for the user icon.
     * The tooltip content is built based on the user login status.
     */
    protected void createTooltipPopup() {
        String cssFile = getClass().getResource("/style.css").toExternalForm();

        tooltipPopup = new Popup();
        tooltipBox = new VBox(5); // Reduce spacing between buttons.
        tooltipBox.getStylesheets().add(cssFile);
        tooltipBox.getStyleClass().add("tooltip-box");

        // Check if the user is logged in.
        boolean isLoggedIn = UserSession.getInstance().getUser().getId() != -1;

        if (isLoggedIn) {
            // For clients, add "Past Orders" button.
            if ("client".equals(UserSession.getInstance().getUser().getRole())) {
                Button ordersButton = new Button("Past Orders");
                ordersButton.getStyleClass().add("text-like-button");
                ordersButton.setOnAction(event -> {
                    handlePastOrders();
                    tooltipPopup.hide();
                });
                tooltipBox.getChildren().add(ordersButton);
            }

            // Add Account and Logout buttons.
            Button accountButton = new Button("Account");
            Button logoutButton = new Button("Logout");
            accountButton.getStyleClass().add("text-like-button");
            logoutButton.getStyleClass().add("text-like-button");

            accountButton.setOnAction(event -> {
                handleAccount();
                tooltipPopup.hide();
            });
            logoutButton.setOnAction(event -> {
                handleLogout();
                tooltipPopup.hide();
            });

            tooltipBox.getChildren().addAll(accountButton, logoutButton);
        } else {
            // For not logged in users, show only the Login button.
            Button loginButton = new Button("Login");
            loginButton.getStyleClass().add("text-like-button");
            loginButton.setOnAction(event -> {
                handleAccount(); // Redirect to login page.
                tooltipPopup.hide();
            });
            tooltipBox.getChildren().add(loginButton);
        }

        tooltipPopup.getContent().add(tooltipBox);

        // Keep the popup open if the mouse is over it.
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

    /**
     * Displays the tooltip popup near the user icon.
     */
    protected void showTooltip() {
        if (!tooltipPopup.isShowing()) {
            Window window = personIcon.getScene().getWindow();
            double x = window.getX() + personIcon.localToScene(0, 0).getX() + personIcon.getScene().getX();
            double y = window.getY() + personIcon.localToScene(0, 0).getY() + personIcon.getScene().getY() + personIcon.getFitHeight() + 5;
            tooltipPopup.show(personIcon, x, y);
        }
    }

    /**
     * Starts a delay after which the tooltip popup will be hidden.
     */
    protected void startHideTooltipDelay() {
        if (hideTooltipDelay == null) {
            hideTooltipDelay = new PauseTransition(Duration.seconds(0.5));
            hideTooltipDelay.setOnFinished(event -> hideTooltip());
        }
        hideTooltipDelay.playFromStart();
    }

    /**
     * Hides the tooltip popup.
     */
    protected void hideTooltip() {
        tooltipPopup.hide();
    }

    /**
     * Handles the action to display the shopping cart.
     */
    @FXML
    protected void handleCart() {
        try {
            SceneManager.getInstance().showScene("Cart");
        } catch (Exception e) {
        	showErrorMessage("There was an issue loading the next scene.");
        }
    }

    /**
     * Conditionally clears the shopping cart if required.
     * If the flag is set, it resets the order in the user session.
     */
    protected void conditionalClearCart() {
        if (needToClearCart) {
            needToClearCart = false;
            UserSession.getInstance().setOrder(new Order());
        }
    }

    /**
     * Handles navigation to the account or login page.
     * Chooses the destination scene based on the user login status and role.
     */
    protected void handleAccount() {
        String sceneToShow;
        conditionalClearCart();

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
        	showErrorMessage("There was an issue loading the next scene.");
        }
    }

    /**
     * Handles navigation to the catalog scene.
     */
    @FXML
    protected void handleCatalog() {
        conditionalClearCart();
        try {
            SceneManager.getInstance().showScene("Catalog");
        } catch (Exception e) {
        	showErrorMessage("There was an issue loading the next scene.");
        }
    }

    /**
     * Handles the user logout process.
     * Resets the user session and navigates back to the catalog.
     */
    protected void handleLogout() {
        UserSession.getInstance().setOrder(new Order());
        UserSession.getInstance().setUser(new User());
        handleCatalog();
    }

    /**
     * Handles navigation to the past orders (order history) scene.
     */
    @FXML
    protected void handlePastOrders() {
        conditionalClearCart();
        try {
            SceneManager.getInstance().showScene("OrderHistory");
        } catch (Exception e) {
        	showErrorMessage("There was an issue loading the next scene.");
        }
    }

    /**
     * Handles navigation to the admin menu scene.
     */
    @FXML
    protected void handleReturnMenu() {
        try {
            SceneManager.getInstance().showScene("MenuAdmin");
        } catch (Exception e) {
        	showErrorMessage("There was an issue loading the next scene.");
        }
    }
    
    
    /**
     * Affiche un message d'erreur dans le label.
     * @param message Le message d'erreur à afficher
     */
    protected void showErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().remove("info-message"); // Supprime le style d'information
        messageLabel.getStyleClass().add("error-message");   // Applique le style d'erreur
        messageLabel.setVisible(true);                       // Rend le label visible
    }

    /**
     * Affiche un message d'information dans le label.
     * @param message Le message d'information à afficher
     */
    protected void showInfoMessage(String message) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().remove("error-message"); // Supprime le style d'erreur
        messageLabel.getStyleClass().add("info-message");     // Applique le style d'information
        messageLabel.setVisible(true);                        // Rend le label visible
    }

    /**
     * Efface le message affiché et cache le label.
     */
    protected void clearMessage() {
        messageLabel.setText("");
        messageLabel.getStyleClass().remove("error-message");
        messageLabel.getStyleClass().remove("info-message");
        messageLabel.setVisible(false); // Cache le label
    }
}
