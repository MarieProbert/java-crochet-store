package tables;

import enums.Category;
import enums.Color;
import enums.Fabric;
import enums.Size;
import enums.Theme;

/**
 * Represents a product in the system with details such as name, creator, price, and attributes like color, fabric, size, etc.
 */
public class Product {

    /** Unique identifier for the product. */
    private int productID;

    /** Name of the product. */
    private String name;

    /** Creator or manufacturer of the product. */
    private String creator;

    /** Price of the product. */
    private double price;

    /** Description of the product. */
    private String description;

    /** Color of the product. */
    private Color color;

    /** Number of items available in stock. */
    private int stock;

    /** Size of the product. */
    private Size size;

    /** Theme associated with the product. */
    private Theme theme;

    /** File path to the product's image. */
    private String imagePath;

    /** Category of the product. */
    private Category category;

    /**
     * Constructor to initialize a product with only its ID.
     *
     * @param productID The unique identifier for the product.
     */
    public Product(int productID) {
        this.productID = productID;
    }
    
    public int getProductID() {
    	return productID;
    }
    
    public void setProductID(int productID) {
    	this.productID = productID;
    }

    /**
     * Returns the product's name.
     * 
     * @return The name of the product.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the product's name.
     * 
     * @param name The name to be set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the product's price.
     * 
     * @return The price of the product.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the product's price.
     * 
     * @param price The price to be set.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns the product's image path.
     * 
     * @return The image path of the product.
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Sets the product's image path.
     * 
     * @param imagePath The image path to be set.
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Returns the product's creator.
     * 
     * @return The creator of the product.
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Sets the product's creator.
     * 
     * @param creator The creator to be set.
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * Returns the product's description.
     * 
     * @return The description of the product.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the product's description.
     * 
     * @param description The description to be set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the product's color.
     * 
     * @return The color of the product.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the product's color from a string.
     * 
     * @param color The color string to be converted.
     */
    public void setColorFromString(String color) {
        this.color = Color.fromStringToColor(color);
    }


    /**
     * Returns the product's stock quantity.
     * 
     * @return The stock of the product.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the product's stock quantity.
     * 
     * @param stock The stock quantity to be set.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Returns the product's size.
     * 
     * @return The size of the product.
     */
    public Size getSize() {
        return size;
    }

    /**
     * Sets the product's size from a string.
     * 
     * @param size The size string to be converted.
     */
    public void setSizeFromString(String size) {
        this.size = Size.fromStringToSize(size);
    }

    /**
     * Returns the product's theme.
     * 
     * @return The theme of the product.
     */
    public Theme getTheme() {
        return theme;
    }

    /**
     * Sets the product's theme from a string.
     * 
     * @param theme The theme string to be converted.
     */
    public void setThemeFromString(String theme) {
        this.theme = Theme.fromStringToSize(theme);
    }

    /**
     * Returns the product's category.
     * 
     * @return The category of the product.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the product's category from a string.
     * 
     * @param category The category string to be converted.
     */
    public void setCategoryFromString(String category) {
        this.category = Category.fromStringToSize(category);
    }

    /**
     * Returns a string representation of the product.
     * 
     * @return A string containing the product's details.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Product reference: ").append(productID).append("; ");
        sb.append("Product name: ").append(name).append("; ");
        sb.append("Product creator: ").append(creator).append("; ");
        sb.append("Price: ").append(price).append("; ");
        sb.append("Description: ").append(description).append("; ");
        sb.append("Color: ").append(color != null ? color.toString() : "N/A").append("; ");
        sb.append("Stock: ").append(stock).append("; ");
        sb.append("Size: ").append(size != null ? size.toString() : "N/A").append("; ");
        sb.append("Theme: ").append(theme != null ? theme.toString() : "N/A").append("; ");
        sb.append("Image Path: ").append(imagePath != null ? imagePath : "N/A").append("; ");
        sb.append("Category: ").append(category != null ? category.toString() : "N/A").append(".");
        return sb.toString();
    }
}
