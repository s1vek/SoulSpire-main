package com.example.soulspire.World;

import com.example.soulspire.Core.GameConfig;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A single cell in the floor's tile grid.
 */
public class Tile {

    private TileType type;

    public Tile(TileType type) {
        this.type = type;
    }

    /**
     * Renders this tile at the given grid position.
     */
    public void render(GraphicsContext gc, int gridX, int gridY, double cameraX, double cameraY) {
        double screenX = gridX * GameConfig.TILE_SIZE - cameraX;
        double screenY = gridY * GameConfig.TILE_SIZE - cameraY;
        int size = GameConfig.TILE_SIZE;

        gc.setFill(getTileColor());
        gc.fillRect(screenX, screenY, size, size);
    }

    /**
     * Returns a placeholder color for each tile type.
     */
    private Color getTileColor() {
        return switch (type) {
            case FLOOR, SPAWN -> Color.DIMGRAY;
            case WALL -> Color.DARKSLATEGRAY;
            case DOOR -> Color.SIENNA;
            case TRAP -> Color.DIMGRAY;
            case CHEST_SPOT -> Color.DIMGRAY;
            case SAFE_ZONE -> Color.DARKSEAGREEN;
            case LEVER -> Color.SLATEGRAY;
            case DESTRUCTIBLE_WALL -> Color.ROSYBROWN;
            case EXIT -> Color.LIGHTBLUE;
        };
    }

    public boolean isWalkable() { return type.isWalkable(); }
    public TileType getType() { return type; }
    public void setType(TileType type) { this.type = type; }
}