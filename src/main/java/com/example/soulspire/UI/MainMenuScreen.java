package com.example.soulspire.UI;

import com.example.soulspire.Core.GameEngine;
import com.example.soulspire.Core.GameState;
import com.example.soulspire.Core.SaveManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Main menu screen shown at game start.
 * Provides options to start a new game, load a save, or quit.
 */
public class MainMenuScreen extends VBox {

    public MainMenuScreen(GameEngine engine, ScreenManager screenManager) {

        setAlignment(Pos.CENTER);
        setSpacing(20);
        setPadding(new Insets(40));
        setStyle("-fx-background-color: rgb(0, 0, 0);");

        Font medievalFont = Font.loadFont(getClass().getResourceAsStream("/com/example/soulspire/fonts/MedievalSharp-Regular.ttf"), 1);

        Label title = new Label("SOULSPIRE");
        title.setStyle("-fx-font-family: 'MedievalSharp'; -fx-font-size: 48; -fx-text-fill: white;");

        Label subtitle = new Label("Conquer the Tower");
        subtitle.setStyle("-fx-font-family: 'MedievalSharp'; -fx-font-size: 16; -fx-text-fill: white;");

        Button newGameBtn = new Button("New Game");
        newGameBtn.setStyle("-fx-font-family: 'MedievalSharp'; -fx-font-size: 16; -fx-text-fill: black;");
        newGameBtn.setPrefWidth(200);
        newGameBtn.setOnAction(e -> {
            engine.getStateManager().setState(GameState.CHARACTER_SELECT);
            screenManager.showScreen(GameState.CHARACTER_SELECT);
        });
        newGameBtn.setFocusTraversable(false);

        Button loadBtn = new Button("Load Game");
        loadBtn.setStyle("-fx-font-family: 'MedievalSharp'; -fx-font-size: 16; -fx-text-fill: black;");
        loadBtn.setPrefWidth(200);
        loadBtn.setDisable(!SaveManager.hasSaveFile());
        loadBtn.setOnAction(e -> {
            engine.loadGame();
            screenManager.showScreen(com.example.soulspire.Core.GameState.PLAYING);
        });
        loadBtn.setFocusTraversable(false);


        Button quitBtn = new Button("Quit");
        quitBtn.setStyle("-fx-font-family: 'MedievalSharp'; -fx-font-size: 16; -fx-text-fill: black;");

        quitBtn.setPrefWidth(200);
        quitBtn.setOnAction(e -> System.exit(0));
        quitBtn.setFocusTraversable(false);

        getChildren().addAll(title, subtitle, newGameBtn, loadBtn, quitBtn);
    }
}