package com.example.soulspire.Item;

import javafx.scene.image.Image;

/**
 * Abstract base class for all items that can exist in a player's inventory.
 *
 * <p>Subclasses:</p>
 * <ul>
 *   <li>{@link Equipment} — weapons and armor with stat bonuses, equippable</li>
 *   <li>{@link Material} — crafting resources (Iron Ore, Ethereal Dust), stackable</li>
 *   <li>{@link SoulEcho} — temporary ability modifiers, active for one tower run</li>
 * </ul>
 */
public abstract class Item {

    /** Internal item name used for identification and display. */
    protected String name;

    /** Description text shown in inventory tooltips. */
    protected String description;

    /** Icon displayed in the inventory UI. May be null during development. */
    protected Image icon;

    /**
     * Creates a new item.
     *
     * @param name        display name
     * @param description tooltip description
     */
    protected Item(String name, String description) {
        this.name = name;
        this.description = description;
        this.icon = null;
    }

    /**
     * Returns whether this item can be stacked with another item in the inventory.
     * Only {@link Material} items of the same type are stackable.
     *
     * @param other the item to check stacking with
     * @return true if the items can be combined into one stack
     */
    public boolean canStackWith(Item other) {
        return false;
    }

    /**
     * Returns a copy of this item. Used when dropping loot or splitting stacks.
     *
     * @return a new Item instance with the same properties
     */
    public abstract Item copy();

    /**
     * Getters and setters.
     */

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Image getIcon() { return icon; }
    public void setIcon(Image icon) { this.icon = icon; }
}