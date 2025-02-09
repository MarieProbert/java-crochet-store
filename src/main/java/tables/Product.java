package tables;

import enums.Category;
import enums.Color;
import enums.Size;

/**
 * Represents a product with details such as name, creator, price, description, and attributes like color, size, etc.
 */
public class Product {

    private int productID;
    private String name;
    private String creator;
    private double price;
    private String description;
    private Color color;
    private int stock;
    private Size size;
    private String imagePath;
    private Category category;

    /**
     * Constructor to initialize a product with a specific ID.
     *
     * @param productID the unique identifier for the product
     */
    public Product(int productID) {
        this.productID = productID;
    }

    /**
     * Default constructor initializing productID to -1.
     */
    public Product() {
        this.productID = -1;
    }

    // Getters and setters

    public int getProductID() {
        return productID;
    }
 
    public void setProductID(int productID) {
        this.productID = productID;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getCreator() {
        return creator;
    }
 
    public void setCreator(String creator) {
        this.creator = creator;
    }
 
    public double getPrice() {
        return price;
    }
 
    public void setPrice(double price) {
        this.price = price;
    }
 
    public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }
 
    public int getStock() {
        return stock;
    }
 
    public void setStock(int stock) {
        this.stock = stock;
    }
 
    public Color getColor() {
        return color;
    }
 
    public void setColor(Color color) {
        this.color = color;
    }
 
    public Size getSize() {
        return size;
    }
 
    public void setSize(Size size) {
        this.size = size;
    }
 
    public String getImagePath() {
        return imagePath;
    }
 
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
 
    public Category getCategory() {
        return category;
    }
 
    public void setCategory(Category category) {
        this.category = category;
    }
 
    /**
     * Sets the product's color using a string representation.
     *
     * @param colorStr the string representing the color
     */
    public void setColorFromString(String colorStr) {
        this.color = Color.fromStringToColor(colorStr);
    }
 
    /**
     * Sets the product's size using a string representation.
     *
     * @param sizeStr the string representing the size
     */
    public void setSizeFromString(String sizeStr) {
        this.size = Size.fromStringToSize(sizeStr);
    }
 
    /**
     * Sets the product's category using a string representation.
     *
     * @param categoryStr the string representing the category
     */
    public void setCategoryFromString(String categoryStr) {
        this.category = Category.fromStringToCategory(categoryStr);
    }
 
    /**
     * Returns a string representation of the product.
     *
     * @return a string containing the product's details
     */
    @Override
    public String toString() {
        return "Product reference: " + productID +
               "; Product name: " + name +
               "; Product creator: " + creator +
               "; Price: " + price +
               "; Description: " + description +
               "; Color: " + (color != null ? color.toString() : "N/A") +
               "; Stock: " + stock +
               "; Size: " + (size != null ? size.toString() : "N/A") +
               "; Image Path: " + (imagePath != null ? imagePath : "N/A") +
               "; Category: " + (category != null ? category.toString() : "N/A") + ".";
    }
}
