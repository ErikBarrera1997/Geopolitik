package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.init.loadCombatActions;
import com.msservices.geopolitik.init.entity.combatDescription;
import com.msservices.geopolitik.init.entity.combatOption;
import com.msservices.geopolitik.init.loadFlags;
import com.msservices.geopolitik.init.loadImages;
import com.msservices.geopolitik.init.entity.flag;
import com.msservices.geopolitik.interfaces.attack;
import com.msservices.geopolitik.interfaces.interaction;
import com.msservices.geopolitik.functions.combatFuctions.artilleryAttack;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class AttackController implements interaction, attack {

    @FXML private VBox listaAcciones;
    @FXML private Button botonAtras;
    @FXML private ImageView lblCountryOrigin;
    @FXML private ImageView lblCountryDestiny;
    @FXML private Label descripcion;
    @FXML private ListView<HBox> listaOpciones;
    @FXML private ListView<HBox> listaResultados;
    @FXML private Button botonAtaque;
    @FXML private Button botonCancelar;

    private combatOption option;
    private int countryId;
    private String countryName;

    private static final int ORIGIN_COUNTRY_ID = 11;

    private static final String[][] ATTACK_TYPES = {
        {"Atacar infraestructura civil", "1"},
        {"Atacar infraestructura militar", "2"},
        {"Atacar civiles", "12"},
        {"Atacar objetivos militares", "11"}
    };

    @FXML
    public void initialize() {
        if (botonAtras != null) {
            botonAtras.setOnAction(e -> goBack());
            loadCombatActions();
        }
        if (botonCancelar != null) {
            botonCancelar.setOnAction(e -> goBack());
        }
        if (botonAtaque != null) {
            botonAtaque.setOnAction(e -> launchAttack());
        }
        if (listaOpciones != null) {
            loadAttackTypes();
        }
    }

    public void setCombatOption(combatOption option) {
        this.option = option;
        if (descripcion != null) {
            List<combatDescription> descriptions = loadCombatActions.getCombatDescriptionsList();
            for (combatDescription desc : descriptions) {
                if (desc.name().equals(option.name())) {
                    descripcion.setText(desc.desc());
                    return;
                }
            }
            descripcion.setText(option.name());
        }
    }

    public void setCountryData(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
        loadFlags();
    }

    private void loadFlags() {
        if (lblCountryOrigin != null) {
            flag originFlag = loadFlags.getFlag(ORIGIN_COUNTRY_ID);
            if (originFlag != null) {
                lblCountryOrigin.setImage(new Image(originFlag.path(), 80, 40, true, true));
            }
        }
        if (lblCountryDestiny != null && countryId > 0) {
            flag destinyFlag = loadFlags.getFlag(countryId);
            if (destinyFlag != null) {
                lblCountryDestiny.setImage(new Image(destinyFlag.path(), 80, 40, true, true));
            }
        }
    }

    private void loadAttackTypes() {
        listaOpciones.getItems().clear();
        for (String[] attack : ATTACK_TYPES) {
            HBox row = new HBox(10);
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            ImageView icon = new ImageView();
            icon.setFitWidth(28);
            icon.setFitHeight(28);
            Image img = loadImages.getAttackIcon(attack[1]);
            if (img != null) {
                icon.setImage(img);
            }

            Label label = new Label(attack[0]);
            label.getStyleClass().add("texto-dato");

            row.getChildren().addAll(icon, label);
            listaOpciones.getItems().add(row);
        }

        listaOpciones.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                }
            }
        });
    }

    private void loadCombatActions() {
        List<combatOption> combatList = loadCombatActions.getCombatList();
        listaAcciones.getChildren().clear();

        for (combatOption opt : combatList) {
            HBox row = new HBox(10);
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            row.getStyleClass().add("fila-accion");
            row.setPickOnBounds(true);

            ImageView icon = new ImageView();
            icon.setFitWidth(28);
            icon.setFitHeight(28);
            Image attackImage = loadImages.getAttackIcon(String.valueOf(opt.value()));
            if (attackImage != null) {
                icon.setImage(attackImage);
            }

            Label label = new Label(opt.name());
            label.getStyleClass().add("texto-dato");

            row.getChildren().addAll(icon, label);
            row.setOnMouseClicked(e -> openAttackView(opt));
            listaAcciones.getChildren().add(row);
        }
    }

    private void openAttackView(combatOption option) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/msservices/geopolitik/views/attackView.fxml"));
            Parent root = loader.load();
            AttackController controller = loader.getController();
            controller.setCombatOption(option);
            controller.setCountryData(countryId, countryName);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(botonAtras.getScene().getWindow());
            stage.setTitle(option.name());
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void launchAttack() {
        artilleryAttack.launchAttackToMilitaryUnits(2, 100, 10);
    }

    @Override
    public void goBack() {
        Stage stage;
        if (botonCancelar != null && botonCancelar.getScene() != null) {
            stage = (Stage) botonCancelar.getScene().getWindow();
        } else {
            stage = (Stage) botonAtras.getScene().getWindow();
        }
        stage.close();
    }

    @Override
    public void goAhead() {
    }
}
