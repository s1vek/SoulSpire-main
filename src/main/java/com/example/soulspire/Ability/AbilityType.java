package com.example.soulspire.Ability;

/**
 * Enum used for types of abilities
 */

public enum AbilityType {

    /** Deals damage to enemies (Bladewhirl, ArcaneOrb, SpreadShot, Firestrike). */
    OFFENSIVE,

    /** Protects the player from damage (IceBlock). */
    DEFENSIVE,

    /** Moves the player quickly (Charge, Teleport, Leap, AstralWolf). */
    MOBILITY,

    /** Provides buffs or support effects (Enrage, HealingTotem, FrostTrap). */
    UTILITY
}
