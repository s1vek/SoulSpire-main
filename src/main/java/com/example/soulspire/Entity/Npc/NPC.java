package com.example.soulspire.Entity.Npc;

import com.example.soulspire.Entity.Entity;
import com.example.soulspire.Entity.Interactable;
import com.example.soulspire.Entity.Player.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Abstract base class for non-player characters found in safe zones.
 */

public abstract class NPC extends Entity implements Interactable {

    /** Default interaction range for all NPCs in pixels. */
    protected static final double DEFAULT_INTERACTION_RANGE = 80.0;

    /** Display name shown above the NPC's head. */
    protected String name;

    /** Dialogue lines shown when the player interacts. */
    protected String[] dialogueLines;

    /**
     * Creates a new NPC at the given position.
     *
     * @param name   display name of the NPC
     * @param x      x position on the map
     * @param y      y position on the map
     * @param width  hitbox width
     * @param height hitbox height
     */
    protected NPC(String name, double x, double y, double width, double height) {
        super(x, y, width, height);
        this.name = name;
        this.dialogueLines = new String[0];
    }

    /**
     * NPCs do not update their state — they are static entities.
     * Override in subclasses if you want idle animations.
     */
    @Override
    public void update(double deltaTime) {
        // NPCs are static — no update logic needed
    }

    /**
     * Renders the NPC sprite and their name above their head.
     */
    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        renderNpc(gc, cameraX, cameraY, Color.OLIVEDRAB);
    }

    protected void renderNpc(GraphicsContext gc, double cameraX, double cameraY, Color fill) {
        drawBox(gc, cameraX, cameraY, fill, Color.BLACK);
        double sx = x - cameraX;
        double sy = y - cameraY;
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(11));
        double textW = name.length() * 6.0;
        gc.fillText(name, sx + (width - textW) / 2.0, sy - 5);
    }

    @Override
    public double getInteractionRange() {
        return DEFAULT_INTERACTION_RANGE;
    }
    @Override
    public boolean canInteract() {
        return true;
    }
    public String getName() { return name; }
    public String[] getDialogueLines() { return dialogueLines; }
}