
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
    }

    @Override
    protected void initAbilities() {

    }

    @Override
    public void attack(double targetX, double targetY) {

    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        double screenX = getX() - cameraX;
        double screenY = getY() - cameraY;
        gc.setFill(Color.RED);
        gc.fillRect(screenX, screenY, getWidth(), getHeight());
    }

    @Override
    public Map<String, Object> toSaveData() {
        return Map.of();
    }
}