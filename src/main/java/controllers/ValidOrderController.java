package controllers;

import javafx.fxml.FXML;

public class ValidOrderController extends BaseController {

	
    @FXML
    public void initialize() {
    	bannerImage.setImage(loadImage(bannerPath, defaultImagePath));
    }
    	
	@FXML
	public void handleDownloadInvoice() {
		
	}
}
