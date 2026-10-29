package com.msservices.geopolitik.controllers.diplomacy;

import com.msservices.geopolitik.init.entity.flag;
import com.msservices.geopolitik.init.loadFlags;
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
import javafx.stage.Stage;

public class cancelTreatyController implements interaction {

    private static final String COMMERCIAL_ACTION = "Romper alianza comercial";
    private static final String MILITARY_ACTION = "Romper alianza militar";

    @FXML
    private Label titleLabel;

    @FXML
    private Label flag1;

    @FXML
    private Label flag2;

    @FXML
    private Label optionIcon;

    @FXML
    private Label infoLabel;

    @FXML
    private VBox conflictList;

    @FXML
    private Button acceptButton;

    @FXML
    private Button rejectButton;

    @FXML
    public void initialize() {
        if (rejectButton != null) {
            rejectButton.setOnAction(e -> goBack());
        }
    }

    public void setContext(int sourceCountryId, int targetCountryId, String countryName, String action) {
        boolean commercial = COMMERCIAL_ACTION.equals(action);
        String type = commercial ? "comercial" : "militar";
        String target = countryName == null || countryName.isBlank() ? "el país seleccionado" : countryName;

        titleLabel.setText("Romper alianza " + type);
        infoLabel.setText(commercial
                ? "Vas a romper la alianza comercial con " + target + ". Se perderá el intercambio de recursos entre ambos países."
                : "Vas a romper la alianza militar con " + target + ". Se perderá el apoyo militar y la defensa mutua entre ambos países.");

        applyFlag(flag1, sourceCountryId);
        applyFlag(flag2, targetCountryId);

        Image icon = loadImages.getIcon("diplomacy");
        if (icon != null) {
            ImageView view = new ImageView(icon);
            view.setFitWidth(50);
            view.setFitHeight(50);
            view.setPreserveRatio(true);
            optionIcon.setGraphic(view);
        }

        conflictList.getChildren().clear();
        conflictList.getChildren().add(buildRow("Alianza " + type + " con " + target));
    }

    private HBox buildRow(String text) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("fila-accion");

        Image icon = loadImages.getIcon("diplomacy");
        if (icon != null) {
            ImageView view = new ImageView(icon);
            view.setFitWidth(18);
            view.setFitHeight(18);
            view.setPreserveRatio(true);
            row.getChildren().add(view);
        }

        row.getChildren().add(new Label(text));
        return row;
    }

    private void applyFlag(Label label, int countryId) {
        flag f = loadFlags.getFlag(countryId);
        if (f != null) {
            label.setStyle("-fx-background-image: url('" + f.path() + "'); -fx-background-size: cover; -fx-background-repeat: no-repeat; -fx-background-position: center;");
        }
    }

    @Override
    public void goBack() {
        if (rejectButton != null && rejectButton.getScene() != null) {
            Stage stage = (Stage) rejectButton.getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void goAhead() {
    }
}
