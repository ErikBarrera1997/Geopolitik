package com.msservices.geopolitik;

import com.msservices.geopolitik.init.loadDataBases;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        loadDataBases.load();

        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/mainView.fxml"));
        Scene scene = new Scene(loader.load(), screenWidth, screenHeight);
        stage.setTitle("Geopolitik");
        stage.setScene(scene);

        stage.setX(screen.getVisualBounds().getMinX());
        stage.setY(screen.getVisualBounds().getMinY());

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
