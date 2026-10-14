package com.msservices.geopolitik.controllers.diplomacy;

import com.msservices.geopolitik.init.loadImages;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class diplomacyActionsController implements interaction {

    @FXML
    private VBox actionsList;

    @FXML
    private Button backButton;

    private static final List<String> ACCIONES = List.of(
            "Romper alianza",
            "Revisar tratados",
            "Pedir ayuda económica",
            "Pedir ayuda militar",
            "Retirar embajada",
            "Declarar la guerra",
            "Exigir recursos",
            "Pedir tregua",
            "Firmar tratado de paz",
            "Retirar embajada"
    );

    @FXML
    public void initialize() {
        if (backButton != null) {
            backButton.setOnAction(e -> goBack());
        }

        actionsList.getChildren().clear();
        for (String accion : ACCIONES) {
            HBox row = new HBox(8);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("fila-accion");

            ImageView icon = new ImageView(loadImages.getIcon("diplomacy"));
            icon.setFitWidth(20);
            icon.setFitHeight(20);
            icon.setPreserveRatio(true);
            row.getChildren().add(icon);

            row.getChildren().add(new Label(accion));
            actionsList.getChildren().add(row);
        }
    }

    @Override
    public void goBack() {
        if (backButton != null && backButton.getScene() != null) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void goAhead() {
    }
}
