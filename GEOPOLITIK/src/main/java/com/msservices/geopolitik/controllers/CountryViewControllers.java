package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.connection.DatabaseConnection;
import com.msservices.geopolitik.connection.Data.Province;
import com.msservices.geopolitik.connection.Views.CountryDetails;
import com.msservices.geopolitik.connection.Views.ProvinceData;
import com.msservices.geopolitik.connection.queries;
import com.msservices.geopolitik.connection.Views.provinceQueries;
import com.msservices.geopolitik.init.loadFlags;
import com.msservices.geopolitik.init.entity.flag;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CountryViewControllers implements interaction {

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

    private int currentCountryId;
    private String currentCountryName;

    public void setCountryData(String countryName, int countryId) {
        this.currentCountryName = countryName;
        this.currentCountryId = countryId;
        lblNombrePais.setText(countryName);
        cargarDatos(countryId);
    }

    private void cargarDatos(int countryId) {
        queries queries = new queries(DatabaseConnection.getInstance().getConnection());

        CountryDetails details = queries.getCountryDetails(countryId);
        java.util.List<Province> listaProvincias = queries.getProvincesByCountry(countryId);

        if (details != null) {
            lblCapital.setText(details.getCapitalName());
            lblGobierno.setText(details.getGovernment());
            lblPoblacion.setText(String.valueOf(details.getTotalPopulation()));
            lblIngresos.setText(String.valueOf(details.getTotalIngresos()));
        }

        Image flagImage = loadFlag(countryId);

        provincias.getChildren().clear();
        for (Province p : listaProvincias) {
            HBox row = new HBox(5);
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            row.getStyleClass().add("fila-accion");
            ImageView icon = new ImageView();
            icon.setFitWidth(20);
            icon.setFitHeight(20);
            if (flagImage != null) {
                icon.setImage(flagImage);
            }
            Label nameLabel = new Label(p.getName());
            if (p.isCapital()) {
                nameLabel.setText(nameLabel.getText() + " (Capital)");
            }
            row.getChildren().addAll(icon, nameLabel);
            row.setOnMouseClicked(e -> openProvinceView(p.getName(), currentCountryId, currentCountryName));
            provincias.getChildren().add(row);
        }
    }

    private Image loadFlag(int countryId) {
        flag f = loadFlags.getFlag(countryId);
        if (f != null) {
            return new Image(f.path(), 20, 20, true, true);
        }
        return null;
    }

    private void openProvinceView(String provinceName, int countryId, String countryName) {
        try {
            provinceQueries pQueries = new provinceQueries(DatabaseConnection.getInstance().getConnection());
            ProvinceData data = pQueries.getProvinceData(provinceName, countryId);
            if (data == null) return;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/msservices/geopolitik/views/provinceView.fxml"));
            Parent root = loader.load();
            ProvinceViewControllers controller = loader.getController();
            controller.setProvinceData(data, countryName);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(provincias.getScene().getWindow());
            stage.setTitle(provinceName);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
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
