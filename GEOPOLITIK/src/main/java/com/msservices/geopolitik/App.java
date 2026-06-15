package com.msservices.geopolitik;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass.getResource("mainView.fxml"));
            Parent root = loader.load();
            
            // Crear la escena y aplicar los estilos desde el FXML
            Scene scene = new Scene(root, 800, 600);
            
            // Configurar el escenario
            stage.setTitle("GeoPolitik");
            stage.setScene(scene);
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
