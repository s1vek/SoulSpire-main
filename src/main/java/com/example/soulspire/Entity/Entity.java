package com.example.soulspire.Entity;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


import java.awt.*;


abstract public class Entity {

    /** X position in pixels on the game map. */
    protected double x;

    /** Y position in pixels on the game map. */
    protected double y;

    /** Width of the entity's hitbox in pixels. */
    protected double width;

    /** Height of the entity's hitbox in pixels. */
    protected double height;

    /** When false, the entity is marked for removal from the game world. */
    protected boolean active;

    /**
     * Creates a new entity at the given position with the given hitbox size.
     *
     * @param x      initial x position in pixels
     * @param y      initial y position in pixels
     * @param width  hitbox width in pixels
     * @param height hitbox height in pixels
     */
    protected Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.active = true;
    }

    /**
     * Updates the entity's state. Called once per frame by the game loop.
     *
     * @param deltaTime time elapsed since the last frame in seconds
     */
    public abstract void update(double deltaTime);

    /**
     * Renders the entity to the screen. Called once per frame after update.
     * Coordinates should be adjusted by camera offset before drawing.
     *
     * @param gc        the graphics context to draw on
     * @param cameraX   horizontal camera offset in pixels
     * @param cameraY   vertical camera offset in pixels
     */
    public abstract void render(GraphicsContext gc, double cameraX, double cameraY);

    /**
     * Returns the axis-aligned bounding box for collision detection.
     *
     * @return rectangle representing this entity's hitbox
     */
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    /**
     * Calculates the distance between the centers of this entity and another.
     *
     * @param other the other entity
     * @return distance in pixels
     */
    public double distanceTo(Entity other) {
        double dx = getCenterX() - other.getCenterX();
        double dy = getCenterY() - other.getCenterY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    protected void drawBox(GraphicsContext gc, double cameraX, double cameraY, Color fill, Color border) {
        double sx = x - cameraX;
        double sy = y - cameraY;
        gc.setFill(fill);
        gc.fillRect(sx, sy, width, height);
        if (border != null) {
            gc.setStroke(border);
            gc.setLineWidth(1);
            gc.strokeRect(sx + 0.5, sy + 0.5, width - 1, height - 1);
        }
    }


    /**
     * @return center X coordinate of this entity
     */
    public double getCenterX() {
        return x + width / 2;
    }

    /**
     * @return center Y coordinate of this entity
     */
    public double getCenterY() {
        return y + height / 2;
    }


    /**
     * Getters and setters.
     */
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

