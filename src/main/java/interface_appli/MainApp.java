package interface_appli;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Root layout
        StackPane root = new StackPane();

        // Ajout d'un bouton
        Button btn = new Button("Click Me!");
        root.getChildren().add(btn);

        // Définir la scène et afficher la fenêtre
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("My JavaFX App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Démarre l'application JavaFX
    }
}

