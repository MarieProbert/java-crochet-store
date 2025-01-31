package main;

import controllers.BaseController;
import javafx.application.Application;
import javafx.stage.Stage;
import util.DataSingleton;
import util.SceneManager;
import util.UserSession;

public class MainApp extends Application {
   
	 @Override
	    public void start(Stage primaryStage) throws Exception {
		 
	        primaryStage.setWidth(800);           
	        primaryStage.setHeight(700); 
	        primaryStage.setTitle("Crochet Store");
	        
		 	SceneManager sceneManager = SceneManager.getInstance();
		 	UserSession userSession = UserSession.getInstance();
		 	DataSingleton dataSingleton = DataSingleton.getInstance();
	        sceneManager.setStage(primaryStage);
	        
	        // Montrer la scène de Login
	        sceneManager.showScene("Catalog");

	    }

	    public static void main(String[] args) {
	    	new BaseController();
	        launch(args);
	    }
}

