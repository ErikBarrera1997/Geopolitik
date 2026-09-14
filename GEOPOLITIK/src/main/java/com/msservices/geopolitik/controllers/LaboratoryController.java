package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.connection.DatabaseConnection;
import com.msservices.geopolitik.connection.queries.improvement.Improvement;
import com.msservices.geopolitik.connection.queries.improvement.improvementQueries;
import com.msservices.geopolitik.interfaces.interaction;
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

import java.util.List;

public class LaboratoryController implements interaction {

    @FXML
    private Circle iconSuperior;

    @FXML
    private Rectangle iconExtra;

    @FXML
    private VBox listaInvestigacion;

    @FXML
    private Button botonAtras;

    @FXML
    public void initialize() {
        botonAtras.setOnAction(e -> goBack());
        cargarMejoras();
    }

    private void cargarMejoras() {
        improvementQueries queries = new improvementQueries(DatabaseConnection.getInstance().getConnection());
        List<Improvement> improvements = queries.getImprovements();

        listaInvestigacion.getChildren().clear();
        for (Improvement improvement : improvements) {
            listaInvestigacion.getChildren().add(crearFila(improvement));
        }
    }

    private HBox crearFila(Improvement improvement) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("fila-investigacion");

        Rectangle icon = new Rectangle(30, 30);
        icon.getStyleClass().add("icono-blanco");

        Label nameLabel = new Label(improvement.name());
        nameLabel.getStyleClass().add("texto-dato");

        Region spacer = new Region();
        spacer.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        String precio = improvement.price() == null ? "—" : String.format("$%.0f", improvement.price());
        Label priceLabel = new Label(precio);
        priceLabel.getStyleClass().add("precio");

        row.getChildren().addAll(icon, nameLabel, spacer, priceLabel);
        return row;
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
