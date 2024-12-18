CREATE DATABASE crochet_store;

USE crochet_store;

CREATE TABLE Client (
    clientID INT AUTO_INCREMENT PRIMARY KEY,  -- Identifiant unique, clé primaire
    email VARCHAR(50) NOT NULL UNIQUE,        -- Nom d'utilisateur unique
    password VARCHAR(255) NOT NULL,           -- Mot de passe
    firstName VARCHAR(50),                    -- Prénom
    lastName VARCHAR(50),                     -- Nom de famille
    address VARCHAR(255),                     -- Adresse
    city VARCHAR(50),                         -- Ville
    postCode INT,                             -- Code postal
    country VARCHAR(50)                       -- Pays
);

CREATE TABLE Admin (
    adminID INT AUTO_INCREMENT PRIMARY KEY, -- Identifiant unique, clé primaire
    email VARCHAR(50) NOT NULL UNIQUE,      -- Email d'utilisateur unique
    password VARCHAR(255) NOT NULL          -- Mot de passe
);

CREATE TABLE Product (
    productID INT AUTO_INCREMENT PRIMARY KEY,  -- Identifiant unique, clé primaire
    name VARCHAR(100) NOT NULL,                -- Nom du produit
    creator VARCHAR(100),                      -- Créateur du produit
    price DECIMAL(10, 2) NOT NULL,             -- Prix du produit (avec deux décimales pour les cents)
    description TEXT,                          -- Description du produit
    color VARCHAR(50),                         -- Couleur (par exemple, une chaîne représentant la couleur)
    fabric VARCHAR(50),                        -- Tissu (par exemple, une chaîne représentant le tissu)
    stock INT NOT NULL,                        -- Quantité en stock
    size VARCHAR(20),                          -- Taille (par exemple, "S", "M", "L" ou "Petit", "Moyen", "Grand")
    theme VARCHAR(50),                         -- Thème du produit (par exemple, "Sport", "Classique")
    imagePath VARCHAR(255)                     -- Chemin vers l'image du produit
);

CREATE TABLE `Order` (
    orderID INT AUTO_INCREMENT PRIMARY KEY,  -- Identifiant unique de la commande
    clientID INT,                            -- Identifiant du client (clé étrangère)
    status ENUM('In progress', 'Confirmed', 'Delivered') NOT NULL,  -- Statut de la commande (panier, validé, livrée)
    FOREIGN KEY (clientID) REFERENCES Client(clientID)  -- Clé étrangère vers la table Client
);

CREATE TABLE OrderXProduct (
    orderID INT,                            -- Identifiant de la commande (clé étrangère)
    productID INT,                          -- Identifiant du produit (clé étrangère)
    quantity INT NOT NULL,                  -- Quantité de produits commandés
    PRIMARY KEY (orderID, productID),       -- Clé primaire composée de orderID et productID
    FOREIGN KEY (orderID) REFERENCES `Order`(orderID),  -- Clé étrangère vers la table Order
    FOREIGN KEY (productID) REFERENCES Product(productID)  -- Clé étrangère vers la table Product
);

ALTER TABLE Product
ADD COLUMN category ENUM('Plushies', 'Clothes', 'Accessories') NOT NULL;

