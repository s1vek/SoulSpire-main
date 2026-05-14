package com.example.soulspire.Core;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

/**
 * GameLoop class
 */

public class GameLoop {

    private AnimationTimer timer;
    private long lastNanoTime;
    private boolean running;
    private GameEngine engine;
    private GraphicsContext gc;

    /**
     * Creates a game loop connected to the given engine and graphics context.
     *
     * @param engine the game engine to update and render
     * @param gc     the canvas graphics context to draw on
     */
    public GameLoop(GameEngine engine, GraphicsContext gc) {
        this.engine = engine;
        this.gc = gc;
        this.running = false;

        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastNanoTime == 0) {
                    lastNanoTime = now;
                    return;
                }
                double deltaTime = (now - lastNanoTime) / 1_000_000_000.0;
                lastNanoTime = now;

                deltaTime = Math.min(deltaTime, 0.05);

                engine.update(deltaTime);
                engine.render(gc);
            }
        };
    }

    /**
     * Starts the game loop.
     */
    public void start() {
        lastNanoTime = 0;
        running = true;
        timer.start();
    }

    /**
     * Stops the game loop completely.
     */
    public void stop() {
        running = false;
        timer.stop();
    }

    public boolean isRunning() { return running; }
}