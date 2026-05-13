package com.example.soulspire.Entity.Enemy;

import com.example.soulspire.Core.GameConfig;
import com.example.soulspire.Entity.Direction;
import com.example.soulspire.World.Tile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.example.soulspire.Entity.LivingEntity;
import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.Item.Item;
import com.example.soulspire.Item.LootTable;
import com.example.soulspire.Util.GameLogger;

import java.util.List;

/**
 * Abstract base class for all enemy types in the tower.
 * Each enemy has AI behavior, aggro detection, and a loot table.
 */

public abstract class Enemy extends LivingEntity {

    private static final GameLogger logger = GameLogger.getLogger(Enemy.class);

    /** Loot table defining what items this enemy drops on death. */
    protected LootTable lootTable;

    /** Distance in pixels at which the enemy detects the player. */
    protected double aggroRange;

    /** Distance in pixels at which the enemy can perform attacks. */
    protected double attackRange;

    /** Time between attacks in seconds. */
    protected double attackCooldown;

    protected double stunTimer = 0;

    /** Remaining time until the next attack. */
    protected double currentAttackCooldown;

    /** Whether this enemy is currently aware of and chasing the player. */
    protected boolean aggroed;

    /** The floor number this enemy was spawned on (for stat scaling). */
    protected int floorNumber;

    private boolean lootCollected = false;

    protected abstract Color getBodyColor();

    /**
     * Creates a new enemy with base stats that will be scaled by floor number.
     *
     * @param x             initial x position
     * @param y             initial y position
     * @param width         hitbox width
     * @param height        hitbox height
     * @param baseHealth    health before floor scaling
     * @param baseAttack    attack damage before floor scaling
     * @param defense       defense value
     * @param moveSpeed     movement speed in pixels per second
     * @param aggroRange    detection range in pixels
     * @param attackRange   attack range in pixels
     * @param attackCooldown time between attacks in seconds
     * @param floorNumber   floor this enemy is on (for difficulty scaling)
     */
    protected Enemy(double x, double y, double width, double height,
                    int baseHealth, int baseAttack, int defense, double moveSpeed,
                    double aggroRange, double attackRange, double attackCooldown,
                    int floorNumber) {
        super(x, y, width, height,
                scaleValue(baseHealth, floorNumber),
                scaleValue(baseAttack, floorNumber),
                defense, moveSpeed);
        this.aggroRange = aggroRange;
        this.attackRange = attackRange;
        this.attackCooldown = attackCooldown;
        this.currentAttackCooldown = 0;
        this.aggroed = false;
        this.floorNumber = floorNumber;
        this.lootTable = new LootTable();
    }

    /**
     * AI logic executed each frame. Each enemy subclass implements its own behavior:
     * melee enemies chase, ranged enemies keep distance, bosses switch phases.
     *
     * @param target    the player to react to
     * @param deltaTime time elapsed since last frame
     */
    public abstract void updateAI(Player target, double deltaTime);

    /**
     * Moves the enemy toward the given target coordinates.
     * Chooses the dominant axis direction for movement.
     *
     * @param targetX   x coordinate to move toward
     * @param targetY   y coordinate to move toward
     * @param deltaTime time elapsed since last frame
     */
    protected void moveToward(double targetX, double targetY, double deltaTime) {
        double dx = targetX - getCenterX();
        double dy = targetY - getCenterY();

        if (Math.abs(dx) > Math.abs(dy)) {
            move(dx > 0 ? Direction.RIGHT : Direction.LEFT, deltaTime);
        } else {
            move(dy > 0 ? Direction.DOWN : Direction.UP, deltaTime);
        }

    }

    protected void moveAway(double targetX, double targetY, double deltaTime) {
        double dx = getCenterX() - targetX;
        double dy = getCenterY() - targetY;
        if (Math.abs(dx) > Math.abs(dy)) {
            move(dx > 0 ? Direction.RIGHT : Direction.LEFT, deltaTime);
        } else {
            move(dy > 0 ? Direction.DOWN : Direction.UP, deltaTime);
        }
    }

    /**
     * Checks whether the player is within aggro range and updates the aggroed flag.
     *
     * @param target the player to check distance against
     */
    protected void checkAggro(Player target) {
        if (aggroed) {
            return;
        }
        if (distanceTo(target) > aggroRange){
            return;
        }
        if (!hasLineOfSight(target)){
            return;
        }
        aggroed = true;
    }

    protected boolean hasLineOfSight(Player target) {
        if (currentFloor == null) return true;

        double startX = getCenterX();
        double startY = getCenterY();
        double dx = target.getCenterX() - startX;
        double dy = target.getCenterY() - startY;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist < 0.001) return true;

        int steps = (int) (dist / 8);
        for (int i = 1; i < steps; i++) {
            double t = i / (double) steps;
            double sampleX = startX + dx * t;
            double sampleY = startY + dy * t;
            Tile tile = currentFloor.getTileAtPixel(sampleX, sampleY);
            if (tile == null || !tile.isWalkable()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Rolls the loot table and returns dropped items.
     * Called by the game engine when this enemy dies.
     *
     * @return list of items to spawn on the map
     */
    public List<Item> dropLoot() {
        return lootTable.roll();
    }

    /**
     * Called when this enemy's health reaches zero.
     * Subclasses can override to add specific death behavior (e.g. ChestGuardian
     * unlocks its guarded chest).
     */
    @Override
    protected void onDeath() {

    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        double screenX = x - cameraX;
        double screenY = y - cameraY;

        gc.setFill(getBodyColor());
        gc.fillOval(screenX, screenY, width, height);

        if (aggroed) {
            gc.setStroke(Color.YELLOW);
            gc.setLineWidth(1.5);
            gc.strokeRect(screenX - 1, screenY - 1, width + 2, height + 2);
        }

        if (isStunned()) {
            gc.setFill(Color.CYAN);
            gc.fillOval(screenX + width / 2 - 4, screenY - 18, 8, 8);
        }

        double hpPct = (double) currentHealth / maxHealth;
        gc.setFill(Color.BLACK);
        gc.fillRect(screenX, screenY - 8, width, 4);
        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(screenX, screenY - 8, width * hpPct, 4);
    }


    @Override
    public void update(double deltaTime) {
        if(stunTimer > 0) {
            stunTimer -= deltaTime;
        }
        if (currentAttackCooldown > 0) {
            currentAttackCooldown =- deltaTime;
        }
        updateInvulnerability(deltaTime);

    }

    @Override
    public void takeDamage(int amount) {
        super.takeDamage(amount);
        aggroed = true;
    }

    /**
     * Scales a base stat value according to the floor number and difficulty multiplier.
     *
     * @param baseValue base stat before scaling
     * @param floor     current floor number (0-indexed)
     * @return scaled stat value
     */
    private static int scaleValue(int baseValue, int floor) {
        return (int) (baseValue * (1.0 + floor * GameConfig.DIFFICULTY_SCALE));
    }

    /**
     * Stuns a enemy.
     * @param duration
     */
    public void applyStun(double duration) {
        if (duration > stunTimer) stunTimer = duration;
    }

    /**
     * Check if enemy is stunned
     * @return
     */
    public boolean isStunned() {
        return stunTimer > 0;
    }

    public LootTable getLootTable() { return lootTable; }
    public void setLootTable(LootTable lootTable) { this.lootTable = lootTable; }
    public boolean isAggroed() { return aggroed; }
    public int getFloorNumber() { return floorNumber; }
    public boolean isLootCollected() { return lootCollected; }
    public void setLootCollected(boolean v) { this.lootCollected = v; }
}