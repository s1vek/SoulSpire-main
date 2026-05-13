
package com.example.soulspire.Entity.Player;

import com.example.soulspire.Ability.AstralWolfAbility;
import com.example.soulspire.Ability.FirestrikeAbility;
import com.example.soulspire.Ability.HealingTotemAbility;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Map;


public class Shaman extends Player {

    private static final Color BODY_COLOR = Color.MEDIUMPURPLE;

    private static final double ATTACK_RADIUS = 32;
    private static final double ATTACK_OFFSET = 22;

    public boolean wolfForm = false;

    public Shaman(String name, double x, double y) {
        super(name, PlayerType.SHAMAN, x, y, 32, 32);
        this.attackCooldown = 0.5;
    }

    @Override
    protected void initAbilities() {
        abilities[0] = new HealingTotemAbility();
        abilities[1] = new FirestrikeAbility();
        abilities[2] = new AstralWolfAbility();
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
        if (dist < 0.001) {
            return;
        }

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

        Color body = wolfForm ? Color.DIMGRAY : Color.STEELBLUE;

        if (invulnerable) {
            boolean flashOn = ((int)(invulnerabilityTimer * 10)) % 2 == 0;
            gc.setFill(flashOn ? Color.WHITE : body);
        } else {
            gc.setFill(body);
        }

        if (wolfForm) {
            gc.fillOval(screenX, screenY, getWidth(), getHeight());
        } else {
            gc.fillRect(screenX, screenY, getWidth(), getHeight());
        }
    }

    @Override
    public Map<String, Object> toSaveData() {
        return Map.of();
    }
}