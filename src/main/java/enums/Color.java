package enums;

/**
 * Represents a color option.
 */
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

    /**
     * Constructs a Color with the specified string value.
     *
     * @param value the string value representing the color
     */
    Color(String value) {
        this.value = value;
    }

    /**
     * Returns the string value of this color.
     *
     * @return the color value
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns the Color corresponding to the given string.
     *
     * @param color the string representation of the color
     * @return the matching Color
     * @throws IllegalArgumentException if no matching color is found
     */
    public static Color fromStringToColor(String color) {
        for (Color c : Color.values()) {
            if (c.value.equalsIgnoreCase(color)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Unknown color: " + color);
    }
}
