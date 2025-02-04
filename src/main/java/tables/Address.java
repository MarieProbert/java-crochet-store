package tables;

public class Address {

    private int id;

    private String street;
    private String city;
    private String postCode;
    private String country;

    private User user;
    
    
    public Address() {
    	street = null;
    	city = null;
    	postCode = null;
    	country = null;
    }
    
    public Address(String street, String city, String postCode, String country, User user) {
    	this.street = street;
    	this.city = city;
    	this.postCode = postCode;
    	this.country = country;
    	this.user = user;
    }
    
    public Address(int id, String street, String city, String postCode, String country, User user) {
    	this.id = id;
    	this.street = street;
    	this.city = city;
    	this.postCode = postCode;
    	this.country = country;
    	this.user = user;
    }


	/**
     * Returns the client's street address.
     *
     * @return The client's street address.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the client's street address.
     *
     * @param street The new street address for the client.
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Returns the client's city.
     *
     * @return The client's city.
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the client's city.
     *
     * @param city The new city for the client.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns the client's postal code.
     *
     * @return The client's postal code.
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * Sets the client's postal code.
     *
     * @param postCode The new postal code for the client.
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * Returns the client's country.
     *
     * @return The client's country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the client's country.
     *
     * @param country The new country for the client.
     */
    public void setCountry(String country) {
        this.country = country;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
    
    
}
