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

public class CatalogController extends BaseController {

    @FXML private GridPane productGrid;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    
    // Filtres supplémentaires
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private ComboBox<Theme> themeComboBox;
    @FXML private ComboBox<Size> sizeComboBox;
    @FXML private ComboBox<Color> colorComboBox;
    
    @FXML private Pagination pagination;
    
    // Catalogue actuellement affiché (objet chargé en mémoire)
    private Catalog catalog;
    
    // Nombre de produits par page (2 lignes maximum : calculé dynamiquement = colonnes * 2)
    private int PRODUCTS_PER_PAGE = 4;

    @FXML
    public void initialize() {
        super.initialize();
        
        // Charger le catalogue complet initialement
        catalog = DataSingleton.getInstance().getCatalog();
        
        // Initialiser les ComboBox avec toutes les valeurs possibles de chaque enum
        initializeComboBoxWithAllOption(categoryComboBox, Category.values());
        initializeComboBoxWithAllOption(themeComboBox, Theme.values());
        initializeComboBoxWithAllOption(sizeComboBox, Size.values());
        initializeComboBoxWithAllOption(colorComboBox, Color.values());

        // Ajouter un listener sur la scène pour adapter l'affichage en cas de redimensionnement
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
        
        // Configurer la pagination : lorsque l'utilisateur change de page, on met à jour la grille
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> updateGrid());
    }
    
    private <T extends Enum<T>> void initializeComboBoxWithAllOption(ComboBox<T> comboBox, T[] values) {
        List<T> items = new ArrayList<>();
        items.add(null); // "Tous"
        items.addAll(Arrays.asList(values));
        comboBox.getItems().setAll(items);

        // Configurer l'affichage avec "Tous" pour la valeur null
        comboBox.setConverter(new StringConverter<T>() {
            @Override
            public String toString(T value) {
                return value == null ? "Tous" : value.toString();
            }

            @Override
            public T fromString(String string) {
                return string.equals("Tous") ? null : Enum.valueOf(values[0].getDeclaringClass(), string);
            }
        });
    }
    
    /**
     * Méthode appelée lors du clic sur le bouton "Rechercher".
     * Elle récupère la valeur du champ de recherche (qui s'applique désormais au créateur),
     * les valeurs saisies dans les TextField de prix et les sélections dans les ComboBox,
     * puis filtre le catalogue en mémoire pour afficher l'intersection de ces critères.
     */
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();
        System.out.println("Recherche de produit pour le créateur contenant : " + searchText);
        
        // Effectuer la recherche avec l'ensemble des critères
        List<Product> filteredProducts = searchProducts(searchText);
        
        // Créer un nouveau catalogue avec uniquement les produits filtrés
        Catalog newCatalog = new Catalog();
        for (Product p : filteredProducts) {
            newCatalog.addProduct(p);
        }
        catalog = newCatalog;
        
        // Mettre à jour l'affichage (pagination et grille)
        updateGrid();
    }
    
    private List<Product> searchProducts(String searchText) {
        List<Product> results = new ArrayList<>();

        // Normaliser le texte de recherche (insensible à la casse)
        String lowerSearch = (searchText == null ? "" : searchText.toLowerCase()).trim();

        // Lire le filtre de prix
        double minPrice = -1, maxPrice = -1;
        if (!minPriceField.getText().trim().isEmpty()) {
            try {
                minPrice = Double.parseDouble(minPriceField.getText().trim());
            } catch (NumberFormatException e) {
                System.out.println("Format de prix minimum invalide.");
            }
        }
        if (!maxPriceField.getText().trim().isEmpty()) {
            try {
                maxPrice = Double.parseDouble(maxPriceField.getText().trim());
            } catch (NumberFormatException e) {
                System.out.println("Format de prix maximum invalide.");
            }
        }

        // Lire les filtres des ComboBox pour catégorie, thème, taille et couleur
        Category selectedCategory = categoryComboBox.getValue();
        Theme selectedTheme = themeComboBox.getValue();
        Size selectedSize = sizeComboBox.getValue();
        Color selectedColor = colorComboBox.getValue();

        // Détecter l'opérateur logique dans la requête
        boolean useAndOperator = lowerSearch.contains(" and ");
        boolean useOrOperator = lowerSearch.contains(" or ");

        // Diviser la requête en mots-clés en fonction de l'opérateur
        String[] keywords;
        if (useAndOperator) {
            keywords = lowerSearch.split(" and ");
        } else if (useOrOperator) {
            keywords = lowerSearch.split(" or ");
        } else {
            keywords = new String[]{lowerSearch}; // Recherche simple
        }

        // Parcourir tous les produits du catalogue complet
        for (Product p : DataSingleton.getInstance().getCatalog().getProducts()) {
            // Vérifier la recherche textuelle dans le titre, la description et le nom du créateur
            boolean matchesSearch = false;
            if (useAndOperator) {
                // Tous les mots-clés doivent être présents dans au moins un des champs
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
                // Au moins un des mots-clés doit être présent dans l'un des champs
                for (String keyword : keywords) {
                    if (p.getCreator().toLowerCase().contains(keyword) ||
                        p.getName().toLowerCase().contains(keyword) ||
                        p.getDescription().toLowerCase().contains(keyword)) {
                        matchesSearch = true;
                        break;
                    }
                }
            } else {
                // Recherche simple (sans opérateur)
                matchesSearch = lowerSearch.isEmpty() ||
                    p.getCreator().toLowerCase().contains(lowerSearch) ||
                    p.getName().toLowerCase().contains(lowerSearch) ||
                    p.getDescription().toLowerCase().contains(lowerSearch);
            }

            // Vérifier le filtre de prix, si renseigné
            boolean matchesPrice = true;
            double price = p.getPrice();
            if (minPrice >= 0 && price < minPrice) {
                matchesPrice = false;
            }
            if (maxPrice >= 0 && price > maxPrice) {
                matchesPrice = false;
            }

            // Vérifier les autres filtres (s'ils sont sélectionnés)
            boolean matchesCategory = (selectedCategory == null) || (p.getCategory() == selectedCategory);
            boolean matchesTheme = (selectedTheme == null) || (p.getTheme() == selectedTheme);
            boolean matchesSize = (selectedSize == null) || (p.getSize() == selectedSize);
            boolean matchesColor = (selectedColor == null) || (p.getColor() == selectedColor);

            // Le produit doit satisfaire tous les critères
            if (matchesSearch && matchesPrice && matchesCategory && matchesTheme && matchesSize && matchesColor) {
                results.add(p);
            }
        }
        return results;
    }
    
    /**
     * Met à jour la grille d'affichage des produits et la pagination.
     * Le nombre de colonnes est calculé dynamiquement en fonction de la largeur de la fenêtre.
     * Seules 2 lignes maximum de produits sont affichées par page.
     */
    private void updateGrid() {
        if (productGrid.getScene() == null) return;
        
        productGrid.getChildren().clear();
        
        // Calculer la largeur disponible dans la scène (avec une marge)
        double gridWidth = productGrid.getScene().getWidth() - 150;
        double productBoxWidth = 200; // largeur approximative d'une VBox pour un produit
        int columns = Math.max(1, (int) (gridWidth / productBoxWidth));
        
        // Limiter l'affichage à 2 lignes maximum par page
        PRODUCTS_PER_PAGE = columns * 2;
        
        // Calculer le nombre total de pages en fonction du nombre de produits filtrés
        int totalProducts = catalog.getProducts().size();
        int pageCount = (int) Math.ceil((double) totalProducts / PRODUCTS_PER_PAGE);
        pagination.setPageCount(pageCount);
        
        // Récupérer l'index de la page actuelle et afficher les produits correspondants
        int currentPage = pagination.getCurrentPageIndex();
        afficherProduits(currentPage, columns);
    }
    
    /**
     * Affiche dans le GridPane les produits de la page spécifiée.
     * @param pageIndex L'index de la page à afficher.
     * @param columns Le nombre de colonnes calculé dynamiquement.
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
     * Crée une VBox affichant les détails d'un produit (image, nom, prix, bouton "Add to cart").
     */
    private VBox createProductBox(Product product) {
        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(10));
        
        // Charger l'image du produit
        Image image = loadImage(product.getImagePath(), "defaultImagePath.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        vbox.getChildren().add(imageView);
        
        // Nom du produit (clic pour afficher les détails)
        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-underline: true;");
        nameLabel.setOnMouseClicked(e -> SceneManager.getInstance().showProductScene(product));
        vbox.getChildren().add(nameLabel);
        
        // Prix du produit
        Label priceLabel = new Label(String.format("%.2f €", product.getPrice()));
        vbox.getChildren().add(priceLabel);
        
        // Bouton "Add to cart"
        Button addToCartButton = new Button("Add to cart");
        addToCartButton.setOnAction(e -> {
            UserSession.getInstance().getOrder().addToCart(product, 1);
            System.out.println("Cart updated: " + UserSession.getInstance().getOrder().getCart().keySet());
        });
        vbox.getChildren().add(addToCartButton);
        
        return vbox;
    }

}
