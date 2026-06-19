package com.msservices.geopolitik.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CountryViewControllers {

    @FXML private Pane mapaProvincias;
    @FXML private Rectangle bandera;
    @FXML private Rectangle icon1;
    @FXML private Rectangle icon2;
    @FXML private Rectangle icon3;
    @FXML private Rectangle icon4;
    @FXML private Rectangle icon5;
    @FXML private VBox provincias;
    @FXML private Button botonAtras;

    @FXML
    public void initialize() {
        botonAtras.setOnAction(e -> {
            Stage stage = (Stage) botonAtras.getScene().getWindow();
            stage.close();
        });
    }
}
