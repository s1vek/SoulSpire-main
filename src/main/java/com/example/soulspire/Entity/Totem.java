package com.example.soulspire.Entity;

import com.example.soulspire.Entity.Player.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A healing totem placed by the Shaman's HealingTotemAbility.
 * Periodically heals the player when they are within its radius.
 * Despawns after its duration expires.
 */
public class Totem extends Entity {

    private int healAmount;
    private double healInterval;
    private double healTimer;
    private double radius;
    private double duration;
    private double remainingDuration;
    private Player owner;

    /**
     * Creates a new healing totem.
     *
     * @param x            x position
     * @param y            y position
     * @param healAmount   HP restored per tick
     * @param healInterval seconds between heal ticks
     * @param radius       range in pixels the player must be within
     * @param duration     total lifetime in seconds
     * @param owner        the player who placed this totem
     */
    public Totem(double x, double y, int healAmount, double healInterval,
                 double radius, double duration, Player owner) {
        super(x, y, 24, 24);
        this.healAmount = healAmount;
        this.healInterval = healInterval;
        this.healTimer = 0;
        this.radius = radius;
        this.duration = duration;
        this.remainingDuration = duration;
        this.owner = owner;
    }

    @Override
    public void update(double deltaTime) {
        if (owner == null) {
            active = false;
            return;
        }
        remainingDuration -= deltaTime;
        if (remainingDuration <= 0) {
            active = false;
            return;
        }

        healTimer += deltaTime;
        if (healTimer >= healInterval) {
            healTimer = 0;
            double dx = owner.getCenterX() - getCenterX();
            double dy = owner.getCenterY() - getCenterY();
            if (dx * dx + dy * dy <= radius * radius) {
                owner.heal(healAmount);
            }
        }
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        double sx = getX() - cameraX;
        double sy = getY() - cameraY;

        gc.setStroke(Color.LIMEGREEN.deriveColor(0, 1, 1, 0.3));
        gc.setLineWidth(1);
        double r = 100;
        gc.strokeOval(getCenterX() - cameraX - r, getCenterY() - cameraY - r, r * 2, r * 2);

        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(sx, sy, getWidth(), getHeight());

        gc.setFill(Color.WHITE);
        gc.setStroke(Color.LIMEGREEN);
        double bar = getWidth();
        double pct = remainingDuration / duration;
        gc.fillRect(sx, sy - 6, bar * pct, 3);

    }

    public double getRemainingDuration() { return remainingDuration; }
}