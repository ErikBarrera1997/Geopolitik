package com.msservices.geopolitik.controllers.diplomacy;

import com.msservices.geopolitik.init.entity.flag;
import com.msservices.geopolitik.init.loadFlags;
import com.msservices.geopolitik.init.loadImages;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class warDeclarationController implements interaction {

    @FXML
    private Label iconLabel;

    @FXML
    private Label flag1;

    @FXML
    private Label flag2;

    @FXML
    private Label declarationLabel;

    @FXML
    private VBox casusBelliList;

    @FXML
    private Button declareWarButton;

    @FXML
    private Button rejectButton;

    @FXML
    public void initialize() {
        if (rejectButton != null) {
            rejectButton.setOnAction(e -> goBack());
        }
    }

    public void setCountryData(int sourceCountryId, int targetCountryId) {
        applyFlag(flag1, sourceCountryId);
        applyFlag(flag2, targetCountryId);

        Image icon = loadImages.getIcon("military_actions");
        if (icon != null) {
            ImageView view = new ImageView(icon);
            view.setFitWidth(60);
            view.setFitHeight(60);
            view.setPreserveRatio(true);
            iconLabel.setGraphic(view);
        }
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
