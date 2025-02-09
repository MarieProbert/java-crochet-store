package main;

import controllers.BaseController;
import javafx.application.Application;
import javafx.stage.Stage;
import util.DataSingleton;
import util.SceneManager;
import util.UserSession;

/**
 * Main entry point for the Crochet Store application.
 * Launches the application and sets up the initial scene (Catalog).
 */
public class MainApp extends Application {

    /**
     * Starts the JavaFX application.
     * Initializes key components like the scene manager, user session, and data handler,
     * sets up the stage, and shows the initial scene.
     *
     * @param primaryStage the main window (stage) of the application
     * @throws Exception if any error occurs during initialization
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setWidth(800);
        primaryStage.setHeight(700);
        primaryStage.setTitle("Crochet Store");

        SceneManager sceneManager = SceneManager.getInstance();
        UserSession.getInstance();
        DataSingleton.getInstance();

        sceneManager.setStage(primaryStage);
        sceneManager.showScene("Catalog");
    }

    /**
     * The main entry point for launching the JavaFX application.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        // Initialize necessary controllers
        new BaseController();
        launch(args);
    }
}
