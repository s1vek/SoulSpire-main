package com.example.soulspire.Entity.Player;

import com.example.soulspire.Ability.FrostTrapAbility;
import com.example.soulspire.Ability.LeapAbility;
import com.example.soulspire.Ability.SpreadShotAbility;
import com.example.soulspire.Entity.Projectile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Map;

/**
 * Ranged archer class with high speed and kiting abilities.
 * Abilities: Spread Shot (multi-arrow), Frost Trap (freeze), Leap (dodge back).
 * Basic attack: fires an arrow toward the mouse cursor.
 */
public class Hunter extends Player {

    private static final double ARROW_SPEED = 500;
    private static final double ARROW_RANGE = 600;

    private static final Color BODY_COLOR = Color.FORESTGREEN;

    public Hunter(String name, double x, double y) {
        super(name, PlayerType.HUNTER, x, y, 32, 32);
        this.attackCooldown = 1;
    }

    @Override
    protected void initAbilities() {
        abilities[0] = new SpreadShotAbility();
        abilities[1] = new FrostTrapAbility();
        abilities[2] = new LeapAbility();
    }

    @Override
    public void attack(double targetX, double targetY) {
        if (!canAttack()) {
            return;
        }
        if (currentFloor == null) {
            return;
        }

        Projectile arrow = new Projectile(getCenterX() - 4, getCenterY() - 4, targetX, targetY, ARROW_SPEED, getEffectiveAttackDamage(), ARROW_RANGE, this);
        arrow.setColor(Color.FORESTGREEN);
        currentFloor.addEntity(arrow);

        resetAttackCooldown();
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        double screenX = getX() - cameraX;
        double screenY = getY() - cameraY;

        Color body = Color.FORESTGREEN;

        if (invulnerable) {
            boolean flashOn = ((int)(invulnerabilityTimer * 10)) % 2 == 0;
            gc.setFill(flashOn ? Color.WHITE : body);
        } else {
            gc.setFill(body);
        }
        gc.fillRect(screenX, screenY, getWidth(), getHeight());
    }

    @Override
    public Map<String, Object> toSaveData() {
        return Map.of();
    }
}