package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import enums.Category;
import enums.Color;
import enums.Size;
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
 * Controller for managing the product catalog screen.
 * Supports filtering (by price, category, size, and color), searching using a query parser,
 * and dynamic pagination based on window size.
 */
public class CatalogController extends BaseController {

    @FXML private GridPane productGrid;
    @FXML private TextField searchField;
    @FXML private Button searchButton;

    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private ComboBox<Size> sizeComboBox;
    @FXML private ComboBox<Color> colorComboBox;

    @FXML private Pagination pagination;

    // Catalog currently displayed (loaded in memory)
    private Catalog catalog;

    // Number of products per page (dynamically calculated: columns * 2)
    private int PRODUCTS_PER_PAGE = 4;

    /**
     * Initializes the catalog controller by loading the full catalog and setting up filters and listeners.
     */
    @FXML
    public void initialize() {
        super.initialize();
        catalog = DataSingleton.getInstance().getCatalog();

        initializeComboBoxWithAllOption(categoryComboBox, Category.values());
        initializeComboBoxWithAllOption(sizeComboBox, Size.values());
        initializeComboBoxWithAllOption(colorComboBox, Color.values());

        // Adjust the grid when the scene is resized.
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

        // Update grid when pagination page changes.
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> updateGrid());
    }

    /**
     * Initializes a ComboBox by adding a null (interpreted as "All") option along with all enum values.
     *
     * @param <T>      the enum type
     * @param comboBox the ComboBox to initialize
     * @param values   the array of enum values
     */
    private <T extends Enum<T>> void initializeComboBoxWithAllOption(ComboBox<T> comboBox, T[] values) {
        List<T> items = new ArrayList<>();
        items.add(null); // Represents "All"
        items.addAll(Arrays.asList(values));
        comboBox.getItems().setAll(items);

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
     * Handles the search action when the "Search" button is clicked.
     * Reads filter values, performs a search, updates the catalog, and refreshes the display.
     */
    @FXML
    private void handleSearch() {
        clearMessage();
        String searchText = searchField.getText().trim();
        List<Product> filteredProducts = searchProducts(searchText);

        Catalog newCatalog = new Catalog();
        for (Product p : filteredProducts) {
            newCatalog.addProduct(p);
        }
        catalog = newCatalog;

        updateGrid();
    }

    /**
     * Inner class that parses a search query string and builds a corresponding Predicate.
     * The parser respects operator precedence (with "and" having higher precedence than "or").
     */
    private class QueryParser {
        private String[] tokens;
        private int index;

        public QueryParser(String input) {
            // Add spaces around parentheses for proper tokenization.
            input = input.replace("(", " ( ").replace(")", " ) ");
            tokens = input.trim().split("\\s+");
            index = 0;
        }

        /**
         * Parses the entire expression and returns the corresponding predicate.
         *
         * @return a Predicate for Product matching the query
         * @throws Exception if a parsing error occurs
         */
        public Predicate<Product> parse() throws Exception {
            Predicate<Product> result = parseExpression();
            if (index < tokens.length) {
                showErrorMessage("Syntax error: Unconsumed token: " + tokens[index]);
                return null;
            }
            return result;
        }

        /**
         * Parses an expression: Term { "or" Term }
         */
        private Predicate<Product> parseExpression() throws Exception {
            Predicate<Product> term = parseTerm();
            while (index < tokens.length && tokens[index].equals("or")) {
                index++; // Consume "or"
                Predicate<Product> nextTerm = parseTerm();
                term = term.or(nextTerm);
            }
            return term;
        }

        /**
         * Parses a term: Factor { "and" Factor }
         */
        private Predicate<Product> parseTerm() throws Exception {
            Predicate<Product> factor = parseFactor();
            while (index < tokens.length && tokens[index].equals("and")) {
                index++; // Consume "and"
                Predicate<Product> nextFactor = parseFactor();
                factor = factor.and(nextFactor);
            }
            return factor;
        }

        /**
         * Parses a factor: either an expression within parentheses or a keyword.
         */
        private Predicate<Product> parseFactor() throws Exception {
            if (index >= tokens.length) {
                showErrorMessage("Incomplete query");
                return null;
            }
            String token = tokens[index];
            if (token.equals("(")) {
                index++; // Consume "("
                Predicate<Product> expr = parseExpression();
                if (index >= tokens.length || !tokens[index].equals(")")) {
                    showErrorMessage("Closing parenthesis expected");
                    return null;
                }
                index++; // Consume ")"
                return expr;
            } else {
                index++; // Consume keyword
                return productMatches(token);
            }
        }

        /**
         * Returns a predicate that tests if a product contains the keyword in one of its text fields.
         *
         * @param keyword the search keyword
         * @return the predicate testing the product's creator, name, or description
         */
        private Predicate<Product> productMatches(String keyword) {
            return p -> p.getCreator().toLowerCase().contains(keyword)
                     || p.getName().toLowerCase().contains(keyword)
                     || p.getDescription().toLowerCase().contains(keyword);
        }
    }

    /**
     * Combines textual search with filters (price, category, size, and color) and returns matching products.
     *
     * @param searchText the search text entered by the user
     * @return a list of products that match all criteria
     */
    private List<Product> searchProducts(String searchText) {
        List<Product> results = new ArrayList<>();

        double minPrice = -1, maxPrice = -1;
        if (!minPriceField.getText().trim().isEmpty()) {
            try {
                minPrice = Double.parseDouble(minPriceField.getText().trim());
            } catch (NumberFormatException e) {
                showErrorMessage("Invalid minimum price format.");
            }
        }
        if (!maxPriceField.getText().trim().isEmpty()) {
            try {
                maxPrice = Double.parseDouble(maxPriceField.getText().trim());
            } catch (NumberFormatException e) {
                showErrorMessage("Invalid maximum price format.");
            }
        }

        Category selectedCategory = categoryComboBox.getValue();
        Size selectedSize = sizeComboBox.getValue();
        Color selectedColor = colorComboBox.getValue();

        Predicate<Product> searchPredicate = p -> true;
        String normalizedSearch = (searchText == null ? "" : searchText.trim());
        if (!normalizedSearch.isEmpty()) {
            try {
                QueryParser parser = new QueryParser(normalizedSearch.toLowerCase());
                searchPredicate = parser.parse();
            } catch (Exception e) {
                showErrorMessage("Error parsing search query: " + e.getMessage());
                String query = normalizedSearch.toLowerCase();
                searchPredicate = p -> p.getCreator().toLowerCase().contains(query)
                                     || p.getName().toLowerCase().contains(query)
                                     || p.getDescription().toLowerCase().contains(query);
            }
        }

        for (Product p : DataSingleton.getInstance().getCatalog().getProducts()) {
            if (!searchPredicate.test(p)) {
                continue;
            }
            double price = p.getPrice();
            if (minPrice >= 0 && price < minPrice) {
                continue;
            }
            if (maxPrice >= 0 && price > maxPrice) {
                continue;
            }
            if (selectedCategory != null && p.getCategory() != selectedCategory) {
                continue;
            }
            if (selectedSize != null && p.getSize() != selectedSize) {
                continue;
            }
            if (selectedColor != null && p.getColor() != selectedColor) {
                continue;
            }
            results.add(p);
        }
        return results;
    }

    /**
     * Updates the product grid and pagination. Dynamically calculates the number of columns based on window width
     * and displays only two rows per page.
     */
    private void updateGrid() {
        if (productGrid.getScene() == null) return;

        productGrid.getChildren().clear();

        double gridWidth = productGrid.getScene().getWidth() - 200;
        double productBoxWidth = 200; // Approximate width of a product box
        int columns = Math.max(1, (int) (gridWidth / productBoxWidth));

        PRODUCTS_PER_PAGE = columns * 2;
        int totalProducts = catalog.getProducts().size();
        int pageCount = (int) Math.ceil((double) totalProducts / PRODUCTS_PER_PAGE);
        pagination.setPageCount(pageCount);

        int currentPage = pagination.getCurrentPageIndex();
        displayProducts(currentPage, columns);
    }

    /**
     * Displays the products for the given page index in the grid.
     *
     * @param pageIndex the index of the page to display
     * @param columns   the number of columns in the grid
     */
    private void displayProducts(int pageIndex, int columns) {
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
     * Creates a VBox displaying product details (image, name, price, and an "Add to cart" button).
     *
     * @param product the product to display
     * @return a VBox containing the product details
     */
    private VBox createProductBox(Product product) {
        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(10));

        Image image = loadImage(product.getImagePath(), "defaultImagePath.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        vbox.getChildren().add(imageView);

        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-underline: true;");
        nameLabel.setOnMouseClicked(e -> {
            boolean success = SceneManager.getInstance().showProductScene(product);
            if (!success) {
                System.out.println("Error: Unable to display product scene.");
            }
        });
        vbox.getChildren().add(nameLabel);

        Label priceLabel = new Label(String.format("%.2f €", product.getPrice()));
        vbox.getChildren().add(priceLabel);

        Button addToCartButton = new Button("Add to cart");
        addToCartButton.setOnAction(e -> {
            boolean success = UserSession.getInstance().getOrder().addToCart(product, 1);
            if (success) {
                showInfoMessage("Product added to cart");
            } else {
                showErrorMessage("Not enough stock");
            }
        });
        vbox.getChildren().add(addToCartButton);

        return vbox;
    }
}
