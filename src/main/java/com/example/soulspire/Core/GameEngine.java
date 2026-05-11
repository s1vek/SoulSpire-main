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
import com.example.soulspire.World.Tile;
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

        System.out.println("floor size:" + firstFloor.getWidthInTiles() + "x:" + firstFloor.getHeightInTiles());

    }

    /**
     * Main update method called every frame by the GameLoop.
     */
    public void update(double deltaTime) {
        if (stateManager.getState() != GameState.PLAYING) {
            return;
        }

        handlePlayerInput(deltaTime);
        updateCamera();

    }

    /**
     * Processes keyboard input for player movement, attacks, and abilities.
     */
    private void handlePlayerInput(double deltaTime) {
        if (inputHandler.isKeyPressed(KeyCode.W)) {
            tryMove(Direction.UP, deltaTime);
        }
        if (inputHandler.isKeyPressed(KeyCode.S)) {
            tryMove(Direction.DOWN, deltaTime);
        }
        if (inputHandler.isKeyPressed(KeyCode.A)) {
            tryMove(Direction.LEFT, deltaTime);
        }
        if (inputHandler.isKeyPressed(KeyCode.D)) {
            tryMove(Direction.RIGHT, deltaTime);
        }

    }

    /**
     * Attempts to move the player, checking tile collisions first.
     */
    private void tryMove(Direction dir, double deltaTime) {

        double distance = player.getMoveSpeed() * deltaTime;

        double newX = player.getX();
        double newY = player.getY();

        switch (dir) {
            case UP    -> newY -= distance;
            case DOWN  -> newY += distance;
            case LEFT  -> newX -= distance;
            case RIGHT -> newX += distance;
        }

        Floor floor = tower.getCurrentFloor();
        double centerX = newX + player.getWidth()  / 2.0;
        double centerY = newY + player.getHeight() / 2.0;
        Tile target = floor.getTileAtPixel(centerX, centerY);

        if (target != null && target.isWalkable()) {
            player.setX(newX);
            player.setY(newY);
        }

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
        if (player == null) {
            return;
        }

        double viewW = GameConfig.WINDOW_WIDTH;
        double viewH = GameConfig.WINDOW_HEIGHT;

        cameraX = player.getCenterX() - viewW / 2.0;
        cameraY = player.getCenterY() - viewH / 2.0;

        Floor floor = tower.getCurrentFloor();
        double worldW = floor.getWidthInTiles() * GameConfig.TILE_SIZE;
        double worldH = floor.getHeightInTiles() * GameConfig.TILE_SIZE;

        if (cameraX < 0) {
            cameraX = 0;
        }

        if (cameraY < 0) {
            cameraY = 0;
        }

        if (cameraX > worldW - viewW) {
            cameraX = worldW - viewW;
        }

        if (cameraY > worldH - viewH){
            cameraY = worldH - viewH;
        }

        System.out.println("camera=(" + cameraX + "," + cameraY + "player:" + player.getCenterX() + "," + player.getCenterY());
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
        gc.fillRect(0,0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        if (player == null) {
            return;
        }

        Floor currentFloor = tower.getCurrentFloor();
        currentFloor.render(gc, cameraX, cameraY);
        player.render(gc, cameraX, cameraY);

    }

    public Player getPlayer() { return player; }
    public Tower getTower() { return tower; }
    public GameStateManager getStateManager() { return stateManager; }
    public CombatSystem getCombatSystem() { return combatSystem; }
    public InputHandler getInputHandler() { return inputHandler; }
}