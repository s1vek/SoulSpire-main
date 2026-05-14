package com.example.soulspire.Item;

import com.example.soulspire.Ability.AbilityType;
import com.example.soulspire.Entity.Player.Player;

/**
 * A temporary modifier found inside guarded chests.
 * Active only for the current tower run — lost on death or completion.
 * Modifies a specific category of abilities (e.g. +20% offensive damage).
 */
public class SoulEcho extends Item {

    private final SoulEchoType type;
    private final int value;
    private boolean active;

    public SoulEcho(String name, String description, SoulEchoType type, int value) {
        super(name, description);
        this.type = type;
        this.value = value;
        this.active = false;
    }
    /**
     * Applies the bonus directly to the player's stats.
     * Multiple SoulEchoes naturally stack — each just adds to the current stat.
     */
    public void apply(Player player) {
        switch (type) {
            case DAMAGE -> player.setAttackDamage(player.getAttackDamage() + value);
            case HEALTH -> {
                player.setMaxHealth(player.getMaxHealth() + value);
                player.setCurrentHealth(player.getCurrentHealth() + value);
            }
            case DEFENSE -> player.setDefense(player.getDefense() + value);
            case SPEED   -> player.setMoveSpeed(player.getMoveSpeed() + value);
        }
        this.active = true;
    }

    @Override
    public Item copy() {
        return new SoulEcho(name, description, type, value);
    }

    public SoulEchoType getType() { return type; }
    public int getValue() { return value; }
    public boolean isActive() { return active; }
}