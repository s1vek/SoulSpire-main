package com.example.soulspire.Core;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

/**
 * Tracks keyboard and mouse input state each frame.
 * Registers event handlers on the JavaFX Scene and maintains
 * a set of currently pressed keys.
 */
public class InputHandler {

    private final Set<KeyCode> pressedKeys;
    private final Set<KeyCode> justPressedKeys;
    private double mouseX;
    private double mouseY;

    public InputHandler() {
        this.pressedKeys = new HashSet<>();
        this.justPressedKeys = new HashSet<>();
        this.mouseX = 0;
        this.mouseY = 0;
    }

    /**
     * Registers key and mouse event handlers on the given scene.
     * Must be called once after the scene is created.
     *
     * @param scene the JavaFX scene to attach handlers to
     */
    public void registerHandlers(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (!pressedKeys.contains(e.getCode())) {
                justPressedKeys.add(e.getCode());
            }
            pressedKeys.add(e.getCode());
        });
        scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
        scene.setOnMouseMoved(e -> { mouseX = e.getX(); mouseY = e.getY(); });
        scene.setOnMouseDragged(e -> { mouseX = e.getX(); mouseY = e.getY(); });
    }

    /**
     * @return true if the key is currently held down
     */
    public boolean isKeyPressed(KeyCode key) {
        return pressedKeys.contains(key);
    }

    /**
     * @return true only on the first frame the key is pressed (not held)
     */
    public boolean isKeyJustPressed(KeyCode key) {
        return justPressedKeys.contains(key);
    }

    /**
     * Clears the just-pressed set. Call at the end of each frame.
     */
    public void update() {
        justPressedKeys.clear();
    }
    public Point2D getMousePosition() { return new Point2D(mouseX, mouseY); }
    public double getMouseX() { return mouseX; }
    public double getMouseY() { return mouseY; }
}