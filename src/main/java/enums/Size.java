package enums;

/**
 * Represents a size option.
 */
public enum Size {
    XS("XS"),
    S("S"),
    M("M"),
    L("L"),
    XL("XL"),
    ONESIZE("One Size");

    private final String value;

    /**
     * Constructs a Size with the specified string value.
     *
     * @param value the string value representing the size
     */
    Size(String value) {
        this.value = value;
    }

    /**
     * Returns the string value of this size.
     *
     * @return the size value
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns the Size corresponding to the given string.
     *
     * @param size the string representation of the size
     * @return the matching Size
     * @throws IllegalArgumentException if no matching size is found
     */
    public static Size fromStringToSize(String size) {
        for (Size s : Size.values()) {
            if (s.value.equalsIgnoreCase(size)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown size: " + size);
    }
}
