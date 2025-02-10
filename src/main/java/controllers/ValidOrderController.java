package controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import util.InvoicePDFGenerator;
import util.SceneManager;
import util.UserSession;

/**
 * Controller for the valid order screen.
 * Allows the user to download the invoice PDF for their order.
 */
public class ValidOrderController extends BaseController {

    @FXML
    public void initialize() {
        super.initialize();
        needToClearCart = true;
    }
        
    /**
     * Handles the action to download the invoice as a PDF.
     */
    @FXML
    private void handleDownloadInvoice() {
        Stage stage = SceneManager.getInstance().getStage();
        try {
            InvoicePDFGenerator.generateInvoicePDF(
                    stage,
                    UserSession.getInstance().getUser(),
                    UserSession.getInstance().getOrder(),
                    UserSession.getInstance().getInvoice()
            );
        } catch (Exception e) {
            showErrorMessage("Error: Failed to generate the invoice.");
        }
    }
}
