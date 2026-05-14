package com.example.soulspire.Ability;
import com.example.soulspire.Entity.Player.Player;

/**
 * Shaman ability 1: Places a healing totem that periodically restores HP
 * to the player when they are within its radius.
 */
public class HealingTotemAbility extends Ability {

    private static final int HEAL_AMOUNT = 5;
    private static final double HEAL_INTERVAL = 1.5;
    private static final double RADIUS = 100;
    private static final double DURATION = 12.0;

    public HealingTotemAbility() {
        super("Healing Totem", "Place a totem that heals you nearby", 18.0, AbilityType.UTILITY);
        this.icon = loadIcon("/com/example/soulspire/images/healtotem.png");
    }

    /**
     * Executing HealingTotem ability.
     * @param caster  the player using this ability
     * @param targetX mouse X position in world coordinates
     * @param targetY mouse Y position in world coordinates
     */
    @Override
    public void execute(Player caster, double targetX, double targetY) {
        if (currentCooldown > 0) {
            return;
        }
        if (caster.getCurrentFloor() == null) {
            return;
        }

        com.example.soulspire.Entity.Totem totem = new com.example.soulspire.Entity.Totem(caster.getCenterX() - 12, caster.getCenterY() - 12, HEAL_AMOUNT, HEAL_INTERVAL, RADIUS, DURATION, caster);
        caster.getCurrentFloor().addEntity(totem);

        resetCooldown();

    }
}