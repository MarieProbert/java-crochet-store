package main;

import controllers.BaseController;
import javafx.application.Application;
import javafx.stage.Stage;
import managers.SceneManager;

public class MainApp extends Application {
   
	 @Override
	    public void start(Stage primaryStage) throws Exception {
		 
	        primaryStage.setWidth(800);           
	        primaryStage.setHeight(700); 
	        primaryStage.setTitle("Crochet Store");
	        
		 	SceneManager sceneManager = SceneManager.getInstance();
	        sceneManager.setStage(primaryStage);
	        // Charger toutes les scènes nécessaires au lancement
	        sceneManager.loadScene("Login", "/views/LoginView.fxml");
	        sceneManager.loadScene("Catalog", "/views/CatalogView.fxml");
	        sceneManager.loadScene("Cart", "/views/CartView.fxml");
	        
	        
	        // Montrer la scène de Login
	        sceneManager.showScene("Login");

	    }

	    public static void main(String[] args) {
	    	new BaseController();
	        launch(args);
	    }
}

