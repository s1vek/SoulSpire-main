package com.example.soulspire.Core;

/**
 * Enum used for game states.
 */

public enum GameState {

    /** Main menu — start new game, load save, settings, quit. */
    MAIN_MENU,

    /** Player is choosing one of the four character classes. */
    CHARACTER_SELECT,

    /** Active gameplay — player is moving, fighting, exploring a floor. */
    PLAYING,

    /** Game is paused — overlay shown, game logic frozen. */
    PAUSED,

    /** Player is in a safe zone — NPCs available, crafting, no enemies. */
    SAFE_ZONE,

    /** All lives lost — game over screen displayed. */
    GAME_OVER,

    /** Final boss defeated — victory screen displayed. */
    VICTORY
}
