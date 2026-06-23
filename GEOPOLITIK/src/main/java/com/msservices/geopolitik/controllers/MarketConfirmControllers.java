package com.msservices.geopolitik.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MarketConfirmControllers {

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
            Label label = new Label(item);
            label.setStyle("-fx-text-fill: #1a3a1a; -fx-font-size: 13px;");
            listaElementos.getChildren().add(label);
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

    @FXML
    private void onCancelarClick() {
        if (stage != null) {
            stage.close();
        }
    }
}
