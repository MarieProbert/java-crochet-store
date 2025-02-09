package enums;

/**
 * Represents a product category.
 */
public enum Category {
    PLUSHIES("Plushies"),
    CLOTHES("Clothes"),
    ACCESSORIES("Accessories"),
    DECORATIONS("Decorations");

    private final String value;

    /**
     * Constructs a Category with the specified string value.
     *
     * @param value the string value representing the category
     */
    Category(String value) {
        this.value = value;
    }

    /**
     * Returns the string value of this category.
     *
     * @return the category value
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns the Category corresponding to the given string.
     *
     * @param category the string representation of the category
     * @return the matching Category
     * @throws IllegalArgumentException if no matching category is found
     */
    public static Category fromStringToCategory(String category) {
        for (Category c : Category.values()) {
            if (c.value.equalsIgnoreCase(category)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Unknown category: " + category);
    }
}
