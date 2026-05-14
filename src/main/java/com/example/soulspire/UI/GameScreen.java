package com.example.soulspire.UI;

import com.example.soulspire.Core.GameConfig;
import com.example.soulspire.Core.GameEngine;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;

/**
 * The main game screen containing the Canvas for rendering
 * and the HUD overlay for player stats.
 */
public class GameScreen extends StackPane {

    private Canvas canvas;
    private HUDOverlay hud;
    private GraphicsContext gc;
    private InventoryUI inventoryUI;
    private CraftingUI craftingUI;

    public GameScreen(GameEngine engine) {
        canvas = new Canvas();
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());

        gc = canvas.getGraphicsContext2D();
        hud = new HUDOverlay(engine);

        getChildren().addAll(canvas, hud);
    }

    public void attachInventoryUI(InventoryUI ui) {
        this.inventoryUI = ui;
        ui.setVisible(false);
        StackPane.setAlignment(ui, Pos.CENTER);
        getChildren().add(ui);
    }

    public void attachCraftingUI(CraftingUI ui) {
        this.craftingUI = ui;
        ui.setVisible(false);
        StackPane.setAlignment(ui, Pos.CENTER);
        getChildren().add(ui);
    }


    /**
     * Getters and setters.
     */

    public GraphicsContext getGraphicsContext() { return gc; }
    public HUDOverlay getHud() { return hud; }
    public InventoryUI getInventoryUI() { return inventoryUI; }
    public CraftingUI getCraftingUI() { return craftingUI; }
}