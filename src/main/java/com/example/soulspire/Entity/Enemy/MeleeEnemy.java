package com.example.soulspire.Entity.Enemy;

import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.Item.Material;
import com.example.soulspire.Item.MaterialType;
import com.example.soulspire.Util.SpriteLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Basic enemy that chases the player and attacks at close range.
 * The most common enemy type in the tower.
 */

public class MeleeEnemy extends Enemy {

    private static final Color BODY_COLOR = Color.DARKRED;
    private static final Image TEXTURE = SpriteLoader.loadSprite("/com/example/soulspire/images/enemy.png");

    /**
     * Creates a melee enemy with default stats scaled by floor number.
     *
     * @param x           spawn x position
     * @param y           spawn y position
     * @param floorNumber current floor (for difficulty scaling)
     */
    public MeleeEnemy(double x, double y, int floorNumber) {
        super(x, y, 28, 28, 40, 12, 3, 70, 200, 40, 1.0, floorNumber);
        lootTable.addDrop(new Material(MaterialType.IRON_ORE, 1), 0.6);
        lootTable.addDrop(new Material(MaterialType.ETHEREAL_DUST, 1), 0.15);
    }

    @Override
    protected Color getBodyColor() {
        return Color.CRIMSON;
    }

    @Override
    public void updateAI(Player target, double deltaTime) {
        if (isStunned()) {
            return;
        }

        checkAggro(target);
        if (!aggroed) {
            return;
        }

        double dist = distanceTo(target);

        if (dist <= attackRange) {
            if (currentAttackCooldown <= 0) {
                target.takeDamage(attackDamage);
                currentAttackCooldown = attackCooldown;
            }
        } else {
            moveToward(target.getCenterX(), target.getCenterY(), deltaTime);
        }

    }

    @Override
    protected Image getTexture() {
        return TEXTURE;
    }

}