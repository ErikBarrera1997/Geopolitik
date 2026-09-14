package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class NewsController implements interaction {

    @FXML
    private Circle iconSuperior;

    @FXML
    private VBox listaRegistros;

    @FXML
    private Button botonAtras;

    @FXML
    public void onFechaAnteriorClick() {
    }

    @FXML
    public void onFechaClick() {
    }

    @FXML
    public void onFechaSiguienteClick() {
    }

    @FXML
    public void initialize() {
        botonAtras.setOnAction(e -> goBack());
    }

    @Override
    public void goBack() {
        Stage stage = (Stage) botonAtras.getScene().getWindow();
        stage.close();
    }

    @Override
    public void goAhead() {
    }
}
