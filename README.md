# Projet Gestion de Vente de produit(s)
## Description
Ce projet est une application JavaFX qui permet de vendre des produits crochetés. Il utilise JavaFX 23.0.1 pour l'interface graphique et Maven pour la gestion des dépendances.

## Auteur
[Marie Probert] - [marie.probert@dauphine.eu]

## Prérequis
Avant de commencer, assurez-vous d'avoir les éléments suivants installés sur votre machine :
- **Java 21** (ou supérieur)
- **JavaFX SDK 23.0.1** (téléchargeable [ici](https://gluonhq.com/products/javafx/))
- **Maven 3.9.x** (ou supérieur)
- **Git** (pour cloner le dépôt)

## Installation
1. Clonez le dépôt :
   `git clone https://github.com/MarieProbert/java-crochet-store.git`

2. Accédez au dossier du projet :
`cd java-crochet-store`

## Compilation

Pour compiler le projet, exécutez la commande suivante :
`mvn clean package`

Cette commande va :
Nettoyer le projet (clean).
Compiler le code source et générer un fichier JAR dans le dossier target.

## Exécution
Pour exécuter l'application, utilisez la commande suivante :

`java --module-path "chemin/vers/javafx-sdk-23.0.1/lib" --add-modules javafx.controls,javafx.fxml -jar target/marie-probert.jar`

Remplacez "chemin/vers/javafx-sdk-23.0.1/lib" par le chemin réel où vous avez extrait le SDK JavaFX 23.0.1.
