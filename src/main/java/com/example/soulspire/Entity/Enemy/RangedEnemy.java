package com.example.soulspire.Entity.Enemy;

import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.Entity.Projectile;
import com.example.soulspire.Item.Material;
import com.example.soulspire.Item.MaterialType;
import com.example.soulspire.Util.SpriteLoader;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Enemy that maintains distance and fires projectiles at the player.
 * Retreats when the player gets too close.
 */

public class RangedEnemy extends Enemy {

    private double projectileSpeed;
    private double preferredDistance;
    private static final double FIRE_RATE = 0.8;
    private Projectile lastFiredProjectile;
    private double shootCooldown = 0;
    private static final Image TEXTURE = SpriteLoader.loadSprite("/com/example/soulspire/images/enemy.png");

    public RangedEnemy(double x, double y, int floorNumber) {
        super(x, y, 26, 26, 25, 15, 1, 60, 250, 200, 0.4, floorNumber);
        this.projectileSpeed = 300;
        this.preferredDistance = 150;
        lootTable.addDrop(new Material(MaterialType.IRON_ORE, 1), 0.4);
        lootTable.addDrop(new Material(MaterialType.ETHEREAL_DUST, 1), 0.3);
    }

    @Override
    protected Color getBodyColor() {
        return Color.GOLDENROD;
    }

    @Override
    public void updateAI(Player target, double deltaTime) {
        shootCooldown -= deltaTime;

        if (isStunned()) {
            return;
        }

        checkAggro(target);
        if (!aggroed) {
            return;
        }

        double dist = distanceTo(target);
        double tolerance = 30;

        if (dist < preferredDistance - tolerance) {
            moveAway(target.getCenterX(), target.getCenterY(), deltaTime);
        } else if (dist > preferredDistance + tolerance) {
            moveToward(target.getCenterX(), target.getCenterY(), deltaTime);
        }

        if (shootCooldown <= 0 && dist <= attackRange) {
            shoot(target.getCenterX(), target.getCenterY());
            shootCooldown = FIRE_RATE;
        }

    }

    /**
     * Fires a projectile toward the target coordinates.
     * The projectile must be added to the floor's entity list by GameEngine.
     */
    private void shoot(double targetX, double targetY) {
        if (currentFloor == null) {
            return;
        }
        Projectile p = new Projectile(
                getCenterX() - 4, getCenterY() - 4,
                targetX, targetY,
                projectileSpeed, attackDamage,
                attackRange + 80, this
        );
        p.setColor(Color.ORANGERED);
        currentFloor.addEntity(p);

    }

    @Override
    protected Image getTexture() {
        return TEXTURE;
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