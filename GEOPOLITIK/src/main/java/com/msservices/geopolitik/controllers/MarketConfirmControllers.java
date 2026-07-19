package com.msservices.geopolitik.controllers;

import com.msservices.geopolitik.connection.DatabaseConnection;
import com.msservices.geopolitik.connection.queries.country.countryQueries;
import com.msservices.geopolitik.init.loadImages;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class MarketConfirmControllers implements interaction {

    @FXML
    private VBox listaElementos;

    @FXML
    private Label lblCosto;

    @FXML
    private Label lblIngresos;

    @FXML
    private Label lblTotal;

    @FXML
    private Button botonComprar;

    @FXML
    private Button botonCancelar;

    @FXML
    private Button botonReset;

    private Stage stage;
    private boolean resetCart = false;

    @FXML
    public void initialize() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean isResetCart() {
        return resetCart;
    }

    @FXML
    private void onResetClick() {
        resetCart = true;
        if (stage != null) {
            stage.close();
        }
    }

    public void setCartData(List<String> weaponNames, List<Integer> quantities, List<Double> costs, int countryId) {
        listaElementos.getChildren().clear();

        double total = 0;
        for (int i = 0; i < weaponNames.size(); i++) {
            String name = weaponNames.get(i);
            int qty = quantities.get(i);
            double unitCost = costs.get(i);
            double subtotal = unitCost * qty;
            total += subtotal;

            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(4, 6, 4, 6));
            row.getStyleClass().add("fila-accion");

            Image img = loadImages.getWeaponImage(name);
            ImageView icon = new ImageView();
            if (img != null) {
                icon.setImage(img);
            }
            icon.setFitWidth(32);
            icon.setFitHeight(32);

            Label lblName = new Label(name);
            lblName.setStyle("-fx-text-fill: #1a3a1a; -fx-font-size: 13px; -fx-font-weight: bold;");
            lblName.setPrefWidth(140);

            Label lblQty = new Label("x" + qty);
            lblQty.setStyle("-fx-text-fill: #1a3a1a; -fx-font-size: 12px;");
            lblQty.setPrefWidth(40);

            Label lblSubtotal = new Label("$" + String.format("%.0f", subtotal));
            lblSubtotal.setStyle("-fx-text-fill: #5a3a1a; -fx-font-size: 12px; -fx-font-weight: bold;");

            row.getChildren().addAll(icon, lblName, lblQty, lblSubtotal);
            row.setMouseTransparent(true);
            listaElementos.getChildren().add(row);
        }

        countryQueries q = new countryQueries(DatabaseConnection.getInstance().getConnection());
        double money = q.getCountryMoney(11);

        lblCosto.setText("$" + String.format("%.0f", total));
        lblIngresos.setText("$" + String.format("%.0f", money));

        if (money < total) {
            lblTotal.setText("No te lo puedes permitir");
            lblTotal.setStyle("-fx-text-fill: #8b0000; -fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #d9c97a; -fx-padding: 6 12; -fx-border-radius: 4; -fx-background-radius: 4;");
            botonComprar.setDisable(true);
        } else {
            lblTotal.setText("Total: $" + String.format("%.0f", total));
            lblTotal.setStyle("-fx-text-fill: #c49a3a; -fx-font-size: 15px; -fx-font-weight: bold;");
            botonComprar.setDisable(false);
        }
    }

    @FXML
    private void onComprarClick() {
        if (stage != null) {
            stage.close();
        }
    }

    @Override
    public void goBack() {
        if (stage != null) {
            stage.close();
        }
    }

    @Override
    public void goAhead() {
    }
}
