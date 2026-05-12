package com.example.soulspire.UI;

import com.example.soulspire.Ability.Ability;
import com.example.soulspire.Core.GameEngine;
import com.example.soulspire.Entity.Player.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.awt.*;

/**
 * Heads-up display overlay showing player health, lives, floor number,
 * and ability cooldowns during gameplay.
 */

public class HUDOverlay extends BorderPane {

    private final GameEngine engine;
    private ProgressBar healthBar;
    private Label livesLabel;
    private Label floorLabel;
    private ImageView[] slotIcons;
    private Rectangle[] slotCdOverlays;
    private Label[] slotCdLabels;
    private Label[] slotNames;

    public HUDOverlay(GameEngine engine) {
        this.engine = engine;
        setPickOnBounds(false);

        VBox topBar = new VBox(4);
        topBar.setPadding(new Insets(10));
        topBar.setMaxWidth(300);

        healthBar = new ProgressBar(1.0);
        healthBar.setPrefWidth(250);
        healthBar.setStyle("-fx-accent: red;");

        livesLabel = new Label("Lives: 3");
        livesLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");

        floorLabel = new Label("Floor: 1");
        floorLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");

        topBar.getChildren().addAll(healthBar, livesLabel, floorLabel);
        setTop(topBar);

        HBox abilityBar = new HBox(10);
        abilityBar.setAlignment(Pos.CENTER);
        abilityBar.setPadding(new Insets(10));

        slotIcons = new ImageView[3];
        slotCdOverlays = new Rectangle[3];
        slotCdLabels = new Label[3];
        slotNames = new Label[3];

        for (int i = 0; i < 3; i++) {
            slotIcons[i] = new ImageView();
            slotIcons[i].setFitWidth(56);
            slotIcons[i].setFitHeight(56);
            slotIcons[i].setPreserveRatio(true);

            slotCdOverlays[i] = new Rectangle(56, 56);
            slotCdOverlays[i].setFill(Color.rgb(0, 0, 0, 0.7));
            slotCdOverlays[i].setVisible(false);

            slotCdLabels[i] = new Label("");
            slotCdLabels[i].setStyle(
                    "-fx-text-fill: #ffffff; -fx-font-size: 20; -fx-font-weight: bold; " +
                            "-fx-font-family: 'MedievalSharp';");

            StackPane slot = new StackPane(slotIcons[i], slotCdOverlays[i], slotCdLabels[i]);
            slot.setPrefSize(56, 56);

            slotNames[i] = new Label("");
            slotNames[i].setStyle("-fx-text-fill: #ffffff; -fx-font-size: 11; " + "-fx-font-family: 'MedievalSharp';");

            VBox col = new VBox(2, slot, slotNames[i]);
            col.setAlignment(Pos.CENTER);
            abilityBar.getChildren().add(col);

        }

        setBottom(abilityBar);

    }

    /**
     * Updates all HUD elements. Should be called each frame.
     */
    public void update() {
        Player player = engine.getPlayer();
        if (player == null) {
            return;
        }

        healthBar.setProgress(player.getHealthPercent());
        livesLabel.setText("Lives: " + player.getLives());
        floorLabel.setText("Floor: " + (engine.getTower().getCurrentFloorNumber() + 1));

        Ability[] abilities = player.getAbilities();
        for (int i = 0; i < 3; i++) {
            if (abilities[i] == null) {
                slotIcons[i].setImage(null);
                slotNames[i].setText("");
                slotCdOverlays[i].setVisible(false);
                slotCdLabels[i].setText("");
                continue;
            }

            slotIcons[i].setImage(abilities[i].getIcon());
            slotNames[i].setText(abilities[i].getName());

            if (abilities[i].isReady()) {
                slotCdOverlays[i].setVisible(false);
                slotCdLabels[i].setText("");
            } else {
                slotCdOverlays[i].setVisible(true);
                slotCdLabels[i].setText(String.format("%.1f", abilities[i].getCurrentCooldown()));
            }
        }
    }
}