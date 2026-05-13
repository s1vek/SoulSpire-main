
package com.example.soulspire.Entity.Player;

import com.example.soulspire.Ability.BladewhirlAbility;
import com.example.soulspire.Ability.ChargeAbility;
import com.example.soulspire.Ability.EnrageAbility;
import com.example.soulspire.Util.SoundManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Map;

public class Warrior extends Player {

    private static final Color BODY_COLOR = Color.CRIMSON;
    private static final double ATTACK_RADIUS = 35;
    private static final double ATTACK_OFFSET = 25;

    public Warrior(String name, double x, double y) {
        super(name, PlayerType.WARRIOR, x, y, 32, 32);
        this.attackCooldown = 0.5;
    }

    @Override
    protected void initAbilities() {
        abilities[0] = new ChargeAbility();
        abilities[1] = new BladewhirlAbility();
        abilities[2] = new EnrageAbility();
    }

    @Override
    public void attack(double targetX, double targetY) {
        if (!canAttack()) {
            return;
        }

        if (currentFloor == null || combatSystem == null) {
            return;
        }

        double dx = targetX - getCenterX();
        double dy = targetY - getCenterY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist < 0.001) return;

        double dirX = dx / dist;
        double dirY = dy / dist;

        double centerX = getCenterX() + dirX * ATTACK_OFFSET;
        double centerY = getCenterY() + dirY * ATTACK_OFFSET;

        combatSystem.processAreaDamage(centerX, centerY, ATTACK_RADIUS,
                attackDamage, currentFloor.getEntities(), this);

        resetAttackCooldown();

    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        double screenX = getX() - cameraX;
        double screenY = getY() - cameraY;

        Color body = Color.RED;

        if (invulnerable) {
            boolean flashOn = ((int)(invulnerabilityTimer * 10)) % 2 == 0;
            gc.setFill(flashOn ? Color.WHITE : body);
        } else {
            gc.setFill(body);
        }
        gc.fillRect(screenX, screenY, getWidth(), getHeight());

        if (frozen) {
            gc.setStroke(Color.LIGHTCYAN);
            gc.setLineWidth(3);
            gc.strokeRect(screenX - 2, screenY - 2, getWidth() + 4, getHeight() + 4);
        }
    }

    @Override
    public Map<String, Object> toSaveData() {
        return Map.of();
    }
}