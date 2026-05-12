package com.example.soulspire.Ability;

import com.example.soulspire.Ability.Ability;
import com.example.soulspire.Ability.AbilityType;
import com.example.soulspire.Combat.CombatSystem;
import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.World.Floor;

/**
 * Warrior ability 2: Spins and damages all enemies in a radius.
 */
public class BladewhirlAbility extends Ability {

    private static final double RADIUS = 80;
    private static final double DAMAGE_MULTIPLIER = 1.5;

    public BladewhirlAbility() {
        super("Bladewhirl", "Spin attack hitting all nearby enemies", 12.0, AbilityType.OFFENSIVE);
        this.icon = loadIcon("/com/example/soulspire/images/bladewhirl.png");
    }

    @Override
    public void execute(Player caster, double targetX, double targetY) {
        if (currentCooldown > 0) {
            return;
        }

        Floor floor = caster.getCurrentFloor();
        CombatSystem combat = caster.getCombatSystem();
        if (floor == null || combat == null) {
            return;
        }

        int damage = (int)(caster.getAttackDamage() * DAMAGE_MULTIPLIER);
        combat.processAreaDamage(caster.getCenterX(), caster.getCenterY(), RADIUS, damage, floor.getEntities(), caster);

        resetCooldown();

    }
}