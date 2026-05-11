package com.example.soulspire.Entity.Enemy;

import com.example.soulspire.Core.GameConfig;
import com.example.soulspire.Entity.Direction;
import com.example.soulspire.Entity.LivingEntity;
import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.Item.Item;
import com.example.soulspire.Item.LootTable;
import com.example.soulspire.Util.GameLogger;

import java.util.List;

/**
 * Abstract base class for all enemy types in the tower.
 * Each enemy has AI behavior, aggro detection, and a loot table.
 *
 * <ul>
 *   <li>{@link MeleeEnemy} — chases and attacks at close range</li>
 *   <li>{@link RangedEnemy} — keeps distance and fires projectiles</li>
 *   <li>{@link BossEnemy} — multi-phase fight on the final floor</li>
 *   <li>{@link ChestGuardian} — guards a chest, only aggros nearby</li>
 * </ul>
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

    /** Remaining time until the next attack. */
    protected double currentAttackCooldown;

    /** Whether this enemy is currently aware of and chasing the player. */
    protected boolean aggroed;

    /** The floor number this enemy was spawned on (for stat scaling). */
    protected int floorNumber;

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

    }

    /**
     * Checks whether the player is within aggro range and updates the aggroed flag.
     *
     * @param target the player to check distance against
     */
    protected void checkAggro(Player target) {

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
    public void update(double deltaTime) {

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

    public LootTable getLootTable() { return lootTable; }
    public void setLootTable(LootTable lootTable) { this.lootTable = lootTable; }
    public boolean isAggroed() { return aggroed; }
    public int getFloorNumber() { return floorNumber; }
}