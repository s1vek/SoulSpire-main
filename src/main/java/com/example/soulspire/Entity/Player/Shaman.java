
package com.example.soulspire.Entity.Player;

import com.example.soulspire.Ability.AstralWolfAbility;
import com.example.soulspire.Ability.FirestrikeAbility;
import com.example.soulspire.Ability.HealingTotemAbility;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Map;


public class Shaman extends Player {

    public Shaman(String name, double x, double y) {
        super(name, PlayerType.SHAMAN, x, y, 32, 32);
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