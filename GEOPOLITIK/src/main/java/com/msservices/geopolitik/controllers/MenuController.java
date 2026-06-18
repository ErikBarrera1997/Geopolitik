package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.init.loadMap;
import com.msservices.geopolitik.init.loadMatch;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class MenuController {

    @FXML
    private ImageView imagenPrincipal;

    @FXML
    private ScrollPane scrollMapa;

    @FXML
    private Circle etiquetaRoja;

    @FXML
    private Rectangle etiquetaAzul;

    @FXML
    private VBox listaSuperior;

    @FXML
    private VBox listaInferior;

    @FXML
    private Button botonEjecutar;

    @FXML
    public void initialize() {
        loadMatch.loadMatch(loadMatch.getJsonData());
        loadMap.loadMapFromMatch(imagenPrincipal);

        scrollMapa.viewportBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            if (newBounds != null) {
                imagenPrincipal.setFitWidth(newBounds.getWidth());
            }
        });
    }
}
