package com.example.soulspire.Ability;

import com.example.soulspire.Ability.Ability;
import com.example.soulspire.Ability.AbilityType;
import com.example.soulspire.Entity.Direction;
import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.World.Floor;
import com.example.soulspire.World.Tile;


/**
 * Hunter ability 3: Quick leap backwards to create distance from enemies.
 */
public class LeapAbility extends Ability {

    private static final double LEAP_DISTANCE = 120;
    private static final double STEP_SIZE = 8;

    public LeapAbility() {
        super("Leap", "Dodge backwards to create distance", 5.0, AbilityType.MOBILITY);
        this.icon = loadIcon("/com/example/soulspire/images/leap.png");
    }

    @Override
    public void execute(Player caster, double targetX, double targetY) {
        if (currentCooldown > 0) {
            return;
        }

        Floor floor = caster.getCurrentFloor();
        if (floor == null) {
            return;
        }

        double dx = caster.getCenterX() - targetX;
        double dy = caster.getCenterY() - targetY;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist < 0.001) {
            return;
        }
        double dirX = dx / dist;
        double dirY = dy / dist;

        double startX = caster.getX();
        double startY = caster.getY();
        double finalX = startX;
        double finalY = startY;

        int maxSteps = (int) (LEAP_DISTANCE / STEP_SIZE);
        for (int i = 1; i <= maxSteps; i++) {
            double checkX = startX + dirX * i * STEP_SIZE;
            double checkY = startY + dirY * i * STEP_SIZE;
            double centerX = checkX + caster.getWidth() / 2;
            double centerY = checkY + caster.getHeight() / 2;
            Tile tile = floor.getTileAtPixel(centerX, centerY);
            if (tile == null || !tile.isWalkable()) break;
            finalX = checkX;
            finalY = checkY;
        }

        caster.setX(finalX);
        caster.setY(finalY);

        resetCooldown();
    }
}