package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.connection.Views.ProvinceData;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Map;

public class ProvinceViewControllers implements interaction {

    @FXML private Rectangle iconPoblacion;
    @FXML private Rectangle iconPIB;
    @FXML private Rectangle iconSoberania;
    @FXML private Label lblNombreProvincia;
    @FXML private Label valorPoblacion;
    @FXML private Label valorPIB;
    @FXML private Label valorSoberania;
    @FXML private VBox listaRecursos;
    @FXML private Button botonAcciones;
    @FXML private Button botonAtras;

    public void setProvinceData(ProvinceData data, String countryName) {
        lblNombreProvincia.setText(data.getName());
        valorPoblacion.setText(String.valueOf(data.getTotalCountryPopulation()));
        valorPIB.setText(String.valueOf(data.getTotalCountryIngresos()));
        valorSoberania.setText(countryName);
        populateResources(data.getResources());
    }

    private void populateResources(Map<String, Integer> resources) {
        listaRecursos.getChildren().clear();
        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
            HBox row = new HBox(5);
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            Label nameLabel = new Label(entry.getKey() + ": " + entry.getValue());
            row.getChildren().add(nameLabel);
            listaRecursos.getChildren().add(row);
        }
    }

    @Override
    public void goBack() {
        Stage stage = (Stage) botonAtras.getScene().getWindow();
        stage.close();
    }

    @Override
    public void goAhead() {
    }

    @FXML
    public void initialize() {
        botonAtras.setOnAction(e -> goBack());
    }
}
