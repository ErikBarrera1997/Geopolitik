package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.connection.DatabaseConnection;
import com.msservices.geopolitik.connection.queries.defense.ArmyUnit;
import com.msservices.geopolitik.connection.queries.weapon.weaponQueries;
import com.msservices.geopolitik.init.loadImages;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.List;

public class WarMinisterController implements interaction {

    @FXML private VBox listaEjercito;
    @FXML private VBox listaAerea;
    @FXML private VBox listaMarina;
    @FXML private Button botonAtras;

    private HBox lastSelected;

    @FXML
    public void initialize() {
        botonAtras.setOnAction(e -> goBack());
    }

    public void setCountryId(int countryId) {
        weaponQueries queries = new weaponQueries(DatabaseConnection.getInstance().getConnection());
        List<ArmyUnit> army = queries.getArmy(countryId);

        listaEjercito.getChildren().clear();
        listaAerea.getChildren().clear();
        listaMarina.getChildren().clear();

        for (ArmyUnit unit : army) {
            HBox row = buildArmyRow(unit);
            switch (unit.type()) {
                case "tierra" -> listaEjercito.getChildren().add(row);
                case "aire" -> listaAerea.getChildren().add(row);
                case "mar" -> listaMarina.getChildren().add(row);
                default -> System.out.println("Tipo de arma no reconocido: " + unit.type());
            }
        }
    }

    private HBox buildArmyRow(ArmyUnit unit) {
        Rectangle icon = new Rectangle(24, 24);
        icon.setFill(Color.web("#4a6a4a"));
        icon.setStroke(Color.web("#1a3a1a"));
        icon.setStrokeWidth(1);
        icon.setArcWidth(4);
        icon.setArcHeight(4);

        Image img = loadImages.getWeaponImage(unit.name());
        if (img != null) {
            ImageView iv = new ImageView(img);
            iv.setFitWidth(24);
            iv.setFitHeight(24);
            HBox.setHgrow(iv, javafx.scene.layout.Priority.NEVER);
            HBox row = new HBox(8, iv, buildArmyLabel(unit));
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("arma-fila");
            row.setOnMouseClicked(e -> selectRow(row));
            return row;
        }

        HBox row = new HBox(8, icon, buildArmyLabel(unit));
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("arma-fila");
        row.setOnMouseClicked(e -> selectRow(row));
        return row;
    }

    private Label buildArmyLabel(ArmyUnit unit) {
        String text = unit.name() + "  |  Elementos: " + unit.totalQuantity() + "  |  Moral: " + String.format("%.0f", unit.avgMorale());
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #1a3a1a; -fx-font-size: 12px;");
        return label;
    }

    private void selectRow(HBox row) {
        VBox parent = (VBox) row.getParent();
        if (lastSelected != null) {
            lastSelected.getStyleClass().remove("arma-fila-selected");
        }
        row.getStyleClass().add("arma-fila-selected");
        lastSelected = row;
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
