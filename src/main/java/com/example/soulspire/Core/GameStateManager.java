package com.example.soulspire.Core;

import com.example.soulspire.Util.GameLogger;

/**
 * Manages the current {@link GameState} and transitions between states.
 * Controls which systems are active and which UI screens are shown.
 */
public class GameStateManager {

    private static final GameLogger logger = GameLogger.getLogger(GameStateManager.class);

    private GameState currentState;
    private GameState previousState;

    public GameStateManager() {
        this.currentState = GameState.MAIN_MENU;
        this.previousState = GameState.MAIN_MENU;
    }

    /**
     * Changes the game state. Stores the previous state for unpause.
     *
     * @param newState the state to transition to
     */
    public void setState(GameState newState) {
        this.previousState = this.currentState;
        this.currentState = newState;
        logger.info("State changed: " + previousState + " -> " + currentState);
    }

    /**
     * Toggles between PLAYING and PAUSED states.
     */
    public void togglePause() {
        if (currentState == GameState.PLAYING || currentState == GameState.SAFE_ZONE) {
            setState(GameState.PAUSED);
        } else if (currentState == GameState.PAUSED) {
            setState(previousState);
        }
    }

    /**
     * Getters and setters.
     */
    public GameState getState() { return currentState; }
    public GameState getPreviousState() { return previousState; }
    public boolean isPlaying() { return currentState == GameState.PLAYING; }
    public boolean isPaused() { return currentState == GameState.PAUSED; }
    public boolean isInSafeZone() { return currentState == GameState.SAFE_ZONE; }
}