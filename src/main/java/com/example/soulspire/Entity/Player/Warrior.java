
package com.example.soulspire.Entity.Player;

import com.example.soulspire.Ability.BladewhirlAbility;
import com.example.soulspire.Ability.ChargeAbility;
import com.example.soulspire.Ability.EnrageAbility;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Map;

public class Warrior extends Player {

    private static final Color BODY_COLOR = Color.CRIMSON;

    public Warrior(String name, double x, double y) {
        super(name, PlayerType.WARRIOR, x, y, 32, 32);
    }

    @Override
    protected void initAbilities() {

    }

    @Override
    public void attack(double targetX, double targetY) {

    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        renderWithHealthBar(gc, cameraX, cameraY, BODY_COLOR);

    }

    @Override
    public Map<String, Object> toSaveData() {
        return Map.of();
    }
}