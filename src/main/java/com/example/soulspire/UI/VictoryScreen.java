package com.example.soulspire.UI;

import com.example.soulspire.Core.GameEngine;
import com.example.soulspire.Core.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Screen displayed when the player defeats the final boss and completes the tower.
 */
public class VictoryScreen extends VBox {

    private final Label timeLabel;

    public VictoryScreen(GameEngine engine, ScreenManager screenManager) {
        setAlignment(Pos.CENTER);
        setSpacing(20);
        setPadding(new Insets(40));
        setStyle("-fx-background-color: rgba(0,0,0,0.9);");

        Label title = new Label("VICTORY!");
        title.setStyle("-fx-text-fill: gold; -fx-font-size: 48;  -fx-font-family: 'MedievalSharp';");

        Label subtitle = new Label("You have conquered the Soulspire!");
        subtitle.setStyle("-fx-text-fill: #ccc; -fx-font-size: 16; -fx-font-family: 'MedievalSharp';");

        timeLabel = new Label("Time: 00:00");
        timeLabel.setStyle("-fx-text-fill: gold; -fx-font-size: 20;  -fx-font-family: 'MedievalSharp';");

        Button menuBtn = new Button("Return to Menu");
        menuBtn.setPrefWidth(200);
        menuBtn.setOnAction(e -> {
            engine.getStateManager().setState(GameState.MAIN_MENU);
            screenManager.showScreen(GameState.MAIN_MENU);
        });

        getChildren().addAll(title, subtitle, timeLabel, menuBtn);
    }

    /**
     * Updates the displayed time. Called by ScreenManager before showing.
     */
    public void setFinalTime(String formattedTime) {
        timeLabel.setText("Final Time: " + formattedTime);
    }
}