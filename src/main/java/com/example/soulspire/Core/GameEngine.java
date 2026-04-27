package com.example.soulspire.Core;

import com.example.soulspire.Combat.CollisionDetector;
import com.example.soulspire.Combat.CombatSystem;
import com.example.soulspire.Entity.Entity;
import com.example.soulspire.Entity.Enemy.Enemy;
import com.example.soulspire.Entity.Enemy.RangedEnemy;
import com.example.soulspire.Entity.LivingEntity;
import com.example.soulspire.Entity.Player.*;
import com.example.soulspire.Entity.Projectile;
import com.example.soulspire.Entity.Direction;
import com.example.soulspire.Util.GameLogger;
import com.example.soulspire.World.Floor;
import com.example.soulspire.World.Tower;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

/**
 * Central game engine that coordinates all game systems.
 * Handles the game loop cycle: input → update → collision → render.
 */
public class GameEngine {

    private static final GameLogger logger = GameLogger.getLogger(GameEngine.class);

    private Tower tower;
    private Player player;
    private GameStateManager stateManager;
    private CombatSystem combatSystem;
    private InputHandler inputHandler;
    private double cameraX;
    private double cameraY;

    public GameEngine(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        this.stateManager = new GameStateManager();
        this.combatSystem = new CombatSystem();
        this.tower = new Tower();
        this.cameraX = 0;
        this.cameraY = 0;
    }

    /**
     * Initializes a new game with the selected character type.
     *
     * @param type the chosen character class
     * @param name the player's name
     */
    public void initNewGame(PlayerType type, String name) {

        tower.generateFloors();

        Floor firstFloor = tower.getCurrentFloor();
        double spawnx = firstFloor.getSpawnX();
        double spawny = firstFloor.getSpawnY();

        this.player = switch (type) {
            case WARRIOR -> new Warrior(name, spawnx, spawny);
            case SHAMAN -> new Shaman(name, spawnx, spawny);
            case HUNTER -> new Hunter(name, spawnx, spawny);
            case MAGE -> new Mage(name, spawnx, spawny);
        };

        stateManager.setState(GameState.PLAYING);
        logger.info("New game started");

    }

    /**
     * Main update method called every frame by the GameLoop.
     */
    public void update(double deltaTime) {

    }

    /**
     * Processes keyboard input for player movement, attacks, and abilities.
     */
    private void handlePlayerInput(double deltaTime) {

    }

    /**
     * Attempts to move the player, checking tile collisions first.
     */
    private void tryMove(Direction dir, double deltaTime) {

    }

    /**
     * Checks all projectile-entity collisions on the current floor.
     */
    private void checkCollisions(Floor floor) {

    }

    /**
     * Centers the camera on the player.
     */
    private void updateCamera() {

    }

    /**
     * Handles player death: respawn or game over.
     */
    private void handlePlayerDeath() {

    }

    /**
     * Renders the current game state.
     */
    public void render(GraphicsContext gc) {

        gc.setFill(Color.BLACK);
        gc.fillRect(0,0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);

        if (player == null) {
            return;
        }

        Floor currentFloor = tower.getCurrentFloor();
        currentFloor.render(gc, cameraX, cameraY);
        player.render(gc, cameraX, cameraY);

    }

    // --- Getters for UI and other systems ---

    public Player getPlayer() { return player; }
    public Tower getTower() { return tower; }
    public GameStateManager getStateManager() { return stateManager; }
    public CombatSystem getCombatSystem() { return combatSystem; }
    public InputHandler getInputHandler() { return inputHandler; }
}