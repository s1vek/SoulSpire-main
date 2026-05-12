
package com.example.soulspire.Entity.Player;

import com.example.soulspire.Ability.ArcaneOrbAbility;
import com.example.soulspire.Ability.IceBlockAbility;
import com.example.soulspire.Ability.TeleportAbility;
import com.example.soulspire.Entity.Projectile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Map;


public class Mage extends Player {

    private static final double PROJECTILE_SPEED = 400;
    private static final double PROJECTILE_RANGE = 500;

    public Mage(String name, double x, double y) {
        super(name, PlayerType.MAGE, x, y, 32, 32);
        this.attackCooldown = 1;
    }

    @Override
    protected void initAbilities() {
        abilities[0] = new ArcaneOrbAbility();
        abilities[1] = new IceBlockAbility();
        abilities[2] = new TeleportAbility();
    }

    @Override
    public void attack(double targetX, double targetY) {
        if (!canAttack()) return;
        if (currentFloor == null) return;

        Projectile p = new Projectile(
                getCenterX() - 4, getCenterY() - 4,
                targetX, targetY,
                PROJECTILE_SPEED, attackDamage,
                PROJECTILE_RANGE, this
        );
        p.setColor(Color.MEDIUMPURPLE);
        currentFloor.addEntity(p);

        resetAttackCooldown();
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        double screenX = getX() - cameraX;
        double screenY = getY() - cameraY;

        Color body = Color.MEDIUMPURPLE;

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