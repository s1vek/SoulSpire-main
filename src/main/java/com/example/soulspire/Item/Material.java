package com.example.soulspire.Item;

/**
 * A stackable crafting material collected from enemies and the environment.
 * Two types exist: {@link MaterialType#IRON_ORE} and {@link MaterialType#ETHEREAL_DUST}.
 */
public class Material extends Item {

    private MaterialType materialType;
    private int quantity;

    /**
     * Creates a new material stack.
     *
     * @param materialType the type of material
     * @param quantity     how many units in this stack
     */
    public Material(MaterialType materialType, int quantity) {
        super(materialType.getDisplayName(), "Crafting material");
        this.materialType = materialType;
        this.quantity = quantity;
    }

    @Override
    public boolean canStackWith(Item other) {
        return other instanceof Material mat && mat.materialType == this.materialType;
    }

    @Override
    public Item copy() {
        return new Material(materialType, quantity);
    }

    /**
     * Increases the stack size.
     *
     * @param amount units to add
     */
    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    /**
     * Decreases the stack size.
     *
     * @param amount units to remove
     */
    public void removeQuantity(int amount) {
        this.quantity = Math.max(0, this.quantity - amount);
    }

    /**
     * @param amount required quantity
     * @return true if this stack has at least the given amount
     */
    public boolean hasEnough(int amount) {
        return this.quantity >= amount;
    }

    /**
     * Getters and setters.
     */

    public MaterialType getMaterialType() { return materialType; }
    public int getQuantity() { return quantity; }
}