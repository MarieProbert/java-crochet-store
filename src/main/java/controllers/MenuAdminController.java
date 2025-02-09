package controllers;

import javafx.fxml.FXML;
import util.SceneManager;

public class MenuAdminController extends BaseController {
    
    @FXML
    public void initialize() {
    	super.initialize();
    }

	@FXML
	private void handleCustomerSystem() {
    	try {
            SceneManager.getInstance().showScene("AdminClients");

            
        } catch (Exception e) {
            showErrorMessage("Error : there was an issue loading the next scene.");
        }

	}
	
	@FXML
	private void handleProductCatalogSystem() {
    	try {
            SceneManager.getInstance().showScene("AdminCatalog");

            
        } catch (Exception e) {
        	showErrorMessage("Error : there was an issue loading the next scene.");
        }

	}
	
	@FXML
	private void handleInvoicingSystem() {
    	try {
            SceneManager.getInstance().showScene("AdminOrder");

            
        } catch (Exception e) {
        	showErrorMessage("Error : there was an issue loading the next scene.");
        }
	}
}
