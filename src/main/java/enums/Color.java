package enums;

public enum Color {
	RED("Red"), 
	BLUE("Blue"), 
	GREEN("Green"), 
	YELLOW("Yellow"),
	PINK("Pink"),
	PURPLE("Purple"),
	BEIGE("Beige"),
	ORANGE("Orange"),
	GREY("Grey"),
	WHITE("White"), 
	BLACK("Black"), 
	BROWN("Brown");
	
	private final String value;
	
	Color(String value) {
		this.value = value;
	}
	
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
	
	public static Color fromStringToColor(String color) {
		for (Color c : Color.values()) {
            if (c.value.equalsIgnoreCase(color)) {
                return c;
            }
        }
        // If the color doesn't exist
        throw new IllegalArgumentException("Unkwown color: " + color);
    }

}
