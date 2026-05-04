package com.example.soulspire.Core;

public class GameConfig {

    private GameConfig() {
        // Prevent instantiation — utility class
    }

    // === Window ===

    /** Game window width in pixels. */
    public static final int WINDOW_WIDTH = 1280;

    /** Game window height in pixels. */
    public static final int WINDOW_HEIGHT = 720;

    /** Window title shown in the title bar. */
    public static final String WINDOW_TITLE = "Soulspire";

    // === Tile and map ===

    /** Size of one tile in pixels (tiles are square). */
    public static final int TILE_SIZE = 48;

    public static final int FLOOR_WIDTH_TILES = 60;
    public static final int FLOOR_HEIGHT_TILES = 40;

    // === Game rules ===

    /** Total number of floors in the tower. */
    public static final int TOTAL_FLOORS = 10;

    /** Number of lives the player starts with. */
    public static final int PLAYER_LIVES = 3;

    /**
     * Difficulty scaling per floor — enemy health and damage increase
     * by this fraction for each floor above the first.
     * Example: 0.15 means +15% per floor.
     */
    public static final double DIFFICULTY_SCALE = 0.15;

    /** Every Nth floor is a safe zone (value 2 = every other floor). */
    public static final int SAFE_ZONE_INTERVAL = 2;

    // === Timing ===

    /** Target frames per second. Used for delta time normalization. */
    public static final double TARGET_FPS = 60.0;

    // === Save ===

    /** Default file path for save data. */
    public static final String SAVE_FILE_PATH = "soulspire_save.json";

    // === Inventory ===

    /** Maximum number of items the player can carry. */
    public static final int INVENTORY_MAX_SIZE = 20;

    // === Interaction ===

    /** Default interaction range for Interactable entities (in pixels). */
    public static final double DEFAULT_INTERACTION_RANGE = 80.0;
}
