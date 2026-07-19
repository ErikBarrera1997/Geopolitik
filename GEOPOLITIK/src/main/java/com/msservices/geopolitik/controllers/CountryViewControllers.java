package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.connection.DatabaseConnection;
import com.msservices.geopolitik.connection.queries.province.Province;
import com.msservices.geopolitik.connection.queries.country.CountryDetails;
import com.msservices.geopolitik.connection.queries.province.ProvinceData;
import com.msservices.geopolitik.connection.queries.country.countryQueries;
import com.msservices.geopolitik.connection.queries.province.provinceQueries;
import com.msservices.geopolitik.init.loadFlags;
import com.msservices.geopolitik.init.loadImages;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CountryViewControllers implements interaction {

    @FXML private Pane mapaProvincias;
    @FXML private ImageView bandera;
    @FXML private ImageView icon1;
    @FXML private ImageView icon2;
    @FXML private ImageView icon3;
    @FXML private ImageView icon4;
    @FXML private ImageView icon5;
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
        countryQueries countryQueries = new countryQueries(DatabaseConnection.getInstance().getConnection());

        CountryDetails details = countryQueries.getCountryDetails(countryId);
        java.util.List<Province> listaProvincias = countryQueries.getProvincesByCountry(countryId);

        if (details != null) {
            lblCapital.setText(details.capitalName());
            lblGobierno.setText(details.government());
            lblPoblacion.setText(String.valueOf(details.totalPopulation()));
            lblIngresos.setText(String.valueOf(details.totalIngresos()));

            String gov = details.government().toLowerCase();
            if (gov.contains("república") || gov.contains("republic")) {
                icon2.setImage(loadImages.getIcon("republic"));
            } else if (gov.contains("monarquía") || gov.contains("monarchy")) {
                icon2.setImage(loadImages.getIcon("monarchy"));
            } else if (gov.contains("teocrácia") || gov.contains("theocracy")) {
                icon2.setImage(loadImages.getIcon("teocracy"));
            } else {
                icon2.setImage(loadImages.getIcon("dictatorship"));
            }
        }

        Image flagImage = loadFlag(countryId);
        if (flagImage != null) {
            bandera.setImage(flagImage);
        }

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
            Label nameLabel = new Label(p.name());
            if (p.isCapital()) {
                nameLabel.setText(nameLabel.getText() + " (Capital)");
            }
            row.getChildren().addAll(icon, nameLabel);
            row.setOnMouseClicked(e -> openProvinceView(p.name(), currentCountryId, currentCountryName));
            provincias.getChildren().add(row);
        }
    }

    private Image loadFlag(int countryId) {
        flag f = loadFlags.getFlag(countryId);
        if (f != null) {
            return new Image(f.path(), 120, 60, true, true);
        }
        return null;
    }

    private void openProvinceView(String provinceName, int countryId, String countryName) {
        try {
            provinceQueries pQueries = new provinceQueries(DatabaseConnection.getInstance().getConnection());
            ProvinceData data = pQueries.getProvinceData(provinceName, countryId);
            if (data == null) return;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/msservices/geopolitik/views/countryViews/provinceView.fxml"));
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

    @FXML
    private void openCountryDiplomacia() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/msservices/geopolitik/views/diplomacyViews/countryDiplomacyView.fxml"));
            Parent root = loader.load();
            RelacionesController controller = loader.getController();
            controller.setCountryId(currentCountryId);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(provincias.getScene().getWindow());
            stage.setTitle("Diplomacia - " + currentCountryName);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        icon1.setImage(loadImages.getIcon("capital"));
        icon3.setImage(loadImages.getIcon("population"));
        icon4.setImage(loadImages.getIcon("money"));
        icon5.setImage(loadImages.getIcon("diplomacy"));
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
