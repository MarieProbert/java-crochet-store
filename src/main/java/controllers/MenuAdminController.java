package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import util.SceneManager;

public class MenuAdminController extends BaseController {
    @FXML private Label errorLabel;
    
    @FXML
    public void initialize() {
    	super.initialize();
    }

	@FXML
	public void handleCustomerSystem() {
    	try {
            SceneManager.getInstance().showScene("AdminClients");

            
        } catch (Exception e) {
        	System.out.println("erreur");
            e.printStackTrace();
        }

	}
	
	@FXML
	public void handleProductCatalogSystem() {
    	try {
            SceneManager.getInstance().showScene("AdminCatalog");

            
        } catch (Exception e) {
        	System.out.println("erreur");
            e.printStackTrace();
        }

	}
	
	@FXML
	public void handleInvoicingSystem() {
    	try {
            SceneManager.getInstance().showScene("AdminOrder");

            
        } catch (Exception e) {
        	System.out.println("erreur");
            e.printStackTrace();
        }
	}
}
