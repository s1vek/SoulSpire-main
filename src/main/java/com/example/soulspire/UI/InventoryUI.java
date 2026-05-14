package com.example.soulspire.UI;

import com.example.soulspire.Item.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Inventory screen showing equipped items, materials, and soul echoes.
 * Built programmatically without Scene Builder (assignment requirement).
 */
public class InventoryUI extends VBox {

    private final Inventory inventory;
    private final Label ironLabel = new Label();
    private final Label dustLabel = new Label();
    private final Label echoLabel = new Label();

    public InventoryUI(Inventory inventory) {
        this.inventory = inventory;

        setSpacing(4);
        setPadding(new Insets(10));
        setPrefSize(160, 100);
        setMaxSize(160, 100);
        setStyle("-fx-background-color: black; -fx-border-color: white;");

        for (Label l : new Label[]{ironLabel, dustLabel, echoLabel}) {
            l.setStyle("-fx-text-fill: white; -fx-font-family: 'MedievalSharp';");
        }

        getChildren().addAll(ironLabel, dustLabel, echoLabel);
    }

    /**
     * Refreshes counts and active SoulEcho. Call when inventory toggles open.
     */
    public void refresh() {
        ironLabel.setText("Iron: " + inventory.getMaterialCount(MaterialType.IRON_ORE));
        dustLabel.setText("Dust: " + inventory.getMaterialCount(MaterialType.ETHEREAL_DUST));
        SoulEcho a = inventory.getActiveSoulEchoes().stream()
                .filter(SoulEcho::isActive).findFirst().orElse(null);
        echoLabel.setText(a != null ? a.getName() : "-");
        }
}
