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
                2.0,   // moveSpeed
                400,   // aggroRange (always detects)
                50,    // attackRange
                1.2,   // attackCooldown
                floorNumber);
        this.currentPhase = 1;
        this.phaseTransitioned = false;
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

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        double sx = x - cameraX;
        double sy = y - cameraY;
        Color fill = isInvulnerable() ? Color.WHITE : (currentPhase == 2 ? BODY_COLOR_PHASE_2 : BODY_COLOR_PHASE_1);
        gc.setFill(fill);
        gc.fillRect(sx, sy, width, height);
        gc.setStroke(BORDER_COLOR);
        gc.setLineWidth(3);
        gc.strokeRect(sx + 1.5, sy + 1.5, width - 3, height - 3);


        if (currentHealth < maxHealth) {
            double barW = width;
            double barH = 6;
            double barY = sy - barH - 4;
            gc.setFill(Color.web("#1a0000"));
            gc.fillRect(sx, barY, barW, barH);
            double pct = getHealthPercent();
            gc.setFill(pct > 0.5 ? Color.web("#3aaf3a") : Color.RED);
            gc.fillRect(sx, barY, barW * pct, barH);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            gc.strokeRect(sx + 0.5, barY + 0.5, barW - 1, barH - 1);
        }


    }

    public int getCurrentPhase() { return currentPhase; }
}