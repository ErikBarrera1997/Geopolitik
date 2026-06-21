package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.connection.Data.Country;
import com.msservices.geopolitik.connection.DatabaseConnection;
import com.msservices.geopolitik.connection.Views.CountryResources;
import com.msservices.geopolitik.connection.Queries;
import com.msservices.geopolitik.functions.mapFunctions;
import com.msservices.geopolitik.init.loadImages;
import com.msservices.geopolitik.init.loadMap;
import com.msservices.geopolitik.init.loadMatch;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
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
    private ImageView iconArmamento;

    @FXML
    private ImageView iconAccionesMilitares;

    @FXML
    private ImageView iconNoticias;

    @FXML
    private ImageView iconProvincias;

    @FXML
    private ImageView iconInvestigacion;

    @FXML
    private ImageView iconDiplomacia;

    @FXML
    private ImageView iconMundo;

    private int selectedInferiorIndex = -1;

    @FXML
    private Button botonEjecutar;

    @FXML
    private Label lblPoblacion;

    @FXML
    private Label lblFinanzas;

    @FXML
    private Label lblArea;

    @FXML
    private Label lblGasNatural;

    @FXML
    private Label lblPetroleo;

    @FXML
    private Label lblMadera;

    @FXML
    private Label lblPesca;

    @FXML
    private Label lblUranio;

    @FXML
    private Label lblOro;

    @FXML
    private Label lblAgricultura;

    @FXML
    private Label lblAcero;

    @FXML
    private Label lblIndustria;

    @FXML
    private Label lblElectricidad;

    @FXML
    public void initialize() {
        loadMatch.loadMatch(loadMatch.getJsonData());
        loadMap.loadMapFromMatch(imagenPrincipal);

        cargarDatosPais();
        cargarIconos();

        Platform.runLater(() -> ajustarMapaAlContenedor());

        scrollMapa.viewportBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            if (newBounds != null && newBounds.getWidth() > 0 && newBounds.getHeight() > 0) {
                ajustarMapaAlContenedor();
            }
        });
    }

    private void cargarDatosPais() {
        Queries queries = new Queries(DatabaseConnection.getInstance().getConnection());
        CountryResources recursos = queries.getCountryResources(11);
        if (recursos == null) return;

        lblPoblacion.setText("Población: " + recursos.getPopulation());
        lblFinanzas.setText("Finanzas: " + recursos.getIngresos());
        lblArea.setText("Área: " + String.format("%.1f", recursos.getArea()));
        lblGasNatural.setText("Gas natural: " + recursos.getGasNatural());
        lblPetroleo.setText("Petróleo: " + recursos.getPetroleo());
        lblMadera.setText("Madera: " + recursos.getMadera());
        lblPesca.setText("Pesca: " + recursos.getPesca());
        lblUranio.setText("Uranio: " + recursos.getUranio());
        lblOro.setText("Oro: " + recursos.getOro());
        lblAgricultura.setText("Agricultura: " + recursos.getAgricultura());
        lblAcero.setText("Acero: " + recursos.getAcero());
        lblIndustria.setText("Industria: " + recursos.getIndustria());
        lblElectricidad.setText("Electricidad: " + recursos.getElectricidad());
    }

    private void cargarIconos() {
        iconArmamento.setImage(loadImages.getIcon("weapons"));
        iconAccionesMilitares.setImage(loadImages.getIcon("military_actions"));
        iconNoticias.setImage(loadImages.getIcon("news"));
        iconProvincias.setImage(loadImages.getIcon("provinces"));
        iconInvestigacion.setImage(loadImages.getIcon("research"));
        iconDiplomacia.setImage(loadImages.getIcon("diplomacy"));
        iconMundo.setImage(loadImages.getIcon("world"));
    }

    @FXML
    private void onInferiorItemClick(MouseEvent event) {
        Node source = (Node) event.getSource();
        while (source != null && !(source instanceof HBox)) {
            source = source.getParent();
        }
        if (source == null) return;

        HBox row = (HBox) source;
        int index = listaInferior.getChildren().indexOf(row);
        if (index < 0) return;

        for (Node child : listaInferior.getChildren()) {
            child.getStyleClass().remove("list-item-selected");
        }

        row.getStyleClass().add("list-item-selected");
        selectedInferiorIndex = index;
    }

    public int getSelectedInferiorIndex() {
        return selectedInferiorIndex;
    }

    private void ajustarMapaAlContenedor() {
        if (imagenPrincipal.getImage() == null) return;
        Bounds viewportBounds = scrollMapa.getViewportBounds();
        if (viewportBounds == null) return;

        double viewportWidth = viewportBounds.getWidth();
        if (viewportWidth <= 0) return;

        imagenPrincipal.setFitWidth(viewportWidth);
        imagenPrincipal.setFitHeight(0);
    }

    @FXML
    private void onImagenPrincipalClick(MouseEvent event) {
        double colorValue = mapFunctions.getColor((int) event.getX(), (int) event.getY());
        String countryName = mapFunctions.getCountryFromList(colorValue);
        if (countryName.equals("Desconocido"))
            return;

        Queries queries = new Queries(DatabaseConnection.getInstance().getConnection());
        Country country = queries.getCountryByName(countryName);
        if (country == null) return;

        openCountryView(country);
    }

    private void openCountryView(Country country) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/msservices/geopolitik/views/countryView.fxml"));
            Parent root = loader.load();
            CountryViewControllers controller = loader.getController();
            controller.setCountryData(country.getName(), country.getIdCountry());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(imagenPrincipal.getScene().getWindow());
            stage.setTitle(country.getName());
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
