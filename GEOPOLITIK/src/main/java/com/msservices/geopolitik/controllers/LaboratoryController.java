package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.connection.DatabaseConnection;
import com.msservices.geopolitik.connection.queries.country.countryQueries;
import com.msservices.geopolitik.connection.queries.improvement.Improvement;
import com.msservices.geopolitik.connection.queries.improvement.improvementQueries;
import com.msservices.geopolitik.functions.improvementsFunctions.countryInvestigations;
import com.msservices.geopolitik.functions.improvementsFunctions.investigations;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class LaboratoryController implements interaction {

    private static final int ID_PAIS = 11;

    @FXML
    private Circle iconSuperior;

    @FXML
    private Rectangle iconExtra;

    @FXML
    private VBox listaInvestigacion;

    @FXML
    private VBox listaSeleccionadas;

    @FXML
    private Button botonAtras;

    @FXML
    private Button filtroTodas;

    @FXML
    private Button filtroMilitares;

    @FXML
    private Button filtroCiviles;

    @FXML
    private Label precioTotal;

    private List<Improvement> improvements;

    private double dineroRestante;

    private String filtroActivo = "Todas";

    @FXML
    public void initialize() {
        botonAtras.setOnAction(e -> goBack());
        filtroTodas.setOnAction(e -> filtrar("Todas"));
        filtroMilitares.setOnAction(e -> filtrar("Militar"));
        filtroCiviles.setOnAction(e -> filtrar("Civil"));
        cargarDinero();
        cargarMejoras();
        cargarSeleccionadas();
        marcarFiltroActivo(filtroTodas);
    }

    private void cargarDinero() {
        countryQueries queries = new countryQueries(DatabaseConnection.getInstance().getConnection());
        dineroRestante = queries.getCountryMoney(ID_PAIS);
        actualizarPrecioTotal();
    }

    private void actualizarPrecioTotal() {
        precioTotal.setText("$" + String.format("%.0f", dineroRestante));
    }

    private void cargarMejoras() {
        improvementQueries queries = new improvementQueries(DatabaseConnection.getInstance().getConnection());
        improvements = queries.getImprovements();

        pintarMejoras(filtroActivo);
    }

    private void filtrar(String type) {
        filtroActivo = type;
        marcarFiltroActivo(type.equals("Militar") ? filtroMilitares
                : type.equals("Civil") ? filtroCiviles : filtroTodas);
        pintarMejoras(type);
    }

    private void marcarFiltroActivo(Button activo) {
        for (Button boton : new Button[]{filtroTodas, filtroMilitares, filtroCiviles}) {
            boton.getStyleClass().remove("activo");
        }
        activo.getStyleClass().add("activo");
    }

    private void pintarMejoras(String type) {
        listaInvestigacion.getChildren().clear();
        for (Improvement improvement : improvements) {
            if (type.equals("Todas") || improvement.type().equals(type)) {
                listaInvestigacion.getChildren().add(crearFila(improvement));
            }
        }
    }

    private HBox crearFila(Improvement improvement) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("fila-investigacion");

        Rectangle icon = new Rectangle(30, 30);
        icon.getStyleClass().add("icono-blanco");

        VBox datos = new VBox(2);
        datos.getStyleClass().add("columna-detalle");

        Label nameLabel = new Label(improvement.name());
        nameLabel.getStyleClass().add("texto-dato");

        Label descriptionLabel = new Label(improvement.description());
        descriptionLabel.getStyleClass().add("texto-descripcion");
        descriptionLabel.setWrapText(true);

        datos.getChildren().addAll(nameLabel, descriptionLabel);

        row.getChildren().addAll(icon, datos);
        row.setOnMouseClicked(e -> seleccionarMejora(improvement, row));
        return row;
    }

    private void seleccionarMejora(Improvement improvement, HBox row) {
        investigations mejorasPais = countryInvestigations.getInstance().addInvestigations(ID_PAIS);

        if (!mejorasPais.addImprovement(improvement)) {
            return;
        }

        double precio = improvement.price() == null ? 0 : improvement.price();

        if (dineroRestante < precio) {
            mejorasPais.removeRow(improvement.idImprovement());
            mostrarNotificacion(row, "Dinero insuficiente");
            return;
        }

        dineroRestante -= precio;
        actualizarPrecioTotal();
        listaSeleccionadas.getChildren().add(crearFilaSeleccionada(improvement));
    }

    private void mostrarNotificacion(HBox row, String mensaje) {
        Label notificacion = new Label(mensaje);
        notificacion.getStyleClass().add("notificacion");
        notificacion.setWrapText(true);

        int index = listaInvestigacion.getChildren().indexOf(row);
        listaInvestigacion.getChildren().add(index, notificacion);

        PauseTransition espera = new PauseTransition(Duration.seconds(2));
        espera.setOnFinished(e -> listaInvestigacion.getChildren().remove(notificacion));
        espera.play();
    }

    private HBox crearFilaSeleccionada(Improvement improvement) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("fila-investigacion");

        Rectangle icon = new Rectangle(30, 30);
        icon.getStyleClass().add("icono-blanco");

        VBox datos = new VBox(2);
        datos.getStyleClass().add("columna-detalle");

        Label nameLabel = new Label(improvement.name());
        nameLabel.getStyleClass().add("texto-dato");

        String produccion = improvement.productionValue() == null
                ? "—"
                : String.format("%.2f", improvement.productionValue());
        Label productionLabel = new Label("Producción: " + produccion);
        productionLabel.getStyleClass().add("texto-secundario");

        Label typeLabel = new Label(improvement.type());
        typeLabel.getStyleClass().add("precio");

        datos.getChildren().addAll(nameLabel, productionLabel, typeLabel);

        Region spacer = new Region();
        spacer.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        Label durationLabel = new Label(improvement.duration() + " turnos");
        durationLabel.getStyleClass().add("texto-dato");
        durationLabel.setMinWidth(80);
        durationLabel.setAlignment(Pos.CENTER_RIGHT);

        Button deleteButton = new Button("Borrar");
        deleteButton.getStyleClass().add("boton-borrar");
        deleteButton.setOnAction(e -> borrarMejora(improvement, row));

        row.getChildren().addAll(icon, datos, spacer, durationLabel, deleteButton);
        return row;
    }

    private void borrarMejora(Improvement improvement, HBox row) {
        investigations mejorasPais = countryInvestigations.getInstance().getInvestigations(ID_PAIS);

        if (mejorasPais != null) {
            mejorasPais.removeRow(improvement.idImprovement());
        }

        dineroRestante += improvement.price() == null ? 0 : improvement.price();
        actualizarPrecioTotal();

        listaSeleccionadas.getChildren().remove(row);
    }

    private void cargarSeleccionadas() {
        investigations mejorasPais = countryInvestigations.getInstance().getInvestigations(ID_PAIS);

        listaSeleccionadas.getChildren().clear();
        if (mejorasPais == null) {
            return;
        }

        for (Improvement improvement : mejorasPais.getImprovements()) {
            dineroRestante -= improvement.price() == null ? 0 : improvement.price();
            listaSeleccionadas.getChildren().add(crearFilaSeleccionada(improvement));
        }

        actualizarPrecioTotal();
    }

    @Override
    public void goBack() {
        Stage stage = (Stage) botonAtras.getScene().getWindow();
        stage.close();
    }

    @Override
    public void goAhead() {
    }
}