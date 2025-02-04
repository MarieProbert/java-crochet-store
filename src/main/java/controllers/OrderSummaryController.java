package controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tables.Invoice;
import tables.Product;
import tables.User;
import util.DataSingleton;
import util.SceneManager;
import util.UserSession;
import util.ValidationUtils;

public class OrderSummaryController extends BaseController {
    @FXML private GridPane productGrid;
    
    @FXML private Label price;
    private double totalprice;
    
    @FXML private Label emailField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField streetField;
    @FXML private TextField cityField;
    @FXML private TextField postCodeField;
    @FXML private TextField countryField;
    @FXML private Label errorLabel;
    
    private User user;
    
    @FXML
    public void initialize() {
    	super.initialize();
    	
    	user = UserSession.getInstance().getUser();
    	
        // Afficher les valeurs actuelles dans les champs
        emailField.setText(user.getEmail());
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());

        System.out.println(user.getRole());
        // Si l'utilisateur est un client, on récupère et affiche son adresse
        if ("client".equalsIgnoreCase(user.getRole())) {
            streetField.setText(user.getAddress().getStreet());
            cityField.setText(user.getAddress().getCity());
            postCodeField.setText(String.valueOf(user.getAddress().getPostCode()));
            countryField.setText(user.getAddress().getCountry());
        } else {
            // Si l'utilisateur n'est pas un client, on ne remplit pas ces champs
            streetField.setText("");
            cityField.setText("");
            postCodeField.setText("");
            countryField.setText("");
        }
        
        displayItems();
    }
    
    private void displayItems() {
    	productGrid.getChildren().clear();
    	totalprice = 0;
        int i = 0;

        System.out.println("GridPane children count: " + productGrid.getChildren().size());
        System.out.println(UserSession.getInstance().getOrder().getCart().keySet());
        for (Product p : UserSession.getInstance().getOrder().getCart().keySet()) {

            // Load the product image
            Image image = loadImage(p.getImagePath(), defaultImagePath);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);  // Adjust the width
            imageView.setPreserveRatio(true); // Preserve the aspect ratio
            imageView.setSmooth(true); // Enable smoothing

            // Create labels for the name and price
            Label nameLabel = new Label(p.getName());

            Label priceLabel = new Label(String.format("%.2f €", p.getPrice()));
          
            Label quantityLabel = new Label(String.format("Quantity: %d", UserSession.getInstance().getOrder().getCart().get(p)));

            
            Label totalPriceLabel = new Label(String.format("%.2f €",p.getPrice()*UserSession.getInstance().getOrder().getCart().get(p)));

            totalprice += p.getPrice()*UserSession.getInstance().getOrder().getCart().get(p);
            
            // VBox to organize all the information vertically
            VBox infoBox = new VBox(5);
            infoBox.getChildren().addAll(nameLabel, priceLabel, quantityLabel, totalPriceLabel);

            // Create an HBox to align the image and information horizontally
            HBox hbox = new HBox(10); // 10 px spacing
            hbox.setPadding(new Insets(10));
            hbox.getChildren().addAll(imageView, infoBox);

            // Add the HBox to the GridPane
            productGrid.add(hbox, 0, i);
            
            i++;
        }
        price.setText(String.format("%.2f €", totalprice));
    }

    
    @FXML
    public void handleCheckout() {
    	
 	    String firstName = firstNameField.getText();
 	    String lastName = lastNameField.getText();
 	    String street = streetField.getText();
 	    String city = cityField.getText();
 	    String postCode = postCodeField.getText();
 	    String country = countryField.getText();

 	    // Vérification des modifications
 	    String errorMessage = ValidationUtils.verifyModifications(firstName, lastName, street, city, postCode, country);

 	    // Si une erreur est détectée, on l'affiche et on arrête l'exécution
 	    if (errorMessage != null) {
 	        errorLabel.setText(errorMessage);
 	        return;
 	    }

 	    // Effacer le message d'erreur si tout est valide
 	    errorLabel.setText("");

 	    // --- Mise à jour des informations du client ---
 	    user.setFirstName(firstName);
 	    user.setLastName(lastName);
 	    
        // Si l'utilisateur est un client, on met aussi à jour son adresse
        if ("client".equalsIgnoreCase(user.getRole())) {

            // Mise à jour de l'adresse
            user.getAddress().setStreet(street);
            user.getAddress().setCity(city);
            user.getAddress().setPostCode(postCode);
            user.getAddress().setCountry(country);


        }
 	
 	// --- Mise à jour des informations du client et de la commande ---
 	   boolean updateSuccess = true;  // Variable pour suivre le succès global

 	   UserSession.getInstance().getOrder().setStatusFromString("Confirmed");
 	   System.out.println(UserSession.getInstance().getOrder().getStatus());
 	   // Mise à jour du client
 	   updateSuccess &= DataSingleton.getInstance().getUserDAO().updateUser(user);  // On utilise &= pour s'assurer que toutes les étapes réussissent

 	   // Insertion de la commande
 	   int orderID = DataSingleton.getInstance().getOrderDAO().insertOrder(UserSession.getInstance().getOrder());

 	   // Vérification de succès
 	   if (updateSuccess && orderID != -1) {
 	       System.out.println("Les modifications du Client et de la commande ont été enregistrées avec succès !");
 	      UserSession.getInstance().getOrder().setOrderID(orderID);

 	      Invoice invoice = new Invoice(UserSession.getInstance().getOrder().getOrderID(), UserSession.getInstance().getOrder().getClientID(), UserSession.getInstance().getOrder().getPurchaseDate(), UserSession.getInstance().getOrder().calculateCartTotal());
 	      int invoiceID = DataSingleton.getInstance().getInvoiceDAO().insertInvoice(invoice);
 	      
 	      invoice.setInvoiceID(invoiceID);
 	      UserSession.getInstance().setInvoice(invoice);
 	      
 	       // Passage à la scène suivante uniquement si tout a réussi
 	       try {
 	           SceneManager.getInstance().showScene("ValidOrder");
 	       } catch (Exception e) {
 	           e.printStackTrace();
 	       }
 	   } else {
 	       // Affichage d'un message d'erreur générique si l'une des étapes a échoué
 	       errorLabel.setText("Erreur lors de la mise à jour du compte ou de l'insertion de la commande !");
 	   }
 	    
 	
    }
    	
    
}
