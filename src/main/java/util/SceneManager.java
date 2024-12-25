package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    public static void changeScene(Stage stage, String fxmlFile) throws Exception {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/views/" + fxmlFile));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
