package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import managers.SceneManager;
import managers.UserSession;

public class OrderSummaryController extends BaseController {

    @FXML private Button back;
    
    @FXML private TextField emailField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField streetField;
    @FXML private TextField cityField;
    @FXML private TextField postCodeField;
    @FXML private TextField countryField;
    @FXML private Label errorLabel;
    
    
    @FXML
    public void handleReturn() {
    	UserSession.getInstance().setValidate(false);
    	
    	try {
            SceneManager.getInstance().showScene("Cart");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
