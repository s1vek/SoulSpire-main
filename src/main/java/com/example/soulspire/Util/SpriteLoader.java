package com.example.soulspire.Util;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * Loads and caches sprite images from the resources folder.
 * Uses a static cache to prevent loading the same image twice.
 */
public class SpriteLoader {

    private static final GameLogger logger = GameLogger.getLogger(SpriteLoader.class);
    private static final Map<String, Image> cache = new HashMap<>();

    private SpriteLoader() {}

    /**
     * Loads a sprite image from the resources path. Returns the cached version
     * if the same path was loaded before.
     *
     * @param path resource path (e.g. "/sprites/player/warrior.png")
     * @return the loaded Image, or null if loading fails
     */
    public static Image loadSprite(String path) {
        if (cache.containsKey(path)) {
            return cache.get(path);
        }
        try {
            var stream = SpriteLoader.class.getResourceAsStream(path);
            if (stream == null) {
                logger.warn("Sprite not found: " + path);
                return null;
            }
            Image image = new Image(stream);
            cache.put(path, image);
            logger.info("Loaded sprite: " + path);
            return image;
        } catch (Exception e) {
            logger.error("Failed to load sprite: " + path, e);
            return null;
        }
    }

    /**
     * Splits a sprite sheet into individual frames.
     *
     * @param path    resource path to the sprite sheet
     * @param frameW  width of each frame in pixels
     * @param frameH  height of each frame in pixels
     * @return array of frame images, or empty array on failure
     */
    public static Image[] loadSpriteSheet(String path, int frameW, int frameH) {
        Image sheet = loadSprite(path);
        if (sheet == null) return new Image[0];

        int cols = (int)(sheet.getWidth() / frameW);
        int rows = (int)(sheet.getHeight() / frameH);
        Image[] frames = new Image[cols * rows];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                javafx.scene.image.WritableImage frame =
                        new javafx.scene.image.WritableImage(
                                sheet.getPixelReader(),
                                col * frameW, row * frameH, frameW, frameH
                        );
                frames[row * cols + col] = frame;
            }
        }
        logger.info("Loaded sprite sheet: " + path + " (" + frames.length + " frames)");
        return frames;
    }

    /**
     * Clears all cached sprites from memory.
     */
    public static void clearCache() {
        cache.clear();
        logger.info("Sprite cache cleared");
    }
}