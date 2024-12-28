package tables;

public class Client extends Person {
	
	private String firstName;
	private String lastName;
	private String street;
	private String city;
	private int postCode;
	private String country;
	
	public Client(int id, String email, String password, String firstName, String lastName, String street, String city, int postCode, String country) {
		super(id, email, password);
		this.firstName = firstName;
		this.lastName = lastName;
		this.street = street;
		this.city = city;
		this.postCode = postCode;
		this.country = country;
		
	}
	
	public Client( String email, String password, String firstName, String lastName, String street, String city, int postCode, String country) {
		super(email, password);
		this.firstName = firstName;
		this.lastName = lastName;
		this.street = street;
		this.city = city;
		this.postCode = postCode;
		this.country = country;
		
	}
	
	public Client(String email, String password) {
		super(email, password);
		
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPostCode() {
		return postCode;
	}

	public void setPostCode(int postCode) {
		this.postCode = postCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	

}
