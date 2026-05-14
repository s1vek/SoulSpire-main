package com.example.soulspire.Ability;
import com.example.soulspire.Entity.Player.Player;

/**
 * Shaman ability 2: Powerful fire-enhanced melee strike with extended range.
 */
public class FirestrikeAbility extends Ability {

    private static final double DAMAGE_MULTIPLIER = 2.5;
    private static final double RANGE = 60;

    public FirestrikeAbility() {
        super("Firestrike", "Devastating fire-enhanced weapon strike", 10.0, AbilityType.OFFENSIVE);
        this.icon = loadIcon("/com/example/soulspire/images/firestrike.png");
    }

    @Override
    public void execute(Player caster, double targetX, double targetY) {
        if (currentCooldown > 0) {
            return;
        }
        var floor = caster.getCurrentFloor();
        var combat = caster.getCombatSystem();
        if (floor == null || combat == null) {
            return;
        }

        double dx = targetX - caster.getCenterX();
        double dy = targetY - caster.getCenterY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist < 0.001) {
            return;
        }

        double dirX = dx / dist;
        double dirY = dy / dist;
        double centerX = caster.getCenterX() + dirX * 40;
        double centerY = caster.getCenterY() + dirY * 40;

        int damage = (int)(caster.getAttackDamage() * DAMAGE_MULTIPLIER);
        combat.processAreaDamage(centerX, centerY, RANGE, damage, floor.getEntities(), caster);

        resetCooldown();

    }
}