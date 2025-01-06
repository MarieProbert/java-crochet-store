package enums;

public enum Theme {
    SPRING("spring"),
    SUMMER("summer"),
    AUTUMN("autumn"),
    WINTER("winter"),
    FANTASY("fantasy"),
    GAMING("gaming"),
    MOVIES("movies"),
    MUSIC("music"),
    ANIMAL("animal"),
    FLOWER("flower"),
    MYTHOLOGY("mythology"),
    NATURE("nature"),
    OTHER("other");

    private final String value;

    Theme(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
    
    public static Theme fromStringToSize(String theme) {
		for (Theme t : Theme.values()) {
            if (t.value.equalsIgnoreCase(theme)) {
                return t;
            }
        }
        // If the theme doesn't exist
        throw new IllegalArgumentException("Unkwown theme: " + theme);
    }
}
