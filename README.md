# Product Sales Management Project  

## Description  
This project is a JavaFX application designed to sell crocheted products. It uses JavaFX 23.0.1 for the graphical user interface and Maven for dependency management.  

## Author  
[Marie Probert] - [marie.probert@dauphine.eu]  

## Prerequisites  
Before starting, ensure you have the following installed on your machine:  
- **Java 21** (or later)  
- **JavaFX SDK 23.0.1** (downloadable [here](https://gluonhq.com/products/javafx/))  
- **Maven 3.9.x** (or later)   

The project only works on the laptop containing the local host database, here on Marie Probert's computer.  

## Compilation  

To compile the project, run the following command:  
   `mvn clean package`  

This command will:  
- Clean the project (`clean`).  
- Compile the source code and generate a JAR file in the `target` folder.

## Execution  
To run the application, use the following command:  

   `java --module-path "path/to/javafx-sdk-23.0.1/lib" --add-modules javafx.controls,javafx.fxml -jar target/marie-probert.jar`  

Replace `"path/to/javafx-sdk-23.0.1/lib"` with the actual path where you extracted the JavaFX SDK 23.0.1.  
