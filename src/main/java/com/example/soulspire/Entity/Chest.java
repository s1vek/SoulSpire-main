package com.example.soulspire.Entity;

import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.Item.SoulEcho;
import com.example.soulspire.Util.GameLogger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A treasure chest guarded by a {@link com.example.soulspire.Entity.Enemy.ChestGuardian}.
 * Contains a {@link SoulEcho} modifier that the player receives upon opening.
 * Can only be opened after the guardian is defeated.
 */
public class Chest extends Entity implements Interactable {

    private static final GameLogger logger = GameLogger.getLogger(Chest.class);
    private static final double INTERACTION_RANGE = 60.0;

    private static final Color LOCKED_COLOR = Color.web("#5a4530");
    private static final Color UNLOCKED_COLOR = Color.web("#b8862e");
    private static final Color OPEN_COLOR = Color.web("#3a3a3a");


    private SoulEcho reward;
    private boolean opened;
    private boolean guardianDefeated;

    /**
     * Creates a new chest at the given position.
     *
     * @param x      x position on the map
     * @param y      y position on the map
     * @param reward the SoulEcho modifier inside
     */
    public Chest(double x, double y, SoulEcho reward) {
        super(x, y, 32, 32);
        this.reward = reward;
        this.opened = false;
        this.guardianDefeated = false;
    }

    @Override
    public void onInteract(Player player) {

    }

    @Override
    public boolean canInteract() {
        return !opened && guardianDefeated;
    }

    @Override
    public double getInteractionRange() {
        return INTERACTION_RANGE;
    }

    @Override
    public void update(double deltaTime) {
        // Chests are static — no update needed
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        Color fill = opened ? OPEN_COLOR : (guardianDefeated ? UNLOCKED_COLOR : LOCKED_COLOR);
        drawBox(gc, cameraX, cameraY, fill, Color.BLACK);

        double sx = x - cameraX;
        double sy = y - cameraY;
        gc.setFill(Color.GOLD);
        gc.fillRect(sx + 2, sy + height / 2 - 2, width - 4, 4);
        if (!opened) {
            gc.setFill(guardianDefeated ? Color.YELLOW : Color.DARKGRAY);
            gc.fillOval(sx + width / 2 - 3, sy + height / 2 - 3, 6, 6);
        }


    }

    /**
     * Called by ChestGuardian.onDeath() to unlock this chest for interaction.
     */
    public void setGuardianDefeated(boolean defeated) {
        this.guardianDefeated = defeated;
    }

    public boolean isOpened() { return opened; }
    public boolean isGuardianDefeated() { return guardianDefeated; }
}