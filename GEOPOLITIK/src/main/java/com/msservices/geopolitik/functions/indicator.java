package com.msservices.geopolitik.functions;

import javafx.fxml.FXML;

import javafx.scene.shape.Line;

//GLOBAL INDICATOR
class ProbabilityGaugeController {

    @FXML
    private Line probabilityIndicator;

    private double currentValue = 0;

    private static final int MIN_VALUE = -100;
    private static final int MAX_VALUE = 100;

    /**
     * Receives a value between -100 and 100
     */
    public void setProbability(int value) {

        value = Math.max(MIN_VALUE,
                Math.min(MAX_VALUE, value));

        currentValue = value;

        double minX = 10;
        double maxX = 290;

        double position =
                minX + ((value - MIN_VALUE)
                        * (maxX - minX))
                        / (double)(MAX_VALUE - MIN_VALUE);

        probabilityIndicator.setStartX(position);
        probabilityIndicator.setEndX(position);
    }

    public double getProbability() {
        return currentValue;
    }

    @FXML
    public void initialize() {

        // Start centered (0)
        setProbability(0);
    }
}
