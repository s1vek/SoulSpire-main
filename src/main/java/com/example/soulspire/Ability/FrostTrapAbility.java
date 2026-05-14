package com.example.soulspire.Ability;

import com.example.soulspire.Ability.Ability;
import com.example.soulspire.Ability.AbilityType;
import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.Entity.Trap;

/**
 * Hunter ability 2: Places an invisible trap at the player's position.
 * When an enemy steps on it, they are frozen and immobilized.
 */
public class FrostTrapAbility extends Ability {

    private static final double FREEZE_DURATION = 3.0;
    private static final double TRAP_RADIUS = 30;

    public FrostTrapAbility() {
        super("Frost Trap", "Place a trap that freezes enemies", 15.0, AbilityType.UTILITY);
        this.icon = loadIcon("/com/example/soulspire/images/trap.png");
    }

    /**
     * Executing FrostTrap ability.
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

        com.example.soulspire.Entity.Trap trap = new com.example.soulspire.Entity.Trap(caster.getCenterX(), caster.getCenterY(), TRAP_RADIUS, FREEZE_DURATION, caster.getCurrentFloor());
        caster.getCurrentFloor().addEntity(trap);

        resetCooldown();

    }
}