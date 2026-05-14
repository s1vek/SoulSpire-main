package com.example.soulspire.Core;
import com.example.soulspire.Combat.CombatSystem;
import com.example.soulspire.Crafting.CraftingSystem;
import com.example.soulspire.Entity.Entity;
import com.example.soulspire.Entity.Npc.Blacksmith;
import com.example.soulspire.Entity.Player.*;
import com.example.soulspire.Entity.Direction;
import com.example.soulspire.UI.*;
import com.example.soulspire.Util.GameLogger;
import com.example.soulspire.World.Floor;
import com.example.soulspire.World.Tile;
import com.example.soulspire.World.TileType;
import com.example.soulspire.World.Tower;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import com.example.soulspire.UI.CraftingUI;

import java.util.Map;


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
    private HUDOverlay hud;
    private double cameraX;
    private double cameraY;
    private static final double VISION_RADIUS = 400;
    private ScreenManager screenManager;
    private InventoryUI inventoryUI;
    private GameScreen gameScreen;
    private CraftingSystem craftingSystem;
    private CraftingUI craftingUI;

    public GameEngine(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        this.stateManager = new GameStateManager();
        this.combatSystem = new CombatSystem();
        this.tower = new Tower();
        this.cameraX = 0;
        this.cameraY = 0;
        this.craftingSystem = new CraftingSystem();
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

        player.setCurrentFloor(firstFloor);
        player.setCombatSystem(combatSystem);
        stateManager.setState(GameState.PLAYING);

        if (gameScreen != null && inventoryUI == null) {
            inventoryUI = new InventoryUI(player.getInventory());
            gameScreen.attachInventoryUI(inventoryUI);
            craftingUI = new CraftingUI(craftingSystem, player.getInventory());
            gameScreen.attachCraftingUI(craftingUI);
        }

        logger.info("New game started");
    }

    private void checkFloorTransition() {
        if (player == null) {
            return;
        }
        Floor current = tower.getCurrentFloor();
        Tile tile = current.getTileAtPixel(player.getCenterX(), player.getCenterY());
        if (tile == null || tile.getType() != TileType.EXIT) {
            return;
        }

        if (tower.advanceFloor()) {
            Floor next = tower.getCurrentFloor();
            player.setCurrentFloor(next);
            player.setX(next.getSpawnX());
            player.setY(next.getSpawnY());
            logger.info("Advanced to floor " + tower.getCurrentFloorNumber());
        } else {
            logger.info("VICTORY — final floor cleared");
            stateManager.setState(GameState.VICTORY);
        }
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

        if (inputHandler.isKeyJustPressed(KeyCode.ESCAPE) && screenManager != null) {
            stateManager.togglePause();
            screenManager.showScreen(stateManager.getState());
        }

        if (inputHandler.isKeyJustPressed(KeyCode.I) && inventoryUI != null) {
            inventoryUI.setVisible(!inventoryUI.isVisible());
            if (inventoryUI.isVisible()) inventoryUI.refresh();
        }

        if (player == null) {
            return;
        }

        double mouseWorldX = inputHandler.getMouseX() + cameraX;
        double mouseWorldY = inputHandler.getMouseY() + cameraY;

        if (inputHandler.isKeyPressed(KeyCode.SPACE)) {
            player.attack(mouseWorldX, mouseWorldY);
        }
        if (inputHandler.isKeyPressed(KeyCode.DIGIT1)) {
            player.useAbility(0, mouseWorldX, mouseWorldY);
        }
        if (inputHandler.isKeyPressed(KeyCode.DIGIT2)) {
            player.useAbility(1, mouseWorldX, mouseWorldY);
        }
        if (inputHandler.isKeyPressed(KeyCode.DIGIT3)) {
            player.useAbility(2, mouseWorldX, mouseWorldY);
        }

        if (inputHandler.isKeyJustPressed(KeyCode.E) && craftingUI != null) {
            Blacksmith bs = findNearbyBlacksmith();
            if (bs != null) {
                craftingUI.setVisible(!craftingUI.isVisible());
                if (craftingUI.isVisible()) craftingUI.refresh();
            }
        }
    }

    private Blacksmith findNearbyBlacksmith() {
        if (player == null) {
            return null;
        }
        for (Entity e : tower.getCurrentFloor().getEntities()) {
            if (e instanceof Blacksmith bs) {
                double dx = bs.getCenterX() - player.getCenterX();
                double dy = bs.getCenterY() - player.getCenterY();
                if (dx * dx + dy * dy <= 80 * 80) {
                    return bs;
                }
            }
        }
        return null;
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
     * Main update method called every frame by the GameLoop.
     */
    public void update(double deltaTime) {
        if (stateManager.getState() != GameState.PLAYING) {
            inputHandler.update();
            return;
        }

        handlePlayerInput(deltaTime);

        Floor currFloor = tower.getCurrentFloor();
        currFloor.update(deltaTime, player);

        if (player != null) {
            player.update(deltaTime);
        }

        if (hud != null) {
            hud.update();
        }

        checkFloorTransition();
        inputHandler.update();

    }

    /**
     * Centers the camera on the player.
     */
    private void updateCamera(double viewW, double viewH) {
        if (player == null) {
            return;
        }

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
        double viewW = gc.getCanvas().getWidth();
        double viewH = gc.getCanvas().getHeight();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, viewW, viewH);

        if (player == null) {
            return;
        }

        updateCamera(viewW, viewH);

        Floor currentFloor = tower.getCurrentFloor();
        currentFloor.render(gc, cameraX, cameraY, viewW, viewH);
        player.render(gc, cameraX, cameraY);
        drawAimIndicator(gc);
        
        drawFog(gc, viewW, viewH);
    }

    /**
     * In-game effect of fog.
     * @param gc
     * @param viewW
     * @param viewH
     */
    private void drawFog(GraphicsContext gc, double viewW, double viewH) {
        double px = player.getCenterX() - cameraX;
        double py = player.getCenterY() - cameraY;

        javafx.scene.paint.RadialGradient grad = new javafx.scene.paint.RadialGradient(0, 0, px, py, VISION_RADIUS, false, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new javafx.scene.paint.Stop(0.99, Color.TRANSPARENT),
                new javafx.scene.paint.Stop(1.0,  Color.BLACK)
        );

        gc.setFill(grad);
        gc.fillRect(0, 0, viewW, viewH);
    }

    /**
     * Rendering Aim indicator for direction facing.
     * @param gc
     */
    private void drawAimIndicator(GraphicsContext gc) {
        double px = player.getCenterX() - cameraX;
        double py = player.getCenterY() - cameraY;
        double mx = inputHandler.getMouseX();
        double my = inputHandler.getMouseY();

        double dx = mx - px;
        double dy = my - py;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist < 0.001) return;

        double dirX = dx / dist;
        double dirY = dy / dist;

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeLine(px, py, px + dirX * 25, py + dirY * 25);
    }


    public void loadGame() {
        Map<String, Object> data = SaveManager.load();
        if (data == null) {
            logger.warn("No save data to load");
            return;
        }

        Map<String, Object> playerData = (Map<String, Object>) data.get("player");
        Map<String, Object> towerData = (Map<String, Object>) data.get("tower");

        tower.generateFloors();
        tower.loadSaveData(towerData);

        PlayerType type = PlayerType.valueOf((String) playerData.get("type"));
        String name = (String) playerData.get("name");

        this.player = switch (type) {
            case WARRIOR -> new Warrior(name, 0, 0);
            case SHAMAN  -> new Shaman(name, 0, 0);
            case HUNTER  -> new Hunter(name, 0, 0);
            case MAGE    -> new Mage(name, 0, 0);
        };
        player.loadSaveData(playerData);
        player.setCurrentFloor(tower.getCurrentFloor());
        player.setCombatSystem(combatSystem);

        if (gameScreen != null) {
            inventoryUI = new InventoryUI(player.getInventory());
            gameScreen.attachInventoryUI(inventoryUI);
            craftingUI = new CraftingUI(craftingSystem, player.getInventory());
            gameScreen.attachCraftingUI(craftingUI);
        }

        stateManager.setState(GameState.PLAYING);
        logger.info("Game loaded");
    }

    /**
     * Getters and setters.
     */
    public Player getPlayer() { return player; }
    public Tower getTower() { return tower; }
    public GameStateManager getStateManager() { return stateManager; }
    public CombatSystem getCombatSystem() { return combatSystem; }
    public InputHandler getInputHandler() { return inputHandler; }
    public void setHud(HUDOverlay hud) { this.hud = hud; }
    public void setScreenManager(ScreenManager sm) { this.screenManager = sm; }
    public void setInventoryUI(InventoryUI ui) { this.inventoryUI = ui; }
    public void setGameScreen(GameScreen gs) { this.gameScreen = gs; }
}