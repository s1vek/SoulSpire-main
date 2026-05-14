package com.example.soulspire.UI;

import com.example.soulspire.Core.GameEngine;
import com.example.soulspire.Core.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Screen displayed when the player loses all lives.
 */
public class GameOverScreen extends VBox {

    public GameOverScreen(GameEngine engine, ScreenManager screenManager) {
        setAlignment(Pos.CENTER);
        setSpacing(20);
        setPadding(new Insets(40));
        setStyle("-fx-background-color: rgba(0,0,0,0.9);");

        Label title = new Label("GAME OVER");
        title.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 48; -fx-font-family: 'MedievalSharp';");

        Label subtitle = new Label("The tower has claimed another soul...");
        subtitle.setStyle("-fx-text-fill: #aaa; -fx-font-size: 16; -fx-font-family: 'MedievalSharp';");

        Button menuBtn = new Button("Return to Menu");
        menuBtn.setPrefWidth(200);
        menuBtn.setOnAction(e -> {
            engine.getStateManager().setState(GameState.MAIN_MENU);
            screenManager.showScreen(GameState.MAIN_MENU);
        });
        menuBtn.setFocusTraversable(false);

        getChildren().addAll(title, subtitle, menuBtn);
    }
}