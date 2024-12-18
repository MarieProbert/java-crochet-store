package main;

import dao.ClientDAO;
import dao.ProductDAO;

public class Main {

    public static void main(String[] args) {
        ClientDAO productDAO = new ClientDAO();
        productDAO.getAllClients(); // Affiche tous les produits de la base de données
    }
}
