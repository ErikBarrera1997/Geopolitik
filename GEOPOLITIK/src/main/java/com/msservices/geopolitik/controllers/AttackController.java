package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.init.loadCombatActions;
import com.msservices.geopolitik.init.entity.combatOption;
import com.msservices.geopolitik.init.loadImages;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class AttackController implements interaction {

    @FXML private VBox listaAcciones;
    @FXML private Button botonAtras;

    @FXML private Rectangle cuadroIzquierdo;
    @FXML private Rectangle cuadroDerecho;
    @FXML private TextArea descripcion;
    @FXML private VBox listaOpciones;
    @FXML private CheckBox checkCivil;
    @FXML private CheckBox checkMilitar;
    @FXML private CheckBox checkPoblacion;
    @FXML private CheckBox checkObjetivos;
    @FXML private Rectangle iconCivil;
    @FXML private Rectangle iconMilitar;
    @FXML private Rectangle iconPoblacion;
    @FXML private Rectangle iconObjetivos;
    @FXML private TextArea efecto;
    @FXML private Button botonAtaque;
    @FXML private Button botonCancelar;

    private combatOption option;

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

    @FXML
    public void initialize() {
        if (botonAtras != null) {
            botonAtras.setOnAction(e -> goBack());
            loadCombatActions();
        }
        if (botonCancelar != null) {
            botonCancelar.setOnAction(e -> goBack());
        }
    }

    public void setCombatOption(combatOption option) {
        this.option = option;
        descripcion.setText("Acción seleccionada: " + option.name());
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
}
