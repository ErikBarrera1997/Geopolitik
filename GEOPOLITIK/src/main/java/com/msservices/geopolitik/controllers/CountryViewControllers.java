package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.connection.DatabaseConnection;
import com.msservices.geopolitik.connection.Data.Province;
import com.msservices.geopolitik.connection.Views.CountryDetails;
import com.msservices.geopolitik.connection.Queries;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
    @FXML private Label lblNombrePais;
    @FXML private Label lblCapital;
    @FXML private Label lblGobierno;
    @FXML private Label lblPoblacion;
    @FXML private Label lblIngresos;

    public void setCountryData(String countryName, int countryId) {
        lblNombrePais.setText(countryName);
        cargarDatos(countryId);
    }

    private void cargarDatos(int countryId) {
        Queries queries = new Queries(DatabaseConnection.getInstance().getConnection());

        CountryDetails details = queries.getCountryDetails(countryId);
        java.util.List<Province> listaProvincias = queries.getProvincesByCountry(countryId);

        if (details != null) {
            lblCapital.setText(details.getCapitalName());
            lblGobierno.setText(details.getGovernment());
            lblPoblacion.setText(String.valueOf(details.getTotalPopulation()));
            lblIngresos.setText(String.valueOf(details.getTotalIngresos()));
        }

        provincias.getChildren().clear();
        for (Province p : listaProvincias) {
            HBox row = new HBox(5);
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            ImageView icon = new ImageView();
            icon.setFitWidth(20);
            icon.setFitHeight(20);
            Label nameLabel = new Label(p.getName());
            if (p.isCapital()) {
                nameLabel.setText(nameLabel.getText() + " (Capital)");
            }
            row.getChildren().addAll(icon, nameLabel);
            provincias.getChildren().add(row);
        }
    }

    @FXML
    public void initialize() {
        botonAtras.setOnAction(e -> {
            Stage stage = (Stage) botonAtras.getScene().getWindow();
            stage.close();
        });
    }
}
