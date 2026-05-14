package com.example.soulspire.Item;

/**
 * Represents an equippable item (weapon or armor) that modifies player stats.
 * Crafted at the Blacksmith NPC using materials, or found as loot.
 */
public class Equipment extends Item {

    private int attackBonus;
    private int defenseBonus;
    private double moveSpeedBonus;
    private EquipmentSlot slot;

    public Equipment(String name, String description, int attackBonus, int defenseBonus, EquipmentSlot slot) {
        this(name, description, attackBonus, defenseBonus, 0, slot);
    }

    public Equipment(String name, String description, int attackBonus, int defenseBonus, double moveSpeedBonus, EquipmentSlot slot) {
        super(name, description);
        this.attackBonus = attackBonus;
        this.defenseBonus = defenseBonus;
        this.moveSpeedBonus = moveSpeedBonus;
        this.slot = slot;
    }

    @Override
    public Item copy() {
        return new Equipment(name, description, attackBonus, defenseBonus, moveSpeedBonus, slot);
    }

    /**
     * Getters and setters.
     */

    public int getAttackBonus() { return attackBonus; }
    public int getDefenseBonus() { return defenseBonus; }
    public double getMoveSpeedBonus() { return moveSpeedBonus; }
    public EquipmentSlot getSlot() { return slot; }
}