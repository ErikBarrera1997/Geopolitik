package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.connection.DatabaseConnection;
import com.msservices.geopolitik.connection.Views.ProvinceData;
import com.msservices.geopolitik.connection.Views.ProvinceDefense;
import com.msservices.geopolitik.connection.defensesQueries;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
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
    @FXML private Circle lockDefensas;
    @FXML private ScrollPane defenseScroll;
    @FXML private VBox defenseList;
    @FXML private Button botonAcciones;
    @FXML private Button botonAtras;

    private int currentProvinceId;
    private int currentCountryId;
    private String currentCountryName;

    public void setProvinceData(ProvinceData data, String countryName) {
        currentProvinceId = data.getIdProvince();
        currentCountryId = data.getIdCountry();
        currentCountryName = countryName;
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
            row.getStyleClass().add("fila-dato");
            Label nameLabel = new Label(entry.getKey() + ": " + entry.getValue());
            row.getChildren().add(nameLabel);
            listaRecursos.getChildren().add(row);
        }
    }

    private void unlockDefenses() {
        lockDefensas.setFill(javafx.scene.paint.Color.web("#4a8c4a"));
        lockDefensas.setDisable(true);
        defenseScroll.setVisible(true);
        defenseScroll.setManaged(true);
        loadDefenses();
    }

    private void loadDefenses() {
        defensesQueries queries = new defensesQueries(DatabaseConnection.getInstance().getConnection());
        List<ProvinceDefense> defenses = queries.getProvinceDefenses(currentProvinceId);

        defenseList.getChildren().clear();
        for (ProvinceDefense d : defenses) {
            addDefenseRow(d);
        }
    }

    private void addDefenseRow(ProvinceDefense defense) {
        HBox row = new HBox(5);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        row.getStyleClass().add("fila-dato");
        Label label = new Label(defense.weaponName() + ": " + defense.quantity());
        row.getChildren().add(label);
        defenseList.getChildren().add(row);
    }

    private void openCombatView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/msservices/geopolitik/views/combatView.fxml"));
            Parent root = loader.load();
            AttackController controller = loader.getController();
            controller.setCountryData(currentCountryId, currentCountryName);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(botonAcciones.getScene().getWindow());
            stage.setTitle("Acciones militares");
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
        botonAcciones.setOnAction(e -> openCombatView());
        lockDefensas.setOnMouseClicked(e -> unlockDefenses());
    }
}
