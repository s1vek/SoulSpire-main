package com.example.soulspire.Util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameClock {

    private final ScheduledExecutorService scheduler;
    private long elapsedSeconds = 0;

    public GameClock() {scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "GameClock");
            t.setDaemon(true);
            return t;
        });
    }

    public void start() {
        scheduler.scheduleAtFixedRate(() -> elapsedSeconds++, 1, 1, TimeUnit.SECONDS);
    }

    public long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public String getFormattedTime() {
        return String.format("%02d:%02d", elapsedSeconds / 60, elapsedSeconds % 60);
    }

}
