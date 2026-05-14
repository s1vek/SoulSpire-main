package com.example.soulspire.Ability;
import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.Entity.Projectile;

/**
 * Mage ability 1: Fires a magic orb that explodes on impact dealing AoE damage.
 */
public class ArcaneOrbAbility extends Ability {

    private static final double ORB_SPEED = 400;
    private static final double ORB_RANGE = 400;
    private static final double EXPLOSION_RADIUS = 30;

    public ArcaneOrbAbility() {
        super("Arcane Orb", "Launch an orb that explodes on impact", 10.0, AbilityType.OFFENSIVE);
        this.icon = loadIcon("/com/example/soulspire/images/arcaneorb.png");
    }

    /**
     * Executing ArcaneOrb ability.
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

        int damage = (int)(caster.getAttackDamage() * 2.0);

        Projectile orb = new Projectile(caster.getCenterX() - 8, caster.getCenterY() - 8, targetX, targetY, ORB_SPEED, damage, ORB_RANGE, caster);
        orb.setColor(javafx.scene.paint.Color.MAGENTA);
        orb.setExplosionRadius(EXPLOSION_RADIUS);

        caster.getCurrentFloor().addEntity(orb);
        resetCooldown();
    }
}