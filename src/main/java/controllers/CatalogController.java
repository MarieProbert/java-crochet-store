package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import enums.Category;
import enums.Color;
import enums.Size;
import enums.Theme;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import tables.Catalog;
import tables.Product;
import util.DataSingleton;
import util.SceneManager;
import util.UserSession;

/**
 * Controller for managing the product catalog screen, including filtering, searching, and displaying products.
 * Handles filtering based on various criteria like price, category, theme, size, and color, and manages pagination.
 */
public class CatalogController extends BaseController {

    @FXML private GridPane productGrid;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    
    // Additional filters
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private ComboBox<Theme> themeComboBox;
    @FXML private ComboBox<Size> sizeComboBox;
    @FXML private ComboBox<Color> colorComboBox;
    
    @FXML private Pagination pagination;
    
    // Catalog currently displayed (loaded into memory)
    private Catalog catalog;
    
    // Number of products per page (2 rows max: calculated dynamically = columns * 2)
    private int PRODUCTS_PER_PAGE = 4;

    /**
     * Initializes the CatalogController, loading the complete catalog and setting up filters.
     * Adds listeners to handle screen resizing and pagination changes.
     */
    @FXML
    public void initialize() {
        super.initialize();
        
        // Load the full catalog initially
        catalog = DataSingleton.getInstance().getCatalog();
        
        // Initialize ComboBoxes with all possible values for each enum
        initializeComboBoxWithAllOption(categoryComboBox, Category.values());
        initializeComboBoxWithAllOption(themeComboBox, Theme.values());
        initializeComboBoxWithAllOption(sizeComboBox, Size.values());
        initializeComboBoxWithAllOption(colorComboBox, Color.values());

        // Add listener to adjust the display when the scene is resized
        productGrid.sceneProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends javafx.scene.Scene> observable,
                                javafx.scene.Scene oldScene, javafx.scene.Scene newScene) {
                if (newScene != null) {
                    newScene.widthProperty().addListener((obs, oldWidth, newWidth) -> updateGrid());
                    newScene.heightProperty().addListener((obs, oldHeight, newHeight) -> updateGrid());
                    updateGrid();
                }
            }
        });
        
        // Configure pagination: when the user changes the page, we update the grid
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> updateGrid());
    }

    /**
     * Initializes a ComboBox with an "All" option and the values from the provided enum array.
     * 
     * @param comboBox the ComboBox to initialize
     * @param values the values of the enum to populate the ComboBox
     */
    private <T extends Enum<T>> void initializeComboBoxWithAllOption(ComboBox<T> comboBox, T[] values) {
        List<T> items = new ArrayList<>();
        items.add(null); // "All"
        items.addAll(Arrays.asList(values));
        comboBox.getItems().setAll(items);

        // Set up display to show "All" for the null value
        comboBox.setConverter(new StringConverter<T>() {
            @Override
            public String toString(T value) {
                return value == null ? "All" : value.toString();
            }

            @Override
            public T fromString(String string) {
                return string.equals("All") ? null : Enum.valueOf(values[0].getDeclaringClass(), string);
            }
        });
    }

    /**
     * Method called when the "Search" button is clicked.
     * It retrieves the values from the search field, price filters, and ComboBox selections,
     * then filters the catalog in memory to display products that match the criteria.
     */
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();
        System.out.println("Searching for products by creator containing: " + searchText);
        
        // Perform search with all criteria
        List<Product> filteredProducts = searchProducts(searchText);
        
        // Create a new catalog with only the filtered products
        Catalog newCatalog = new Catalog();
        for (Product p : filteredProducts) {
            newCatalog.addProduct(p);
        }
        catalog = newCatalog;
        
        // Update display (pagination and grid)
        updateGrid();
    }

    /**
     * Performs the product search based on the entered search text, price filters, and selected category, theme, size, and color.
     * 
     * @param searchText the search text entered by the user
     * @return a list of products that match the search criteria
     */
    private List<Product> searchProducts(String searchText) {
        List<Product> results = new ArrayList<>();

        // Normalize search text (case insensitive)
        String lowerSearch = (searchText == null ? "" : searchText.toLowerCase()).trim();

        // Read price filters
        double minPrice = -1, maxPrice = -1;
        if (!minPriceField.getText().trim().isEmpty()) {
            try {
                minPrice = Double.parseDouble(minPriceField.getText().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid minimum price format.");
            }
        }
        if (!maxPriceField.getText().trim().isEmpty()) {
            try {
                maxPrice = Double.parseDouble(maxPriceField.getText().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid maximum price format.");
            }
        }

        // Read ComboBox filters for category, theme, size, and color
        Category selectedCategory = categoryComboBox.getValue();
        Theme selectedTheme = themeComboBox.getValue();
        Size selectedSize = sizeComboBox.getValue();
        Color selectedColor = colorComboBox.getValue();

        // Detect logical operator in the query
        boolean useAndOperator = lowerSearch.contains(" and ");
        boolean useOrOperator = lowerSearch.contains(" or ");

        // Split the query into keywords based on the operator
        String[] keywords;
        if (useAndOperator) {
            keywords = lowerSearch.split(" and ");
        } else if (useOrOperator) {
            keywords = lowerSearch.split(" or ");
        } else {
            keywords = new String[]{lowerSearch}; // Simple search
        }

        // Check all products in the catalog
        for (Product p : DataSingleton.getInstance().getCatalog().getProducts()) {
            boolean matchesSearch = false;
            if (useAndOperator) {
                matchesSearch = true;
                for (String keyword : keywords) {
                    if (!(p.getCreator().toLowerCase().contains(keyword) ||
                          p.getName().toLowerCase().contains(keyword) ||
                          p.getDescription().toLowerCase().contains(keyword))) {
                        matchesSearch = false;
                        break;
                    }
                }
            } else if (useOrOperator) {
                for (String keyword : keywords) {
                    if (p.getCreator().toLowerCase().contains(keyword) ||
                        p.getName().toLowerCase().contains(keyword) ||
                        p.getDescription().toLowerCase().contains(keyword)) {
                        matchesSearch = true;
                        break;
                    }
                }
            } else {
                matchesSearch = lowerSearch.isEmpty() ||
                    p.getCreator().toLowerCase().contains(lowerSearch) ||
                    p.getName().toLowerCase().contains(lowerSearch) ||
                    p.getDescription().toLowerCase().contains(lowerSearch);
            }

            // Check price filter, if provided
            boolean matchesPrice = true;
            double price = p.getPrice();
            if (minPrice >= 0 && price < minPrice) {
                matchesPrice = false;
            }
            if (maxPrice >= 0 && price > maxPrice) {
                matchesPrice = false;
            }

            // Check other filters (if selected)
            boolean matchesCategory = (selectedCategory == null) || (p.getCategory() == selectedCategory);
            boolean matchesTheme = (selectedTheme == null) || (p.getTheme() == selectedTheme);
            boolean matchesSize = (selectedSize == null) || (p.getSize() == selectedSize);
            boolean matchesColor = (selectedColor == null) || (p.getColor() == selectedColor);

            // Product must match all criteria
            if (matchesSearch && matchesPrice && matchesCategory && matchesTheme && matchesSize && matchesColor) {
                results.add(p);
            }
        }
        return results;
    }

    /**
     * Updates the product grid and pagination.
     * The number of columns is calculated dynamically based on the window width.
     * Only 2 rows of products are displayed per page.
     */
    private void updateGrid() {
        if (productGrid.getScene() == null) return;
        
        productGrid.getChildren().clear();
        
        // Calculate available width in the scene (with margin)
        double gridWidth = productGrid.getScene().getWidth() - 200;
        double productBoxWidth = 200; // approximate width of a VBox for a product
        int columns = Math.max(1, (int) (gridWidth / productBoxWidth));
        
        // Limit display to 2 rows per page
        PRODUCTS_PER_PAGE = columns * 2;
        
        // Calculate total number of pages based on filtered product count
        int totalProducts = catalog.getProducts().size();
        int pageCount = (int) Math.ceil((double) totalProducts / PRODUCTS_PER_PAGE);
        pagination.setPageCount(pageCount);
        
        // Get the current page index and display the corresponding products
        int currentPage = pagination.getCurrentPageIndex();
        afficherProduits(currentPage, columns);
    }

    /**
     * Displays the products of the specified page in the GridPane.
     * 
     * @param pageIndex the index of the page to display
     * @param columns the dynamically calculated number of columns
     */
    private void afficherProduits(int pageIndex, int columns) {
        productGrid.getChildren().clear();
        int startIndex = pageIndex * PRODUCTS_PER_PAGE;
        int endIndex = Math.min(startIndex + PRODUCTS_PER_PAGE, catalog.getProducts().size());
        
        int col = 0, row = 0;
        for (int i = startIndex; i < endIndex; i++) {
            Product product = catalog.getProducts().get(i);
            VBox productBox = createProductBox(product);
            productGrid.add(productBox, col, row);
            col++;
            if (col >= columns) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Creates a VBox displaying product details (image, name, price, "Add to cart" button).
     * 
     * @param product the product to display
     * @return the VBox containing the product details
     */
    private VBox createProductBox(Product product) {
        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(10));
        
        // Load product image
        Image image = loadImage(product.getImagePath(), "defaultImagePath.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        vbox.getChildren().add(imageView);
        
        // Product name (click to view details)
        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-underline: true;");
        nameLabel.setOnMouseClicked(e -> SceneManager.getInstance().showProductScene(product));
        vbox.getChildren().add(nameLabel);
        
        // Product price
        Label priceLabel = new Label(String.format("%.2f €", product.getPrice()));
        vbox.getChildren().add(priceLabel);
        
        // "Add to cart" button
        Button addToCartButton = new Button("Add to cart");
        addToCartButton.setOnAction(e -> {
            UserSession.getInstance().getOrder().addToCart(product, 1);
            System.out.println("Cart updated: " + UserSession.getInstance().getOrder().getCart().keySet());
        });
        vbox.getChildren().add(addToCartButton);
        
        return vbox;
    }
    
}
