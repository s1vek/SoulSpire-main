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
    private VBox equipmentSection;
    private GridPane itemGrid;

    public InventoryUI(Inventory inventory) {
        this.inventory = inventory;

        setAlignment(Pos.TOP_CENTER);
        setSpacing(15);
        setPadding(new Insets(20));
        setMaxWidth(450);
        setStyle("-fx-background-color: rgba(30,30,30,0.95); -fx-background-radius: 10;");

        Label title = new Label("Inventory");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 22; -fx-font-weight: bold;");

        // Equipment section
        equipmentSection = new VBox(5);
        Label eqTitle = new Label("Equipped");
        eqTitle.setStyle("-fx-text-fill: #ccc; -fx-font-size: 14; -fx-font-weight: bold;");
        equipmentSection.getChildren().add(eqTitle);

        // Item grid
        itemGrid = new GridPane();
        itemGrid.setHgap(8);
        itemGrid.setVgap(8);

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> setVisible(false));

        getChildren().addAll(title, equipmentSection, new Label("Items") {{
            setStyle("-fx-text-fill: #ccc; -fx-font-size: 14; -fx-font-weight: bold;");
        }}, itemGrid, closeBtn);

        refresh();
    }

    /**
     * Refreshes the inventory display with current data.
     */
    public void refresh() {
        // Clear and rebuild equipment section
        equipmentSection.getChildren().removeIf(n -> n instanceof HBox);
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Equipment eq = inventory.getEquipped(slot);
            HBox row = new HBox(10);
            row.setPadding(new Insets(3, 8, 3, 8));
            row.setStyle("-fx-background-color: #444; -fx-background-radius: 4;");

            Label slotLabel = new Label(slot.getDisplayName() + ":");
            slotLabel.setStyle("-fx-text-fill: #aaa; -fx-font-size: 12;");
            slotLabel.setMinWidth(70);

            Label itemLabel = new Label(eq != null ? eq.getName() : "(empty)");
            itemLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12;");

            row.getChildren().addAll(slotLabel, itemLabel);
            equipmentSection.getChildren().add(row);
        }

        itemGrid.getChildren().clear();
        int col = 0, row = 0;
        for (Item item : inventory.getItems()) {
            Label cell = new Label(formatItem(item));
            cell.setPadding(new Insets(4, 8, 4, 8));
            cell.setStyle("-fx-text-fill: white; -fx-font-size: 11; " +
                    "-fx-background-color: #555; -fx-background-radius: 4;");
            itemGrid.add(cell, col, row);
            col++;
            if (col >= 4) { col = 0; row++; }
        }
    }

    private String formatItem(Item item) {
        if (item instanceof Material mat) {
            return mat.getName() + " x" + mat.getQuantity();
        } else if (item instanceof SoulEcho echo) {
            return echo.getName() + (echo.isActive() ? " *" : "");
        }
        return item.getName();
    }
}