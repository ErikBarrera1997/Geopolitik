package com.msservices.geopolitik.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controlador para la gestión de imágenes en la aplicación.
 */
public class ImagenController {

    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        // Inicialización del controlador de imágenes
    }

    /**
     * Carga una imagen desde una ruta específica.
     * @param rutaImagen Ruta de la imagen a cargar
     */
    public void cargarImagen(String rutaImagen) {
        if (imageView != null && rutaImagen != null) {
            Image imagen = new Image(rutaImagen);
            imageView.setImage(imagen);
        }
    }
}
