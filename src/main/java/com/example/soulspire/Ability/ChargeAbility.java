package com.example.soulspire.Ability;
import com.example.soulspire.Combat.CombatSystem;
import com.example.soulspire.Core.GameConfig;
import com.example.soulspire.Entity.Enemy.Enemy;
import com.example.soulspire.Entity.Entity;
import com.example.soulspire.Entity.Player.Player;
import com.example.soulspire.World.Floor;
import com.example.soulspire.World.Tile;
import com.example.soulspire.World.TileType;

/**
 * Warrior ability 1: Dashes forward and stuns the first enemy hit.
 * Can break destructible walls.
 */

public class ChargeAbility extends Ability {

    private static final double CHARGE_DISTANCE = 200;
    private static final double STUN_DURATION = 2.0;
    private static final double PATH_HIT_RADIUS = 30;
    private static final double STEP_SIZE = 8;

    public ChargeAbility() {
        super("Charge", "Dash forward and stun the first enemy", 8.0, AbilityType.MOBILITY);
        this.icon = loadIcon("/com/example/soulspire/images/charge.png");
    }

    /**
     * Exectuing Charge ability.
     * @param caster  the player using this ability
     * @param targetX mouse X position in world coordinates
     * @param targetY mouse Y position in world coordinates
     */
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

        double dx = targetX - caster.getCenterX();
        double dy = targetY - caster.getCenterY();
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

        int maxSteps = (int) (CHARGE_DISTANCE / STEP_SIZE);
        for (int i = 1; i <= maxSteps; i++) {
            double checkX = startX + dirX * i * STEP_SIZE;
            double checkY = startY + dirY * i * STEP_SIZE;
            double centerX = checkX + caster.getWidth() / 2;
            double centerY = checkY + caster.getHeight() / 2;
            Tile tile = floor.getTileAtPixel(centerX, centerY);
            if (tile == null || !tile.isWalkable()) {
                break;
            }

            if (tile.getType() == TileType.DESTRUCTIBLE_WALL) {
                int gridX = (int) (centerX / GameConfig.TILE_SIZE);
                int gridY = (int) (centerY / GameConfig.TILE_SIZE);
                floor.setTileAt(gridX, gridY, TileType.FLOOR);
            } else if (!tile.isWalkable()) {
                break;
            }

            finalX = checkX;
            finalY = checkY;
        }

        double traveled = Math.sqrt(Math.pow(finalX - startX, 2) + Math.pow(finalY - startY, 2));
        int damage = caster.getAttackDamage();
        double startCX = caster.getCenterX();
        double startCY = caster.getCenterY();

        for (Entity e : floor.getEntities()) {
            if (!(e instanceof Enemy enemy)){
                continue;
            }
            if (!enemy.isActive()){
                continue;
            }

            double ex = enemy.getCenterX() - startCX;
            double ey = enemy.getCenterY() - startCY;
            double along = ex * dirX + ey * dirY;
            if (along < 0 || along > traveled) {
                continue;
            }

            double perpX = ex - dirX * along;
            double perpY = ey - dirY * along;
            if (perpX * perpX + perpY * perpY <= PATH_HIT_RADIUS * PATH_HIT_RADIUS) {
                enemy.takeDamage(damage);
                enemy.applyStun(STUN_DURATION);
            }
        }
        caster.setX(finalX);
        caster.setY(finalY);

        resetCooldown();

    }
}