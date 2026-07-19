package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.connection.queries.weapon.Weapon;
import com.msservices.geopolitik.init.loadImages;
import com.msservices.geopolitik.init.loadWeapons;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

public class WeaponsMarketControllers implements interaction {

    @FXML
    private Rectangle imagenIcono;

    @FXML
    private Rectangle imagenArma;

    @FXML
    private Label etiquetaTexto;

    @FXML
    private FlowPane gridArmas;

    @FXML
    private ScrollPane scrollArmas;

    @FXML
    private Slider sliderCantidad;

    @FXML
    private TextField campoCantidad;

    @FXML
    private Button botonComprar;

    @FXML
    private Button botonNegro;

    @FXML
    private Button botonRegresar;

    @FXML
    private Button botonCantidad;

    @FXML
    private VBox cantidadPanel;

    private Weapon selectedWeapon;

    private final List<String> cartNames = new ArrayList<>();
    private final List<Integer> cartQtys = new ArrayList<>();
    private final List<Double> cartCosts = new ArrayList<>();

    private Stage cantidadStage;
    private Slider popupSlider;
    private TextField popupCampo;

    @FXML
    public void initialize() {
        List<Weapon> weapons = loadWeapons.getWeapons();
        gridArmas.getChildren().clear();

        for (Weapon weapon : weapons) {
            VBox tile = createWeaponTile(weapon);
            gridArmas.getChildren().add(tile);
        }

        gridArmas.widthProperty().addListener((obs, old, val) -> recalcularAnchoTiles());
        gridArmas.parentProperty().addListener((obs, old, val) -> recalcularAnchoTiles());
        gridArmas.sceneProperty().addListener((obs, old, val) -> recalcularAnchoTiles());

        sliderCantidad.valueProperty().addListener((obs, old, val) -> actualizarBotonCantidad());
        campoCantidad.textProperty().addListener((obs, old, val) -> actualizarBotonCantidad());

        botonComprar.setOnAction(e -> onComprarClick());
    }

    private void recalcularAnchoTiles() {
        double availableWidth = gridArmas.getWidth();
        if (availableWidth <= 0) return;
        int cols = 3;
        double hgap = 8;
        double tileWidth = (availableWidth - (cols - 1) * hgap) / cols;
        for (javafx.scene.Node child : gridArmas.getChildren()) {
            if (child instanceof VBox tile) {
                tile.setPrefWidth(tileWidth);
            }
        }
    }

    private VBox createWeaponTile(Weapon weapon) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);

        Image img = loadImages.getWeaponImage(weapon.name());
        if (img != null) {
            imageView.setImage(img);
        }

        Label nameLabel = new Label(weapon.name());
        nameLabel.setStyle("-fx-text-fill: #f0ead0; -fx-font-size: 10px; -fx-font-weight: bold;");
        nameLabel.setWrapText(true);
        nameLabel.setAlignment(Pos.CENTER);

        Label costLabel = new Label("$" + String.format("%.0f", weapon.cost()));
        costLabel.setStyle("-fx-text-fill: #d9c97a; -fx-font-size: 9px;");
        costLabel.setAlignment(Pos.CENTER);

        VBox tile = new VBox(4, imageView, nameLabel, costLabel);
        tile.setAlignment(Pos.CENTER);
        tile.setPadding(new Insets(6));
        tile.setStyle("-fx-background-color: #3a5a3a; -fx-border-color: #1a3a1a; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-cursor: hand;");

        tile.setOnMouseClicked(e -> {
            selectedWeapon = weapon;
            etiquetaTexto.setText(weapon.name());
            agregarAlCarrito(weapon);
            actualizarBotonCantidad();
            for (javafx.scene.Node child : gridArmas.getChildren()) {
                child.setStyle("-fx-background-color: #3a5a3a; -fx-border-color: #1a3a1a; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-cursor: hand;");
            }
            tile.setStyle("-fx-background-color: #4a6a4a; -fx-border-color: #c49a3a; -fx-border-width: 2; -fx-border-radius: 4; -fx-background-radius: 4; -fx-cursor: hand;");
        });

        tile.setOnMouseEntered(e -> {
            if (selectedWeapon != weapon) {
                tile.setStyle("-fx-background-color: #4a6a4a; -fx-border-color: #1a3a1a; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-cursor: hand;");
            }
        });

        tile.setOnMouseExited(e -> {
            if (selectedWeapon != weapon) {
                tile.setStyle("-fx-background-color: #3a5a3a; -fx-border-color: #1a3a1a; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-cursor: hand;");
            }
        });

        return tile;
    }

    private void agregarAlCarrito(Weapon weapon) {
        int cantidad = (int) sliderCantidad.getValue();
        cartNames.add(weapon.name());
        cartQtys.add(cantidad);
        cartCosts.add(weapon.cost());
    }

    private void actualizarBotonCantidad() {
        int cantidad = (int) sliderCantidad.getValue();
        botonCantidad.setText("Cantidad: " + cantidad);
    }

    @FXML
    private void onSeleccionarCantidad() {
        if (cantidadStage == null) {
            cantidadStage = new Stage();
            cantidadStage.initModality(Modality.APPLICATION_MODAL);
            cantidadStage.initOwner(botonCantidad.getScene().getWindow());
            cantidadStage.initStyle(StageStyle.UNDECORATED);
            cantidadStage.setTitle("Cantidad");

            VBox root = new VBox(15);
            root.setPadding(new Insets(20));
            root.setStyle("-fx-background-color: #2e4e2e; -fx-border-color: #1a3a1a; -fx-border-width: 2; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 6, 0, 2, 2);");

            Label label = new Label("Cantidad");
            label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #f0ead0;");

            popupSlider = new Slider(0, 100, sliderCantidad.getValue());
            popupSlider.setPrefWidth(200);
            popupSlider.setStyle("-fx-background-color: #d9c97a;");

            popupCampo = new TextField(String.valueOf((int) sliderCantidad.getValue()));
            popupCampo.setPrefWidth(60);
            popupCampo.setStyle("-fx-background-color: #f0ead0; -fx-border-color: #1a3a1a; -fx-border-width: 1; -fx-text-fill: #1a3a1a;");

            popupSlider.valueProperty().addListener((obs, old, val) -> {
                popupCampo.setText(String.valueOf(val.intValue()));
                sliderCantidad.setValue(val.doubleValue());
                campoCantidad.setText(String.valueOf(val.intValue()));
            });

            popupCampo.textProperty().addListener((obs, old, val) -> {
                try {
                    double v = Double.parseDouble(val);
                    popupSlider.setValue(v);
                    sliderCantidad.setValue(v);
                } catch (NumberFormatException ignored) {}
            });

            HBox row = new HBox(10, popupSlider, popupCampo);
            row.setAlignment(Pos.CENTER_LEFT);

            Button botonAceptar = new Button("Aceptar");
            botonAceptar.setStyle("-fx-background-color: #3a5a3a; -fx-text-fill: #f0ead0; -fx-font-weight: bold; -fx-font-size: 12px; -fx-padding: 6 16; -fx-border-color: #1a3a1a; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-cursor: hand;");
            botonAceptar.setOnAction(e -> {
                sliderCantidad.setValue(popupSlider.getValue());
                campoCantidad.setText(popupCampo.getText());
                actualizarBotonCantidad();
                if (selectedWeapon != null) {
                    agregarAlCarrito(selectedWeapon);
                }
                cantidadStage.close();
            });

            Button botonRegresarPopup = new Button("Regresar");
            botonRegresarPopup.setStyle("-fx-background-color: #c49a3a; -fx-text-fill: #1a3a1a; -fx-font-weight: bold; -fx-font-size: 12px; -fx-padding: 6 16; -fx-border-color: #1a3a1a; -fx-border-width: 1; -fx-border-radius: 4; -fx-background-radius: 4; -fx-cursor: hand;");
            botonRegresarPopup.setOnAction(e -> cantidadStage.close());

            HBox botonera = new HBox(10, botonAceptar, botonRegresarPopup);
            botonera.setAlignment(Pos.CENTER_RIGHT);

            root.getChildren().addAll(label, row, botonera);
            Scene scene = new Scene(root);
            scene.setFill(null);
            cantidadStage.setScene(scene);
        } else {
            popupSlider.setValue(sliderCantidad.getValue());
            popupCampo.setText(String.valueOf((int) sliderCantidad.getValue()));
        }
        cantidadStage.showAndWait();
    }

    @FXML
    private void onComprarClick() {
        if (cartNames.isEmpty()) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/msservices/geopolitik/views/weaponsViews/marketConfirmView.fxml"));
            Parent root = loader.load();
            MarketConfirmControllers controller = loader.getController();

            controller.setCartData(
                new ArrayList<>(cartNames),
                new ArrayList<>(cartQtys),
                new ArrayList<>(cartCosts),
                1
            );

            Stage stage = new Stage();
            controller.setStage(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(botonComprar.getScene().getWindow());
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Confirmar compra");

            Scene scene = new Scene(root);
            scene.setFill(null);
            stage.setScene(scene);
            stage.showAndWait();

            if (controller.isResetCart()) {
                cartNames.clear();
                cartQtys.clear();
                cartCosts.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void goBack() {
        Stage stage = (Stage) botonRegresar.getScene().getWindow();
        stage.close();
    }

    @Override
    public void goAhead() {
    }
}
