package com.example.soulspire.Combat;
import com.example.soulspire.Entity.Entity;

/**
 * Provides static collision detection methods for entity-entity
 * and entity-tile interactions.
 */

public class CollisionDetector {

    private CollisionDetector() {

    }

    /**
     * Checks if two entities' bounding boxes overlap.
     */
    public static boolean checkCollision(Entity a, Entity b) {
        return a.getBounds().intersects(b.getBounds());
    }

    /**
     * Checks if an entity would collide with a non-walkable tile
     * at the given position.
     *
     * @param entity the entity to check
     * @param floor  the floor containing the tile grid
     * @param newX   proposed x position
     * @param newY   proposed y position
     * @return true if the position is blocked
     */
    /*
    public static boolean checkTileCollision(Entity entity, Floor floor, double newX, double newY) {

    }

     */

    /**
     * Returns all entities within a circular area.
     */

    /*
    public static List<Entity> getEntitiesInArea(double centerX, double centerY,
                                                 double radius, List<Entity> entities) {

    }

     */
}