package com.example.soulspire.UI;

import com.example.soulspire.Core.GameEngine;
import com.example.soulspire.Core.GameState;
import com.example.soulspire.Entity.Player.PlayerType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.util.Map;

public class CharacterSelectScreen extends VBox {

    private static final String CARD_STYLE = "-fx-border-color: #555555; -fx-border-width: 2; -fx-background-color: #111111;";
    private static final String BTN_STYLE  = "-fx-background-color: transparent; -fx-text-fill: #aaaaaa; -fx-border-color: #555; -fx-border-width: 1; -fx-font-family: 'MedievalSharp'; -fx-font-size: 14;";

    private static final Map<PlayerType, String> DESCRIPTIONS = Map.of(
            PlayerType.WARRIOR, "A stalwart knight who absorbs punishment and crushes foes with brute force.",
            PlayerType.SHAMAN,  "A spiritual healer who commands totems and channels ancestral power.",
            PlayerType.MAGE,    "A fragile scholar who obliterates enemies with devastating spells.",
            PlayerType.HUNTER,  "A swift tracker who picks off targets from afar with precise arrows."
    );

    public CharacterSelectScreen(GameEngine engine, ScreenManager screenManager) {
        this.setSpacing(40);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(50));
        this.setStyle("-fx-background-color: black;");

        Font.loadFont(getClass().getResourceAsStream(
                "/com/example/soulspire/fonts/MedievalSharp-Regular.ttf"), 1);

        Label title = new Label("SOULSPIRE");
        title.setStyle("-fx-font-size: 60px; -fx-text-fill: white; -fx-font-family: 'MedievalSharp';");

        Label title2 = new Label("Select your character");
        title2.setStyle("-fx-font-size: 40px; -fx-text-fill: white; -fx-font-family: 'MedievalSharp';");

        HBox boxContainer = new HBox(20);
        boxContainer.setAlignment(Pos.CENTER);

        for (PlayerType type : PlayerType.values()) {
            boxContainer.getChildren().add(buildCard(type, engine, screenManager));
        }

        this.getChildren().addAll(title, title2, boxContainer);
    }

    /**
     * Builds a single character card divided into three sections:
     * portrait + name on top, description and stats in the middle,
     * and a Select button pinned to the bottom.
     */
    private VBox buildCard(PlayerType type, GameEngine engine, ScreenManager screenManager) {
        VBox card = new VBox(0);
        card.setPrefSize(300, 620);
        card.setStyle(CARD_STYLE);
        card.setPadding(new Insets(0, 0, 20, 0));

        VBox topSection = new VBox(12);
        topSection.setAlignment(Pos.TOP_CENTER);
        topSection.setPadding(new Insets(20, 20, 15, 20));
        topSection.setStyle("-fx-background-color: #111111;");
        topSection.getChildren().addAll(loadPortrait(type), buildNameLabel(type));

        VBox middleSection = new VBox(15);
        middleSection.setAlignment(Pos.TOP_CENTER);
        middleSection.setPadding(new Insets(0, 20, 0, 20));
        VBox.setVgrow(middleSection, Priority.ALWAYS);
        middleSection.getChildren().addAll(buildDescLabel(type), buildStatsLabel(type));

        Button selectBtn = new Button("Select");
        selectBtn.setPrefWidth(200);
        selectBtn.setPrefHeight(40);
        selectBtn.setStyle(BTN_STYLE);
        selectBtn.setOnAction(e -> {
            engine.initNewGame(type, "Hero");
            screenManager.showScreen(GameState.PLAYING);
        });
        selectBtn.setFocusTraversable(false);

        VBox bottomSection = new VBox();
        bottomSection.setAlignment(Pos.BOTTOM_CENTER);
        bottomSection.getChildren().add(selectBtn);

        card.getChildren().addAll(topSection, middleSection, bottomSection);
        return card;
    }

    /**
     * Loads the portrait from /com/example/soulspire/images/<type_lowercase>.png.
     * Returns an empty ImageView without crashing if the file is not yet present.
     */
    private ImageView loadPortrait(PlayerType type) {
        ImageView view = new ImageView();
        view.setFitWidth(260);
        view.setFitHeight(300);
        view.setPreserveRatio(true);

        var stream = getClass().getResourceAsStream(
                "/com/example/soulspire/images/" + type.name().toLowerCase() + ".png");
        if (stream != null) view.setImage(new Image(stream));

        return view;
    }

    /** Returns the class name label styled with the MedievalSharp font. */
    private Label buildNameLabel(PlayerType type) {
        Label label = new Label(type.getDisplayName().toUpperCase());
        label.setStyle("-fx-font-size: 22px; -fx-text-fill: white; -fx-font-family: 'MedievalSharp';");
        return label;
    }

    /** Returns a wrapped description label styled with the MedievalSharp font. */
    private Label buildDescLabel(PlayerType type) {
        Label label = new Label(DESCRIPTIONS.get(type));
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: #bbbbbb; -fx-font-family: 'MedievalSharp';");
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        return label;
    }

    /** Returns an HP / ATK / DEF stats label styled with the MedievalSharp font. */
    private Label buildStatsLabel(PlayerType type) {
        Label label = new Label(String.format("HP %d    ATK %d    DEF %d",
                type.getBaseHealth(), type.getBaseAttack(), type.getBaseDefense()));
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: #888888; -fx-font-family: 'MedievalSharp';");
        return label;
    }
}