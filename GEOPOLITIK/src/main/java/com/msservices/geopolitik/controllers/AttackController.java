package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.connection.DatabaseConnection;
import com.msservices.geopolitik.connection.queries.attack.attackQueries;
import com.msservices.geopolitik.connection.queries.attack.ProvinceObjective;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AttackController implements interaction, attack {

    @FXML private VBox listaAcciones;
    @FXML private Button botonAtras;
    @FXML private ImageView lblCountryOrigin;
    @FXML private ImageView lblCountryDestiny;
    @FXML private Label descripcion;
    @FXML private ListView<HBox> listaOpciones;
    @FXML private ListView<VBox> listaResultados;
    @FXML private ListView<VBox> listaObjetivosFijados;
    @FXML private Rectangle botonEliminar;
    @FXML private Button botonAtaque;
    @FXML private Button botonCancelar;

    private final Set<Integer> objetivosAgregados = new HashSet<>();

    private combatOption option;
    private int countryId;
    private String countryName;

    private static final int ORIGIN_COUNTRY_ID = 11;

    private static final String[][] ATTACK_TYPES = {
        {"Atacar infraestructura civil", "civillian_inf_objectives", "CIVIL_INFRA"},
        {"Atacar infraestructura militar", "military_inf_objectives", "MILITARY_INFRA"},
        {"Atacar civiles", "civillian_objectives", "CIVILIANS"},
        {"Atacar objetivos militares", "military_objectives", "MILITARY_OBJECTIVES"}
    };

    private String selectedQuery;

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
        if (listaResultados != null) {
            listaResultados.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    Object data = newVal.getUserData();
                    if (data instanceof List<?> list && !list.isEmpty()) {
                        agregarObjetivosFijados(new ArrayList<>((List<ProvinceObjective>) list));
                    }
                }
            });
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
        if (listaResultados != null) {
            loadResultados(selectedQuery != null ? selectedQuery : "CIVIL_INFRA");
        }
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

    private void loadResultados(String selector) {
        if (countryId <= 0) {
            return;
        }

        listaResultados.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(VBox item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || item == null ? null : item);
            }
        });

        listaResultados.getItems().clear();

        attackQueries queries = new attackQueries(DatabaseConnection.getInstance().getConnection());
        List<ProvinceObjective> rows = switch (selector) {
            case "MILITARY_INFRA" -> queries.getMilitaryInfrastructureByCountry(countryId);
            case "CIVILIANS" -> queries.getCiviliansByCountry(countryId);
            case "MILITARY_OBJECTIVES" -> queries.getMilitaryObjectivesByCountry(countryId);
            default -> queries.getCivilInfrastructureByCountry(countryId);
        };

        Map<Integer, List<ProvinceObjective>> grouped = new LinkedHashMap<>();
        for (ProvinceObjective row : rows) {
            grouped.computeIfAbsent(row.idProvince(), k -> new ArrayList<>()).add(row);
        }

        for (List<ProvinceObjective> prov : grouped.values()) {
            listaResultados.getItems().add(buildProvinceRow(prov));
        }
    }

    private VBox buildProvinceRow(List<ProvinceObjective> infras) {
        ProvinceObjective first = infras.get(0);

        VBox fila = new VBox(5);
        fila.getStyleClass().add("fila-principal");
        fila.setUserData(infras);

        HBox encabezado = new HBox(10);
        encabezado.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        encabezado.getStyleClass().add("fila-encabezado");

        Button botonExpandir = new Button("▼");
        botonExpandir.getStyleClass().add("boton-expandir");

        Label etiquetaProvincia = new Label(first.provinceName());
        etiquetaProvincia.getStyleClass().add("texto-provincia");

        encabezado.getChildren().addAll(botonExpandir, etiquetaProvincia);

        VBox subLista = new VBox(3);
        subLista.getStyleClass().add("sublista");
        subLista.setVisible(false);
        subLista.setManaged(false);

        VBox listaObjetivos = new VBox(3);
        listaObjetivos.setSpacing(3);
        listaObjetivos.getStyleClass().add("lista-objetivos");

        boolean hasInfra = first.objectiveName() != null;
        if (hasInfra) {
            for (ProvinceObjective infra : infras) {
                Label objetivo = new Label("- " + infra.objectiveName() + " (x" + infra.objectiveQuantity() + ")");
                objetivo.getStyleClass().add("texto-objetivo");
                listaObjetivos.getChildren().add(objetivo);
            }
        } else {
            Label sinDatos = new Label("No hay infraestructuras en esta provincia");
            sinDatos.getStyleClass().add("texto-sublista");
            listaObjetivos.getChildren().add(sinDatos);
        }

        subLista.getChildren().add(listaObjetivos);

        fila.getChildren().addAll(encabezado, subLista);

        botonExpandir.setOnAction(e -> {
            boolean show = !subLista.isVisible();
            subLista.setVisible(show);
            subLista.setManaged(show);
            botonExpandir.setText(show ? "▲" : "▼");
        });

        return fila;
    }

    private void agregarObjetivosFijados(List<ProvinceObjective> infras) {
        if (infras == null || infras.isEmpty()) {
            return;
        }
        ProvinceObjective first = infras.get(0);
        if (first.objectiveName() == null) {
            return;
        }
        if (!objetivosAgregados.add(first.idProvince())) {
            return;
        }
        listaObjetivosFijados.getItems().add(buildObjetivoRow(infras));
        updateThirdListSize();
    }

    private VBox buildObjetivoRow(List<ProvinceObjective> infras) {
        ProvinceObjective first = infras.get(0);

        VBox row = new VBox(3);
        row.getStyleClass().add("fila-seleccion");
        row.setUserData(infras);

        Label provincia = new Label(first.provinceName());
        provincia.getStyleClass().add("texto-provincia");
        row.getChildren().add(provincia);

        for (ProvinceObjective infra : infras) {
            Label objetivo = new Label("- " + infra.objectiveName() + " (x" + infra.objectiveQuantity() + ")");
            objetivo.getStyleClass().add("texto-objetivo");
            row.getChildren().add(objetivo);
        }
        return row;
    }

    private void updateThirdListSize() {
        double maxAltura = listaOpciones != null ? Math.max(40, listaOpciones.getPrefHeight()) : 210;
        double contenido = 0;
        for (VBox row : listaObjetivosFijados.getItems()) {
            int objetivos = row.getChildren().size() - 1;
            contenido += 19 + objetivos * 20 + 8;
        }
        double alto = Math.max(40, Math.min(contenido, maxAltura));
        listaObjetivosFijados.setPrefHeight(alto);

        Scene scene = listaObjetivosFijados.getScene();
        if (scene != null && scene.getWindow() instanceof Stage stage) {
            javafx.application.Platform.runLater(stage::sizeToScene);
        }
    }

    @FXML
    private void borrarObjetivos(javafx.scene.input.MouseEvent e) {
        listaObjetivosFijados.getItems().clear();
        objetivosAgregados.clear();
        updateThirdListSize();
    }

    private void loadAttackTypes() {
        listaOpciones.getItems().clear();
        for (String[] attack : ATTACK_TYPES) {
            HBox row = new HBox(10);
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            row.setUserData(attack[2]);

            ImageView icon = new ImageView();
            icon.setFitWidth(28);
            icon.setFitHeight(28);
            Image img = loadImages.getIcon(attack[1]);
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

        int count = listaOpciones.getItems().size();
        if (count > 0) {
            listaOpciones.setFixedCellSize(listaOpciones.getPrefHeight() / count);
        }

        listaOpciones.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Object sel = newVal.getUserData();
                if (sel != null) {
                    selectedQuery = (String) sel;
                }
                if (listaResultados != null) {
                    loadResultados(selectedQuery);
                }
            }
        });

        listaOpciones.getSelectionModel().select(0);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/msservices/geopolitik/views/combatViews/attackView.fxml"));
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
        artilleryAttack.launchAttackToMilitaryUnits(2000);
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
