package com.example.soulspire.Entity.Player;

import com.example.soulspire.Ability.Ability;
import com.example.soulspire.Combat.CombatSystem;
import com.example.soulspire.Core.GameConfig;
import com.example.soulspire.Core.Saveable;
import com.example.soulspire.Entity.Direction;
import com.example.soulspire.Entity.Interactable;
import com.example.soulspire.Entity.Entity;
import com.example.soulspire.Entity.LivingEntity;
import com.example.soulspire.Item.Inventory;
import com.example.soulspire.Item.Item;
import com.example.soulspire.Util.GameLogger;
import com.example.soulspire.World.Floor;
import com.example.soulspire.World.Tile;
import com.example.soulspire.World.TileType;

import java.util.List;
import java.util.Map;

/**
 * Abstract base class for all playable characters (Warrior, Mage, Hunter, Shaman).
 * Manages abilities, inventory, lives, and interaction with the game world.
 *
 * <p>Each concrete subclass must implement {@link #initAbilities()} to create
 * the three unique abilities for that character class, and
 * to define the basic attack behavior.</p>
 *
 * <p>Implements {@link Saveable} so the player's state can be persisted
 * between sessions.</p>
 */
public abstract class Player extends LivingEntity implements Saveable {

    private static final GameLogger logger = GameLogger.getLogger(Player.class);

    /** Number of ability slots each character has. */
    public static final int ABILITY_COUNT = 3;

    /** Player's chosen name. */
    protected String name;

    /** The character class type. Determines base stats and abilities. */
    protected PlayerType playerType;

    /** The player's item inventory and equipped gear. */
    protected Inventory inventory;

    /** Array of 3 abilities mapped to keys 1, 2, 3. */
    protected Ability[] abilities;

    /** Remaining lives. Starts at 3. Game over when it reaches 0. */
    protected int lives;

    /** Cooldown between basic attacks in seconds. */
    protected double attackCooldown;

    /** Remaining time before the next basic attack can be used. */
    protected double currentAttackCooldown;

    protected CombatSystem combatSystem;

    public boolean enranged = false;

    public boolean frozen = false;

    private double trapDamageTimer = 0;

    private static final double TRAP_DAMAGE_INTERVAL = 1.0;

    private static final int TRAP_DAMAGE = 10;

    /**
     * Creates a new player with stats derived from the given character type.
     *
     * @param name       player's chosen name
     * @param playerType character class determining base stats
     * @param x          initial x position
     * @param y          initial y position
     * @param width      hitbox width
     * @param height     hitbox height
     */
    protected Player(String name, PlayerType playerType, double x, double y, double width, double height) {
        super(x, y, width, height,
                playerType.getBaseHealth(),
                playerType.getBaseAttack(),
                playerType.getBaseDefense(),
                playerType.getBaseSpeed());
        this.name = name;
        this.playerType = playerType;
        this.inventory = new Inventory();
        this.abilities = new Ability[ABILITY_COUNT];
        this.lives = GameConfig.PLAYER_LIVES;
        this.attackCooldown = 0.15;
        this.currentAttackCooldown = 0;
        initAbilities();
    }

    /**
     * Initializes the three unique abilities for this character class.
     * Called once during construction. Each subclass creates its own set.
     *
     * <p>Example (Warrior):</p>
     * <pre>
     * abilities[0] = new ChargeAbility();
     * abilities[1] = new BladewhirlAbility();
     * abilities[2] = new EnrageAbility();
     * </pre>
     */
    protected abstract void initAbilities();

    /**
     * Performs the character's basic attack (bound to spacebar).
     * Melee characters attack in an arc, ranged characters fire a projectile.
     *
     * @param targetX mouse X position for aiming (used by ranged classes)
     * @param targetY mouse Y position for aiming (used by ranged classes)
     */
    public abstract void attack(double targetX, double targetY);

    /**
     * Activates the ability at the given index (0, 1, or 2).
     * Does nothing if the ability is still on cooldown.
     *
     * @param index   ability slot (0-2, mapped to keys 1-3)
     * @param targetX mouse X position for ability targeting
     * @param targetY mouse Y position for ability targeting
     */
    public void useAbility(int index, double targetX, double targetY) {
        if (index < 0 || index >= abilities.length) {
            return;
        }
        Ability ability = abilities[index];
        if (ability == null) {
            return;
        }
        if (!ability.isReady()) {
            return;
        }
        ability.execute(this, targetX, targetY);

    }

    /**
     * Attempts to interact with the nearest {@link Interactable} entity in range.
     * Called when the player presses the E key.
     *
     * @param nearbyEntities list of entities on the current floor
     */
    public void interact(List<Entity> nearbyEntities) {

    }

    /**
     * Adds an item to the player's inventory.
     *
     * @param item the item to collect
     * @return true if the item was added, false if inventory is full
     */
    public boolean collectItem(Item item) {
        return false;
    }

    /**
     * Respawns the player at the given position with full health.
     * Consumes one life. Does not reset floor progress.
     *
     * @param spawnX x position to respawn at
     * @param spawnY y position to respawn at
     */
    public void respawn(double spawnX, double spawnY) {

    }

    /**
     * Updates cooldowns and ability timers each frame.
     *
     * @param deltaTime time elapsed since last frame
     */
    @Override
    public void update(double deltaTime) {
        updateInvulnerability(deltaTime);
        checkTrapDamage(deltaTime);

        if (currentAttackCooldown > 0) {
            currentAttackCooldown -= deltaTime;
        }

        for (Ability ability : abilities) {
            if (ability != null) {
                ability.update(deltaTime);
            }
        }

    }

    private void checkTrapDamage(double deltaTime) {
        if (currentFloor == null) return;
        Tile t = currentFloor.getTileAtPixel(getCenterX(), getCenterY());
        if (t == null || t.getType() != TileType.TRAP) {
            trapDamageTimer = 0;
            return;
        }

        trapDamageTimer -= deltaTime;
        if (trapDamageTimer <= 0) {
            takeDamage(TRAP_DAMAGE);
            trapDamageTimer = TRAP_DAMAGE_INTERVAL;
        }
    }

    /**
     * @return true when all lives are spent — triggers game over
     */
    public boolean isGameOver() {
        return lives <= 0;
    }

    /**
     * @return true if the basic attack cooldown has elapsed
     */
    public boolean canAttack() {
        return currentAttackCooldown <= 0;
    }

    /**
     * Resets the basic attack cooldown after an attack is performed.
     */
    protected void resetAttackCooldown() {
        currentAttackCooldown = attackCooldown;
    }

    /*
    @Override
    public Map<String, Object> toSaveData() {

    }

     */

    @Override
    public void loadSaveData(Map<String, Object> data) {

    }

    public String getName() { return name; }
    public PlayerType getPlayerType() { return playerType; }
    public Inventory getInventory() { return inventory; }
    public Ability[] getAbilities() { return abilities; }
    public int getLives() { return lives; }
    public void setLives(int lives) { this.lives = lives; }
    public CombatSystem getCombatSystem() {return combatSystem;}
    public void setCombatSystem(CombatSystem combatSystem) {this.combatSystem = combatSystem;}
    public Floor getCurrentFloor() {return currentFloor;}
    public void setCurrentFloor(Floor currentFloor) {this.currentFloor = currentFloor;}
}