package managers;

import tables.Admin;
import tables.Client;
import tables.User;

public class UserManager<T extends User> {
    private T user;

    public UserManager() {
        this.user = (T) new Client();
    }

    public void setUser(T user) {
        // Remplace l'utilisateur actuel par un nouvel utilisateur
        this.user = (T) user; // Cast pour garantir que 'T' accepte 'Client'
    }
    
    public T getUser() {
        return user;
    }
    
    // Vérifier si un utilisateur est connecté
    protected boolean isUserLoggedIn() {
    	return !user.getEmail().equals("Guest");
    }
    
    /*
    
    // Exemple : Vérifier si l'utilisateur a une permission spécifique
    protected boolean hasPermission(String permission) {
        return currentUser != null && currentUser.getPermissions().contains(permission);
    }*/
    
}

// Le plus simple serait que User ne soit pas une classe abstraite et que donc on puisse initialiser avec
// Mais on garde le type générique pour pouvoir préciser le type au moment de la connexion
// Donc changer le getInstance dans le User et le abstract