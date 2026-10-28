package com.msservices.geopolitik.controllers.diplomacy;

import com.msservices.geopolitik.connection.DatabaseConnection;
import com.msservices.geopolitik.connection.queries.diplomacy.CountryDiplomacyStatus;
import com.msservices.geopolitik.connection.queries.diplomacy.diplomacyQueries;
import com.msservices.geopolitik.init.loadFlags;
import com.msservices.geopolitik.init.entity.flag;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;

public class diplomacyCountryController implements interaction {

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

        if (listaDiplomacia != null) {
            listaDiplomacia.getChildren().forEach(node -> {
                if (node instanceof HBox hbox) {
                    hbox.setCursor(Cursor.HAND);
                    hbox.setOnMouseClicked(e -> openTradeView(getRowText(hbox)));
                }
            });
        }
    }

    private String getRowText(HBox row) {
        for (javafx.scene.Node node : row.getChildren()) {
            if (node instanceof Label label) {
                return label.getText();
            }
        }
        return "";
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
            HBox alliedRow = buildRow(status.alliedId(), status.alliedName());
            alliedRow.setCursor(Cursor.HAND);
            alliedRow.setOnMouseClicked(e -> openDiplomacyActions(status.alliedName(), true));
            listaAliados.getChildren().add(alliedRow);
        }

        if (status.enemyName() != null) {
            HBox enemyRow = buildRow(status.enemyId(), status.enemyName());
            enemyRow.setCursor(Cursor.HAND);
            enemyRow.setOnMouseClicked(e -> openDiplomacyActions(status.enemyName(), false));
            listaEnemigos.getChildren().add(enemyRow);
        }
        if (status.warName() != null) {
            HBox warRow = buildRow(status.warId(), status.warName());
            warRow.setCursor(Cursor.HAND);
            warRow.setOnMouseClicked(e -> openDiplomacyActions(status.warName(), false));
            listaEnemigos.getChildren().add(warRow);
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

    private void openDiplomacyActions(String countryName, boolean allied) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/msservices/geopolitik/views/diplomacyViews/diplomacyActions.fxml"));
            Parent root = loader.load();
            diplomacyActionsController controller = loader.getController();
            controller.setAlliedContext(allied);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            if (botonAtras != null && botonAtras.getScene() != null) {
                stage.initOwner(botonAtras.getScene().getWindow());
            }
            stage.setTitle("Acciones diplomáticas - " + countryName);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openTradeView(String actionName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/msservices/geopolitik/views/diplomacyViews/tradeView.fxml"));
            Parent root = loader.load();
            treatyController controller = loader.getController();
            controller.setCountryData(COUNTRY_ID, COUNTRY_ID, actionName);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            if (botonAtras != null && botonAtras.getScene() != null) {
                stage.initOwner(botonAtras.getScene().getWindow());
            }
            stage.setTitle(actionName);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


