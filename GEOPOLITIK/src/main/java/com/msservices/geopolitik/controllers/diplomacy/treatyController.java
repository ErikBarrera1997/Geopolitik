package com.msservices.geopolitik.controllers.diplomacy;

import com.msservices.geopolitik.connection.DatabaseConnection;
import com.msservices.geopolitik.connection.queries.country.countryQueries;
import com.msservices.geopolitik.connection.queries.diplomacy.countryResource;
import com.msservices.geopolitik.connection.queries.diplomacy.countryResourceQueries;
import com.msservices.geopolitik.init.loadFlags;
import com.msservices.geopolitik.init.loadImages;
import com.msservices.geopolitik.init.entity.flag;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class treatyController implements interaction {

    @FXML private Label treatyImage;
    @FXML private Label treatyNameLabel;
    @FXML private Label sourceCountryFlag;
    @FXML private Label treatyTypeImage;
    @FXML private Label targetCountryFlag;
    @FXML private Label actionInfoLabel;
    @FXML private HBox resourcesBox;
    @FXML private VBox resourceList;
    @FXML private ScrollPane resourceScroll;
    @FXML private TextField percentField;
    @FXML private Label percentLabel;
    @FXML private TextField agreement;
    @FXML private HBox moneyBox;
    @FXML private Label moneyLabel;
    @FXML private TextField moneyField;
    @FXML private ScrollPane militaryListScroll;
    @FXML private VBox militaryList;
    @FXML private ScrollPane ultimatumListScroll;
    @FXML private VBox ultimatumList;
    @FXML private VBox gaugeBox;
    @FXML private Button signTreatyButton;
    @FXML private Button backButton;

    private static final String ACTION_COMMERCIAL = "Acuerdo comercial";
    private static final String ACTION_NON_AGGRESSION = "Firmar tratado de no agresión";
    private static final String ACTION_MILITARY_ALLIANCE = "Crear alianza militar";
    private static final String ACTION_AIRSPACE = "Solicitar acceso a espacio aéreo";
    private static final String ACTION_ECONOMIC_SUPPORT = "Enviar apoyo económico";
    private static final String ACTION_MILITARY_SUPPORT = "Enviar apoyo militar";
    private static final String ACTION_ULTIMATUM = "Enviar ultimátum";

    private int sourceCountryId;
    private int targetCountryId;
    private String actionName;
    private HBox selectedRow;
    private countryResource selectedResource;

    public void setCountryData(int sourceCountryId, int targetCountryId, String actionName) {
        this.sourceCountryId = sourceCountryId;
        this.targetCountryId = targetCountryId;
        this.actionName = actionName;
        treatyNameLabel.setText(actionName);
        loadFlags();
        applyActionLayout();
        if (ACTION_COMMERCIAL.equals(actionName)) {
            loadResources();
        }
        if (ACTION_ECONOMIC_SUPPORT.equals(actionName)) {
            loadMoney();
        }
        militaryList.getChildren().clear();
        ultimatumList.getChildren().clear();
    }

    public void setActionInfo(String text) {
        actionInfoLabel.setText(text == null ? "" : text);
    }

    private void applyActionLayout() {
        boolean commercial = ACTION_COMMERCIAL.equals(actionName);
        boolean economicSupport = ACTION_ECONOMIC_SUPPORT.equals(actionName);
        boolean militarySupport = ACTION_MILITARY_SUPPORT.equals(actionName);
        boolean ultimatum = ACTION_ULTIMATUM.equals(actionName);
        boolean gauge = commercial
                || ACTION_NON_AGGRESSION.equals(actionName)
                || ACTION_MILITARY_ALLIANCE.equals(actionName)
                || ACTION_AIRSPACE.equals(actionName)
                || ultimatum;

        setVisible(resourcesBox, commercial);
        setVisible(agreement, commercial);
        setVisible(moneyBox, economicSupport);
        setVisible(militaryListScroll, militarySupport);
        setVisible(ultimatumListScroll, ultimatum);
        setVisible(gaugeBox, gauge);
    }

    private void setVisible(Node node, boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }

    private void loadMoney() {
        countryQueries queries = new countryQueries(DatabaseConnection.getInstance().getConnection());
        double money = queries.getCountryMoney(sourceCountryId);
        moneyLabel.setText("$" + String.format("%.0f", money));
    }

    private void loadFlags() {
        if (sourceCountryId > 0) {
            flag f = loadFlags.getFlag(sourceCountryId);
            if (f != null) {
                sourceCountryFlag.setStyle("-fx-background-image: url('" + f.path() + "'); -fx-background-size: cover; -fx-background-repeat: no-repeat; -fx-background-position: center;");
            }
        }
        if (targetCountryId > 0) {
            flag f = loadFlags.getFlag(targetCountryId);
            if (f != null) {
                targetCountryFlag.setStyle("-fx-background-image: url('" + f.path() + "'); -fx-background-size: cover; -fx-background-repeat: no-repeat; -fx-background-position: center;");
            }
        }
    }

    private void loadResources() {
        resourceList.getChildren().clear();
        countryResourceQueries queries = new countryResourceQueries(DatabaseConnection.getInstance().getConnection());
        List<countryResource> resources = queries.getCountryResources(sourceCountryId);
        for (countryResource r : resources) {
            HBox row = new HBox(5);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("fila-dato");
            row.setCursor(Cursor.HAND);

            Image icon = loadImages.getIcon(resourceIcon(r.resourceName()));
            if (icon != null) {
                ImageView imageView = new ImageView(icon);
                imageView.setFitWidth(20);
                imageView.setFitHeight(20);
                imageView.setPreserveRatio(true);
                row.getChildren().add(imageView);
            }

            Label nameLabel = new Label(r.resourceName());
            row.getChildren().add(nameLabel);

            row.setOnMouseClicked(e -> selectResource(row, r));
            resourceList.getChildren().add(row);
        }
    }

    private void selectResource(HBox row, countryResource resource) {
        if (selectedRow != null) {
            selectedRow.setStyle("");
        }
        selectedRow = row;
        selectedResource = resource;
        row.setStyle("-fx-border-color: #1e90ff; -fx-border-width: 2;");
        percentField.setDisable(false);
        updateAgreement();
    }

    private void updateAgreement() {
        if (agreement == null) {
            return;
        }
        if (selectedResource == null) {
            agreement.setText("");
            return;
        }
        String percent = percentField.getText();
        agreement.setText(percent.isEmpty()
                ? selectedResource.resourceName()
                : selectedResource.resourceName() + ": " + percent + "%");
    }

    private String resourceIcon(String name) {
        if (name == null) {
            return null;
        }
        return switch (name) {
            case "Agrícultura", "Agricultura" -> "farming";
            case "Petróleo" -> "oil";
            case "Industria" -> "industry";
            case "Madera" -> "wood";
            case "Gas natural" -> "gas";
            case "Pesca" -> "fishing";
            case "Oro" -> "gold";
            case "Uranio" -> "uranium";
            case "Acero" -> "steel";
            case "Electricidad", "electricidad" -> "electricity";
            case "Dinero" -> "money";
            default -> null;
        };
    }

    @FXML
    public void initialize() {
        backButton.setOnAction(e -> goBack());
        percentField.setDisable(true);
        percentField.setTextFormatter(new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change;
            }
            if (!newText.matches("\\d{1,3}")) {
                return null;
            }
            int value = Integer.parseInt(newText);
            return value >= 1 && value <= 100 ? change : null;
        }));
        percentField.textProperty().addListener((obs, oldValue, newValue) -> updateAgreement());
        moneyField.setTextFormatter(new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }
            return change.getControlNewText().matches("\\d{0,9}") ? change : null;
        }));
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