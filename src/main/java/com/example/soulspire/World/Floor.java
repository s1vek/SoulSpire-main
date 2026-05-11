package com.example.soulspire.World;
import com.example.soulspire.Core.GameConfig;
import com.example.soulspire.Entity.Entity;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a single floor (level) of the tower.
 * Contains a 2D tile grid and a list of all entities on this floor
 */

public class Floor {

    private int floorNumber;
    private Tile[][] grid;
    private int widthInTiles;
    private int heightInTiles;
    private List<Entity> entities;
    private boolean isSafeZone;
    private boolean  completed;
    private double spawnX;
    private double spawnY;

    /**
     * Creates a floor with the given dimensions.
     */
    public Floor(int floorNumber, int width, int height, boolean isSafeZone) {
        this.floorNumber = floorNumber;
        this.widthInTiles = width;
        this.heightInTiles = height;
        this.isSafeZone = isSafeZone;
        this.completed = false;
        this.entities = new ArrayList<>();
        this.grid = new Tile[height][width];

        for (int i = 0; i < height; i++) {
            for(int x = 0; x < width; x++) {
                boolean isBorder = (i == 0 || x ==0 || x == width -1 || i == height -1);
                grid[i][x] = new Tile(isBorder ? TileType.WALL : TileType.FLOOR);
            }
        }

        this.spawnX = 2 * GameConfig.TILE_SIZE;
        this.spawnY = 2 * GameConfig.TILE_SIZE;

    }

    /**
     * Updates all entities on this floor and removes inactive ones.
     */
    public void update(double deltaTime) {
        for (Entity entity : entities) {
            if (entity.isActive()) {
                entity.update(deltaTime);
            }
        }
        removeInactiveEntities();
    }

    /**
     * Renders the tile grid and all entities.
     */
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        int startX = Math.max(0, (int)(cameraX / GameConfig.TILE_SIZE));
        int startY = Math.max(0, (int)(cameraY / GameConfig.TILE_SIZE));
        int endX = Math.min(widthInTiles, startX + GameConfig.WINDOW_WIDTH / GameConfig.TILE_SIZE + 2);
        int endY = Math.min(heightInTiles, startY + GameConfig.WINDOW_HEIGHT / GameConfig.TILE_SIZE + 2);

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                grid[y][x].render(gc, x, y, cameraX, cameraY);
            }
        }

        double worldW = widthInTiles * GameConfig.TILE_SIZE;
        double worldH = heightInTiles * GameConfig.TILE_SIZE;
        double viewW = GameConfig.WINDOW_WIDTH;
        double viewH = GameConfig.WINDOW_HEIGHT;

        for (Entity entity : entities) {
            if (!entity.isActive()) continue;
            if (entity.getX() < 0 || entity.getY() < 0
                    || entity.getX() > worldW || entity.getY() > worldH) continue;
            double ex = entity.getX() - cameraX;
            double ey = entity.getY() - cameraY;
            if (ex + entity.getWidth() < 0 || ex > viewW
                    || ey + entity.getHeight() < 0 || ey > viewH) continue;
            entity.render(gc, cameraX, cameraY);
        }

    }

    /**
     * Returns the tile at the given grid coordinates, or null if out of bounds.
     */
    public Tile getTileAt(int gridX, int gridY) {
        if (gridX < 0 || gridX >= widthInTiles || gridY < 0 || gridY >= heightInTiles) {
            return null;
        }
        return grid[gridY][gridX];
    }


    /**
     * Returns the tile at the given pixel coordinates.
     */
    public Tile getTileAtPixel(double pixelX, double pixelY) {
        int gridX = (int)(pixelX / GameConfig.TILE_SIZE);
        int gridY = (int)(pixelY / GameConfig.TILE_SIZE);
        return getTileAt(gridX, gridY);
    }

    /**
     * Removes all entities marked as inactive.
     */
    private void removeInactiveEntities() {
        Iterator<Entity> it = entities.iterator();
        while (it.hasNext()) {
            if (!it.next().isActive()) {
                it.remove();
            }
        }
    }

    /**
     * Finds all entities within a given radius of a point.
     */
    public List<Entity> getEntitiesInRange(double x, double y, double radius) {
        List<Entity> result = new ArrayList<>();
        for (Entity e : entities) {
            double dx = e.getCenterX() - x;
            double dy = e.getCenterY() - y;
            if (Math.sqrt(dx * dx + dy * dy) <= radius) {
                result.add(e);
            }
        }
        return result;
    }

    public void setTileAt(int x, int y, TileType type) {
        if (x >= 0 && x < widthInTiles && y >= 0 && y < heightInTiles) {
            grid[y][x] = new Tile(type);
        }
    }

    public void addEntity(Entity entity) { entities.add(entity); }
    public void removeEntity(Entity entity) { entities.remove(entity); }
    public List<Entity> getEntities() { return entities; }
    public int getFloorNumber() { return floorNumber; }
    public boolean isSafeZone() { return isSafeZone; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public double getSpawnX() { return spawnX; }
    public double getSpawnY() { return spawnY; }
    public void setSpawnPosition(double x, double y) { this.spawnX = x; this.spawnY = y; }
    public int getWidthInTiles() { return widthInTiles; }
    public int getHeightInTiles() { return heightInTiles; }


}