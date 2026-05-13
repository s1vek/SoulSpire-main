package com.example.soulspire.Entity;

import com.example.soulspire.Util.GameLogger;
import com.example.soulspire.World.Floor;
import com.example.soulspire.World.Tile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Abstract base class for entities that have health and can move, attack, and die.
 */

public abstract class LivingEntity extends Entity {

    private static final GameLogger logger = GameLogger.getLogger(LivingEntity.class);

    /** Duration of invulnerability after taking damage, in seconds. */
    protected static final double IFRAME_DURATION = 0.5;

    /** Maximum health points this entity can have. */
    protected int maxHealth;

    /** Current health points. When this reaches 0, the entity dies. */
    protected int currentHealth;

    /** Base attack damage dealt by this entity. */
    protected int attackDamage;

    /** Defense value — subtracted from incoming damage. */
    protected int defense;

    /** Movement speed in pixels per second. */
    protected double moveSpeed;

    /** The direction this entity is currently facing. Affects attack direction. */
    protected Direction facing;

    /** Whether this entity is currently immune to damage (iframe active). */
    protected boolean invulnerable;

    /** Remaining invulnerability time in seconds. Counts down to zero. */
    protected double invulnerabilityTimer;

    protected Floor currentFloor;

    /**
     * Creates a new living entity with the given stats.
     *
     * @param x            initial x position
     * @param y            initial y position
     * @param width        hitbox width
     * @param height       hitbox height
     * @param maxHealth    maximum health points
     * @param attackDamage base attack damage
     * @param defense      damage reduction value
     * @param moveSpeed    movement speed in pixels per second
     */
    protected LivingEntity(double x, double y, double width, double height,
                           int maxHealth, int attackDamage, int defense, double moveSpeed) {
        super(x, y, width, height);
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.attackDamage = attackDamage;
        this.defense = defense;
        this.moveSpeed = moveSpeed;
        this.facing = Direction.DOWN;
        this.invulnerable = false;
        this.invulnerabilityTimer = 0;
    }

    /**
     * Applies damage to this entity after subtracting defense.
     * Damage is ignored if the entity is currently invulnerable.
     * Activates invulnerability frames after a successful hit.
     */
    public void takeDamage(int amount) {
        if (invulnerable || isDead()) {
            return;
        }

        int actual = Math.max(1, amount - defense);
        currentHealth -= actual;

        if (currentHealth <= 0) {
            currentHealth = 0;
            active = false;
            onDeath();
        } else {
            invulnerable = true;
            invulnerabilityTimer = IFRAME_DURATION;
        }
    }

    /**
     * Restores health points, capped at maximum health.
     *
     * @param amount health points to restore (must be positive)
     */
    public void heal(int amount) {
        if (amount <= 0 || isDead()) return;
        currentHealth = Math.min(maxHealth, currentHealth + amount);
    }

    /**
     * Moves the entity in the given direction based on its speed and elapsed time.
     * Updates the facing direction. Tile collision must be checked externally
     * by {@link com.example.soulspire.Combat.CollisionDetector CollisionDetector}.
     *
     * @param dir       the direction to move
     * @param deltaTime time elapsed since last frame in seconds
     */
    public void move(Direction dir, double deltaTime) {
        double dx = dir.getDx() * moveSpeed * deltaTime;
        double dy = dir.getDy() * moveSpeed * deltaTime;

        if (currentFloor != null) {
            if (canMoveTo(x + dx, y)) x += dx;
            if (canMoveTo(x, y + dy)) y += dy;
        } else {
            x += dx;
            y += dy;
        }
        this.facing = dir;
    }

    private boolean canMoveTo(double newX, double newY) {
        double m = 1;
        Tile tl = currentFloor.getTileAtPixel(newX + m, newY + m);
        Tile tr = currentFloor.getTileAtPixel(newX + width - m, newY + m);
        Tile bl = currentFloor.getTileAtPixel(newX + m, newY + height - m);
        Tile br = currentFloor.getTileAtPixel(newX + width - m, newY + height - m);
        return tl != null && tl.isWalkable()
                && tr != null && tr.isWalkable()
                && bl != null && bl.isWalkable()
                && br != null && br.isWalkable();
    }

    /**
     * Called when health reaches zero. Subclasses override to add behavior
     * (e.g. enemies drop loot, players lose a life).
     */
    protected void onDeath() {
        setActive(false);
    }

    /**
     * Updates invulnerability timer. Should be called by subclass update() methods.
     *
     * @param deltaTime time elapsed since last frame
     */
    protected void updateInvulnerability(double deltaTime) {
        if (invulnerable) {
            invulnerabilityTimer -= deltaTime;
            if (invulnerabilityTimer <= 0) {
                invulnerable = false;
            }
        }
    }

    /**
     * @return true if current health is zero or below
     */
    public boolean isDead() {
        return currentHealth <= 0;
    }

    /**
     * Returns health as a fraction of max health (0.0 to 1.0) for the HP bar UI.
     * @return health percentage
     */
    public double getHealthPercent() {
        return (double) currentHealth / maxHealth;
    }

    protected void renderWithHealthBar(GraphicsContext gc, double cameraX, double cameraY, Color fillColor) {
        Color effective = invulnerable ? Color.WHITE : fillColor;
        drawBox(gc, cameraX, cameraY, effective, Color.BLACK);

        if (currentHealth < maxHealth) {
            double sx = x - cameraX;
            double sy = y - cameraY;
            double barW = width;
            double barH = 4;
            double barY = sy - barH - 3;
            gc.setFill(Color.web("#1a0000"));
            gc.fillRect(sx, barY, barW, barH);
            double pct = getHealthPercent();
            Color hpColor = pct > 0.5 ? Color.web("#3aaf3a") : pct > 0.25 ? Color.ORANGE : Color.RED;
            gc.setFill(hpColor);
            gc.fillRect(sx, barY, barW * pct, barH);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            gc.strokeRect(sx + 0.5, barY + 0.5, barW - 1, barH - 1);
        }
    }


    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
    public int getCurrentHealth() { return currentHealth; }
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = Math.min(currentHealth, maxHealth);
    }
    public int getAttackDamage() { return attackDamage; }
    public void setAttackDamage(int attackDamage) { this.attackDamage = attackDamage; }
    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }
    public double getMoveSpeed() { return moveSpeed; }
    public void setMoveSpeed(double moveSpeed) { this.moveSpeed = moveSpeed; }
    public Direction getFacing() { return facing; }
    public void setFacing(Direction facing) { this.facing = facing; }
    public boolean isInvulnerable() { return invulnerable; }
    public void setInvulnerable(boolean invulnerable) { this.invulnerable = invulnerable; }
    public Floor getCurrentFloor() { return currentFloor; }
    public void setCurrentFloor(Floor floor) { this.currentFloor = floor; }

}