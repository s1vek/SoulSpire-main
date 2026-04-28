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
 * (enemies, projectiles, chests, NPCs, totems).
 */
public class Floor {



    /**
     * Creates a floor with the given dimensions.
     */
    public Floor(int floorNumber, int width, int height, boolean isSafeZone) {

    }

    /**
     * Updates all entities on this floor and removes inactive ones.
     */
    public void update(double deltaTime) {

    }

    /**
     * Renders the tile grid and all entities.
     */
    public void render(GraphicsContext gc, double cameraX, double cameraY) {

    }

    /**
     * Returns the tile at the given grid coordinates, or null if out of bounds.
     */

    /*
    public Tile getTileAt(int gridX, int gridY) {

    }

     */

    /**
     * Returns the tile at the given pixel coordinates.
     */

    /*
    public Tile getTileAtPixel(double pixelX, double pixelY) {

    }

     */


    /**
     * Removes all entities marked as inactive.
     */
    private void removeInactiveEntities() {

    }

    /**
     * Finds all entities within a given radius of a point.
     */

    /*
    public List<Entity> getEntitiesInRange(double x, double y, double radius) {

    }

     */

    /*
    public void addEntity(Entity entity) { entities.add(entity); }
    public void removeEntity(Entity entity) { entities.remove(entity); }
    public List<Entity> getEntities() { return entities; }
    public void setTileAt(int x, int y, TileType type) { grid[y][x] = new Tile(type); }
    public int getFloorNumber() { return floorNumber; }
    public boolean isSafeZone() { return isSafeZone; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public double getSpawnX() { return spawnX; }
    public double getSpawnY() { return spawnY; }
    public void setSpawnPosition(double x, double y) { this.spawnX = x; this.spawnY = y; }
    public int getWidthInTiles() { return widthInTiles; }
    public int getHeightInTiles() { return heightInTiles; }

     */
}