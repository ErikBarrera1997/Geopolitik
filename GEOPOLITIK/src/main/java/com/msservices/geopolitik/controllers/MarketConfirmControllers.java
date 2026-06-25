package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MarketConfirmControllers implements interaction {

    @FXML
    private VBox listaElementos;

    @FXML
    private Label lblCosto;

    @FXML
    private Label lblIngresos;

    @FXML
    private Label lblTotal;

    @FXML
    private Button botonComprar;

    @FXML
    private Button botonCancelar;

    private Stage stage;

    @FXML
    public void initialize() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPurchaseData(String[] items, String costo, String ingresos, String total) {
        listaElementos.getChildren().clear();
        for (String item : items) {
            HBox row = new HBox(5);
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            row.getStyleClass().add("fila-accion");
            Label label = new Label(item);
            row.getChildren().add(label);
            listaElementos.getChildren().add(row);
        }
        lblCosto.setText(costo);
        lblIngresos.setText(ingresos);
        lblTotal.setText(total);
    }

    @FXML
    private void onComprarClick() {
        if (stage != null) {
            stage.close();
        }
    }

    @Override
    public void goBack() {
        if (stage != null) {
            stage.close();
        }
    }

    @Override
    public void goAhead() {
    }
}
