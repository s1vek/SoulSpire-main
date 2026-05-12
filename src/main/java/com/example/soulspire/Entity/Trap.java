package com.example.soulspire.Entity;

import com.example.soulspire.Entity.Enemy.Enemy;
import com.example.soulspire.World.Floor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Trap extends Entity{

    private final double effectRadius;
    private final double freezeDuration;
    private final Floor floor;

    public Trap(double centerX, double centerY, double radius, double freezeDuration, Floor floor) {
        super(centerX - radius, centerY - radius, radius * 2, radius * 2);
        this.effectRadius = radius;
        this.freezeDuration = freezeDuration;
        this.floor = floor;
    }

    @Override
    public void update(double deltaTime) {
        if (floor == null) {
            return;
        }
        for (Entity e : floor.getEntities()) {
            if (!(e instanceof Enemy enemy) || !e.isActive()) continue;
            double dx = enemy.getCenterX() - getCenterX();
            double dy = enemy.getCenterY() - getCenterY();
            if (dx * dx + dy * dy <= effectRadius * effectRadius) {
                enemy.applyStun(freezeDuration);
                active = false;
                return;
            }
        }

    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        gc.setStroke(Color.LIGHTCYAN.deriveColor(0, 1, 1, 0.4));
        gc.setLineWidth(2);
        gc.strokeOval(getX() - cameraX, getY() - cameraY, getWidth(), getHeight());
    }
}
