package com.example.soulspire.World;

import com.example.soulspire.Core.GameConfig;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.util.EnumMap;
import java.util.Map;

/**
 * A single cell in the floor's tile grid.
 */
public class Tile {

    private static final Map<TileType, Image> textures = new EnumMap<>(TileType.class);

    public static void loadTextures() {
        load(TileType.WALL, "/com/example/soulspire/images/wall.png");
        load(TileType.FLOOR, "/com/example/soulspire/images/floor.png");
    }

    private static void load(TileType type, String path) {
        try {
            var stream = Tile.class.getResourceAsStream(path);
            if (stream != null) {
                textures.put(type, new Image(stream));
            } else {
                System.err.println("Tile texture not found: " + path);
            }
        } catch (Exception e) {
            System.err.println("Failed to load tile " + path + ": " + e.getMessage());
        }
    }

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

        Image tex = textures.get(type);
        if (tex != null) {
            gc.setImageSmoothing(false);
            gc.drawImage(tex, screenX, screenY, size, size);
        } else {
            gc.setFill(getFallbackColor());
            gc.fillRect(screenX, screenY, size, size);
        }
    }

    /**
     * Returns a placeholder color for each tile type.
     */
    private Color getFallbackColor() {
        return switch (type) {
            case FLOOR, SPAWN, CHEST_SPOT, TRAP -> Color.DIMGRAY;
            case WALL -> Color.DARKSLATEGRAY;
            case DOOR -> Color.SIENNA;
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