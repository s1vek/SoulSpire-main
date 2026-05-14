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
    private static final Color CHEST_COLOR = Color.web("#b8862e");
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
        if (opened || !guardianDefeated) return;
        opened = true;

        if (reward != null) {
            reward.apply(player);
            player.getInventory().addItem(reward);
            player.setPickupMessage("Got " + reward.getName());
        }
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
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        drawBox(gc, cameraX, cameraY, CHEST_COLOR, Color.BLACK);

        double sx = x - cameraX;
        double sy = y - cameraY;
        gc.setFill(Color.GOLD);
        gc.fillRect(sx + 2, sy + height / 2 - 2, width - 4, 4);
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