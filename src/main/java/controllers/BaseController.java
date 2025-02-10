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
     * Initializes the controller, loads images, and sets up the tooltip for the user icon.
     */
    public void initialize() {
        clearMessage();
        bannerImage.setImage(loadImage(bannerPath, defaultImagePath));
        personIcon.setImage(loadImage("C:/Users/marie/eclipse-workspace/projet-java/pictures/others/user_icon.png", defaultImagePath));
        createTooltipPopup();

        personIcon.setOnMouseEntered(event -> showTooltip());
        personIcon.setOnMouseExited(event -> {
            if (!tooltipBox.isHover()) {
                startHideTooltipDelay();
            }
        });
    }

    /**
     * Loads an image from the specified path, returning a default image if not found.
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
     * The tooltip content is built based on the user's login status.
     */
    protected void createTooltipPopup() {
        String cssFile = getClass().getResource("/style.css").toExternalForm();
        tooltipPopup = new Popup();
        tooltipBox = new VBox(5);
        tooltipBox.getStylesheets().add(cssFile);
        tooltipBox.getStyleClass().add("tooltip-box");

        boolean isLoggedIn = UserSession.getInstance().getUser().getId() != -1;
        if (isLoggedIn) {
            if ("client".equals(UserSession.getInstance().getUser().getRole())) {
                Button ordersButton = new Button("Past Orders");
                ordersButton.getStyleClass().add("text-like-button");
                ordersButton.setOnAction(event -> {
                    handlePastOrders();
                    tooltipPopup.hide();
                });
                tooltipBox.getChildren().add(ordersButton);
            }

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
            Button loginButton = new Button("Login");
            loginButton.getStyleClass().add("text-like-button");
            loginButton.setOnAction(event -> {
                handleAccount();
                tooltipPopup.hide();
            });
            tooltipBox.getChildren().add(loginButton);
        }
        tooltipPopup.getContent().add(tooltipBox);

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
     * Navigates to the shopping cart scene.
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
     * Clears the shopping cart if flagged.
     */
    protected void conditionalClearCart() {
        if (needToClearCart) {
            needToClearCart = false;
            UserSession.getInstance().setOrder(new Order());
        }
    }

    /**
     * Navigates to the account or login page based on the user's login status and role.
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
     * Navigates to the catalog scene.
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
     * Logs the user out and navigates back to the catalog.
     */
    protected void handleLogout() {
        UserSession.getInstance().setOrder(new Order());
        UserSession.getInstance().setUser(new User());
        handleCatalog();
    }

    /**
     * Navigates to the order history (past orders) scene.
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
     * Navigates to the admin menu scene.
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
     * Displays an error message in the message label.
     *
     * @param message the error message to display
     */
    protected void showErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().remove("info-message");
        if (!messageLabel.getStyleClass().contains("error-message")) {
            messageLabel.getStyleClass().add("error-message");
        }
        messageLabel.setVisible(true);
    }

    /**
     * Displays an informational message in the message label.
     *
     * @param message the information message to display
     */
    protected void showInfoMessage(String message) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().remove("error-message");
        if (!messageLabel.getStyleClass().contains("info-message")) {
            messageLabel.getStyleClass().add("info-message");
        }
        messageLabel.setVisible(true);
    }

    /**
     * Clears the message label.
     */
    protected void clearMessage() {
        messageLabel.setText("");
        messageLabel.getStyleClass().remove("error-message");
        messageLabel.getStyleClass().remove("info-message");
        messageLabel.setVisible(false);
    }
}
