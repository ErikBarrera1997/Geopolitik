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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class RelacionesController implements interaction {

    @FXML private Rectangle iconSuperior;
    @FXML private Rectangle etiquetaSuperior;
    @FXML private VBox listaAliados;
    @FXML private VBox listaEnemigos;
    @FXML private VBox listaEstado;
    @FXML private Button botonAtras;

    private int countryId;

    @FXML
    public void initialize() {
        botonAtras.setOnAction(e -> goBack());
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;

        diplomacyQueries spy = new diplomacyQueries(DatabaseConnection.getInstance().getConnection());
        CountryDiplomacyStatus status = spy.getCountryDiplomacyStatus(countryId);
        if (status == null) return;

        listaAliados.getChildren().clear();
        listaEnemigos.getChildren().clear();
        listaEstado.getChildren().clear();

        if (status.alliedName() != null) {
            listaAliados.getChildren().add(buildCountryRow(status.alliedId(), status.alliedName()));
        }

        if (status.enemyName() != null) {
            listaEnemigos.getChildren().add(buildCountryRow(status.enemyId(), status.enemyName()));
        }
        if (status.warName() != null) {
            listaEnemigos.getChildren().add(buildCountryRow(status.warId(), status.warName()));
        }

        if (status.peaceName() != null) {
            listaEstado.getChildren().add(buildStatusRow("En paz con", status.peaceId(), status.peaceName()));
        }
        if (status.neutralName() != null) {
            listaEstado.getChildren().add(buildStatusRow("Neutral", status.neutralId(), status.neutralName()));
        }
        if (status.vassalName() != null) {
            listaEstado.getChildren().add(buildStatusRow("Vasallo", status.vassalId(), status.vassalName()));
        }
        if (status.tradeName() != null) {
            listaEstado.getChildren().add(buildStatusRow("Acuerdo comercial", status.tradeId(), status.tradeName()));
        }
        if (status.militaryName() != null) {
            listaEstado.getChildren().add(buildStatusRow("Alianza militar", status.militaryId(), status.militaryName()));
        }
    }

    private HBox buildCountryRow(int countryId, String countryName) {
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

    private HBox buildStatusRow(String relation, int countryId, String countryName) {
        Label relationLabel = new Label(relation + ":");
        relationLabel.setStyle("-fx-text-fill: #1a3a1a; -fx-font-size: 12px; -fx-font-weight: bold;");

        ImageView flagView = new ImageView();
        flagView.setFitWidth(24);
        flagView.setFitHeight(16);
        flag f = loadFlags.getFlag(countryId);
        if (f != null) {
            flagView.setImage(new Image(f.path(), 24, 16, true, true));
        }

        Label nameLabel = new Label(countryName);
        nameLabel.setStyle("-fx-text-fill: #1a3a1a; -fx-font-size: 13px;");

        HBox row = new HBox(8, relationLabel, flagView, nameLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("fila-pais");
        return row;
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
