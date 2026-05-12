package com.example.soulspire.Entity.Enemy;

import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.Util.GameLogger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The final boss of the tower, found on the last floor.
 * Has multiple combat phases that change behavior at health thresholds.
 * Phase 1 (100-50% HP): standard melee attacks.
 * Phase 2 (below 50% HP): faster attacks, increased damage.
 */
public class BossEnemy extends Enemy {

    private static final GameLogger logger = GameLogger.getLogger(BossEnemy.class);
    private static final Color BODY_COLOR_PHASE_1 = Color.BLACK;
    private static final Color BODY_COLOR_PHASE_2 = Color.DARKRED;
    private static final Color BORDER_COLOR = Color.CRIMSON;

    private int currentPhase;
    private boolean phaseTransitioned;

    public BossEnemy(double x, double y, int floorNumber) {
        super(x, y, 48, 48,
                200,   // baseHealth (tanky)
                25,    // baseAttack
                10,    // defense
                80,   // moveSpeed
                400,   // aggroRange (always detects)
                50,    // attackRange
                1.2,   // attackCooldown
                floorNumber);
        this.currentPhase = 1;
        this.phaseTransitioned = false;
    }

    @Override
    protected Color getBodyColor() {
        return Color.BLACK;
    }

    @Override
    public void updateAI(Player target, double deltaTime) {

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



    public int getCurrentPhase() { return currentPhase; }
}