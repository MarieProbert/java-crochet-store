package enums;

public enum Fabric {
	COTTON("cotton"),
	WOOL("wool"),
	ACRYLIC("acrylic"),
	ALPACA("alpaca"),
	POLYESTER("polyester"),
	CASHMERE("cashmere");
	
	private final String value;
	
	Fabric(String value) {
		this.value = value;
	}
	
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
	
	public static Fabric fromStringToFabric(String fabric) {
		for (Fabric f : Fabric.values()) {
            if (f.value.equalsIgnoreCase(fabric)) {
                return f;
            }
        }
        // If the fabric doesn't exist
        throw new IllegalArgumentException("Unkwown fabric: " + fabric);
    }

}
