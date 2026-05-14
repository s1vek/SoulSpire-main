package com.example.soulspire.Entity.Enemy;

import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.Entity.Projectile;
import com.example.soulspire.Item.Material;
import com.example.soulspire.Item.MaterialType;
import com.example.soulspire.Util.GameLogger;
import com.example.soulspire.Util.SpriteLoader;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

/**
 * The final boss of the tower, found on the last floor.
 */

public class BossEnemy extends Enemy {

    private static final GameLogger logger = GameLogger.getLogger(BossEnemy.class);
    private static final double RANGED_INTERVAL = 3.0;
    private static final double PROJECTILE_SPEED = 280;
    private static final double PROJECTILE_RANGE = 450;
    private double rangedCooldown;
    private int currentPhase;
    private boolean phaseTransitioned;
    private static final Image TEXTURE = SpriteLoader.loadSprite("/com/example/soulspire/images/boss.png");


    public BossEnemy(double x, double y, int floorNumber) {
        super(x, y, 48, 48, 200, 25, 10, 80, 400, 50, 1.2, floorNumber);
        this.rangedCooldown = 3;
        lootTable.addDrop(new Material(MaterialType.IRON_ORE, 5), 1.0);
        lootTable.addDrop(new Material(MaterialType.ETHEREAL_DUST, 3), 1.0);
    }

    @Override
    protected Color getBodyColor() {
        return Color.BLACK;
    }

    @Override
    public void updateAI(Player target, double deltaTime) {
        if (isStunned()) {
            return;
        }

        rangedCooldown -= deltaTime;

        checkAggro(target);
        if (!aggroed) {
            return;
        }

        double dist = distanceTo(target);

        if (rangedCooldown <= 0 && hasLineOfSight(target)) {
            shootAt(target.getCenterX(), target.getCenterY());
            rangedCooldown = RANGED_INTERVAL;
        }

        if (dist <= attackRange) {
            if (currentAttackCooldown <= 0) {
                target.takeDamage(attackDamage);
                currentAttackCooldown = attackCooldown;
            }
        } else {
            moveToward(target.getCenterX(), target.getCenterY(), deltaTime);
        }

    }

    private void shootAt(double targetX, double targetY) {
        if (currentFloor == null) {
            return;
        }
        Projectile p = new Projectile(
                getCenterX() - 5, getCenterY() - 5,
                targetX, targetY,
                PROJECTILE_SPEED, attackDamage,
                PROJECTILE_RANGE, this
        );
        p.setColor(Color.CRIMSON);
        currentFloor.addEntity(p);
    }

    /**
     * Checks if the boss should transition to a new phase based on remaining health.
     */
    private void checkPhaseTransition() {

    }

    @Override
    protected void onDeath() {
        super.onDeath();
        logger.info("Boss defeated. Tower cleared!");
    }

    @Override
    protected Image getTexture() {
        return TEXTURE;
    }


    public int getCurrentPhase() { return currentPhase; }
}