package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WeaponsMarketControllers implements interaction {

    @FXML
    private Rectangle imagenIcono;

    @FXML
    private Rectangle imagenArma;

    @FXML
    private Label etiquetaTexto;

    @FXML
    private ComboBox<String> listaDesplegable;

    @FXML
    private Slider sliderCantidad;

    @FXML
    private TextField campoCantidad;

    @FXML
    private Button botonComprar;

    @FXML
    private Button botonNegro;

    @FXML
    private Button botonRegresar;

    @FXML
    public void initialize() {
        listaDesplegable.setPromptText("Armas disponibles");
        listaDesplegable.getItems().addAll("Pistola", "Rifle", "Misil");
        botonComprar.setOnAction(e -> onComprarClick());
    }

    @FXML
    private void onComprarClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/msservices/geopolitik/views/marketConfirmView.fxml"));
            Parent root = loader.load();
            MarketConfirmControllers controller = loader.getController();

            Stage stage = new Stage();
            controller.setStage(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(botonComprar.getScene().getWindow());
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Confirmar compra");

            Scene scene = new Scene(root);
            scene.setFill(null);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void goBack() {
        Stage stage = (Stage) botonRegresar.getScene().getWindow();
        stage.close();
    }

    @Override
    public void goAhead() {
    }
}
