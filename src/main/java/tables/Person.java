package tables;

public abstract class Person {
	private int id;
    private String email;
    private String password; 
    
    public Person(int id, String email, String password) {
    	this.id = id;
    	this.email = email;
    	this.password = password;
    	
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    
}
