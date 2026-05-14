package com.example.soulspire.UI;

import com.example.soulspire.Crafting.CraftingRecipe;
import com.example.soulspire.Crafting.CraftingSystem;
import com.example.soulspire.Item.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Map;

/**
 * Crafting UI displayed when interacting with the Blacksmith.
 * Shows available recipes, required materials, and allows crafting.
 * Built programmatically without Scene Builder (assignment requirement).
 */
public class CraftingUI extends VBox {

    private final CraftingSystem craftingSystem;
    private final Inventory inventory;
    private final Label materialsLabel = new Label();
    private final VBox recipeList = new VBox(6);

    public CraftingUI(CraftingSystem craftingSystem, Inventory inventory) {
        this.craftingSystem = craftingSystem;
        this.inventory = inventory;

        setAlignment(Pos.TOP_CENTER);
        setSpacing(8);
        setPadding(new Insets(14));
        setPrefSize(360, 420);
        setMaxSize(360, 420);
        setStyle("-fx-background-color: black; -fx-border-color: white;");

        Label title = new Label("Blacksmith");
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-family: 'MedievalSharp';");
        materialsLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'MedievalSharp';");
        recipeList.setAlignment(Pos.CENTER);

        Button closeBtn = new Button("Close");
        closeBtn.setFocusTraversable(false);
        closeBtn.setOnAction(e -> setVisible(false));

        getChildren().addAll(title, materialsLabel, recipeList, closeBtn);
        refresh();
    }

    /**
     * Refreshes the recipe list and material counts.
     */
    public void refresh() {
        materialsLabel.setText("Iron: " + inventory.getMaterialCount(MaterialType.IRON_ORE) + "   Dust: " + inventory.getMaterialCount(MaterialType.ETHEREAL_DUST));

        recipeList.getChildren().clear();
        for (CraftingRecipe recipe : craftingSystem.getAllRecipes()) {
            HBox row = new HBox(8);
            row.setAlignment(Pos.CENTER_LEFT);

            StringBuilder cost = new StringBuilder();
            for (Map.Entry<MaterialType, Integer> e : recipe.getRequiredMaterials().entrySet()) {
                cost.append(e.getValue()).append(e.getKey() == MaterialType.IRON_ORE ? "I " : "D ");
            }

            String displayName = recipe.getResultName() + " — " + recipe.getResult().getDescription();
            Label name = new Label(displayName);
            name.setStyle("-fx-text-fill: white; -fx-font-family: 'MedievalSharp';");
            name.setMinWidth(190);

            Label costLbl = new Label(cost.toString().trim());
            costLbl.setStyle("-fx-text-fill: white; -fx-font-family: 'MedievalSharp';");
            costLbl.setMinWidth(55);

            Button craftBtn = new Button("Craft");
            craftBtn.setFocusTraversable(false);
            craftBtn.setDisable(!recipe.canCraft(inventory));
            craftBtn.setOnAction(e -> {
                if (craftingSystem.craft(recipe, inventory)) {
                    refresh();
                }
            });

            row.getChildren().addAll(name, costLbl, craftBtn);
            recipeList.getChildren().add(row);
        }
    }

}
