package com.example.soulspire.Entity.Enemy;
import javafx.scene.image.Image;
import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.Util.GameLogger;
import com.example.soulspire.Util.SpriteLoader;
import javafx.scene.paint.Color;


import java.awt.*;

public class FinalBoss extends Enemy {

    private static final GameLogger logger = GameLogger.getLogger(FinalBoss.class);
    private static final Image TEXTURE = SpriteLoader.loadSprite("/com/example/soulspire/images/finalboss.png");

    private int currentPhase;
    private boolean phaseTransitioned;

    public FinalBoss(double x, double y, int floorNumber) {
        super(x, y, 56, 56,
                500,   // baseHealth
                40,    // baseAttack
                15,    // defense
                90,    // moveSpeed
                600,   // aggroRange
                55,    // attackRange
                0.9,   // attackCooldown
                floorNumber);
        this.currentPhase = 1;
        this.phaseTransitioned = false;
    }

    @Override
    protected Color getBodyColor() {
        return null;
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

    /**
     * Override damage: in phase 1, instead of dying, boss refills HP
     * and enters phase 2 with stronger attacks.
     */
    @Override
    public void takeDamage(int amount) {
        if (invulnerable || isDead()) return;

        int actual = Math.max(1, amount - defense);
        currentHealth -= actual;

        if (currentHealth <= 0) {
            if (!phaseTransitioned) {
                phaseTransitioned = true;
                currentPhase = 2;
                currentHealth = maxHealth;
                attackDamage = (int)(attackDamage * 1.5);
                invulnerable = true;
                invulnerabilityTimer = 2.0;
                logger.info("Final boss enters Phase 2!");
            } else {
                currentHealth = 0;
                active = false;
                onDeath();
            }
        } else {
            invulnerable = true;
            invulnerabilityTimer = IFRAME_DURATION;
        }
    }

    @Override
    protected void onDeath() {
        super.onDeath();
        logger.info("Final boss defeated. Tower cleared!");
    }

    public int getCurrentPhase() { return currentPhase; }
}

