package com.example.soulspire.Ability;
import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.Entity.Projectile;

/**
 * Hunter ability 1: Fires multiple arrows in a fan pattern toward the cursor.
 */
public class SpreadShotAbility extends Ability {

    private static final int ARROW_COUNT = 5;
    private static final double SPREAD_ANGLE = Math.toRadians(40);
    private static final double ARROW_SPEED = 450;
    private static final double ARROW_RANGE = 500;

    public SpreadShotAbility() {
        super("Spread Shot", "Fire 5 arrows in a fan", 8.0, AbilityType.OFFENSIVE);
        this.icon = loadIcon("/com/example/soulspire/images/spread.png");
    }

    /**
     * Exectuing SpreadShot ability.
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

        double dx = targetX - caster.getCenterX();
        double dy = targetY - caster.getCenterY();
        double dist = Math.hypot(dx, dy);
        if (dist < 0.001) {
            return;
        }

        double centerAngle = Math.atan2(dy, dx);
        double step = SPREAD_ANGLE / (ARROW_COUNT - 1);
        double startAngle = centerAngle - SPREAD_ANGLE / 2;
        int damage = caster.getAttackDamage();

        for (int i = 0; i < ARROW_COUNT; i++) {
            double angle = startAngle + step * i;
            double tx = caster.getCenterX() + Math.cos(angle) * 100;
            double ty = caster.getCenterY() + Math.sin(angle) * 100;

            Projectile arrow = new Projectile(caster.getCenterX() - 4, caster.getCenterY() - 4, tx, ty, ARROW_SPEED, damage, ARROW_RANGE, caster);
            arrow.setColor(javafx.scene.paint.Color.LIMEGREEN);
            caster.getCurrentFloor().addEntity(arrow);
        }

        resetCooldown();

    }
}