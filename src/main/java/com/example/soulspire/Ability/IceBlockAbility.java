package com.example.soulspire.Ability;

import com.example.soulspire.Entity.Player.Player;

/**
 * Mage ability 2: Freeze in ice for 5 seconds, becoming immune to all damage.
 * Player cannot move while active. Can block trap projectiles.
 */
public class IceBlockAbility extends Ability {

    private static final double DURATION = 5.0;

    private boolean active;
    private double remainingDuration;
    private Player casterRef;

    public IceBlockAbility() {
        super("Ice Block", "Become immune to damage for 5 seconds", 20.0, AbilityType.DEFENSIVE);
        this.active = false;
        this.icon = loadIcon("/com/example/soulspire/images/iceblock.png");
    }

    @Override
    public void execute(Player caster, double targetX, double targetY) {
        if (currentCooldown > 0 || active) {
            return;
        }
        active = true;
        casterRef = caster;
        remainingDuration = DURATION;
        caster.frozen = true;
        resetCooldown();
    }

    @Override
    public void update(double deltaTime) {
        if (currentCooldown > 0) currentCooldown -= deltaTime;
        if (active) {
            remainingDuration -= deltaTime;
            if (remainingDuration <= 0) {
                if (casterRef != null) casterRef.frozen = false;
                active = false;
                casterRef = null;
            }
        }
    }
}