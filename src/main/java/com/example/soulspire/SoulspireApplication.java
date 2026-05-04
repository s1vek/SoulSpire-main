package com.example.soulspire;

import com.example.soulspire.Core.*;
import com.example.soulspire.UI.CharacterSelectScreen;
import com.example.soulspire.UI.GameScreen;
import com.example.soulspire.UI.ScreenManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SoulspireApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        InputHandler inputHandler = new InputHandler();

        GameEngine engine = new GameEngine(inputHandler);

        StackPane root = new StackPane();

        GameScreen gameScreen = new GameScreen(engine);
        root.getChildren().add(gameScreen);

        ScreenManager screenManager =  new ScreenManager(root, engine);
        screenManager.initScreens();
        screenManager.showScreen(GameState.MAIN_MENU);

        Scene scene = new Scene(root, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);

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
