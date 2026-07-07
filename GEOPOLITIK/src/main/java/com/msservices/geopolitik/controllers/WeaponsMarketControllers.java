package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.connection.Data.Weapon;
import com.msservices.geopolitik.init.loadImages;
import com.msservices.geopolitik.init.loadWeapons;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    private ComboBox<Weapon> listaDesplegable;

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
        ObservableList<Weapon> weaponItems = FXCollections.observableArrayList(weapons);
        listaDesplegable.setItems(weaponItems);

        listaDesplegable.setPromptText("Armas disponibles");

        listaDesplegable.setCellFactory(param -> new WeaponTileCell());
        listaDesplegable.setButtonCell(new ListCell<Weapon>() {
            @Override
            protected void updateItem(Weapon weapon, boolean empty) {
                super.updateItem(weapon, empty);
                if (empty || weapon == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(weapon.getName());
                    setGraphic(null);
                }
            }
        });

        listaDesplegable.setOnAction(e -> {
            selectedWeapon = listaDesplegable.getValue();
            if (selectedWeapon != null) {
                etiquetaTexto.setText(selectedWeapon.getName());
                agregarAlCarrito(selectedWeapon);
            }
            actualizarBotonCantidad();
        });

        sliderCantidad.valueProperty().addListener((obs, old, val) -> actualizarBotonCantidad());
        campoCantidad.textProperty().addListener((obs, old, val) -> actualizarBotonCantidad());

        botonComprar.setOnAction(e -> onComprarClick());
    }

    private void agregarAlCarrito(Weapon weapon) {
        int cantidad = (int) sliderCantidad.getValue();
        cartNames.add(weapon.getName());
        cartQtys.add(cantidad);
        cartCosts.add(weapon.getCost());
    }

    private void actualizarBotonCantidad() {
        int cantidad = (int) sliderCantidad.getValue();
        botonCantidad.setText("Cantidad: " + cantidad);
    }

    private class WeaponTileCell extends ListCell<Weapon> {
        @Override
        protected void updateItem(Weapon weapon, boolean empty) {
            super.updateItem(weapon, empty);
            if (empty || weapon == null) {
                setGraphic(null);
                setText(null);
            } else {
                Image img = loadImages.getWeaponImage(weapon.getName());
                javafx.scene.Node graphic;
                if (img != null) {
                    ImageView imageView = new ImageView(img);
                    imageView.setFitWidth(40);
                    imageView.setFitHeight(40);
                    graphic = imageView;
                } else {
                    Rectangle rect = new Rectangle(40, 40);
                    rect.setFill(Color.web("#d9c97a"));
                    rect.setStroke(Color.web("#1a3a1a"));
                    rect.setStrokeWidth(2);
                    rect.setArcWidth(5);
                    rect.setArcHeight(5);
                    graphic = rect;
                }

                Label nameLabel = new Label(weapon.getName());
                nameLabel.setStyle("-fx-text-fill: #1a3a1a; -fx-font-size: 11px; -fx-font-weight: bold;");

                Label costLabel = new Label("$" + String.format("%.0f", weapon.getCost()));
                costLabel.setStyle("-fx-text-fill: #5a3a1a; -fx-font-size: 10px;");

                VBox info = new VBox(2, nameLabel, costLabel);
                info.setAlignment(Pos.CENTER_LEFT);

                HBox cell = new HBox(10, graphic, info);
                cell.setAlignment(Pos.CENTER_LEFT);
                cell.setPadding(new Insets(4));

                setGraphic(cell);
                setText(null);
            }
        }
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/msservices/geopolitik/views/marketConfirmView.fxml"));
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
