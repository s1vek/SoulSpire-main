package com.example.soulspire.Util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Game-wide logging utility wrapping {@link java.util.logging.Logger}.
 * Provides a simple API for logging game events with runtime enable/disable.
 */

public class GameLogger {

    /** Global flag controlling whether any log output is produced. */
    private static boolean enabled;

    /** The underlying java.util.logging logger. */
    private final Logger logger;

    static {
        String prop = System.getProperty("soulspire.logging", "true");
        enabled = Boolean.parseBoolean(prop);
    }

    /**
     * Private constructor — use {@link #getLogger(Class)} factory method.
     */
    private GameLogger(Class<?> clazz) {
        this.logger = Logger.getLogger(clazz.getName());
        logger.setUseParentHandlers(false);
        if (logger.getHandlers().length == 0) {
            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(Level.ALL);
            logger.addHandler(handler);
        }
        logger.setLevel(Level.ALL);
    }

    /**
     * Factory method to obtain a logger for a given class.
     *
     * @param clazz the class requesting the logger (used as logger name)
     * @return a GameLogger instance
     */
    public static GameLogger getLogger(Class<?> clazz) {
        return new GameLogger(clazz);
    }

    /**
     * Logs an informational message (game events, state changes).
     *
     * @param message the message to log
     */
    public void info(String message) {
        if (enabled) {
            logger.info(message);
        }
    }

    /**
     * Logs a warning message (recoverable issues, unexpected states).
     *
     * @param message the warning message
     */
    public void warn(String message) {
        if (enabled) {
            logger.warning(message);
        }
    }

    /**
     * Logs an error message with an associated exception.
     *
     * @param message the error description
     * @param throwable the exception that caused the error
     */
    public void error(String message, Throwable throwable) {
        if (enabled) {
            logger.log(Level.SEVERE, message, throwable);
        }
    }

    /**
     * Enables or disables all logging globally.
     * Can be called from the pause menu toggle or settings screen.
     *
     * @param enabled true to enable logging, false to suppress all output
     */
    public static void setEnabled(boolean enabled) {
        GameLogger.enabled = enabled;
    }

    /**
     * @return whether logging is currently enabled
     */
    public static boolean isEnabled() {
        return enabled;
    }
}