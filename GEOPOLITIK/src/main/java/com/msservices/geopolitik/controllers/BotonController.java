package com.msservices.geopolitik.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

/**
 * Controlador para la gestión de botones en la aplicación.
 */
public class BotonController {

    @FXML
    private Button button;

    @FXML
    public void initialize() {
        // Inicialización del controlador de botones
    }

    /**
     * Maneja el evento de clic del botón.
     * @param event Evento de acción
     */
    @FXML
    public void handleButtonAction(ActionEvent event) {
        // Lógica para manejar el clic del botón
        System.out.println("Botón presionado");
    }

    /**
     * Configura el texto del botón.
     * @param texto Texto a mostrar en el botón
     */
    public void setButtonText(String texto) {
        if (button != null) {
            button.setText(texto);
        }
    }

    /**
     * Habilita o deshabilita el botón.
     * @param habilitado true para habilitar, false para deshabilitar
     */
    public void setButtonDisabled(boolean habilitado) {
        if (button != null) {
            button.setDisable(!habilitado);
        }
    }
}
