package com.example.soulspire.Ability;
import com.example.soulspire.Entity.Player.Player;

/**
 * Mage ability 3: Instantly teleport a short distance in facing direction.
 */
public class TeleportAbility extends Ability {

    private static final double TELEPORT_DISTANCE = 150;

    public TeleportAbility() {
        super("Teleport", "Blink forward a short distance", 6.0, AbilityType.MOBILITY);
        this.icon = loadIcon("/com/example/soulspire/images/teleport.png");
    }

    /**
     * Exectuing Teleport ability.
     * @param caster  the player using this ability
     * @param targetX mouse X position in world coordinates
     * @param targetY mouse Y position in world coordinates
     */
    @Override
    public void execute(Player caster, double targetX, double targetY) {
        if (currentCooldown > 0) {
            return;
        }
        var floor = caster.getCurrentFloor();
        if (floor == null) {
            return;
        }

        double dx = targetX - caster.getCenterX();
        double dy = targetY - caster.getCenterY();
        double d = Math.hypot(dx, dy);
        if (d < 0.001) {
            return;
        }
        double dirX = dx / d, dirY = dy / d;

        double destX = caster.getX(), destY = caster.getY();
        for (double frac = 1.0; frac >= 0.25; frac -= 0.25) {
            double tx = caster.getX() + dirX * TELEPORT_DISTANCE * frac;
            double ty = caster.getY() + dirY * TELEPORT_DISTANCE * frac;
            var t = floor.getTileAtPixel(tx + caster.getWidth() / 2, ty + caster.getHeight() / 2);
            if (t != null && t.isWalkable()) {
                destX = tx; destY = ty; break;
            }
        }

        caster.setX(destX);
        caster.setY(destY);
        resetCooldown();

    }
}