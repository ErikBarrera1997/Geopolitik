package com.msservices.geopolitik.controllers.diplomacy;

import com.msservices.geopolitik.init.loadImages;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class diplomacyActionsController implements interaction {

    @FXML
    private VBox actionsList;

    @FXML
    private VBox enemyActionsList;

    @FXML
    private ScrollPane allyScroll;

    @FXML
    private ScrollPane enemyScroll;

    @FXML
    private Button backButton;

    private static final List<String> ALLY_ACTIONS = List.of(
            "Romper alianza",
            "Revisar tratados",
            "Pedir ayuda económica",
            "Retirar embajada",
            "Pedir ayuda militar"
    );

    private static final List<String> ENEMY_ACTIONS = List.of(
            "Declarar la guerra",
            "Exigir recursos",
            "Pedir tregua",
            "Firmar tratado de paz",
            "Retirar embajada"
    );

    private boolean alliedContext = true;

    @FXML
    public void initialize() {
        if (backButton != null) {
            backButton.setOnAction(e -> goBack());
        }

        fillList(actionsList, ALLY_ACTIONS);
        fillList(enemyActionsList, ENEMY_ACTIONS);
        applyContext();
    }

    public void setAlliedContext(boolean allied) {
        this.alliedContext = allied;
        applyContext();
    }

    private void applyContext() {
        allyScroll.setVisible(alliedContext);
        allyScroll.setManaged(alliedContext);
        enemyScroll.setVisible(!alliedContext);
        enemyScroll.setManaged(!alliedContext);
    }

    private void fillList(VBox target, List<String> actions) {
        target.getChildren().clear();
        for (String action : actions) {
            target.getChildren().add(buildRow(action));
        }
    }

    private HBox buildRow(String action) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("fila-accion");

        ImageView icon = new ImageView(loadImages.getIcon("diplomacy"));
        icon.setFitWidth(20);
        icon.setFitHeight(20);
        icon.setPreserveRatio(true);
        row.getChildren().add(icon);

        row.getChildren().add(new Label(action));
        return row;
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
