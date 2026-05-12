package com.example.soulspire.Entity.Enemy;

import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.Entity.Projectile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Enemy that maintains distance and fires projectiles at the player.
 * Retreats when the player gets too close.
 */

public class RangedEnemy extends Enemy {

    private double projectileSpeed;
    private double preferredDistance;
    private static final Color BODY_COLOR = Color.DARKORANGE;
    private Projectile lastFiredProjectile;

    public RangedEnemy(double x, double y, int floorNumber) {
        super(x, y, 26, 26,
                25,    // baseHealth (fragile)
                15,    // baseAttack
                1,     // defense
                60,   // moveSpeed
                250,   // aggroRange
                200,   // attackRange
                1.5,   // attackCooldown
                floorNumber);
        this.projectileSpeed = 300;
        this.preferredDistance = 150;
        this.lastFiredProjectile = null;
    }

    @Override
    protected Color getBodyColor() {
        return Color.GOLDENROD;
    }

    @Override
    public void updateAI(Player target, double deltaTime) {

    }

    /**
     * Fires a projectile toward the target coordinates.
     * The projectile must be added to the floor's entity list by GameEngine.
     */
    private void shoot(double targetX, double targetY) {

    }

    /**
     * Returns and clears the last fired projectile.
     * Called by GameEngine to add the projectile to the floor.
     *
     * @return the projectile to spawn, or null if none was fired this frame
     */

    /*
    public Projectile consumeProjectile() {

    }

     */

}