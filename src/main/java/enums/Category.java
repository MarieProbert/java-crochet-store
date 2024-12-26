package enums;

public enum Category {
	PLUSHIES("plushies"),
	CLOTHES("clothes"),
	ACCESSORIES("accessories"),
	DECORATIONS("decorations");
	
	private final String value;
	
	Category(String value) {
		this.value = value;
	}
	
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
	
	public static Category fromStringToSize(String category) {
		for (Category c : Category.values()) {
            if (c.value.equalsIgnoreCase(category)) {
                return c;
            }
        }
        // If the category doesn't exist
        throw new IllegalArgumentException("Unkwown category: " + category);
    }
}
