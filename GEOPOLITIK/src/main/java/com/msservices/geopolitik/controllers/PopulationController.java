package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.init.loadFlags;
import com.msservices.geopolitik.init.entity.flag;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class PopulationController implements interaction {

    @FXML private ImageView flagImage;
    @FXML private Label valorTotal;
    @FXML private Rectangle iconHabitantes;
    @FXML private LineChart<Number, Number> graficaPoblacion;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private Button botonAtras;

    private XYChart.Series<Number, Number> series;

    public void setPopulationData(String countryName, long totalPopulation, int countryId) {
        valorTotal.setText(String.valueOf(totalPopulation));

        flag f = loadFlags.getFlag(countryId);
        if (f != null) {
            flagImage.setImage(new Image(f.path(), 80, 40, true, true));
        }

        series = new XYChart.Series<>();
        series.setName(countryName);
        series.getData().add(new XYChart.Data<>(0, totalPopulation));
        graficaPoblacion.getData().add(series);
    }

    public void addTurnData(int turn, long population) {
        if (series == null) {
            series = new XYChart.Series<>();
            graficaPoblacion.getData().add(series);
        }
        series.getData().add(new XYChart.Data<>(turn, population));
        valorTotal.setText(String.valueOf(population));
    }

    @Override
    public void goBack() {
        Stage stage = (Stage) botonAtras.getScene().getWindow();
        stage.close();
    }

    @Override
    public void goAhead() {
    }

    @FXML
    public void initialize() {
        botonAtras.setOnAction(e -> goBack());
    }
}
