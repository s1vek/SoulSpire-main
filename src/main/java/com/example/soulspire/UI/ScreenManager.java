package com.example.soulspire.UI;

import com.example.soulspire.Core.GameEngine;
import com.example.soulspire.Core.GameState;
import javafx.scene.layout.StackPane;

/**
 * Manages switching between different UI screens based on the current {@link GameState}.
 * Overlays screens on top of the game canvas.
 */

public class ScreenManager {

    private final StackPane root;
    private final GameEngine engine;

    private MainMenuScreen mainMenu;
    private CharacterSelectScreen characterSelect;
    private PauseMenu pauseMenu;
    private GameOverScreen gameOver;
    private VictoryScreen victory;

    public ScreenManager(StackPane root, GameEngine engine) {
        this.root = root;
        this.engine = engine;
    }

    /**
     * Initializes all screens. Call once after construction.
     */
    public void initScreens() {
        this.mainMenu        = new MainMenuScreen(engine, this);
        this.characterSelect = new CharacterSelectScreen(engine, this);
        this.pauseMenu       = new PauseMenu(engine, this);
        this.gameOver        = new GameOverScreen(engine, this);
        this.victory         = new VictoryScreen(engine, this);
    }

    /**
     * Shows the appropriate screen overlay for the given game state.
     */
    public void showScreen(GameState state) {

        hideAll();

        switch (state) {
            case MAIN_MENU         -> root.getChildren().add(mainMenu);
            case CHARACTER_SELECT  -> root.getChildren().add(characterSelect);
            case PAUSED            -> root.getChildren().add(pauseMenu);
            case GAME_OVER         -> root.getChildren().add(gameOver);
            case VICTORY           -> root.getChildren().add(victory);
            case PLAYING, SAFE_ZONE -> { }
        }

    }

    /**
     * Removes all screen overlays.
     */
    public void hideAll() {

        if (root.getChildren().size() > 1) {
            root.getChildren().remove(1, root.getChildren().size());
        }

    }
}