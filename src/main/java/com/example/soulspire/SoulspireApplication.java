package com.example.soulspire;

import com.example.soulspire.Core.*;
import com.example.soulspire.UI.CharacterSelectScreen;
import com.example.soulspire.UI.GameScreen;
import com.example.soulspire.UI.ScreenManager;
import com.example.soulspire.Util.SoundManager;
import com.example.soulspire.World.Tile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SoulspireApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Tile.loadTextures();

        InputHandler inputHandler = new InputHandler();

        GameEngine engine = new GameEngine(inputHandler);

        StackPane root = new StackPane();

        SoundManager soundManager = new SoundManager();
        soundManager.playMusic("/com/example/soulspire/sounds/SoulSpire.mp3");

        GameScreen gameScreen = new GameScreen(engine);
        engine.setHud(gameScreen.getHud());
        engine.setGameScreen(gameScreen);
        root.getChildren().add(gameScreen);

        ScreenManager screenManager =  new ScreenManager(root, engine);
        screenManager.initScreens();
        screenManager.showScreen(GameState.MAIN_MENU);

        engine.setScreenManager(screenManager);
        engine.setInventoryUI(gameScreen.getInventoryUI());

        Scene scene = new Scene(root, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        root.setFocusTraversable(true);
        root.requestFocus();
        scene.setOnMouseClicked(e -> root.requestFocus());

        inputHandler.registerHandlers(scene);

        stage.setTitle("SoulSpire");
        stage.setScene(scene);
        stage.show();
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        GameLoop loop = new GameLoop(engine, gameScreen.getGraphicsContext());
        loop.start();

    }
}
