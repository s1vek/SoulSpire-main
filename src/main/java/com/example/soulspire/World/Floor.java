package com.example.soulspire.World;
import com.example.soulspire.Combat.CollisionDetector;
import com.example.soulspire.Core.GameConfig;
import com.example.soulspire.Entity.Enemy.Enemy;
import com.example.soulspire.Entity.Entity;
import com.example.soulspire.Entity.LivingEntity;
import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.Entity.Projectile;
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
    public void update(double deltaTime, Player player) {
        for (Entity entity : new ArrayList<>(entities)) {
            if (entity.isActive()) {
                if (entity instanceof Enemy enemy) {
                    enemy.updateAI(player, deltaTime);
                }
                entity.update(deltaTime);
            }
        }
        for (Entity e : new ArrayList<>(entities)) {
            if (e instanceof Projectile p && p.isActive()) {
                checkProjectileHit(p, player);
            }
        }
        removeInactiveEntities();
    }

    private void checkProjectileHit(Projectile p, Player player) {
        Tile t = getTileAtPixel(p.getCenterX(), p.getCenterY());
        if (t != null && !t.isWalkable()) {
            explode(p, player);
            p.setActive(false);
            return;
        }

        for (Entity e : entities) {
            if (e == p.getOwner() || e == p || !e.isActive()) continue;
            if (!(e instanceof LivingEntity living)) continue;
            if (CollisionDetector.checkCollision(p,e)) {
                living.takeDamage(p.getDamage());
                explode(p, player);
                p.setActive(false);
                return;
            }
        }

        if (player != null && p.getOwner() != player && CollisionDetector.checkCollision(p, player)) {
            player.takeDamage(p.getDamage());
            explode(p, player);
            p.setActive(false);
        }
    }

    private void explode(Projectile p, Player player) {
        double r = p.getExplosionRadius();
        if (r <= 0) return;
        int dmg = p.getDamage();
        for (Entity e : entities) {
            if (e == p.getOwner() || !e.isActive() || !(e instanceof LivingEntity living)) continue;
            double dx = e.getCenterX() - p.getCenterX();
            double dy = e.getCenterY() - p.getCenterY();
            if (dx * dx + dy * dy <= r * r) living.takeDamage(dmg);
        }
        if (player != null && p.getOwner() != player) {
            double dx = player.getCenterX() - p.getCenterX();
            double dy = player.getCenterY() - p.getCenterY();
            if (dx * dx + dy * dy <= r * r) player.takeDamage(dmg);
        }
    }

    /**
     * Renders the tile grid and all entities.
     */
    public void render(GraphicsContext gc, double cameraX, double cameraY, double viewW, double viewH) {
        int tileSize = GameConfig.TILE_SIZE;

        int startX = Math.max(0, (int)(cameraX / tileSize));
        int startY = Math.max(0, (int)(cameraY / tileSize));
        int endX = Math.min(widthInTiles, startX + (int)(viewW / tileSize) + 2);
        int endY = Math.min(heightInTiles, startY + (int)(viewH / tileSize) + 2);

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                grid[y][x].render(gc, x, y, cameraX, cameraY);
            }
        }

        double worldW = widthInTiles * tileSize;
        double worldH = heightInTiles * tileSize;

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

    public void addEntity(Entity entity) {
        if (entity instanceof LivingEntity le) {
            le.setCurrentFloor(this);
        }
        entities.add(entity);
    }

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