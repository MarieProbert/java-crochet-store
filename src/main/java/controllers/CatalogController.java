package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

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
     * Classe interne pour analyser la chaîne de recherche en respectant la priorité des opérateurs.
     */
    private class QueryParser {
        private String[] tokens;
        private int index;

        public QueryParser(String input) {
            // On ajoute des espaces autour des parenthèses pour que le découpage se fasse correctement.
            input = input.replace("(", " ( ").replace(")", " ) ");
            tokens = input.trim().split("\\s+");
            index = 0;
        }

        /**
         * Analyse l'expression complète et renvoie le prédicat correspondant.
         */
        public Predicate<Product> parse() throws Exception {
            Predicate<Product> result = parseExpression();
            if (index < tokens.length) {
                throw new Exception("Token non consommé : " + tokens[index]);
            }
            return result;
        }

        /**
         * Expression : Term { "or" Term }
         */
        private Predicate<Product> parseExpression() throws Exception {
            Predicate<Product> term = parseTerm();
            while (index < tokens.length && tokens[index].equals("or")) {
                index++; // consomme le token "or"
                Predicate<Product> nextTerm = parseTerm();
                term = term.or(nextTerm);
            }
            return term;
        }

        /**
         * Term : Factor { "and" Factor }
         */
        private Predicate<Product> parseTerm() throws Exception {
            Predicate<Product> factor = parseFactor();
            while (index < tokens.length && tokens[index].equals("and")) {
                index++; // consomme le token "and"
                Predicate<Product> nextFactor = parseFactor();
                factor = factor.and(nextFactor);
            }
            return factor;
        }

        /**
         * Factor : ( Expression ) | keyword
         */
        private Predicate<Product> parseFactor() throws Exception {
            if (index >= tokens.length) {
                throw new Exception("Requête incomplète");
            }
            String token = tokens[index];
            if (token.equals("(")) {
                index++; // consomme "("
                Predicate<Product> expr = parseExpression();
                if (index >= tokens.length || !tokens[index].equals(")")) {
                    throw new Exception("Parenthèse fermante attendue");
                }
                index++; // consomme ")"
                return expr;
            } else {
                // Considère le token comme un mot-clé
                index++; // consomme le mot-clé
                return productMatches(token);
            }
        }

        /**
         * Renvoie un prédicat qui teste si un produit contient le mot-clé dans l'un de ses champs textuels.
         */
        private Predicate<Product> productMatches(String keyword) {
            return p -> p.getCreator().toLowerCase().contains(keyword)
                      || p.getName().toLowerCase().contains(keyword)
                      || p.getDescription().toLowerCase().contains(keyword);
        }
    }





    /**
     * Effectue la recherche en combinant la recherche textuelle avec les filtres (prix, catégorie, etc.).
     */
    private List<Product> searchProducts(String searchText) {
        List<Product> results = new ArrayList<>();

        // Lecture des filtres de prix
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
        
        // Lecture des autres filtres (catégorie, thème, taille, couleur)
        Category selectedCategory = categoryComboBox.getValue();
        Theme selectedTheme = themeComboBox.getValue();
        Size selectedSize = sizeComboBox.getValue();
        Color selectedColor = colorComboBox.getValue();

        // Construction du prédicat de recherche à partir de la chaîne saisie.
        // Si la chaîne est vide, on considère que le filtre textuel est toujours vrai.
        Predicate<Product> searchPredicate = p -> true;
        String normalizedSearch = (searchText == null ? "" : searchText.trim());
        if (!normalizedSearch.isEmpty()) {
            try {
                QueryParser parser = new QueryParser(normalizedSearch.toLowerCase());
                searchPredicate = parser.parse();
            } catch (Exception e) {
                System.out.println("Erreur lors de l'analyse de la requête de recherche : " + e.getMessage());
                // En cas d'erreur de parsing, on effectue une recherche simple par mot-clé
                String query = normalizedSearch.toLowerCase();
                searchPredicate = p -> p.getCreator().toLowerCase().contains(query)
                                   || p.getName().toLowerCase().contains(query)
                                   || p.getDescription().toLowerCase().contains(query);
            }
        }

        // Parcourt tous les produits et applique les différents filtres
        for (Product p : DataSingleton.getInstance().getCatalog().getProducts()) {
            // Vérification de la recherche textuelle
            if (!searchPredicate.test(p)) {
                continue;
            }
            // Vérification des filtres de prix
            double price = p.getPrice();
            if (minPrice >= 0 && price < minPrice) {
                continue;
            }
            if (maxPrice >= 0 && price > maxPrice) {
                continue;
            }
            // Vérification des autres filtres
            if (selectedCategory != null && p.getCategory() != selectedCategory) {
                continue;
            }
            if (selectedTheme != null && p.getTheme() != selectedTheme) {
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





