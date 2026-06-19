package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.functions.mapFunctions;
import com.msservices.geopolitik.init.loadMap;
import com.msservices.geopolitik.init.loadMatch;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

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


    //INIT
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

    //Map clic event
    @FXML
    private void onImagenPrincipalClick(MouseEvent event) {
        double colorValue = mapFunctions.getColor((int) event.getX(), (int) event.getY());
        String country = mapFunctions.getCountryFromList(colorValue);
        openCountryView();
    }

    private void openCountryView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/msservices/geopolitik/views/countryView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("País");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
