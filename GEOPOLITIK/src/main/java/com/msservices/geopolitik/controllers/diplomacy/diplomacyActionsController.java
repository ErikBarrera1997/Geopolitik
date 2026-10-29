package com.msservices.geopolitik.controllers.diplomacy;

import com.msservices.geopolitik.init.loadImages;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
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
            "Romper alianza comercial",
            "Romper alianza militar",
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

    private static final int PLAYER_COUNTRY_ID = 11;
    private static final String WAR_ACTION = "Declarar la guerra";
    private static final String COMMERCIAL_CANCEL_ACTION = "Romper alianza comercial";
    private static final String MILITARY_CANCEL_ACTION = "Romper alianza militar";

    private boolean alliedContext = true;
    private int countryId;
    private String countryName;

    @FXML
    public void initialize() {
        if (backButton != null) {
            backButton.setOnAction(e -> goBack());
        }

        fillList(actionsList, ALLY_ACTIONS);
        fillList(enemyActionsList, ENEMY_ACTIONS);
        applyContext();
    }

    public void setContext(int countryId, String countryName, boolean allied) {
        this.countryId = countryId;
        this.countryName = countryName;
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
        if (WAR_ACTION.equals(action)) {
            row.setOnMouseClicked(e -> openWarDeclaration());
        } else if (COMMERCIAL_CANCEL_ACTION.equals(action) || MILITARY_CANCEL_ACTION.equals(action)) {
            row.setOnMouseClicked(e -> openCancelTreaty(action));
        }
        return row;
    }

    private void openWarDeclaration() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/msservices/geopolitik/views/diplomacyViews/warDeclarationView.fxml"));
            Parent root = loader.load();
            warDeclarationController controller = loader.getController();
            controller.setCountryData(PLAYER_COUNTRY_ID, countryId);

            prepareStage(WAR_ACTION, root).showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openCancelTreaty(String action) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/msservices/geopolitik/views/diplomacyViews/cancelTreaty.fxml"));
            Parent root = loader.load();
            cancelTreatyController controller = loader.getController();
            controller.setContext(PLAYER_COUNTRY_ID, countryId, countryName, action);

            prepareStage(action, root).showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Stage prepareStage(String title, Parent root) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        if (backButton != null && backButton.getScene() != null) {
            stage.initOwner(backButton.getScene().getWindow());
        }
        stage.setTitle(countryName == null ? title : title + " - " + countryName);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        return stage;
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
