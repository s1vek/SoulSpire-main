package com.example.soulspire.Entity.Player;

public enum PlayerType {

    WARRIOR("Warrior", 150, 20, 15, 100),
    SHAMAN("Shaman", 90, 15, 10, 100),
    MAGE("Mage", 70, 18, 5, 100),
    HUNTER("Hunter", 80, 12, 8, 100);

    private final String displayName;
    private final int baseHealth;
    private final int baseAttack;
    private final int baseDefense;
    private final double baseSpeed;

    PlayerType(String displayName, int baseHealth, int baseAttack, int baseDefense, double baseSpeed) {
        this.displayName = displayName;
        this.baseHealth = baseHealth;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.baseSpeed = baseSpeed;
    }

    /**
     * @return human-readable name shown in UI (e.g. "Warrior")
     */
    public String getDisplayName() { return displayName; }

    /**
     * @return starting maximum health points
     */
    public int getBaseHealth() { return baseHealth; }

    /**
     * @return starting attack damage value
     */
    public int getBaseAttack() { return baseAttack; }

    /**
     * @return starting defense value (reduces incoming damage)
     */
    public int getBaseDefense() { return baseDefense; }

    /**
     * @return starting movement speed in tiles per second
     */
    public double getBaseSpeed() { return baseSpeed; }
}
