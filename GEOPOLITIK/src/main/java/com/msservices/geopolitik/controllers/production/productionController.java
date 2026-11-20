package com.msservices.geopolitik.controllers.production;

import com.msservices.geopolitik.connection.DatabaseConnection;
import com.msservices.geopolitik.connection.queries.countryResourceQueries;
import com.msservices.geopolitik.connection.queries.weaponQueries;
import com.msservices.geopolitik.init.loadImages;
import com.msservices.geopolitik.interfaces.interaction;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class productionController implements interaction {

    private static final int COUNTRY_ID = 11;
    private static final double RESOURCE_ICON_SIZE = 16;

    @FXML
    private ImageView productionIcon;

    @FXML
    private VBox productionStatusList;

    @FXML
    private Label insufficientResourcesLabel;

    @FXML
    private HBox queueList;

    @FXML
    private VBox productionList;

    @FXML
    private Button clearProductionButton;

    @FXML
    private Button backButton;

    @FXML
    private HBox moneyRow;

    @FXML
    private ImageView moneyIcon;

    @FXML
    private Label moneyLabel;

    private final Map<String, Integer> availableResources = new HashMap<>();

    @FXML
    public void initialize() {
        if (backButton != null) {
            backButton.setOnAction(e -> goBack());
        }
        if (clearProductionButton != null) {
            clearProductionButton.setOnAction(e -> clearProductionList());
        }
        if (productionIcon != null) {
            Image icon = loadImages.getIcon("factory");
            if (icon != null) {
                productionIcon.setImage(icon);
            }
        }
        loadAvailableResources();
        loadWeaponRecipes();
    }

    private void loadAvailableResources() {
        if (queueList == null) return;
        queueList.getChildren().clear();
        availableResources.clear();

        int moneyAmount = 0;
        try {
            countryResourceQueries queries = new countryResourceQueries(DatabaseConnection.getInstance().getConnection());
            List<countryResourceQueries.countryResource> resources = queries.getCountryResources(COUNTRY_ID);
            for (countryResourceQueries.countryResource resource : resources) {
                availableResources.put(resource.resourceName(), resource.amount());
                if ("Dinero".equals(resource.resourceName())) {
                    moneyAmount = resource.amount();
                    continue;
                }
                queueList.getChildren().add(createAvailableResourceLabel(resource.resourceName(), resource.amount()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateMoneyRow(moneyAmount);
    }

    private Label createAvailableResourceLabel(String resourceName, int amount) {
        Label resourceLabel = new Label("x" + amount);
        resourceLabel.getStyleClass().add("texto-dato");

        String iconKey = getResourceIconKey(resourceName);
        if (iconKey != null) {
            Image image = loadImages.getIcon(iconKey);
            if (image != null) {
                ImageView icon = new ImageView(image);
                icon.setFitWidth(RESOURCE_ICON_SIZE);
                icon.setFitHeight(RESOURCE_ICON_SIZE);
                icon.setPreserveRatio(true);
                resourceLabel.setGraphic(icon);
            }
        }

        return resourceLabel;
    }

    private void updateMoneyRow(int amount) {
        if (moneyRow == null || moneyLabel == null) return;
        Image image = loadImages.getIcon("money");
        if (moneyIcon != null && image != null) {
            moneyIcon.setImage(image);
        }
        moneyLabel.setText("$" + amount);
        moneyRow.setVisible(true);
        moneyRow.setManaged(true);
    }

    private void loadWeaponRecipes() {
        if (productionStatusList == null) return;
        productionStatusList.getChildren().clear();

        try {
            weaponQueries queries = new weaponQueries(DatabaseConnection.getInstance().getConnection());
            List<weaponQueries.WeaponRecipe> recipes = queries.getWeaponRecipes();
            for (weaponQueries.WeaponRecipe recipe : recipes) {
                productionStatusList.getChildren().add(createRecipeRow(recipe));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HBox createRecipeRow(weaponQueries.WeaponRecipe recipe) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("fila-accion");
        row.getStyleClass().add("fila-naranja");

        Image weaponImage = getWeaponIcon(recipe.name());
        if (weaponImage != null) {
            ImageView weaponIcon = new ImageView(weaponImage);
            weaponIcon.setFitWidth(20);
            weaponIcon.setFitHeight(20);
            weaponIcon.setPreserveRatio(true);
            row.getChildren().add(weaponIcon);
        }

        Label nameLabel = new Label(recipe.name());
        nameLabel.getStyleClass().add("texto-dato");
        nameLabel.setMinWidth(130);
        row.getChildren().add(nameLabel);

        addRecipeResource(row, "oil", recipe.petroleo());
        addRecipeResource(row, "industry", recipe.industria());
        addRecipeResource(row, "gold", recipe.oro());
        addRecipeResource(row, "uranium", recipe.uranio());
        addRecipeResource(row, "steel", recipe.acero());
        addRecipeResource(row, "electricity", recipe.electricidad());
        addRecipeResource(row, "money", recipe.dinero());
        addRecipeResource(row, null, recipe.tierrasRaras());

        row.setOnMouseClicked(e -> onRecipeSelected(recipe));
        return row;
    }

    private void addRecipeResource(HBox row, String iconKey, int amount) {
        if (amount <= 0) return;

        HBox resourceBox = new HBox(4);
        resourceBox.setAlignment(Pos.CENTER_LEFT);

        if (iconKey != null) {
            Image image = loadImages.getIcon(iconKey);
            if (image != null) {
                ImageView icon = new ImageView(image);
                icon.setFitWidth(RESOURCE_ICON_SIZE);
                icon.setFitHeight(RESOURCE_ICON_SIZE);
                icon.setPreserveRatio(true);
                resourceBox.getChildren().add(icon);
            }
        }

        Label amountLabel = new Label("x" + amount);
        amountLabel.getStyleClass().add("texto-dato");
        resourceBox.getChildren().add(amountLabel);
        row.getChildren().add(resourceBox);
    }

    private Image getWeaponIcon(String weaponName) {
        if (weaponName == null) return null;
        String normalized = weaponName.toLowerCase();
        Image image = loadImages.getWeaponImage(normalized);
        if (image != null) return image;

        return switch (normalized) {
            case "helicóptero de ataque" -> loadImages.getWeaponImage("helicóptero");
            case "systema antiaéreo" -> loadImages.getWeaponImage("sistema antiaéreo");
            case "sistema antimisiles" -> loadImages.getWeaponImage("sistema antimisil");
            case "misil crucero" -> loadImages.getWeaponImage("misil de crucero");
            default -> null;
        };
    }

    private void onRecipeSelected(weaponQueries.WeaponRecipe recipe) {
        if (!hasEnoughResources(recipe)) {
            showInsufficientResources(true);
            return;
        }
        showInsufficientResources(false);
        if (productionList != null) {
            productionList.getChildren().add(createProductionRow(recipe.name()));
        }
    }

    private boolean hasEnoughResources(weaponQueries.WeaponRecipe recipe) {
        return availableResources.getOrDefault("Petróleo", 0) >= recipe.petroleo()
                && availableResources.getOrDefault("Industria", 0) >= recipe.industria()
                && availableResources.getOrDefault("Oro", 0) >= recipe.oro()
                && availableResources.getOrDefault("Uranio", 0) >= recipe.uranio()
                && availableResources.getOrDefault("Acero", 0) >= recipe.acero()
                && availableResources.getOrDefault("Electricidad", 0) >= recipe.electricidad()
                && availableResources.getOrDefault("Dinero", 0) >= recipe.dinero()
                && availableResources.getOrDefault("Tierras raras", 0) >= recipe.tierrasRaras();
    }

    private HBox createProductionRow(String weaponName) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("fila-accion");

        Label nameLabel = new Label(weaponName);
        Label typeLabel = new Label("Arma");
        Label quantityLabel = new Label("1");
        nameLabel.getStyleClass().add("texto-dato");
        typeLabel.getStyleClass().add("texto-dato");
        quantityLabel.getStyleClass().add("texto-dato");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button removeButton = new Button();
        removeButton.getStyleClass().add("boton-remover");
        removeButton.setOnAction(e -> productionList.getChildren().remove(row));

        row.getChildren().addAll(nameLabel, typeLabel, quantityLabel, spacer, removeButton);
        return row;
    }

    private String getResourceIconKey(String resourceName) {
        if (resourceName == null) return null;
        return switch (resourceName) {
            case "Petróleo" -> "oil";
            case "Industria" -> "industry";
            case "Oro" -> "gold";
            case "Uranio" -> "uranium";
            case "Acero" -> "steel";
            case "Electricidad" -> "electricity";
            case "Dinero" -> "money";
            case "Agrícultura", "Agricultura" -> "farming";
            case "Madera" -> "wood";
            case "Pesca" -> "fishing";
            case "Gas natural" -> "gas";
            default -> null;
        };
    }

    private void clearProductionList() {
        if (productionList != null) {
            productionList.getChildren().clear();
        }
    }

    private void showInsufficientResources(boolean show) {
        if (insufficientResourcesLabel != null) {
            insufficientResourcesLabel.setVisible(show);
            insufficientResourcesLabel.setManaged(show);
        }
    }

    @Override
    public void goBack() {
        if (backButton != null && backButton.getScene() != null) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void goAhead() {
    }
}
