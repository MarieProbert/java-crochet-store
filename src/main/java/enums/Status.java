package enums;

/**
 * Represents the status of an order.
 */
public enum Status {
    INPROGRESS("In progress"), 
    CONFIRMED("Confirmed"), 
    DELIVERED("Delivered");

    private final String value;

    /**
     * Constructs a Status with the specified string value.
     *
     * @param value the string value representing the status
     */
    Status(String value) {
        this.value = value;
    }

    /**
     * Returns the string value of this status.
     *
     * @return the status value
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns the Status corresponding to the given string.
     *
     * @param status the string representation of the status
     * @return the matching Status
     * @throws IllegalArgumentException if no matching status is found
     */
    public static Status fromStringToStatus(String status) {
        for (Status s : Status.values()) {
            if (s.value.equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
