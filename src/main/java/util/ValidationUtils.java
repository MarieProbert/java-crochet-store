package util;

import java.util.regex.Pattern;

import tables.User;

public class ValidationUtils {

    /**
     * Verifies the submission by checking each field for validity.
     * @param email The email to verify.
     * @param password The password to verify.
     * @param firstName The first name to verify.
     * @param lastName The last name to verify.
     * @param street The street address to verify.
     * @param city The city to verify.
     * @param postCode The postal code to verify.
     * @param country The country to verify.
     * @return The error message if there's any issue, otherwise null if everything is valid.
     */
    public static String verifySubmission(String email, String password, String firstName, String lastName, String street, String city, String postCode, String country) {
        String[] errors = {
            verifyEmail(email),
            verifyPassword(password),
            verifyFirstName(firstName),
            verifyLastName(lastName),
            verifyStreet(street),
            verifyCity(city),
            verifyPostCode(postCode),
            verifyCountry(country)
        };

        for (String error : errors) {
            if (error != null) {
                return error; // Returns the first error found
            }
        }

        return null; // No issues detected
    }
    
    
    /**
     * Verifies modifications, checking each field for validity except for the password.
     * If password is provided, it is also verified.
     * @param email The email to verify.
     * @param password The password to verify.
     * @param firstName The first name to verify.
     * @param lastName The last name to verify.
     * @param street The street address to verify.
     * @param city The city to verify.
     * @param postCode The postal code to verify.
     * @param country The country to verify.
     * @return The error message if there's any issue, otherwise null if everything is valid.
     */
    public static String verifyModifications(String email, String password, String firstName, String lastName, String street, String city, String postCode, String country) {
        String[] errors = {
            verifyFirstName(firstName),
            verifyLastName(lastName),
            verifyStreet(street),
            verifyCity(city),
            verifyPostCode(postCode),
            verifyCountry(country),
        };

        // Check the password only if it's provided
        if (!password.isEmpty()) {
            String passwordError = verifyPassword(password);
            if (passwordError != null) return passwordError;
        }

        // Check other errors
        for (String error : errors) {
            if (error != null) {
                return error; // Returns the first error found
            }
        }

        return null; // No issues detected
    }
    
    /**
     * Verifies modifications, checking each field for validity except for the email and password.
     * @param firstName The first name to verify.
     * @param lastName The last name to verify.
     * @param street The street address to verify.
     * @param city The city to verify.
     * @param postCode The postal code to verify.
     * @param country The country to verify.
     * @return The error message if there's any issue, otherwise null if everything is valid.
     */
    public static String verifyModifications(String firstName, String lastName, String street, String city, String postCode, String country) {
        String[] errors = {
            verifyFirstName(firstName),
            verifyLastName(lastName),
            verifyStreet(street),
            verifyCity(city),
            verifyPostCode(postCode),
            verifyCountry(country),
        };

        // Check other errors
        for (String error : errors) {
            if (error != null) {
                return error; // Returns the first error found
            }
        }

        return null; // No issues detected
    }
    
    /**
     * Verifies modifications, checking only the first name and last name.
     * @param firstName The first name to verify.
     * @param lastName The last name to verify.
     * @return The error message if there's any issue, otherwise null if everything is valid.
     */
    public static String verifyModifications(String firstName, String lastName) {
        String[] errors = {
            verifyFirstName(firstName),
            verifyLastName(lastName),
        };
        
        // Check other errors
        for (String error : errors) {
            if (error != null) {
                return error; // Returns the first error found
            }
        }

        return null; // No issues detected
    }
    
    
    /**
     * Verifies modifications, checking only the first name and last name.
     * @param password the password to verify
     * @param firstName The first name to verify.
     * @param lastName The last name to verify.
     * @return The error message if there's any issue, otherwise null if everything is valid.
     */
    public static String verifyModifications(String password, String firstName, String lastName) {
        String[] errors = {
            verifyFirstName(firstName),
            verifyLastName(lastName),
        };
        
        // Check the password only if it's provided
        if (!password.isEmpty()) {
            String passwordError = verifyPassword(password);
            if (passwordError != null) return passwordError;
        }
        
        // Check other errors
        for (String error : errors) {
            if (error != null) {
                return error; // Returns the first error found
            }
        }

        return null; // No issues detected
    }

    /**
     * Verifies if the email is valid.
     * @param email The email to verify.
     * @return The error message if the email is invalid, otherwise null if it's valid.
     */
    public static String verifyEmail(String email) {
        if (email.isEmpty()) {
            return "All the fields must be filled!";
        }

        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regexPattern);
        java.util.regex.Matcher m = p.matcher(email);

        if (!m.matches()) {
            return "The email format is invalid!";
        }
        
        
        //User user = DataSingleton.getInstance().getUserDAO().setUser(email); // Méthode qui récupère un utilisateur par son email
        //if (user != null) {
        //    return "This email address already exists!";
        //}
        
        
        return null;
    }

    /**
     * Verifies if a given email matches the specified regex pattern.
     * @param emailAddress The email to verify.
     * @param regexPattern The regex pattern to match against.
     * @return True if the email matches the pattern, otherwise false.
     */
    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
          .matcher(emailAddress)
          .matches();
    }

    /**
     * Verifies if the password is valid.
     * @param password The password to verify.
     * @return The error message if the password is invalid, otherwise null if it's valid.
     */
    public static String verifyPassword(String password) {
        if (password.isEmpty()) {
            return "All the fields must be filled!";
        }
        if (password.length() <= 8) {
            return "The password must be longer than 8 characters!";
        }
        return null;
    }

    /**
     * Verifies if the first name is valid.
     * @param firstName The first name to verify.
     * @return The error message if the first name is invalid, otherwise null if it's valid.
     */
    public static String verifyFirstName(String firstName) {
        return firstName.isEmpty() ? "All the fields must be filled!" : null;
    }

    /**
     * Verifies if the last name is valid.
     * @param lastName The last name to verify.
     * @return The error message if the last name is invalid, otherwise null if it's valid.
     */
    public static String verifyLastName(String lastName) {
        return lastName.isEmpty() ? "All the fields must be filled!" : null;
    }

    /**
     * Verifies if the street address is valid.
     * @param street The street address to verify.
     * @return The error message if the street address is invalid, otherwise null if it's valid.
     */
    public static String verifyStreet(String street) {
        return street.isEmpty() ? "All the fields must be filled!" : null;
    }
    
    /**
     * Verifies if the city is valid.
     * @param city The city to verify.
     * @return The error message if the city is invalid, otherwise null if it's valid.
     */
    public static String verifyCity(String city) {
        return city.isEmpty() ? "All the fields must be filled!" : null;
    }

    /**
     * Verifies if the postal code is valid.
     * @param postCode The postal code to verify.
     * @return The error message if the postal code is invalid, otherwise null if it's valid.
     */
    public static String verifyPostCode(String postCode) {
    	return postCode.isEmpty() ? "All the fields must be filled!" : null;
    }

    /**
     * Verifies if the country is valid.
     * @param country The country to verify.
     * @return The error message if the country is invalid, otherwise null if it's valid.
     */
    public static String verifyCountry(String country) {
        return country.isEmpty() ? "All the fields must be filled!" : null;
    }
}
