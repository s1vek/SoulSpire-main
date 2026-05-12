package com.example.soulspire.Entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A moving projectile fired by ranged attacks (Mage basic attack, Hunter arrows,
 * RangedEnemy shots, ArcaneOrb, SpreadShot).
 * Travels in a straight line until it hits a target, a wall, or exceeds its max range.
 */
public class Projectile extends Entity {

    private double velocityX;
    private double velocityY;
    private int damage;
    private Entity owner;
    private double maxRange;
    private double distanceTraveled;
    private Color color;
    private double explosionRadius = 0;

    /**
     * Creates a new projectile.
     *
     * @param x        starting x position
     * @param y        starting y position
     * @param targetX  aim target x (projectile flies toward this point)
     * @param targetY  aim target y
     * @param speed    travel speed in pixels per second
     * @param damage   damage dealt on hit
     * @param maxRange maximum travel distance before despawning
     * @param owner    the entity that fired this (to avoid self-hits)
     */
    public Projectile(double x, double y, double targetX, double targetY,
                      double speed, int damage, double maxRange, Entity owner) {
        super(x, y, 8, 8);
        this.damage = damage;
        this.maxRange = maxRange;
        this.owner = owner;
        this.distanceTraveled = 0;
        this.color = Color.YELLOW;

        double dx = targetX - x;
        double dy = targetY - y;
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length > 0) {
            this.velocityX = (dx / length) * speed;
            this.velocityY = (dy / length) * speed;
        } else {
            this.velocityX = 0;
            this.velocityY = speed;
        }
    }

    @Override
    public void update(double deltaTime) {
        double dx = velocityX * deltaTime;
        double dy = velocityY * deltaTime;
        setX(getX() + dx);
        setY(getY() + dy);
        distanceTraveled += Math.hypot(dx, dy);
        if (distanceTraveled >= maxRange) {
            active = false;
        }
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        gc.setFill(color);
        gc.fillOval(getX() - cameraX, getY() - cameraY, getWidth(), getHeight());
    }

    public int getDamage() { return damage; }
    public Entity getOwner() { return owner; }
    public void setColor(Color color) { this.color = color; }
    public double getExplosionRadius() { return explosionRadius; }
    public void setExplosionRadius(double r) { this.explosionRadius = r; }
}