package com.example.soulspire.Ability;
import com.example.soulspire.Entity.Player.Player;

/**
 * Warrior ability 3: Buff increasing damage and max HP for 10 seconds.
 */
public class EnrageAbility extends Ability {

    private static final double DURATION = 10.0;
    private static final double DAMAGE_BOOST = 1.5;
    private static final int HEALTH_BOOST = 50;

    private boolean active;
    private double remainingDuration;
    private int originalDamage;
    private int originalMaxHealth;
    private Player activeCaster;

    public EnrageAbility() {
        super("Enrage", "Increase DMG and HP for 10 seconds", 25.0, AbilityType.UTILITY);
        this.active = false;
        this.icon = loadIcon("/com/example/soulspire/images/enrage.png");
    }

    /**
     * Executing Enrage ability.
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
        activeCaster = caster;
        remainingDuration = DURATION;

        originalDamage = caster.getAttackDamage();
        originalMaxHealth = caster.getMaxHealth();

        caster.setAttackDamage((int) (originalDamage * DAMAGE_BOOST));
        caster.setMaxHealth(originalMaxHealth + HEALTH_BOOST);
        caster.heal(HEALTH_BOOST);
        caster.enranged = true;

        resetCooldown();

    }

    /**
     * Update method mainly used for remaining time of enrage.
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
        if (activeCaster != null) {
            activeCaster.setAttackDamage(originalDamage);
            activeCaster.setMaxHealth(originalMaxHealth);
            activeCaster.setCurrentHealth(Math.min(activeCaster.getCurrentHealth(), originalMaxHealth));
            activeCaster.enranged = false;
        }
        active = false;
        activeCaster = null;
    }
}