package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.init.loadCombatActions;
import com.msservices.geopolitik.init.entity.combatOption;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.List;

public class AccionController implements interaction {

    @FXML private VBox listaAcciones;
    @FXML private Button botonAtras;

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
        loadCombatActions();
    }

    private void loadCombatActions() {
        List<combatOption> combatList = loadCombatActions.getCombatList();
        listaAcciones.getChildren().clear();

        for (combatOption option : combatList) {
            HBox row = new HBox(10);
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            row.getStyleClass().add("fila-accion");
            row.setPickOnBounds(true);

            Rectangle icon = new Rectangle(20, 20);
            icon.getStyleClass().add("icono");

            Label label = new Label(option.name());
            label.getStyleClass().add("texto-dato");

            row.getChildren().addAll(icon, label);
            listaAcciones.getChildren().add(row);
        }
    }
}
