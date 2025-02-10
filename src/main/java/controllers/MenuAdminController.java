package controllers;

import javafx.fxml.FXML;
import util.SceneManager;

/**
 * Controller for the admin menu.
 * Provides navigation to customer management, product catalog management, and invoicing systems.
 */
public class MenuAdminController extends BaseController {
    
    @FXML
    public void initialize() {
        super.initialize();
    }

    /**
     * Navigates to the customer management system.
     */
    @FXML
    private void handleCustomerSystem() {
        try {
            SceneManager.getInstance().showScene("AdminClients");
        } catch (Exception e) {
            showErrorMessage("Error: there was an issue loading the next scene.");
        }
    }
    
    /**
     * Navigates to the product catalog management system.
     */
    @FXML
    private void handleProductCatalogSystem() {
        try {
            SceneManager.getInstance().showScene("AdminCatalog");
        } catch (Exception e) {
            showErrorMessage("Error: there was an issue loading the next scene.");
        }
    }
    
    /**
     * Navigates to the invoicing system.
     */
    @FXML
    private void handleInvoicingSystem() {
        try {
            SceneManager.getInstance().showScene("AdminOrders");
        } catch (Exception e) {
            showErrorMessage("Error: there was an issue loading the next scene.");
        }
    }
}
