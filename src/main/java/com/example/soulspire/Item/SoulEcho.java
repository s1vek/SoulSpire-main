package com.example.soulspire.Item;

import com.example.soulspire.Ability.AbilityType;

/**
 * A temporary modifier found inside guarded chests.
 * Active only for the current tower run — lost on death or completion.
 * Modifies a specific category of abilities (e.g. +20% offensive damage).
 */
public class SoulEcho extends Item {

    private double modifierValue;
    private AbilityType targetAbilityType;
    private boolean active;

    /**
     * Creates a new Soul Echo modifier.
     *
     * @param name              display name (e.g. "Flame Amplifier")
     * @param description       effect description (e.g. "Offensive abilities deal 20% more damage")
     * @param modifierValue     the modifier amount (e.g. 0.2 for +20%)
     * @param targetAbilityType which ability category this affects
     */
    public SoulEcho(String name, String description, double modifierValue, AbilityType targetAbilityType) {
        super(name, description);
        this.modifierValue = modifierValue;
        this.targetAbilityType = targetAbilityType;
        this.active = false;
    }

    /**
     * Activates this modifier on the player. Called when picked up from a chest.
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Deactivates this modifier. Called on player death or run completion.
     */
    public void deactivate() {
        this.active = false;
    }

    @Override
    public Item copy() {
        return new SoulEcho(name, description, modifierValue, targetAbilityType);
    }

    /**
     * Getters and setters.
     */

    public double getModifierValue() { return modifierValue; }
    public AbilityType getTargetAbilityType() { return targetAbilityType; }
    public boolean isActive() { return active; }
}