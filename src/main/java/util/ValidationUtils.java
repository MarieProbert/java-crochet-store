package util;

import java.util.regex.Pattern;

public class ValidationUtils {

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
                return error; // Retourne la première erreur trouvée
            }
        }

        return null; // Aucun problème détecté
    }
    
    
    public static String verifyModifications(String email, String password, String firstName, String lastName, String street, String city, String postCode, String country) {
        String[] errors = {
            verifyFirstName(firstName),
            verifyLastName(lastName),
            verifyStreet(street),
            verifyCity(city),
            verifyPostCode(postCode),
            verifyCountry(country),
        };

        // Vérifier le mot de passe seulement s'il est renseigné
        if (!password.isEmpty()) {
            String passwordError = verifyPassword(password);
            if (passwordError != null) return passwordError;
        }

        // Vérifier les autres erreurs
        for (String error : errors) {
            if (error != null) {
                return error; // Retourne la première erreur trouvée
            }
        }

        return null; // Aucun problème détecté
    }
    
    public static String verifyModifications(String firstName, String lastName, String street, String city, String postCode, String country) {
        String[] errors = {
            verifyFirstName(firstName),
            verifyLastName(lastName),
            verifyStreet(street),
            verifyCity(city),
            verifyPostCode(postCode),
            verifyCountry(country),
        };
        


        // Vérifier les autres erreurs
        for (String error : errors) {
            if (error != null) {
                return error; // Retourne la première erreur trouvée
            }
        }

        return null; // Aucun problème détecté
    }
    
    public static String verifyModifications(String firstName, String lastName) {
        String[] errors = {
            verifyFirstName(firstName),
            verifyLastName(lastName),
            
            
        };
        
        // Vérifier les autres erreurs
        for (String error : errors) {
            if (error != null) {
                return error; // Retourne la première erreur trouvée
            }
        }

        return null; // Aucun problème détecté
    }
    

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
        return null;
    }
    
    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
          .matcher(emailAddress)
          .matches();
    }

    public static String verifyPassword(String password) {
        if (password.isEmpty()) {
            return "All the fields must be filled!";
        }
        if (password.length() <= 8) {
            return "The password must be longer than 8 characters!";
        }
        return null;
    }

    public static String verifyFirstName(String firstName) {
        return firstName.isEmpty() ? "All the fields must be filled!" : null;
    }

    public static String verifyLastName(String lastName) {
        return lastName.isEmpty() ? "All the fields must be filled!" : null;
    }

    public static String verifyStreet(String street) {
        return street.isEmpty() ? "All the fields must be filled!" : null;
    }
    
    public static String verifyCity(String city) {
        return city.isEmpty() ? "All the fields must be filled!" : null;
    }

    public static String verifyPostCode(String postCode) {
    	return postCode.isEmpty() ? "All the fields must be filled!" : null;
    }

    public static String verifyCountry(String country) {
        return country.isEmpty() ? "All the fields must be filled!" : null;
    }
}
