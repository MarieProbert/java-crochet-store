package tables;

import enums.Category;
import enums.Color;
import enums.Fabric;
import enums.Size;
import enums.Theme;

public class Product {

	private int productID;
	private String name;
	private String creator;
	private double price;
	private String description;
	private Color color;
	private Fabric fabric;
	private int stock;
	private Size size; 
	private Theme theme; 
	private String imagePath;
	private Category category; 
	
	public Product(int productID) {
		this.productID = productID;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public Color getColor() {
		return color;
	}
	public void setColorFromString(String color) {
		this.color = Color.fromStringToColor(color);
	}

	
	public Fabric getFabric() {
		return fabric;
	}
	public void setFabricFromString(String fabric) {
		this.fabric = Fabric.fromStringToFabric(fabric);
	}
	
	
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	
	public Size getSize() {
		return size;
	}
	public void setSizeFromString(String size) {
		this.size = Size.fromStringToSize(size);
	}
	
	
	public Theme getTheme() {
		return theme;
	}
	public void setThemeFromString(String theme) {
		this.theme = Theme.fromStringToSize(theme);
	}
	
	
	public Category getCategory() {
		return category;
	}
	public void setCategoryFromString(String category) {
		this.category = Category.fromStringToSize(category);
	}
	
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Product reference: ").append(productID).append("; ");
	    sb.append("Product name: ").append(name).append("; ");
	    sb.append("Product creator: ").append(creator).append("; ");
	    sb.append("Price: ").append(price).append("; ");
	    sb.append("Description: ").append(description).append("; ");
	    sb.append("Color: ").append(color != null ? color.toString() : "N/A").append("; ");
	    sb.append("Fabric: ").append(fabric != null ? fabric.toString() : "N/A").append("; ");
	    sb.append("Stock: ").append(stock).append("; ");
	    sb.append("Size: ").append(size != null ? size.toString() : "N/A").append("; ");
	    sb.append("Theme: ").append(theme != null ? theme.toString() : "N/A").append("; ");
	    sb.append("Image Path: ").append(imagePath != null ? imagePath : "N/A").append("; ");
	    sb.append("Category: ").append(category != null ? category.toString() : "N/A").append(".");

	    return sb.toString();
	}

	
}
