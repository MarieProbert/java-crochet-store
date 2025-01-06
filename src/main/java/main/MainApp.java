package main;

import controllers.BaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import managers.CatalogManager;
import managers.UserManager;

public class MainApp extends Application {
   
	 @Override
	    public void start(Stage primaryStage) throws Exception {
		 	FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
	        Scene scene = new Scene(loader.load());
	        primaryStage.setScene(scene);
	        primaryStage.setWidth(800);           
	        primaryStage.setHeight(700); 
	        primaryStage.setTitle("Crochet Store");
	        primaryStage.show();
	    }

	    public static void main(String[] args) {
	    	BaseController bc = new BaseController();
	        launch(args);
	    }
}

