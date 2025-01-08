package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import managers.SceneManager;

public class CartController extends BaseController {
    @FXML private GridPane productGrid;
    @FXML private Pagination pagination; 
    @FXML private Button searchCatalog;
    
    @FXML
    public void initialize() {
    	bannerImage.setImage(loadImage(bannerPath, defaultImagePath));
    }
    
	@FXML
	public void handleCatalog() {
    	try {
            SceneManager.getInstance().showScene("Catalog");
        } catch (Exception e) {
            e.printStackTrace();
        }
    
	}

}
