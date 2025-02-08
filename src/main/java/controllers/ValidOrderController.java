package controllers;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import tables.Order;
import util.InvoicePDFGenerator;
import util.SceneManager;
import util.UserSession;

public class ValidOrderController extends BaseController {

	
    @FXML
    public void initialize() {
    	super.initialize();
    }
    	
	@FXML
	public void handleDownloadInvoice() throws IOException, DocumentException {
		Stage stage = SceneManager.getInstance().getStage();

	    try {
	    	InvoicePDFGenerator.generateInvoicePDF(stage, UserSession.getInstance().getUser(), UserSession.getInstance().getOrder(), UserSession.getInstance().getInvoice());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@FXML
	public void handleCatalog() {
		UserSession.getInstance().setOrder(new Order());
		super.handleCatalog();
	}
	
	// A faire
     // Commande terminée donc nouveau panier vide
     //UserSession.getInstance().setOrder(new Order());
}
