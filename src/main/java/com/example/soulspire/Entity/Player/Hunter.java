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

    public Hunter(String name, double x, double y) {
        super(name, PlayerType.HUNTER, x, y, 32, 32);
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