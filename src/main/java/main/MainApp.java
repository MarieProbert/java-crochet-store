package main;

import controllers.BaseController;
import javafx.application.Application;
import javafx.stage.Stage;
import util.DataSingleton;
import util.SceneManager;
import util.UserSession;

/**
 * Main entry point for the Crochet Store application.
 * Launches the application and sets up the initial scene (catalog).
 */
public class MainApp extends Application {

    /**
     * Starts the JavaFX application.
     * Initializes key components like the scene manager and user session,
     * sets up the stage, and shows the initial scene.
     *
     * @param primaryStage The main window (stage) of the application.
     * @throws Exception If any error occurs during the initialization.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Set initial window size and title
        primaryStage.setWidth(800);           
        primaryStage.setHeight(700); 
        primaryStage.setTitle("Crochet Store");

        // Initialize singleton instances for scene manager, user session, and data handler
        SceneManager sceneManager = SceneManager.getInstance();
        UserSession.getInstance();
        DataSingleton.getInstance();

        // Pass the main stage (window) to the scene manager for managing scenes
        sceneManager.setStage(primaryStage);

        // Show the "Catalog" scene as the starting point of the application
        sceneManager.showScene("Catalog");
    }

    /**
     * The main entry point for launching the JavaFX application.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        // Initialize any necessary controllers (BaseController in this case)
        new BaseController();
        
        // Launch the JavaFX application
        launch(args);
    }
}
