package com.example.soulspire.UI;

import com.example.soulspire.Core.GameEngine;
import com.example.soulspire.Core.GameState;
import com.example.soulspire.Util.GameLogger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Pause menu overlay with resume, save, toggle logging, and quit options.
 */
public class PauseMenu extends VBox {

    public PauseMenu(GameEngine engine, ScreenManager screenManager) {
        setAlignment(Pos.CENTER);
        setSpacing(15);
        setPadding(new Insets(40));
        setStyle("-fx-background-color: rgba(0,0,0,0.7);");

        Label title = new Label("PAUSED");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 32;");

        Button resumeBtn = new Button("Resume");
        resumeBtn.setPrefWidth(180);
        resumeBtn.setOnAction(e -> {
            engine.getStateManager().togglePause();
            screenManager.hideAll();
        });
        resumeBtn.setFocusTraversable(false);

        Button saveBtn = new Button("Save Game");
        saveBtn.setPrefWidth(180);
        saveBtn.setOnAction(e -> {
            var task = com.example.soulspire.Core.SaveManager.createAutoSaveTask(
                    engine.getPlayer(), engine.getTower()
            );
            new Thread(task).start();
        });
        saveBtn.setFocusTraversable(false);


        CheckBox logToggle = new CheckBox("Enable Logging");
        logToggle.setSelected(GameLogger.isEnabled());
        logToggle.setStyle("-fx-text-fill: white;");
        logToggle.setOnAction(e -> GameLogger.setEnabled(logToggle.isSelected()));

        Button quitBtn = new Button("Quit to Menu");
        quitBtn.setPrefWidth(180);
        quitBtn.setOnAction(e -> {
            engine.getStateManager().setState(GameState.MAIN_MENU);
            screenManager.showScreen(GameState.MAIN_MENU);
        });
        quitBtn.setFocusTraversable(false);

        getChildren().addAll(title, resumeBtn, saveBtn, logToggle, quitBtn);
    }
}