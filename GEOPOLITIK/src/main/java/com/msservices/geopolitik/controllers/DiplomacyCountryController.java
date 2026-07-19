package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.connection.DatabaseConnection;
import com.msservices.geopolitik.connection.queries.diplomacy.CountryDiplomacyStatus;
import com.msservices.geopolitik.connection.queries.diplomacy.diplomacyQueries;
import com.msservices.geopolitik.init.loadFlags;
import com.msservices.geopolitik.init.entity.flag;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DiplomacyCountryController implements interaction {

    @FXML private ImageView iconSuperior;
    @FXML private VBox listaSucesos;
    @FXML private VBox listaAliados;
    @FXML private VBox listaEnemigos;
    @FXML private Button botonStatus;
    @FXML private VBox listaDiplomacia;
    @FXML private Button botonAtras;

    private static final int COUNTRY_ID = 11;

    @FXML
    public void initialize() {
        botonAtras.setOnAction(e -> goBack());
        botonStatus.setOnAction(e -> onStatusClick());
        loadData();
    }

    private void loadData() {
        Image flagImg = loadFlag(COUNTRY_ID);
        if (flagImg != null) {
            iconSuperior.setImage(flagImg);
        }

        diplomacyQueries spy = new diplomacyQueries(DatabaseConnection.getInstance().getConnection());
        CountryDiplomacyStatus status = spy.getCountryDiplomacyStatus(COUNTRY_ID);
        if (status == null) return;

        listaAliados.getChildren().clear();
        listaEnemigos.getChildren().clear();

        if (status.alliedName() != null) {
            listaAliados.getChildren().add(buildRow(status.alliedId(), status.alliedName()));
        }

        if (status.enemyName() != null) {
            listaEnemigos.getChildren().add(buildRow(status.enemyId(), status.enemyName()));
        }
        if (status.warName() != null) {
            listaEnemigos.getChildren().add(buildRow(status.warId(), status.warName()));
        }

    }

    private HBox buildRow(int countryId, String countryName) {
        ImageView flagView = new ImageView();
        flagView.setFitWidth(24);
        flagView.setFitHeight(16);
        flag f = loadFlags.getFlag(countryId);
        if (f != null) {
            flagView.setImage(new Image(f.path(), 24, 16, true, true));
        }

        Label nameLabel = new Label(countryName);
        nameLabel.setStyle("-fx-text-fill: #1a3a1a; -fx-font-size: 13px;");

        HBox row = new HBox(8, flagView, nameLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("fila-pais");
        return row;
    }

    private Image loadFlag(int countryId) {
        flag f = loadFlags.getFlag(countryId);
        if (f != null) {
            return new Image(f.path(), 120, 60, true, true);
        }
        return null;
    }

    private void onStatusClick() {
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
