package com.example.soulspire.Util;

import com.example.soulspire.Ability.AbilityType;
import com.example.soulspire.Core.GameConfig;
import com.example.soulspire.Entity.Chest;
import com.example.soulspire.Entity.Enemy.BossEnemy;
import com.example.soulspire.Entity.Enemy.ChestGuardian;
import com.example.soulspire.Entity.Enemy.MeleeEnemy;
import com.example.soulspire.Entity.Enemy.RangedEnemy;
import com.example.soulspire.Entity.Npc.Blacksmith;
import com.example.soulspire.Entity.Npc.Merchant;
import com.example.soulspire.Item.SoulEcho;
import com.example.soulspire.World.Floor;
import com.example.soulspire.World.TileType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FloorParser {

    private static final GameLogger logger = GameLogger.getLogger(FloorParser.class);
    private static final Random random = new Random();

    public FloorParser() {
    }

    public static Floor loadFloor(int floorNumber, boolean isSafeZone) {
        String resourcePath = "/floors/floor_" + floorNumber + ".txt";
        try (InputStream is = FloorParser.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                logger.info("No layout file for floor " + floorNumber
                        + " (looked for " + resourcePath + ") — using fallback");
                return null;
            }
            List<String> lines = readLines(is);
            return parse(lines, floorNumber, isSafeZone);
        } catch (IOException e) {
            logger.error("Failed to read floor " + floorNumber, e);
            return null;
        }
    }

    private static List<String> readLines(InputStream is) throws IOException {
        List<String> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                out.add(line);
            }
        }
        return out;
    }

    private static Floor parse(List<String> lines, int floorNumber, boolean isSafeZone) {
        int w = GameConfig.FLOOR_WIDTH_TILES;
        int h = GameConfig.FLOOR_HEIGHT_TILES;
        int ts = GameConfig.TILE_SIZE;
        Floor floor = new Floor(floorNumber, w, h, isSafeZone);

        for (int y = 0; y < h; y++) {
            String row = y < lines.size() ? lines.get(y) : "";
            for (int x = 0; x < w; x++) {
                char c = x < row.length() ? row.charAt(x) : '.';
                applyChar(floor, x, y, c, floorNumber, ts);
            }
        }
        logger.info("Loaded floor " + floorNumber + " from resource: "
                + floor.getEntities().size() + " entities");
        return floor;
    }

    private static void applyChar(Floor floor, int x, int y, char c, int fn, int ts) {
        double px = x * ts;
        double py = y * ts;

        switch (c) {
            case '.' -> floor.setTileAt(x, y, TileType.FLOOR);
            case '#' -> floor.setTileAt(x, y, TileType.WALL);
            case '=' -> floor.setTileAt(x, y, TileType.SAFE_ZONE);
            case 'X' -> floor.setTileAt(x, y, TileType.EXIT);
            case 'D' -> floor.setTileAt(x, y, TileType.DOOR);
            case 'T' -> floor.setTileAt(x, y, TileType.TRAP);
            case 'L' -> floor.setTileAt(x, y, TileType.LEVER);
            case 'B' -> floor.setTileAt(x, y, TileType.DESTRUCTIBLE_WALL);

            case '@' -> {
                floor.setTileAt(x, y, TileType.FLOOR);
                floor.setSpawnPosition(px, py);
            }

            case 'm' -> {
                floor.setTileAt(x, y, defaultFloorTile(floor));
                floor.addEntity(new MeleeEnemy(px, py, fn));
            }
            case 'r' -> {
                floor.setTileAt(x, y, defaultFloorTile(floor));
                floor.addEntity(new RangedEnemy(px, py, fn));
            }
            case 'G' -> {
                floor.setTileAt(x, y, TileType.FLOOR);
                Chest chest = new Chest(px + ts, py, randomSoulEcho());
                floor.addEntity(chest);
                floor.addEntity(new ChestGuardian(px, py, fn, chest));
            }
            case 'c' -> {
                floor.setTileAt(x, y, TileType.CHEST_SPOT);
                Chest chest = new Chest(px, py, randomSoulEcho());
                chest.setGuardianDefeated(true);
                floor.addEntity(chest);
            }
            case 'S' -> {
                floor.setTileAt(x, y, defaultFloorTile(floor));
                floor.addEntity(new Blacksmith(px, py));
            }
            case '$' -> {
                floor.setTileAt(x, y, defaultFloorTile(floor));
                floor.addEntity(new Merchant(px, py));
            }
            case 'K' -> {
                floor.setTileAt(x, y, TileType.FLOOR);
                floor.addEntity(new BossEnemy(px, py, fn));
            }

            default -> {
                floor.setTileAt(x, y, TileType.FLOOR);
                if (!Character.isWhitespace(c)) {
                    logger.warn("Unknown char '" + c + "' at (" + x + "," + y + ")");
                }

            }
        }
    }

    private static TileType defaultFloorTile(Floor floor) {
        return floor.isSafeZone() ? TileType.SAFE_ZONE : TileType.FLOOR;
    }

    private static SoulEcho randomSoulEcho() {
        AbilityType[] types = AbilityType.values();
        AbilityType target = types[random.nextInt(types.length)];
        double value = 0.1 + random.nextDouble() * 0.3;
        String name = switch (target) {
            case OFFENSIVE -> "Fury Shard";
            case DEFENSIVE -> "Iron Ward";
            case MOBILITY  -> "Wind Essence";
            case UTILITY   -> "Arcane Focus";
        };
        return new SoulEcho(name,
                target.name() + " abilities +" + (int)(value * 100) + "%",
                value, target);
    }

}
