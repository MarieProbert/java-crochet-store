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

/**
 * Controller for managing the admin product catalog.
 * Provides functionalities for displaying, adding, modifying, and updating product stock.
 */
public class AdminCatalogController extends BaseController {

    @FXML private GridPane productGrid;
    @FXML private TextField searchField;

    @FXML
    public void initialize() {
        super.initialize();
        displayProducts();
    }
    
    /**
     * Opens a popup window to add a new product.
     */
    @FXML
    private void handleAddProduct() {
        Stage popupStage = new Stage();
        popupStage.setTitle("Add Product");
        popupStage.initModality(Modality.APPLICATION_MODAL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Name field
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);

        // Price field
        Label priceLabel = new Label("Price (€):");
        TextField priceField = new TextField();
        grid.add(priceLabel, 0, 1);
        grid.add(priceField, 1, 1);

        // Creator field
        Label creatorLabel = new Label("Creator:");
        TextField creatorField = new TextField();
        grid.add(creatorLabel, 0, 2);
        grid.add(creatorField, 1, 2);

        // Description field
        Label descriptionLabel = new Label("Description:");
        TextField descriptionField = new TextField();
        grid.add(descriptionLabel, 0, 3);
        grid.add(descriptionField, 1, 3);

        // Category (Enum)
        Label categoryLabel = new Label("Category:");
        ComboBox<Category> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(Category.values());
        categoryComboBox.setValue(Category.values()[0]);
        grid.add(categoryLabel, 0, 4);
        grid.add(categoryComboBox, 1, 4);

        // Size (Enum)
        Label sizeLabel = new Label("Size:");
        ComboBox<Size> sizeComboBox = new ComboBox<>();
        sizeComboBox.getItems().addAll(Size.values());
        sizeComboBox.setValue(Size.values()[0]);
        grid.add(sizeLabel, 0, 5);
        grid.add(sizeComboBox, 1, 5);

        // Color (Enum)
        Label colorLabel = new Label("Color:");
        ComboBox<Color> colorComboBox = new ComboBox<>();
        colorComboBox.getItems().addAll(Color.values());
        colorComboBox.setValue(Color.values()[0]);
        grid.add(colorLabel, 0, 6);
        grid.add(colorComboBox, 1, 6);

        // Stock field
        Label quantityLabel = new Label("Stock:");
        TextField quantityField = new TextField();
        grid.add(quantityLabel, 0, 7);
        grid.add(quantityField, 1, 7);

        // Image path field
        Label imageLabel = new Label("Image path:");
        TextField imageField = new TextField();
        grid.add(imageLabel, 0, 8);
        grid.add(imageField, 1, 8);

        // Error message label
        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red;");
        grid.add(errorLabel, 0, 9, 2, 1);

        // Save and Cancel buttons
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        grid.add(buttonBox, 1, 10);

        saveButton.setOnAction(e -> {
            if (nameField.getText().trim().isEmpty() ||
                priceField.getText().trim().isEmpty() ||
                creatorField.getText().trim().isEmpty() ||
                descriptionField.getText().trim().isEmpty() ||
                categoryComboBox.getValue() == null ||
                sizeComboBox.getValue() == null ||
                colorComboBox.getValue() == null ||
                quantityField.getText().trim().isEmpty() ||
                imageField.getText().trim().isEmpty()) {
                errorLabel.setText("All fields must be filled.");
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException ex) {
                errorLabel.setText("Invalid price format.");
                ex.printStackTrace();
                return;
            }

            int stock;
            try {
                stock = Integer.parseInt(quantityField.getText());
                if (stock <= 0) {
                    errorLabel.setText("Enter a positive number.");
                    return;
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Invalid stock format.");
                ex.printStackTrace();
                return;
            }

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

            boolean inserted = DataSingleton.getInstance().getProductDAO().insertProduct(newProduct);
            if (inserted) {
                popupStage.close();
                DataSingleton.getInstance().getCatalog().addProduct(newProduct);
                displayProducts();
            } else {
                errorLabel.setText("Failed to add product.");
                System.err.println("Failed to add product: " + newProduct);
            }
        });

        cancelButton.setOnAction(e -> popupStage.close());

        Scene scene = new Scene(grid);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    /**
     * Displays all products in the catalog in the product grid.
     */
    private void displayProducts() {
        productGrid.getChildren().clear();
        int row = 0;
        for (Product p : DataSingleton.getInstance().getCatalog().getProducts()) {
            Label idLabel = new Label("ID: " + p.getProductID());
            Label nameLabel = new Label("Name: " + p.getName());
            Label priceLabel = new Label(String.format("Price: %.2f €", p.getPrice()));
            Label stockLabel = new Label(p.getStock() > 0 ? "Quantity: " + p.getStock() : "Out of stock");

            Button addStockButton = new Button("Add stock");
            addStockButton.setOnAction(e -> openAddStockPopup(p));

            Button modifyButton = new Button("Modify");
            modifyButton.setOnAction(e -> openModifyPopup(p));

            HBox buttonBox = new HBox(5, addStockButton, modifyButton);
            HBox infoBox = new HBox(10, idLabel, nameLabel, priceLabel, stockLabel, buttonBox);
            infoBox.setPadding(new Insets(10));

            productGrid.add(infoBox, 0, row++);
        }
    }
    
    /**
     * Opens a popup window to add stock to the specified product.
     * @param product The product for which to add stock.
     */
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
                displayProducts();
            } catch (NumberFormatException ex) {
                errorLabel.setText("Invalid number format.");
                ex.printStackTrace();
            }
        });
        cancelButton.setOnAction(e -> popupStage.close());

        Scene scene = new Scene(grid);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }
    
    /**
     * Opens a popup window to modify the specified product.
     * @param product The product to modify.
     */
    private void openModifyPopup(Product product) {
        Stage popupStage = new Stage();
        popupStage.setTitle("Modify Product");
        popupStage.initModality(Modality.APPLICATION_MODAL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField(product.getName());
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        
        Label priceLabel = new Label("Price (€):");
        TextField priceField = new TextField(String.valueOf(product.getPrice()));
        grid.add(priceLabel, 0, 1);
        grid.add(priceField, 1, 1);

        Label creatorLabel = new Label("Creator:");
        TextField creatorField = new TextField(product.getCreator());
        grid.add(creatorLabel, 0, 2);
        grid.add(creatorField, 1, 2);

        Label colorLabel = new Label("Color:");
        ComboBox<Color> colorComboBox = new ComboBox<>();
        initializeComboBoxWithoutNull(colorComboBox, Color.values(), product.getColor());
        grid.add(colorLabel, 0, 3);
        grid.add(colorComboBox, 1, 3);

        Label sizeLabel = new Label("Size:");
        ComboBox<Size> sizeComboBox = new ComboBox<>();
        initializeComboBoxWithoutNull(sizeComboBox, Size.values(), product.getSize());
        grid.add(sizeLabel, 0, 4);
        grid.add(sizeComboBox, 1, 4);

        Label categoryLabel = new Label("Category:");
        ComboBox<Category> categoryComboBox = new ComboBox<>();
        initializeComboBoxWithoutNull(categoryComboBox, Category.values(), product.getCategory());
        grid.add(categoryLabel, 0, 5);
        grid.add(categoryComboBox, 1, 5);

        Label imagePathLabel = new Label("Image Path:");
        TextField imagePathField = new TextField(product.getImagePath());
        grid.add(imagePathLabel, 0, 6);
        grid.add(imagePathField, 1, 6);

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
                ex.printStackTrace();
                return;
            }
            
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
                displayProducts();
            } else {
                errorLabel.setText("Failed to update product.");
                System.err.println("Failed to update product: " + product);
            }
        });

        cancelButton.setOnAction(e -> popupStage.close());

        Scene scene = new Scene(grid);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }
    
    /**
     * Utility method to initialize an enumeration ComboBox without null options.
     * Places the current value at the top of the list and selects it by default.
     *
     * @param comboBox The ComboBox to initialize.
     * @param values The array of enumeration values.
     * @param currentValue The current value to select.
     * @param <T> The enum type.
     */
    private <T extends Enum<T>> void initializeComboBoxWithoutNull(ComboBox<T> comboBox, T[] values, T currentValue) {
        List<T> items = new ArrayList<>(Arrays.asList(values));
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
