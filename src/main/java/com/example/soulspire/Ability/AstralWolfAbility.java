package com.example.soulspire.Ability;
import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.Entity.Player.Shaman;

/**
 * Shaman ability 3: Transform into an astral wolf for 7 seconds, greatly increasing movement speed.
 */
public class AstralWolfAbility extends Ability {

    private static final double DURATION = 7.0;
    private static final double SPEED_MULTIPLIER = 2.0;

    private boolean active;
    private double remainingDuration;
    private Player casterRef;
    private double originalSpeed;

    public AstralWolfAbility() {
        super("Astral Wolf", "Transform into a wolf with increased speed", 20.0, AbilityType.MOBILITY);
        this.active = false;
        this.icon = loadIcon("/com/example/soulspire/images/astralwolf.png");
    }

    /**
     * Executing AstralWolf ability.
     * @param caster  the player using this ability
     * @param targetX mouse X position in world coordinates
     * @param targetY mouse Y position in world coordinates
     */
    @Override
    public void execute(Player caster, double targetX, double targetY) {
        if (currentCooldown > 0 || active) {
            return;
        }

        active = true;
        casterRef = caster;
        remainingDuration = DURATION;
        originalSpeed = caster.getMoveSpeed();
        caster.setMoveSpeed(originalSpeed * SPEED_MULTIPLIER);

        if (caster instanceof com.example.soulspire.Entity.Player.Shaman shaman) {
            shaman.wolfForm = true;
        }

        resetCooldown();

    }

    /**
     * Update method used for remaining time of the ability.
     * @param deltaTime time elapsed since last frame in seconds
     */
    @Override
    public void update(double deltaTime) {
        if (currentCooldown > 0) {
            currentCooldown -= deltaTime;
        }

        if (active) {
            remainingDuration -= deltaTime;
            if (remainingDuration <= 0) {
                deactivate();
            }
        }

    }

    /**
     * Deactivation of ability.
     */
    private void deactivate() {
        if (casterRef != null) {
            casterRef.setMoveSpeed(originalSpeed);
            if (casterRef instanceof Shaman shaman) {
                shaman.wolfForm = false;
            }
        }
        active = false;
        casterRef = null;
    }
}