package com.example.soulspire.Util;

import com.example.soulspire.Core.GameConfig;
import com.example.soulspire.Entity.Chest;
import com.example.soulspire.Entity.Enemy.*;
import com.example.soulspire.Entity.Npc.Blacksmith;
import com.example.soulspire.Entity.Npc.Merchant;
import com.example.soulspire.Ability.AbilityType;
import com.example.soulspire.Item.SoulEcho;
import com.example.soulspire.World.Floor;
import com.example.soulspire.World.TileType;

import java.util.Random;

/**
 * Generates and populates floors with enemies, chests, and NPCs.
 * Currently uses procedural generation; can be extended to load from JSON files
 * as specified in the game design document.
 */
public class FloorLoader {

    private static final GameLogger logger = GameLogger.getLogger(FloorLoader.class);
    private static final Random random = new Random();

    private FloorLoader() {}

    /**
     * Populates a floor with entities based on its floor number.
     * Safe zone floors get NPCs; combat floors get enemies and chests.
     *
     * @param floor the floor to populate
     */
    public static void populateFloor(Floor floor) {
        if (floor.isSafeZone()) {
            populateSafeZone(floor);
        } else {
            populateCombatFloor(floor);
        }
        logger.info("Populated floor " + floor.getFloorNumber() +
                (floor.isSafeZone() ? " (safe zone)" : " (combat)") +
                " with " + floor.getEntities().size() + " entities");
    }

    /**
     * Adds NPCs to a safe zone floor.
     */
    private static void populateSafeZone(Floor floor) {
        int ts = GameConfig.TILE_SIZE;
        floor.addEntity(new Blacksmith(5 * ts, 5 * ts));
        floor.addEntity(new Merchant(8 * ts, 5 * ts));
    }

    /**
     * Adds enemies and a guarded chest to a combat floor.
     */
    private static void populateCombatFloor(Floor floor) {
        int ts = GameConfig.TILE_SIZE;
        int fn = floor.getFloorNumber();
        int enemyCount = 3 + fn;

        for (int i = 0; i < enemyCount; i++) {
            double ex = (3 + random.nextInt(floor.getWidthInTiles() - 6)) * ts;
            double ey = (3 + random.nextInt(floor.getHeightInTiles() - 6)) * ts;
            if (random.nextDouble() < 0.3) {
                floor.addEntity(new RangedEnemy(ex, ey, fn));
            } else {
                floor.addEntity(new MeleeEnemy(ex, ey, fn));
            }
        }

        double chestX = (floor.getWidthInTiles() - 4) * ts;
        double chestY = (floor.getHeightInTiles() - 4) * ts;
        SoulEcho reward = generateRandomSoulEcho();
        Chest chest = new Chest(chestX, chestY, reward);
        floor.addEntity(chest);
        floor.addEntity(new ChestGuardian(chestX - ts, chestY, fn, chest));
        floor.setTileAt(floor.getWidthInTiles() - 2, floor.getHeightInTiles() / 2, TileType.EXIT);

        if (fn == GameConfig.TOTAL_FLOORS - 1) {
            floor.addEntity(new BossEnemy(10 * ts, 7 * ts, fn));
        }
    }

    /**
     * Generates a random SoulEcho modifier for chest rewards.
     */
    private static SoulEcho generateRandomSoulEcho() {
        AbilityType[] types = AbilityType.values();
        AbilityType target = types[random.nextInt(types.length)];
        double value = 0.1 + random.nextDouble() * 0.3;
        String name = switch (target) {
            case OFFENSIVE -> "Fury Shard";
            case DEFENSIVE -> "Iron Ward";
            case MOBILITY -> "Wind Essence";
            case UTILITY -> "Arcane Focus";
        };
        return new SoulEcho(name, target.name() + " abilities +" + (int)(value * 100) + "%", value, target);
    }
}