package enums;

public enum Color {
	RED("red"), 
	BLUE("blue"), 
	GREEN("green"), 
	YELLOW("yellow"),
	PINK("pink"),
	PURPLE("purple"),
	BEIGE("beige"),
	ORANGE("orange"),
	GREY("grey"),
	WHITE("white"), 
	BLACK("black"), 
	BROWN("brown");
	
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
