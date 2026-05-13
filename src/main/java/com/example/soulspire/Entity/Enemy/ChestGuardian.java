package com.example.soulspire.Entity.Enemy;

import com.example.soulspire.Entity.Chest;
import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.Item.Material;
import com.example.soulspire.Item.MaterialType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A special enemy guarding a {@link Chest} containing a SoulEcho.
 * Only aggros when the player is near the chest.
 * When killed, the guarded chest becomes interactable.
 */
public class ChestGuardian extends Enemy {

    private Chest guardedChest;
    private static final Color BODY_COLOR = Color.INDIGO;

    /**
     * Creates a chest guardian near the given chest.
     *
     * @param x            spawn x position (should be near the chest)
     * @param y            spawn y position
     * @param floorNumber  current floor for scaling
     * @param guardedChest the chest this guardian protects
     */
    public ChestGuardian(double x, double y, int floorNumber, Chest guardedChest) {
        super(x, y, 32, 32,
                60,    // baseHealth
                15,    // baseAttack
                5,     // defense
                50,   // moveSpeed
                120,   // aggroRange (short — only near chest)
                40,    // attackRange
                1.0,   // attackCooldown
                floorNumber);
        this.guardedChest = guardedChest;
        lootTable.addDrop(new Material(MaterialType.IRON_ORE, 2), 1.0);
        lootTable.addDrop(new Material(MaterialType.ETHEREAL_DUST, 1), 1.0);
    }

    @Override
    protected Color getBodyColor() {
        return Color.DARKVIOLET;
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
    protected void onDeath() {
        super.onDeath();
        if (guardedChest != null) {
            guardedChest.setGuardianDefeated(true);
        }
    }


}