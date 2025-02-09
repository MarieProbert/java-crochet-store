package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import enums.Category;
import enums.Color;
import enums.Size;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import tables.Product;
import util.DataSingleton;
//here
public class AdminCatalogController extends BaseController {
    @FXML private GridPane productGrid;
    @FXML private TextField searchField;

    @FXML
    public void initialize() {
        super.initialize();
        afficherProduits();
    }
    
    
    @FXML
    private void handleAddProduct() {
        // Création d'une nouvelle fenêtre (popup)
        Stage popupStage = new Stage();
        popupStage.setTitle("Add Product");
        popupStage.initModality(Modality.APPLICATION_MODAL);

        // Configuration du layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Champ Nom
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);

        // Champ Prix
        Label priceLabel = new Label("Price (€):");
        TextField priceField = new TextField();
        grid.add(priceLabel, 0, 1);
        grid.add(priceField, 1, 1);

        // Champ Créateur
        Label creatorLabel = new Label("Creator:");
        TextField creatorField = new TextField();
        grid.add(creatorLabel, 0, 2);
        grid.add(creatorField, 1, 2);

        // Champ Description
        Label descriptionLabel = new Label("Description:");
        TextField descriptionField = new TextField();
        grid.add(descriptionLabel, 0, 3);
        grid.add(descriptionField, 1, 3);

        // Champ Catégorie (Enum)
        Label categoryLabel = new Label("Category:");
        ComboBox<Category> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(Category.values());
        categoryComboBox.setValue(Category.values()[0]);
        grid.add(categoryLabel, 0, 4);
        grid.add(categoryComboBox, 1, 4);

        // Champ Taille (Enum)
        Label sizeLabel = new Label("Size:");
        ComboBox<Size> sizeComboBox = new ComboBox<>();
        sizeComboBox.getItems().addAll(Size.values());
        sizeComboBox.setValue(Size.values()[0]);
        grid.add(sizeLabel, 0, 5);
        grid.add(sizeComboBox, 1, 5);

        // Champ Couleur (Enum)
        Label colorLabel = new Label("Color:");
        ComboBox<Color> colorComboBox = new ComboBox<>();
        colorComboBox.getItems().addAll(Color.values());
        colorComboBox.setValue(Color.values()[0]);
        grid.add(colorLabel, 0, 6);
        grid.add(colorComboBox, 1, 6);

        // Champ Stock
        Label quantityLabel = new Label("Stock:");
        TextField quantityField = new TextField();
        grid.add(quantityLabel, 0, 7);
        grid.add(quantityField, 1, 7);

        // Champ Image path
        Label imageLabel = new Label("Image path:");
        TextField imageField = new TextField();
        grid.add(imageLabel, 0, 8);
        grid.add(imageField, 1, 8);

        // Label d'erreur pour afficher d'éventuels messages de validation
        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red;");
        grid.add(errorLabel, 0, 9, 2, 1);

        // Boutons Save et Cancel
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        grid.add(buttonBox, 1, 10);

        // Action du bouton Save
        saveButton.setOnAction(e -> {
            // Vérification que tous les champs sont renseignés
            if (nameField.getText().trim().isEmpty() ||
                priceField.getText().trim().isEmpty() ||
                creatorField.getText().trim().isEmpty() ||
                descriptionField.getText().trim().isEmpty() ||
                categoryComboBox.getValue() == null ||
                sizeComboBox.getValue() == null ||
                colorComboBox.getValue() == null ||
                quantityField.getText().trim().isEmpty() ||
                imageField.getText().trim().isEmpty()) {
                errorLabel.setText("Tous les champs doivent être remplis.");
                return;
            }

            // Vérification du format du prix
            double price;
            try {
                price = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException ex) {
                errorLabel.setText("Format de prix invalide.");
                return;
            }

            // Vérification du format du stock
            int stock;
            try {
                stock = Integer.parseInt(quantityField.getText());
                if (stock <= 0) {
                    errorLabel.setText("Enter a positive number.");
                    return;
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Format du stock invalide.");
                return;
            }

            // Création d'un nouveau produit
            Product newProduct = new Product();
            newProduct.setName(nameField.getText());
            newProduct.setPrice(price);
            newProduct.setCreator(creatorField.getText());
            newProduct.setDescription(descriptionField.getText());
            newProduct.setCategory(categoryComboBox.getValue());
            newProduct.setSize(sizeComboBox.getValue());
            newProduct.setColor(colorComboBox.getValue());
            newProduct.setStock(stock);
            newProduct.setImagePath(imageField.getText());

            // Insertion du produit via le ProductDAO
            boolean inserted = DataSingleton.getInstance().getProductDAO().insertProduct(newProduct);
            if (inserted) {
                popupStage.close();
                afficherProduits(); // Rafraîchissement de l'affichage
            } else {
                errorLabel.setText("Échec de l'ajout du produit.");
            }
        });

        // Action du bouton Cancel
        cancelButton.setOnAction(e -> popupStage.close());

        // Affichage de la popup
        Scene scene = new Scene(grid);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }


    private void afficherProduits() {
        productGrid.getChildren().clear();
        int row = 0;
        for (Product p : DataSingleton.getInstance().getCatalog().getProducts()) {
            // Création de l'image et des labels (simplifiés ici)
        	

            Label idLabel = new Label("ID : " + p.getProductID());
            Label nameLabel = new Label("Name : " + p.getName());
            Label priceLabel = new Label(String.format("Price : %.2f €", p.getPrice()));
            Label stockLabel = new Label(p.getStock() > 0 ? "Quantity : " + p.getStock() : "Out of stock");
            
            // Bouton 'Add stock'
            Button addStockButton = new Button("Add stock");
            addStockButton.setOnAction(e -> openAddStockPopup(p));
            
            // Bouton 'Modify'
            Button modifyButton = new Button("Modify");
            modifyButton.setOnAction(e -> openModifyPopup(p));
            
            HBox buttonBox = new HBox(5, addStockButton, modifyButton);
            HBox infoBox = new HBox(10, idLabel, nameLabel, priceLabel, stockLabel, buttonBox);
            infoBox.setPadding(new Insets(10));
            
            productGrid.add(infoBox, 0, row);
            row++;
        }
    }
    
    private void openAddStockPopup(Product product) {
        Stage popupStage = new Stage();
        popupStage.setTitle("Add Stock");
        popupStage.initModality(Modality.APPLICATION_MODAL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Label instructionLabel = new Label("Enter quantity to add:");
        TextField quantityField = new TextField();
        grid.add(instructionLabel, 0, 0);
        grid.add(quantityField, 1, 0);

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red;");
        grid.add(errorLabel, 0, 1, 2, 1);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        grid.add(buttonBox, 1, 2);

        saveButton.setOnAction(e -> {
            try {
                int quantityToAdd = Integer.parseInt(quantityField.getText());
                if (quantityToAdd <= 0) {
                    errorLabel.setText("Enter a positive number.");
                    return;
                }
                product.setStock(product.getStock() + quantityToAdd);
                DataSingleton.getInstance().getProductDAO().updateProduct(product);
                popupStage.close();
                afficherProduits();
            } catch (NumberFormatException ex) {
                errorLabel.setText("Invalid number format.");
            }
        });
        cancelButton.setOnAction(e -> popupStage.close());

        Scene scene = new Scene(grid);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }
    
    /**
     * Ouvre la fenêtre de modification d'un produit.
     * Pour les champs de type énumération (Color, Size et Category), on utilise des ComboBox
     * qui affichent en premier la valeur actuelle du produit.
     */
    private void openModifyPopup(Product product) {
        Stage popupStage = new Stage();
        popupStage.setTitle("Modify Product");
        popupStage.initModality(Modality.APPLICATION_MODAL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Champ Name
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField(product.getName());
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        
        // Champ Name
        Label priceLabel = new Label("Price (€):");
        TextField priceField = new TextField(String.valueOf(product.getPrice()));
        grid.add(priceLabel, 0, 1);
        grid.add(priceField, 1, 1);

        // Champ Creator
        Label creatorLabel = new Label("Creator:");
        TextField creatorField = new TextField(product.getCreator());
        grid.add(creatorLabel, 0, 2);
        grid.add(creatorField, 1, 2);

        // Champ Color (Enum)
        Label colorLabel = new Label("Color:");
        ComboBox<Color> colorComboBox = new ComboBox<>();
        initializeComboBoxWithoutNull(colorComboBox, Color.values(), product.getColor());
        grid.add(colorLabel, 0, 3);
        grid.add(colorComboBox, 1, 3);

        // Champ Size (Enum)
        Label sizeLabel = new Label("Size:");
        ComboBox<Size> sizeComboBox = new ComboBox<>();
        initializeComboBoxWithoutNull(sizeComboBox, Size.values(), product.getSize());
        grid.add(sizeLabel, 0, 4);
        grid.add(sizeComboBox, 1, 4);

        // Champ Category (Enum)
        Label categoryLabel = new Label("Category:");
        ComboBox<Category> categoryComboBox = new ComboBox<>();
        initializeComboBoxWithoutNull(categoryComboBox, Category.values(), product.getCategory());
        grid.add(categoryLabel, 0, 5);
        grid.add(categoryComboBox, 1, 5);

        // Champ Image Path
        Label imagePathLabel = new Label("Image Path:");
        TextField imagePathField = new TextField(product.getImagePath());
        grid.add(imagePathLabel, 0, 6);
        grid.add(imagePathField, 1, 6);

        // Champ Description
        Label descriptionLabel = new Label("Description:");
        TextField descriptionField = new TextField(product.getDescription());
        grid.add(descriptionLabel, 0, 7);
        grid.add(descriptionField, 1, 7);

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red;");
        grid.add(errorLabel, 0, 8, 2, 1);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        grid.add(buttonBox, 1, 9);

        saveButton.setOnAction(e -> {
            if (nameField.getText().isEmpty() || creatorField.getText().isEmpty()) {
                errorLabel.setText("Name and Creator cannot be empty.");
                return;
            }
            
            try {
                double price = Double.parseDouble(priceField.getText());
                product.setPrice(price);
            } catch (NumberFormatException ex) {
                errorLabel.setText("Invalid price format.");
                return;
            }
            
            // Mise à jour des informations du produit
            product.setName(nameField.getText());
            product.setCreator(creatorField.getText());
            product.setColor(colorComboBox.getValue());
            product.setSize(sizeComboBox.getValue());
            product.setCategory(categoryComboBox.getValue());
            product.setImagePath(imagePathField.getText());
            product.setDescription(descriptionField.getText());
            
            boolean updated = DataSingleton.getInstance().getProductDAO().updateProduct(product);
            if (updated) {
                popupStage.close();
                afficherProduits();
            } else {
                errorLabel.setText("Failed to update product.");
            }
        });

        cancelButton.setOnAction(e -> popupStage.close());

        Scene scene = new Scene(grid);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }
    
    /**
     * Méthode utilitaire pour initialiser une ComboBox d'énumération
     * sans proposer d'option nulle. La valeur courante est positionnée en tête
     * de liste et sélectionnée par défaut.
     *
     * @param comboBox La ComboBox à initialiser.
     * @param values   Le tableau de valeurs de l'énumération.
     * @param currentValue La valeur actuelle à sélectionner.
     * @param <T>      Le type d'énumération.
     */
    private <T extends Enum<T>> void initializeComboBoxWithoutNull(ComboBox<T> comboBox, T[] values, T currentValue) {
        List<T> items = new ArrayList<>(Arrays.asList(values));
        // Positionne la valeur actuelle en tête de liste
        items.remove(currentValue);
        items.add(0, currentValue);
        comboBox.getItems().setAll(items);
        comboBox.setValue(currentValue);
        comboBox.setConverter(new StringConverter<T>() {
            @Override
            public String toString(T value) {
                return value == null ? "" : value.toString();
            }
            @Override
            public T fromString(String string) {
                return Enum.valueOf(currentValue.getDeclaringClass(), string);
            }
        });
    }
}
