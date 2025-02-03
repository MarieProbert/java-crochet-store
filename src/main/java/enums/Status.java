package enums;

public enum Status {
	INPROGRESS("In progress"), CONFIRMED("Confirmed"), DELIVERED("Delivered");
	
	private final String value;
	
	Status(String value) {
		this.value = value;
	}
	
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
	
	public static Status fromStringToStatus(String status) {
		for (Status s : Status.values()) {
            if (s.value.equalsIgnoreCase(status)) {
                return s;
            }
        }
        // If the status doesn't exist
        throw new IllegalArgumentException("Unkwown status: " + status);
    }



}
