package enums;

public enum Size {
	XS("XS"),
	S("S"),
	M("M"),
	L("L"),
	XL("XL"),
	ONESIZE("One Size"),
	NA("N/A");
	
	private final String value;
	
	Size(String value) {
		this.value = value;
	}
	
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
	
	public static Size fromStringToSize(String size) {
		for (Size s : Size.values()) {
            if (s.value.equalsIgnoreCase(size)) {
                return s;
            }
        }
        // If the size doesn't exist
        throw new IllegalArgumentException("Unkwown size: " + size);
    }


}
