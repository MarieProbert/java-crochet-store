package controllers;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

import javafx.fxml.FXML;
import tables.Client;
import tables.Order;
import util.InvoicePDFGenerator;
import util.UserSession;

public class ValidOrderController extends BaseController {

	
    @FXML
    public void initialize() {
    	super.initialize();
    }
    	
	@FXML
	public void handleDownloadInvoice() throws IOException, DocumentException {
		InvoicePDFGenerator.generateInvoicePDF((Client) UserSession.getInstance().getUser(), UserSession.getInstance().getOrder(), UserSession.getInstance().getInvoice(), "C:/Users/marie/eclipse-workspace/projet-java/");
	}
	
	// A faire
     // Commande terminée donc nouveau panier vide
     //UserSession.getInstance().setOrder(new Order());
}
