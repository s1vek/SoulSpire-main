package com.example.soulspire.Combat;
import com.example.soulspire.Entity.Entity;
import com.example.soulspire.Entity.LivingEntity;
import com.example.soulspire.Entity.Projectile;
import com.example.soulspire.Util.GameLogger;

import java.util.List;

/**
 * Handles all combat damage calculations — melee hits, projectile impacts and AOE abilities.
 */
public class CombatSystem {

    private static final GameLogger logger = GameLogger.getLogger(CombatSystem.class);

    /**
     * Processes a direct melee attack from one entity to another.
     */
    public void processAttack(LivingEntity attacker, LivingEntity target) {

    }

    /**
     * Processes a projectile hitting a living entity.
     * Deactivates the projectile after impact.
     */
    public void processProjectileHit(Projectile projectile, LivingEntity target) {

    }

    /**
     * Deals damage to all living entities within a radius.
     * Used by AoE abilities like Bladewhirl, Arcane Orb explosion, etc.
     *
     * @param centerX  center of the area
     * @param centerY  center of the area
     * @param radius   effect radius in pixels
     * @param damage   damage to deal to each target
     * @param entities all entities on the floor
     * @param owner    the entity causing the damage (excluded from hits)
     */
    public void processAreaDamage(double centerX, double centerY, double radius, int damage, List<Entity> entities, Entity owner) {

        double radiusSq = radius * radius;

        for (Entity e : entities) {
            if (e == owner) {
                continue;
            }
            if (!e.isActive()) {
                continue;
            }
            if (!(e instanceof LivingEntity living)) {
                continue;
            }

            double dx = e.getCenterX() - centerX;
            double dy = e.getCenterY() - centerY;
            if (dx * dx + dy * dy <= radiusSq) {
                living.takeDamage(damage);
                logger.info(owner.getClass().getSimpleName() + " hit " +
                        e.getClass().getSimpleName() + " for " + damage);
            }
        }

    }
}